package com.example.asistentefinanciero.data.repository

import com.example.asistentefinanciero.data.model.Ingreso
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class IngresoRepository {
    private val baseDeDatos = FirebaseFirestore.getInstance()

    suspend fun guardarIngreso(usuarioId: String, ingreso: Ingreso): Boolean {
        return try {
            baseDeDatos.collection("usuarios")
                .document(usuarioId)
                .collection("ingresos")
                .add(ingreso)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun obtenerIngresos(usuarioId: String, onResult: (List<Ingreso>) -> Unit) {
        baseDeDatos.collection("usuarios")
            .document(usuarioId)
            .collection("ingresos")
            .orderBy("fecha", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onResult(emptyList())
                    return@addSnapshotListener
                }

                val ingresos = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Ingreso::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                onResult(ingresos)
            }
    }
}