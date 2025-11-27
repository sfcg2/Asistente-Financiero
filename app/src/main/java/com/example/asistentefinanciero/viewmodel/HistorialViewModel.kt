package com.example.asistentefinanciero.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asistentefinanciero.data.model.Ingreso
import com.example.asistentefinanciero.data.model.Egreso
import com.example.asistentefinanciero.data.repository.IngresoRepository
import com.example.asistentefinanciero.data.repository.EgresoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TransaccionItem(
    val id: String,
    val nombre: String,
    val categoria: String,
    val monto: Double,
    val fecha: String,
    val hora: String,
    val esIngreso: Boolean
)

enum class FiltroHistorial {
    TODOS, INGRESOS, EGRESOS
}

class HistorialViewModel : ViewModel() {
    private val ingresoRepository = IngresoRepository()
    private val egresoRepository = EgresoRepository()

    private val _transacciones = MutableStateFlow<List<TransaccionItem>>(emptyList())
    val transacciones: StateFlow<List<TransaccionItem>> = _transacciones.asStateFlow()

    private val _filtroActual = MutableStateFlow(FiltroHistorial.TODOS)
    val filtroActual: StateFlow<FiltroHistorial> = _filtroActual.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var todosLosIngresos = listOf<Ingreso>()
    private var todosLosEgresos = listOf<Egreso>()

    fun cargarTransacciones(usuarioId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d("HistorialViewModel", "Cargando transacciones para usuario: $usuarioId")

            // Cargar ingresos
            ingresoRepository.obtenerIngresos(usuarioId) { ingresos ->
                Log.d("HistorialViewModel", "Ingresos recibidos: ${ingresos.size}")
                todosLosIngresos = ingresos
                ingresos.forEach { ingreso ->
                    Log.d("HistorialViewModel", "Ingreso: ${ingreso.nombre} - ${ingreso.monto}")
                }
                actualizarListaFiltrada()
                _isLoading.value = false
            }

            // Cargar egresos
            egresoRepository.obtenerEgresos(usuarioId) { egresos ->
                Log.d("HistorialViewModel", "Egresos recibidos: ${egresos.size}")
                todosLosEgresos = egresos
                egresos.forEach { egreso ->
                    Log.d("HistorialViewModel", "Egreso: ${egreso.nombre} - ${egreso.monto}")
                }
                actualizarListaFiltrada()
                _isLoading.value = false
            }
        }
    }

    fun cambiarFiltro(filtro: FiltroHistorial) {
        _filtroActual.value = filtro
        actualizarListaFiltrada()
    }

    private fun actualizarListaFiltrada() {
        val listaCompleta = mutableListOf<TransaccionItem>()

        when (_filtroActual.value) {
            FiltroHistorial.TODOS -> {
                listaCompleta.addAll(todosLosIngresos.map { it.toTransaccionItem(true) })
                listaCompleta.addAll(todosLosEgresos.map { it.toTransaccionItem(false) })
            }
            FiltroHistorial.INGRESOS -> {
                listaCompleta.addAll(todosLosIngresos.map { it.toTransaccionItem(true) })
            }
            FiltroHistorial.EGRESOS -> {
                listaCompleta.addAll(todosLosEgresos.map { it.toTransaccionItem(false) })
            }
        }

        Log.d("HistorialViewModel", "Total transacciones filtradas: ${listaCompleta.size}")

        // Ordenar por fecha más reciente (usando fechaCreacion que es Long)
        _transacciones.value = listaCompleta.sortedByDescending {
            // Intentar parsear la fecha o usar un valor por defecto
            try {
                "${it.fecha} ${it.hora}"
            } catch (e: Exception) {
                ""
            }
        }

        Log.d("HistorialViewModel", "Transacciones actualizadas: ${_transacciones.value.size}")
    }

    private fun Ingreso.toTransaccionItem(esIngreso: Boolean) = TransaccionItem(
        id = this.id,
        nombre = this.nombre.ifEmpty { this.categoria },
        categoria = this.categoria,
        monto = this.monto,
        fecha = this.obtenerFechaFormateada(),
        hora = this.obtenerHoraFormateada(),
        esIngreso = esIngreso
    )

    private fun Egreso.toTransaccionItem(esIngreso: Boolean) = TransaccionItem(
        id = this.id,
        nombre = this.nombre.ifEmpty { this.categoria },
        categoria = this.categoria,
        monto = this.monto,
        fecha = this.obtenerFechaFormateada(),
        hora = this.obtenerHoraFormateada(),
        esIngreso = esIngreso
    )
}