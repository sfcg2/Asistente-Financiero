package com.example.asistentefinanciero.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asistentefinanciero.data.repository.UsuarioRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeViewModel : ViewModel() {

    private val usuarioRepository = UsuarioRepository()

    private val _saldoActual = MutableStateFlow(0.0)
    val saldoActual = _saldoActual.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // ✅ OPTIMIZADO: Solo cargamos el saldo, no ingresos/egresos
    fun cargarDatos(usuarioId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Solo obtener el saldo del usuario
                val usuario = usuarioRepository.obtenerUsuario(usuarioId)
                _saldoActual.value = usuario?.saldo ?: 0.0
            } catch (e: Exception) {
                _saldoActual.value = 0.0
            } finally {
                _isLoading.value = false
            }
        }
    }
}