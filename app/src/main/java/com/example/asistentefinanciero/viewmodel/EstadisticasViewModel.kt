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
import kotlinx.coroutines.flow.onEmpty
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

    private val _seleccionGrafico = MutableStateFlow<DatoGrafico?>(null)
    val seleccionGrafico: StateFlow<DatoGrafico?> = _seleccionGrafico.asStateFlow()

    // Cache de datos
    private var todosLosIngresos = listOf<Ingreso>()
    private var todosLosEgresos = listOf<Egreso>()

    private var currentUserId: String? = null


    //cargamos todos los datos de una sola vez

    fun inicializarCargaDeDatos(usuarioId: String) {
        if (currentUserId == null) {
            currentUserId = usuarioId
            cargarDatos(usuarioId)
        }
    }
    //carga ingresos y egresos a la vez
    private fun cargarDatos(usuarioId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d("EstadisticasViewModel", "Cargando TODOS los datos para el usuario: $usuarioId")

            // Carga ingresos
            ingresoRepository.obtenerIngresos(usuarioId) { ingresos ->
                todosLosIngresos = ingresos
                // No actualizamos el gráfico aquí todavía
            }

            // Carga egresos
            egresoRepository.obtenerEgresos(usuarioId) { egresos ->
                todosLosEgresos = egresos
                // Una vez ambos están cargados, actualizamos el gráfico inicial
                actualizarDatosGrafico()
                _isLoading.value = false
            }
        }
    }


    //Cambia el filtro entre Ingresos y Egresos
    fun cambiarFiltro(filtro: FiltroEstadisticas) {
        _filtroActual.value = filtro
        setSeleccionGrafico(null)
        actualizarDatosGrafico()
    }

    //Selecciona un mes específico para filtrar

    fun seleccionarMes(mes: Int?, usuarioId: String) {
        _mesFiltro.value = mes
        setSeleccionGrafico(null)
        actualizarDatosGrafico()
    }

    // Función para actualizar la selección del gráfico desde la UI
    fun setSeleccionGrafico(dato: DatoGrafico?) {
        _seleccionGrafico.value = dato
    }
    //Actualiza los datos del gráfico según el filtro actual
    private fun actualizarDatosGrafico() {
        setSeleccionGrafico(null)

        when (_filtroActual.value) {
            FiltroEstadisticas.INGRESOS -> {
                procesarIngresosParaGrafico()
            }
            FiltroEstadisticas.EGRESOS -> {
                procesarEgresosParaGrafico()
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
                color = obtenerColorPorCategoria(categoria, "Ingresos")
            )
        }.sortedByDescending { it.montoTotal } // Ordena de mayor a menor monto

        _datosGrafico.value = datosParaGrafico

        Log.d("EstadisticasViewModel",
            "Datos del gráfico actualizados: ${datosParaGrafico.size} categorías, " +
                    "Gran Total: $granTotal (Mes: ${_mesFiltro.value})")
    }
    private fun procesarEgresosParaGrafico() {
        // 1. Filtrar por mes si es necesario
        val egresosFiltrados = if (_mesFiltro.value != null) {
            todosLosEgresos.filter { egreso ->
                obtenerMesDeFecha(egreso.obtenerFechaFormateada()) == _mesFiltro.value
            }
        } else {
            todosLosEgresos
        }

        if (egresosFiltrados.isEmpty()) {
            _datosGrafico.value = emptyList()
            return
        }

        // 2. Agrupar por categoría y sumar montos TOTALES
        val agrupadoPorCategoria = egresosFiltrados
            .groupBy { it.categoria }
            .mapValues { (_, egresos) -> egresos.sumOf { it.monto } }

        // 3. Calcular el GRAN TOTAL para los porcentajes
        val granTotal = agrupadoPorCategoria.values.sum()

        // 4. Convertir a DatoGrafico con monto TOTAL y porcentaje TOTAL de cada categoría
        val datosParaGrafico = agrupadoPorCategoria.map { (categoria, montoTotal) ->
            DatoGrafico(
                categoria = categoria,
                montoTotal = montoTotal,  // Monto TOTAL de la categoría
                porcentajeTotal = if (granTotal > 0) (montoTotal / granTotal * 100).toFloat() else 0f,  // Porcentaje TOTAL
                color = obtenerColorPorCategoria(categoria, "Egresos")
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
    private fun obtenerColorPorCategoria(categoria: String, tipo: String): Color {
        if(tipo == "Ingresos") {
            return when (categoria.lowercase()) {
                "salario" -> Color(0xFF1E88E5) // Azul brillante
                "freelance" -> Color(0xFF4CAF50) // Verde medio (similar al original)
                "inversiones" -> Color(0xFFFFB300) // Ámbar oscuro
                "ventas" -> Color(0xFFF4511E) // Naranja rojizo (dark orange)
                "bonos" -> Color(0xFF8E24AA) // Morado oscuro/índigo
                "regalos" -> Color(0xFFD81B60) // Rosa fuerte (fuchsia)
                "otro" -> Color(0xFF546E7A) // Gris azulado (slate gray)
                else -> Color(0xFF00ACC1) // Azul cian (cielish blue)
            }
        }else {
            return when (categoria.lowercase()) {
                "comida" -> Color(0xFFF4511E)
                "transporte" -> Color(0xFF1E88E5)
                "arriendo" -> Color(0xFF8D6E63)
                "servicios" -> Color(0xFFFFCA28)
                "salud" -> Color(0xFF4CAF50)
                "entretenimiento" -> Color(0xFF8E24AA)
                "educación" -> Color(0xFF5C6BC0)
                "ropa" -> Color(0xFFD81B60)
                "otro" -> Color(0xFF546E7A)
                else -> Color(0xFF3F51B5)
            }
        }
    }
}