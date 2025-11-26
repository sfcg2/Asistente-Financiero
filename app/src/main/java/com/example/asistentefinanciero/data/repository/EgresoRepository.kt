package com.example.asistentefinanciero.data.repository

import com.example.asistentefinanciero.data.model.Egreso
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class EgresoRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun guardarEgreso(usuarioId: String, egreso: Egreso): Boolean {
        return try {
            db.collection("usuarios")
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
        db.collection("usuarios")
            .document(usuarioId)
            .collection("egresos")
            .orderBy("fechaCreacion", Query.Direction.DESCENDING)
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
}