package com.example.asistentefinanciero.data.repository

import com.example.asistentefinanciero.data.model.Egreso
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class EgresoRepository {
    private val baseDeDatos = FirebaseFirestore.getInstance()

    suspend fun guardarEgreso(usuarioId: String, egreso: Egreso): Boolean {
        return try {
            baseDeDatos.collection("usuarios")
                .document(usuarioId)
                .collection("egresos")
                .add(egreso)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun obtenerEgresos(usuarioId: String, onResult: (List<Egreso>) -> Unit) {
        baseDeDatos.collection("usuarios")
            .document(usuarioId)
            .collection("egresos")
            .orderBy("fecha", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onResult(emptyList())
                    return@addSnapshotListener
                }

                val egresos = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Egreso::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                onResult(egresos)
            }
    }

    // Nueva función para actualizar egreso
    suspend fun actualizarEgreso(usuarioId: String, egreso: Egreso): Boolean {
        return try {
            baseDeDatos.collection("usuarios")
                .document(usuarioId)
                .collection("egresos")
                .document(egreso.id)
                .set(egreso)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Nueva función para eliminar egreso
    suspend fun eliminarEgreso(usuarioId: String, egresoId: String): Boolean {
        return try {
            baseDeDatos.collection("usuarios")
                .document(usuarioId)
                .collection("egresos")
                .document(egresoId)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Nueva función para obtener un egreso específico
    suspend fun obtenerEgreso(usuarioId: String, egresoId: String): Egreso? {
        return try {
            val doc = baseDeDatos.collection("usuarios")
                .document(usuarioId)
                .collection("egresos")
                .document(egresoId)
                .get()
                .await()
            doc.toObject(Egreso::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            null
        }
    }
}