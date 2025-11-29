package com.example.asistentefinanciero.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asistentefinanciero.data.model.Ingreso
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

    //Carga los ingresos del usuario y opcionalmente filtra por mes
    fun cargarIngresos(usuarioId: String, mes: Int? = null) {
        _mesFiltro.value = mes

        viewModelScope.launch {
            _isLoading.value = true
            Log.d("EstadisticasViewModel", "Cargando ingresos del usuario: $usuarioId")

            ingresoRepository.obtenerIngresos(usuarioId) { ingresos ->
                todosLosIngresos = ingresos
                actualizarDatosGrafico()
                _isLoading.value = false
            }
        }
    }

    //Cambia el filtro entre Ingresos y Egresos

    fun cambiarFiltro(filtro: FiltroEstadisticas) {
        _filtroActual.value = filtro
        actualizarDatosGrafico()
    }

    //Selecciona un mes específico para filtrar

    fun seleccionarMes(mes: Int?, usuarioId: String) {
        _mesFiltro.value = mes
        cargarIngresos(usuarioId, mes)
    }

    //Actualiza los datos del gráfico según el filtro actual
    private fun actualizarDatosGrafico() {
        when (_filtroActual.value) {
            FiltroEstadisticas.INGRESOS -> {
                procesarIngresosParaGrafico()
            }
            FiltroEstadisticas.EGRESOS -> {
                // Por ahora vacío, implementarás después
                _datosGrafico.value = emptyList()
            }
        }
    }

    //Procesa los ingresos y los agrupa por categoría para el gráfico
     //Cada DatoGrafico contiene: categoría, monto TOTAL y porcentaje TOTAL

    private fun procesarIngresosParaGrafico() {
        // 1. Filtrar por mes si es necesario
        val ingresosFiltrados = if (_mesFiltro.value != null) {
            todosLosIngresos.filter { ingreso ->
                obtenerMesDeFecha(ingreso.obtenerFechaFormateada()) == _mesFiltro.value
            }
        } else {
            todosLosIngresos
        }

        if (ingresosFiltrados.isEmpty()) {
            _datosGrafico.value = emptyList()
            return
        }

        // 2. Agrupar por categoría y sumar montos TOTALES
        val agrupadoPorCategoria = ingresosFiltrados
            .groupBy { it.categoria }
            .mapValues { (_, ingresos) -> ingresos.sumOf { it.monto } }

        // 3. Calcular el GRAN TOTAL para los porcentajes
        val granTotal = agrupadoPorCategoria.values.sum()

        // 4. Convertir a DatoGrafico con monto TOTAL y porcentaje TOTAL de cada categoría
        val datosParaGrafico = agrupadoPorCategoria.map { (categoria, montoTotal) ->
            DatoGrafico(
                categoria = categoria,
                montoTotal = montoTotal,  // Monto TOTAL de la categoría
                porcentajeTotal = if (granTotal > 0) (montoTotal / granTotal * 100).toFloat() else 0f,  // Porcentaje TOTAL
                color = obtenerColorPorCategoria(categoria)
            )
        }.sortedByDescending { it.montoTotal } // Ordena de mayor a menor monto

        _datosGrafico.value = datosParaGrafico

        Log.d("EstadisticasViewModel",
            "Datos del gráfico actualizados: ${datosParaGrafico.size} categorías, " +
                    "Gran Total: $granTotal (Mes: ${_mesFiltro.value})")
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
    private fun obtenerColorPorCategoria(categoria: String): Color {
        return when (categoria.lowercase()) {
            "salario"     -> Color(0xFF1E88E5) // Azul brillante
            "freelance"   -> Color(0xFF4CAF50) // Verde medio (similar al original)
            "inversiones" -> Color(0xFFFFB300) // Ámbar oscuro
            "ventas"      -> Color(0xFFF4511E) // Naranja rojizo (dark orange)
            "bonos"       -> Color(0xFF8E24AA) // Morado oscuro/índigo
            "regalos"     -> Color(0xFFD81B60) // Rosa fuerte (fuchsia)
            "otro"        -> Color(0xFF546E7A) // Gris azulado (slate gray)
            else          -> Color(0xFF00ACC1) // Azul cian (cielish blue)
        }
    }
}