package com.example.asistentefinanciero.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asistentefinanciero.data.model.Ingreso
import com.example.asistentefinanciero.data.repository.IngresoRepository
import com.example.asistentefinanciero.data.repository.UsuarioRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IngresoViewModel : ViewModel() {
    private val ingresoRepository = IngresoRepository()
    private val usuarioRepository = UsuarioRepository()

    // Estados del formulario
    private val _cantidad = MutableStateFlow("")
    val cantidad: StateFlow<String> = _cantidad.asStateFlow()

    private val _categoria = MutableStateFlow("")
    val categoria: StateFlow<String> = _categoria.asStateFlow()

    private val _fecha = MutableStateFlow("")
    val fecha: StateFlow<String> = _fecha.asStateFlow()

    private val _hora = MutableStateFlow("")
    val hora: StateFlow<String> = _hora.asStateFlow()

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _seRepite = MutableStateFlow("No se repite")
    val seRepite: StateFlow<String> = _seRepite.asStateFlow()

    // Estados de UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _mensajeExito = MutableStateFlow<String?>(null)
    val mensajeExito: StateFlow<String?> = _mensajeExito.asStateFlow()

    // Funciones para actualizar estados
    fun actualizarCantidad(value: String) {
        _cantidad.value = value
    }

    fun actualizarCategoria(value: String) {
        _categoria.value = value
    }

    fun actualizarFecha(value: String) {
        _fecha.value = value
    }

    fun actualizarHora(value: String) {
        _hora.value = value
    }

    fun actualizarNombre(value: String) {
        _nombre.value = value
    }

    fun actualizarSeRepite(value: String) {
        _seRepite.value = value
    }

    fun limpiarMensaje() {
        _mensaje.value = null
        _mensajeExito.value = null
    }

    private fun convertirStringATimestamp(fecha: String, hora: String): Timestamp? {
        return try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val fechaCompleta = "$fecha $hora"
            val date = dateFormat.parse(fechaCompleta)
            date?.let { Timestamp(it) }
        } catch (e: Exception) {
            null
        }
    }

    fun guardarIngreso(usuarioId: String) {
        viewModelScope.launch {
            // Validaciones
            if (_cantidad.value.isEmpty()) {
                _mensaje.value = "La cantidad es requerida"
                return@launch
            }

            val cantidadDouble = _cantidad.value.toDoubleOrNull()
            if (cantidadDouble == null || cantidadDouble <= 0) {
                _mensaje.value = "Ingresa una cantidad válida"
                return@launch
            }

            if (_categoria.value.isEmpty()) {
                _mensaje.value = "Selecciona una categoría"
                return@launch
            }

            if (_fecha.value.isEmpty()) {
                _mensaje.value = "Selecciona una fecha"
                return@launch
            }

            if (_hora.value.isEmpty()) {
                _mensaje.value = "Selecciona una hora"
                return@launch
            }

            // Validar formato de fecha
            if (!validarFormatoFecha(_fecha.value)) {
                _mensaje.value = "Formato de fecha inválido. Usa dd/MM/yyyy"
                return@launch
            }

            // Validar formato de hora
            if (!validarFormatoHora(_hora.value)) {
                _mensaje.value = "Formato de hora inválido. Usa HH:mm"
                return@launch
            }

            _isLoading.value = true
            _mensaje.value = null

            try {
                // Convertir fecha y hora a Timestamp
                val timestamp = convertirStringATimestamp(_fecha.value, _hora.value)

                if (timestamp == null) {
                    _mensaje.value = "Error al procesar la fecha y hora"
                    _isLoading.value = false
                    return@launch
                }

                val ingreso = Ingreso(
                    usuarioId = usuarioId,
                    monto = cantidadDouble,
                    nombre = _nombre.value,
                    categoria = _categoria.value,
                    fecha = timestamp,
                    seRepite = _seRepite.value != "No se repite",
                    frecuenciaRepeticion = if (_seRepite.value != "No se repite") _seRepite.value else ""
                )

                // Guardar ingreso
                val guardado = ingresoRepository.guardarIngreso(usuarioId, ingreso)

                if (guardado) {
                    // Actualizar saldo del usuario
                    usuarioRepository.incrementarSaldo(usuarioId, cantidadDouble)

                    _mensajeExito.value = "Ingreso guardado exitosamente. Saldo actualizado."

                    // Limpiar formulario
                    limpiarFormulario()
                } else {
                    _mensaje.value = "Error al guardar el ingreso"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun validarFormatoFecha(fecha: String): Boolean {
        return try {
            val regex = Regex("^\\d{2}/\\d{2}/\\d{4}$")
            if (!regex.matches(fecha)) return false

            val partes = fecha.split("/")
            val dia = partes[0].toInt()
            val mes = partes[1].toInt()
            val anio = partes[2].toInt()

            dia in 1..31 && mes in 1..12 && anio >= 2000
        } catch (e: Exception) {
            false
        }
    }

    private fun validarFormatoHora(hora: String): Boolean {
        return try {
            val regex = Regex("^\\d{2}:\\d{2}$")
            if (!regex.matches(hora)) return false

            val partes = hora.split(":")
            val horas = partes[0].toInt()
            val minutos = partes[1].toInt()

            horas in 0..23 && minutos in 0..59
        } catch (e: Exception) {
            false
        }
    }

    private fun limpiarFormulario() {
        _cantidad.value = ""
        _categoria.value = ""
        _fecha.value = ""
        _hora.value = ""
        _nombre.value = ""
        _seRepite.value = "No se repite"
    }
}