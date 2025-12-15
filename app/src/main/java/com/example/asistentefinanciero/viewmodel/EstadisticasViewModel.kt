package com.example.asistentefinanciero.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asistentefinanciero.data.model.Egreso
import com.example.asistentefinanciero.data.model.Ingreso
import com.example.asistentefinanciero.data.repository.EgresoRepository
import com.example.asistentefinanciero.data.repository.IngresoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class FiltroEstadisticas {
    INGRESOS, EGRESOS
}

data class DatoGrafico(
    val categoria: String,
    val montoTotal: Double,
    val porcentajeTotal: Float,
    val color: Color
)

class EstadisticasViewModel(
    private val ingresoRepository: IngresoRepository,
    private val egresoRepository: EgresoRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _filtroActual = MutableStateFlow(FiltroEstadisticas.INGRESOS)
    val filtroActual: StateFlow<FiltroEstadisticas> = _filtroActual.asStateFlow()

    private val _datosGrafico = MutableStateFlow<List<DatoGrafico>>(emptyList())
    val datosGrafico: StateFlow<List<DatoGrafico>> = _datosGrafico.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _mesFiltro = MutableStateFlow<Int?>(null)
    val mesFiltro: StateFlow<Int?> = _mesFiltro.asStateFlow()

    private val _seleccionGrafico = MutableStateFlow<DatoGrafico?>(null)
    val seleccionGrafico: StateFlow<DatoGrafico?> = _seleccionGrafico.asStateFlow()

    private var todosLosIngresos = emptyList<Ingreso>()
    private var todosLosEgresos = emptyList<Egreso>()
    private var currentUserId: String? = null

    fun inicializarCargaDeDatos(usuarioId: String) {
        if (currentUserId == null) {
            currentUserId = usuarioId
            cargarDatos(usuarioId)
        }
    }

    private fun cargarDatos(usuarioId: String) {
        viewModelScope.launch(dispatcher) {
            _isLoading.value = true

            ingresoRepository.obtenerIngresos(usuarioId) {
                todosLosIngresos = it
            }

            egresoRepository.obtenerEgresos(usuarioId) {
                todosLosEgresos = it
                actualizarDatosGrafico()
                _isLoading.value = false
            }
        }
    }

    fun cambiarFiltro(filtro: FiltroEstadisticas) {
        _filtroActual.value = filtro
        _seleccionGrafico.value = null
        actualizarDatosGrafico()
    }

    fun seleccionarMes(mes: Int?) {
        _mesFiltro.value = mes
        _seleccionGrafico.value = null
        actualizarDatosGrafico()
    }

    fun setSeleccionGrafico(dato: DatoGrafico?) {
        _seleccionGrafico.value = dato
    }

    private fun actualizarDatosGrafico() {
        when (_filtroActual.value) {
            FiltroEstadisticas.INGRESOS -> procesarIngresos()
            FiltroEstadisticas.EGRESOS -> procesarEgresos()
        }
    }

    private fun procesarIngresos() {
        val filtrados = filtrarPorMes(todosLosIngresos.map { it.obtenerFechaFormateada() to it })
            .map { it.second }

        _datosGrafico.value = generarDatosGrafico(
            filtrados.map { it.categoria to it.monto },
            "Ingresos"
        )
    }

    private fun procesarEgresos() {
        val filtrados = filtrarPorMes(todosLosEgresos.map { it.obtenerFechaFormateada() to it })
            .map { it.second }

        _datosGrafico.value = generarDatosGrafico(
            filtrados.map { it.categoria to it.monto },
            "Egresos"
        )
    }

    private fun <T> filtrarPorMes(lista: List<Pair<String, T>>): List<Pair<String, T>> {
        val mes = _mesFiltro.value ?: return lista
        return lista.filter {
            obtenerMesDeFecha(it.first) == mes
        }
    }

    private fun generarDatosGrafico(
        datos: List<Pair<String, Double>>,
        tipo: String
    ): List<DatoGrafico> {

        if (datos.isEmpty()) return emptyList()

        val agrupado = datos.groupBy { it.first }
            .mapValues { it.value.sumOf { v -> v.second } }

        val total = agrupado.values.sum()

        return agrupado.map { (categoria, monto) ->
            DatoGrafico(
                categoria = categoria,
                montoTotal = monto,
                porcentajeTotal = ((monto / total) * 100).toFloat(),
                color = obtenerColorPorCategoria(categoria, tipo)
            )
        }.sortedByDescending { it.montoTotal }
    }

    private fun obtenerMesDeFecha(fechaString: String): Int? {
        return try {
            val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(fechaString)
            Calendar.getInstance().apply { time = date!! }.get(Calendar.MONTH) + 1
        } catch (e: Exception) {
            Log.e("VM", "Error fecha", e)
            null
        }
    }

    private fun obtenerColorPorCategoria(categoria: String, tipo: String): Color {
        return Color.Gray // el color NO se testea
    }
}
