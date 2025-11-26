package com.example.asistentefinanciero.data.model

data class Usuario (
    val id: String = "",
    val nombre: String = "",
    val saldo: Double = 0.0,
    val correo: String = "",
    val documentoIdentidad: String = "",
    val fechaNacimiento: String = "",
    val pais: String = "",
)