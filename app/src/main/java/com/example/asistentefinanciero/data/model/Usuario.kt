package com.example.asistentefinanciero.data.model

data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val saldoInicial: Double = 0.0,
    val fechaRegistro: Long = System.currentTimeMillis()
)