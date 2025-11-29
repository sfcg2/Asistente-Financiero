package com.example.asistentefinanciero.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asistentefinanciero.data.model.Egreso
import com.example.asistentefinanciero.data.model.Ingreso
import com.example.asistentefinanciero.data.repository.EgresoRepository
import com.example.asistentefinanciero.data.repository.IngresoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Enum para el filtro
enum class FiltroEstadisticas {
    INGRESOS, EGRESOS
}

// Modelo de datos para el gráfico circular
data class DatoGrafico(
    val categoria: String,
    val montoTotal: Double,
    val porcentajeTotal: Float,
    val color: Color
)

class EstadisticasViewModel : ViewModel() {

    private val ingresoRepository = IngresoRepository()
    private val egresoRepository = EgresoRepository()

    // Estado del filtro actual (Ingresos o Egresos)
    private val _filtroActual = MutableStateFlow(FiltroEstadisticas.INGRESOS)
    val filtroActual: StateFlow<FiltroEstadisticas> = _filtroActual.asStateFlow()

    // Datos procesados para el gráfico circular
    private val _datosGrafico = MutableStateFlow<List<DatoGrafico>>(emptyList())
    val datosGrafico: StateFlow<List<DatoGrafico>> = _datosGrafico.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Mes seleccionado (null = todos los meses)
    private val _mesFiltro = MutableStateFlow<Int?>(null)
    val mesFiltro: StateFlow<Int?> = _mesFiltro.asStateFlow()

    // Cache de datos
    private var todosLosIngresos = listOf<Ingreso>()
    private var todosLosEgresos = listOf<Egreso>()

    //Carga los ingresos del usuario y opcionalmente filtra por mes



    fun cargarDatos(usuarioId: String, mes: Int? = null) {
        _mesFiltro.value = mes
        _isLoading.value = true

        viewModelScope.launch {
            Log.d("EstadisticasViewModel", "Cargando datos del usuario: $usuarioId")

            // 1. Cargar Ingresos
            ingresoRepository.obtenerIngresos(usuarioId) { ingresos ->
                todosLosIngresos = ingresos
                if (_filtroActual.value == FiltroEstadisticas.INGRESOS) {
                    actualizarDatosGrafico()
                }
                if (todosLosEgresos.isNotEmpty() || ingresos.isEmpty()) checkLoadingFinished()
            }

            // 2. Cargar Egresos
            egresoRepository.obtenerEgresos(usuarioId) { egresos ->
                todosLosEgresos = egresos
                if (_filtroActual.value == FiltroEstadisticas.EGRESOS) {
                    actualizarDatosGrafico()
                }
                checkLoadingFinished()
            }
        }
    }

    private fun checkLoadingFinished() {
        _isLoading.value = false
    }

    fun cambiarFiltro(filtro: FiltroEstadisticas) {
        _filtroActual.value = filtro
        actualizarDatosGrafico()
    }

    //Selecciona un mes específico para filtrar

    fun seleccionarMes(mes: Int?, usuarioId: String) {
        _mesFiltro.value = mes
        actualizarDatosGrafico()
    }

    //Actualiza los datos del gráfico según el filtro actual
    private fun actualizarDatosGrafico() {
        when (_filtroActual.value) {
            FiltroEstadisticas.INGRESOS -> procesarIngresosParaGrafico()
            FiltroEstadisticas.EGRESOS -> procesarEgresosParaGrafico()
        }
    }


    // --- LÓGICA DE INGRESOS ---
    private fun procesarIngresosParaGrafico() {
        val ingresosFiltrados = if (_mesFiltro.value != null) {
            todosLosIngresos.filter { ingreso ->
                obtenerMesDeFecha(ingreso.obtenerFechaFormateada()) == _mesFiltro.value
            }
        } else todosLosIngresos

        if (ingresosFiltrados.isEmpty()) {
            _datosGrafico.value = emptyList()
            return
        }

        val agrupadoPorCategoria = ingresosFiltrados
            .groupBy { it.categoria }
            .mapValues { (_, ingresos) -> ingresos.sumOf { it.monto } }

        val granTotal = agrupadoPorCategoria.values.sum()

        val datosParaGrafico = agrupadoPorCategoria.map { (categoria, montoTotal) ->
            DatoGrafico(
                categoria = categoria,
                montoTotal = montoTotal,
                porcentajeTotal = if (granTotal > 0) (montoTotal / granTotal * 100).toFloat() else 0f,
                color = obtenerColorPorCategoria(categoria, esGasto = false)
            )
        }.sortedByDescending { it.montoTotal }

        _datosGrafico.value = datosParaGrafico
    }

    // --- LÓGICA DE EGRESOS ---
    private fun procesarEgresosParaGrafico() {
        val egresosFiltrados = if (_mesFiltro.value != null) {
            todosLosEgresos.filter { egreso ->
                obtenerMesDeFecha(egreso.obtenerFechaFormateada()) == _mesFiltro.value
            }
        } else todosLosEgresos

        if (egresosFiltrados.isEmpty()) {
            _datosGrafico.value = emptyList()
            return
        }

        val agrupadoPorCategoria = egresosFiltrados
            .groupBy { it.categoria }
            .mapValues { (_, egresos) -> egresos.sumOf { it.monto } }

        val granTotal = agrupadoPorCategoria.values.sum()

        val datosParaGrafico = agrupadoPorCategoria.map { (categoria, montoTotal) ->
            DatoGrafico(
                categoria = categoria,
                montoTotal = montoTotal,
                porcentajeTotal = if (granTotal > 0) (montoTotal / granTotal * 100).toFloat() else 0f,
                color = obtenerColorPorCategoria(categoria, esGasto = true)
            )
        }.sortedByDescending { it.montoTotal }

        _datosGrafico.value = datosParaGrafico

        Log.d("EstadisticasViewModel", "Egresos procesados. Total: $granTotal")
    }


    //Obtenemos el mes de una fecha en formato dd/MM/yyyy

    private fun obtenerMesDeFecha(fechaString: String): Int? {
        return try {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = format.parse(fechaString)
            if (date != null) {
                val cal = Calendar.getInstance()
                cal.time = date
                cal.get(Calendar.MONTH) + 1 // Enero = 1, Diciembre = 12
            } else null
        } catch (e: Exception) {
            Log.e("EstadisticasViewModel", "Error al parsear fecha: $fechaString", e)
            null
        }
    }
    private fun obtenerColorPorCategoria(categoria: String, esGasto: Boolean): Color {
        val catLower = categoria.lowercase().trim()

        return if (esGasto) {
            when (catLower) {
                "alimentación", "comida", "restaurante" -> Color(0xFFEF5350)
                "transporte", "gasolina", "taxi" -> Color(0xFF42A5F5)
                "vivienda", "alquiler", "casa" -> Color(0xFF8D6E63)
                "servicios", "luz", "agua", "internet" -> Color(0xFFFFCA28)
                "salud", "medicina", "farmacia" -> Color(0xFF66BB6A)
                "ocio", "cine", "entretenimiento" -> Color(0xFFAB47BC)
                "educación", "universidad", "libros" -> Color(0xFF5C6BC0)
                "ropa", "vestimenta" -> Color(0xFF26A69A)
                "mascotas" -> Color(0xFFFFA726)
                else -> Color(0xFF78909C)
            }
        } else {
            when (catLower) {
                "salario", "sueldo", "nomina" -> Color(0xFF1E88E5)
                "freelance", "trabajos extra" -> Color(0xFF4CAF50)
                "inversiones", "dividendos" -> Color(0xFFFFB300)
                "ventas" -> Color(0xFFF4511E)
                "bonos", "premios" -> Color(0xFF8E24AA)
                "regalos" -> Color(0xFFD81B60)
                else -> Color(0xFF546E7A)
            }
        }
    }
}