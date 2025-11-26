package com.example.asistentefinanciero.data.model

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class Egreso(
    var id: String = "",
    val usuarioId: String = "",
    var monto: Double = 0.0,
    var nombre: String = "",
    var categoria: String = "",
    var fecha: Timestamp = Timestamp.now(),
    var seRepite: Boolean = false,
    var frecuenciaRepeticion: String = "",
) {
    // Función helper para mostrar fecha formateada en UI
    fun obtenerFechaFormateada(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(fecha.toDate())
    }

    fun obtenerHoraFormateada(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(fecha.toDate())
    }

    // Función para obtener el mes
    fun obtenerMes(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = fecha.toDate()
        return calendar.get(Calendar.MONTH)
    }

    // Función para obtener el año
    fun obtenerAnio(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = fecha.toDate()
        return calendar.get(Calendar.YEAR)
    }
}