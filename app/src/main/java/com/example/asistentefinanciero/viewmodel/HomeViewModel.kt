package com.example.asistentefinanciero.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asistentefinanciero.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val firebaseRepository: FirebaseRepository = FirebaseRepository()
) : ViewModel() {

    private val _saldoActual = MutableStateFlow(0.0)
    val saldoActual = _saldoActual.asStateFlow()

    private val _ingresosMes = MutableStateFlow(0.0)
    val ingresosMes = _ingresosMes.asStateFlow()

    private val _egresosMes = MutableStateFlow(0.0)
    val egresosMes = _egresosMes.asStateFlow()

    fun cargarDatos(usuarioId: String) {
        viewModelScope.launch {
            _saldoActual.value = firebaseRepository.obtenerSaldo(usuarioId)
            _ingresosMes.value = firebaseRepository.obtenerIngresosMes(usuarioId)
            _egresosMes.value = firebaseRepository.obtenerEgresosMes(usuarioId)
        }
    }
}