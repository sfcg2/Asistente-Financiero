package com.example.asistentefinanciero.data.repository
import com.example.asistentefinanciero.data.model.Egreso
import com.example.asistentefinanciero.data.model.Ingreso
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class FirebaseRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun obtenerSaldo(usuarioId: String): Double {
        val doc = db.collection("usuarios").document(usuarioId).get().await()
        return doc.getDouble("saldo") ?: 0.0
    }

    suspend fun obtenerIngresosMes(usuarioId: String): Double {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1

        val snapshot = db.collection("usuarios")
            .document(usuarioId)
            .collection("ingresos")
            .whereEqualTo("anio", year)
            .whereEqualTo("mes", month)
            .get()
            .await()

        var total = 0.0
        for (doc in snapshot) {
            total += doc.getDouble("monto") ?: 0.0
        }
        return total
    }

    suspend fun obtenerEgresosMes(usuarioId: String): Double {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1

        val snapshot = db.collection("usuarios")
            .document(usuarioId)
            .collection("egresos")
            .whereEqualTo("anio", year)
            .whereEqualTo("mes", month)
            .get()
            .await()

        var total = 0.0
        for (doc in snapshot) {
            total += doc.getDouble("monto") ?: 0.0
        }
        return total
    }
}