package com.example.asistentefinanciero.data.repository

import com.example.asistentefinanciero.data.model.Ingreso
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class IngresoRepository {
    private val baseDeDatos = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration? = null // ✅ NUEVO

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

    // ✅ MODIFICADO: Retorna el listener para poder cancelarlo
    fun obtenerIngresos(usuarioId: String, onResult: (List<Ingreso>) -> Unit): ListenerRegistration {
        // Cancelar listener anterior si existe
        listenerRegistration?.remove()

        listenerRegistration = baseDeDatos.collection("usuarios")
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

        return listenerRegistration!!
    }

    // ✅ NUEVO: Método para cancelar listener
    fun cancelarListener() {
        listenerRegistration?.remove()
        listenerRegistration = null
    }

    // Nueva función para actualizar ingreso
    suspend fun actualizarIngreso(usuarioId: String, ingreso: Ingreso): Boolean {
        return try {
            baseDeDatos.collection("usuarios")
                .document(usuarioId)
                .collection("ingresos")
                .document(ingreso.id)
                .set(ingreso)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Nueva función para eliminar ingreso
    suspend fun eliminarIngreso(usuarioId: String, ingresoId: String): Boolean {
        return try {
            baseDeDatos.collection("usuarios")
                .document(usuarioId)
                .collection("ingresos")
                .document(ingresoId)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Nueva función para obtener un ingreso específico
    suspend fun obtenerIngreso(usuarioId: String, ingresoId: String): Ingreso? {
        return try {
            val doc = baseDeDatos.collection("usuarios")
                .document(usuarioId)
                .collection("ingresos")
                .document(ingresoId)
                .get()
                .await()
            doc.toObject(Ingreso::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            null
        }
    }
}