package com.example.asistentefinanciero.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.example.asistentefinanciero.data.repository.UsuarioRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val usuarioRepository = UsuarioRepository()

    private val _cargando = MutableStateFlow(false)
    val cargando = _cargando.asStateFlow()

    private val _errorMensaje = MutableStateFlow<String?>(null)
    val errorMensaje = _errorMensaje.asStateFlow()

    fun registrar(
        correo: String,
        contrasena: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                _cargando.value = true
                _errorMensaje.value = null

                // Validación de contraseña
                if (contrasena.length < 6) {
                    _errorMensaje.value = "La contraseña debe tener al menos 6 caracteres"
                    return@launch
                }

                // Crear usuario en Firebase Auth
                val result = auth.createUserWithEmailAndPassword(correo, contrasena).await()
                val userId = result.user?.uid ?: throw Exception("No se pudo obtener el ID del usuario")

                // Crear documento del usuario en Firestore usando el Repository
                val creado = usuarioRepository.crearUsuario(userId, correo)
                if (!creado) {
                    // Si falla, eliminar el usuario de Auth
                    auth.currentUser?.delete()?.await()
                    throw Exception("Error al crear el perfil del usuario")
                }

                onSuccess()
            } catch (e: FirebaseAuthException) {
                _errorMensaje.value = when (e.errorCode) {
                    "ERROR_INVALID_EMAIL" -> "Correo electrónico inválido"
                    "ERROR_EMAIL_ALREADY_IN_USE" -> "Este correo ya está registrado"
                    "ERROR_WEAK_PASSWORD" -> "La contraseña es muy débil"
                    else -> e.message ?: "Error al registrar"
                }
            } catch (e: Exception) {
                _errorMensaje.value = e.message ?: "Error al registrar"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun login(
        correo: String,
        contrasena: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                _cargando.value = true
                _errorMensaje.value = null

                auth.signInWithEmailAndPassword(correo, contrasena).await()
                onSuccess()
            } catch (e: FirebaseAuthException) {
                _errorMensaje.value = when (e.errorCode) {
                    "ERROR_INVALID_EMAIL" -> "Correo electrónico inválido"
                    "ERROR_WRONG_PASSWORD" -> "Contraseña incorrecta"
                    "ERROR_USER_NOT_FOUND" -> "Usuario no encontrado"
                    "ERROR_USER_DISABLED" -> "Usuario deshabilitado"
                    "ERROR_INVALID_CREDENTIAL" -> "Credenciales inválidas"
                    else -> e.message ?: "Error al iniciar sesión"
                }
            } catch (e: Exception) {
                _errorMensaje.value = e.message ?: "Error al iniciar sesión"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun logout() {
        auth.signOut()
        limpiarError()
    }

    fun haySesionActiva(): Boolean {
        return auth.currentUser != null
    }

    fun setError(msg: String) {
        _errorMensaje.value = msg
    }

    fun limpiarError() {
        _errorMensaje.value = null
    }

    fun obtenerUsuarioActual() = auth.currentUser
}