package com.example.asistentefinanciero.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.example.asistentefinanciero.data.model.Usuario
import kotlinx.coroutines.tasks.await


class UsuarioRepository {
    private val baseDedatos = FirebaseFirestore.getInstance()
    private val coleccionDeUsario = baseDedatos.collection("usuarios")

    suspend fun obtenerUsuario(usuarioId: String): Usuario? {
        return try {
            val document = coleccionDeUsario.document(usuarioId).get().await()
            document.toObject(Usuario::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun actualizarSaldo(usuarioId: String, nuevoSaldo: Double) {
        try {
            coleccionDeUsario.document(usuarioId)
                .update("saldo", nuevoSaldo)
                .await()
        } catch (e: Exception) {
            // Manejar error si es necesario
            println("Error al actualizar saldo: ${e.message}")
        }
    }

    suspend fun incrementarSaldo(usuarioId: String, cantidad: Double) {
        try {
            val usuario = obtenerUsuario(usuarioId)
            if (usuario != null) {
                val nuevoSaldo = usuario.saldo + cantidad
                actualizarSaldo(usuarioId, nuevoSaldo)
            }
        } catch (e: Exception) {
            println("Error al incrementar saldo: ${e.message}")
        }
    }
}