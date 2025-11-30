package com.example.asistentefinanciero.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.example.asistentefinanciero.data.model.Usuario
import kotlinx.coroutines.tasks.await

class UsuarioRepository() {
    private val baseDedatos = FirebaseFirestore.getInstance()
    private val coleccionDeUsuario = baseDedatos.collection("usuarios")

    // Crear usuario en Firestore cuando se registra
    suspend fun crearUsuario(usuarioId: String, correo: String): Boolean {
        return try {
            val usuario = hashMapOf(
                "id" to usuarioId,
                "correo" to correo,
                "nombre" to "",
                "documentoIdentidad" to "",
                "fechaNacimiento" to "",
                "pais" to "",
                "saldo" to 0.0
            )

            coleccionDeUsuario.document(usuarioId)
                .set(usuario)
                .await()
            true
        } catch (e: Exception) {
            println("Error al crear usuario: ${e.message}")
            false
        }
    }

    //Verificar si el usuario existe en Firestore
    suspend fun existeUsuario(usuarioId: String): Boolean {
        return try {
            val document = coleccionDeUsuario.document(usuarioId).get().await()
            document.exists()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun obtenerUsuario(usuarioId: String): Usuario? {
        return try {
            val document = coleccionDeUsuario.document(usuarioId).get().await()
            document.toObject(Usuario::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            null
        }
    }

    //Actualizar datos del perfil del usuario
    suspend fun actualizarPerfil(
        usuarioId: String,
        nombre: String? = null,
        documentoIdentidad: String? = null,
        fechaNacimiento: String? = null,
        pais: String? = null
    ): Boolean {
        return try {
            val updates = mutableMapOf<String, Any>()
            nombre?.let { updates["nombre"] = it }
            documentoIdentidad?.let { updates["documentoIdentidad"] = it }
            fechaNacimiento?.let { updates["fechaNacimiento"] = it }
            pais?.let { updates["pais"] = it }

            if (updates.isNotEmpty()) {
                coleccionDeUsuario.document(usuarioId)
                    .update(updates)
                    .await()
            }
            true
        } catch (e: Exception) {
            println("Error al actualizar perfil: ${e.message}")
            false
        }
    }

    suspend fun actualizarSaldo(usuarioId: String, nuevoSaldo: Double) {
        try {
            coleccionDeUsuario.document(usuarioId)
                .update("saldo", nuevoSaldo)
                .await()
        } catch (e: Exception) {
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

    suspend fun decrementarSaldo(usuarioId: String, cantidad: Double) {
        try {
            val usuario = obtenerUsuario(usuarioId)
            if (usuario != null) {
                val nuevoSaldo = usuario.saldo - cantidad
                actualizarSaldo(usuarioId, nuevoSaldo)
            }
        } catch (e: Exception) {
            println("Error al decrementar saldo: ${e.message}")
        }
    }
}