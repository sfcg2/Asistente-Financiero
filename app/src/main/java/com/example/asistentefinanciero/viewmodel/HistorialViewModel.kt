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
import java.text.SimpleDateFormat
import java.util.*


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

    private val _mesFiltro = MutableStateFlow<Int?>(null)
    val mesFiltro: StateFlow<Int?> = _mesFiltro.asStateFlow()

    private var todosLosIngresos = listOf<Ingreso>()
    private var todosLosEgresos = listOf<Egreso>()

    /**
     * Carga las transacciones y establece un filtro mensual opcional.
     * @param usuarioId El ID del usuario actual.
     * @param mes Número de mes (1 a 12) o null para ver todos los meses.
     */
    fun cargarTransacciones(usuarioId: String, mes: Int? = null) {
        // 1. Actualiza el filtro de mes
        _mesFiltro.value = mes

        // 2. Carga la data (solo si es necesario, o al iniciar)
        if (todosLosIngresos.isEmpty() || todosLosEgresos.isEmpty() || mes == null) {
            viewModelScope.launch {
                _isLoading.value = true
                Log.d("HistorialViewModel", "Cargando transacciones para usuario: $usuarioId")

                ingresoRepository.obtenerIngresos(usuarioId) { ingresos ->
                    todosLosIngresos = ingresos
                    actualizarListaFiltrada()
                    _isLoading.value = false
                }

                egresoRepository.obtenerEgresos(usuarioId) { egresos ->
                    todosLosEgresos = egresos
                    actualizarListaFiltrada()
                    _isLoading.value = false
                }
            }
        } else {
            actualizarListaFiltrada()
        }
    }

    fun cambiarFiltro(filtro: FiltroHistorial) {
        _filtroActual.value = filtro
        actualizarListaFiltrada()
    }

    private fun actualizarListaFiltrada() {
        val listaPorTipo = mutableListOf<TransaccionItem>()

        when (_filtroActual.value) {
            FiltroHistorial.TODOS -> {
                listaPorTipo.addAll(todosLosIngresos.map { it.toTransaccionItem(true) })
                listaPorTipo.addAll(todosLosEgresos.map { it.toTransaccionItem(false) })
            }
            FiltroHistorial.INGRESOS -> {
                listaPorTipo.addAll(todosLosIngresos.map { it.toTransaccionItem(true) })
            }
            FiltroHistorial.EGRESOS -> {
                listaPorTipo.addAll(todosLosEgresos.map { it.toTransaccionItem(false) })
            }
        }

        val mesDeseado = _mesFiltro.value
        val listaPorTipoYMes = if (mesDeseado != null) {
            listaPorTipo.filter { transaccion ->
                obtenerMesDeFecha(transaccion.fecha) == mesDeseado
            }
        } else {
            listaPorTipo
        }

        Log.d("HistorialViewModel", "Total transacciones filtradas: ${listaPorTipoYMes.size} (Mes: $mesDeseado, Tipo: ${_filtroActual.value})")

        // --- 3. Ordenar y Asignar ---
        _transacciones.value = listaPorTipoYMes.sortedByDescending {
            try {
                // Combina fecha y hora para una ordenación precisa
                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse("${it.fecha} ${it.hora}")?.time ?: 0L
            } catch (e: Exception) {
                0L
            }
        }

        Log.d("HistorialViewModel", "Transacciones actualizadas: ${_transacciones.value.size}")
    }

    private fun obtenerMesDeFecha(fechaString: String): Int? {
        return try {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = format.parse(fechaString)
            // El mes en Calendar es de 0 a 11, por lo que sumamos 1
            if (date != null) {
                val cal = Calendar.getInstance()
                cal.time = date
                cal.get(Calendar.MONTH) + 1
            } else null
        } catch (e: Exception) {
            Log.e("HistorialViewModel", "Error al parsear fecha para mes: $fechaString", e)
            null
        }
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

    override fun onCleared() {
        super.onCleared()
        ingresoRepository.cancelarListener()
        egresoRepository.cancelarListener()
    }
}