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

    /**
     * Carga los ingresos del usuario y opcionalmente filtra por mes
     */
    }