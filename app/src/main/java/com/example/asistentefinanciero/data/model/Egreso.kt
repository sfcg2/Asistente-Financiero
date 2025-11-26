package com.example.asistentefinanciero.data.model


data class Egreso(
    val id: String = "",
    val categoria: String = "",
    val fecha: String = "",
    val hora: String = "",
    val nombre: String = "",
    val monto: Double = 0.0,
    val repetido: Boolean = false
)