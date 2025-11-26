package com.example.asistentefinanciero.data.model

import com.google.firebase.Timestamp

data class Ingreso(
    var id: String = "",
    val usuarioId: String = "",
    var monto: Double = 0.0,
    var nombre: String = "",
    var categoria: String = "",
    var fecha: Timestamp = Timestamp.now(),
    var seRepite: Boolean = false,
    var frecuenciaRepeticion: String = "",
)