package com.example.asistentefinanciero.data.repository
import com.example.asistentefinanciero.data.model.Egreso
import com.example.asistentefinanciero.data.model.Ingreso
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()

    // Guardar un ingreso
    suspend fun guardarIngreso(ingreso: Ingreso) {
        try {
            db.collection("ingresos").add(ingreso).await()
        } catch (e: Exception) {
            throw Exception("Error al guardar ingreso: ${e.message}")
        }
    }

    // Guardar un egreso
    suspend fun guardarEgreso(egreso: Egreso) {
        try {
            db.collection("egresos").add(egreso).await()
        } catch (e: Exception) {
            throw Exception("Error al guardar egreso: ${e.message}")
        }
    }
}