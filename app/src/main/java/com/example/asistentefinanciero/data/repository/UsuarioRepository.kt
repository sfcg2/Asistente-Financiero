package com.example.asistentefinanciero.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.asistentefinanciero.data.model.Usuario
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UsuarioRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("ControlCashPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_USUARIOS = "usuarios"
        private const val KEY_USUARIO_ACTUAL = "usuario_actual"
    }

    // Obtener todos los usuarios
    private fun obtenerUsuarios(): MutableList<Usuario> {
        val json = prefs.getString(KEY_USUARIOS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Usuario>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    // Guardar lista de usuarios
    private fun guardarUsuarios(usuarios: List<Usuario>) {
        val json = gson.toJson(usuarios)
        prefs.edit().putString(KEY_USUARIOS, json).apply()
    }

    // Registrar nuevo usuario
    fun registrarUsuario(usuario: Usuario): Result<Usuario> {
        val usuarios = obtenerUsuarios()

        // Verificar si el correo ya existe
        if (usuarios.any { it.correo == usuario.correo }) {
            return Result.failure(Exception("El correo ya está registrado"))
        }

        // Agregar nuevo usuario
        val nuevoUsuario = usuario.copy(id = System.currentTimeMillis().toString())
        usuarios.add(nuevoUsuario)
        guardarUsuarios(usuarios)

        return Result.success(nuevoUsuario)
    }

    // Iniciar sesión
    fun iniciarSesion(correo: String, contrasena: String): Result<Usuario> {
        val usuarios = obtenerUsuarios()
        val usuario = usuarios.find {
            it.correo == correo && it.contrasena == contrasena
        }

        return if (usuario != null) {
            // Guardar usuario actual
            val json = gson.toJson(usuario)
            prefs.edit().putString(KEY_USUARIO_ACTUAL, json).apply()
            Result.success(usuario)
        } else {
            Result.failure(Exception("Correo o contraseña incorrectos"))
        }
    }

    // Obtener usuario actual
    fun obtenerUsuarioActual(): Usuario? {
        val json = prefs.getString(KEY_USUARIO_ACTUAL, null)
        return if (json != null) {
            gson.fromJson(json, Usuario::class.java)
        } else {
            null
        }
    }

    // Cerrar sesión
    fun cerrarSesion() {
        prefs.edit().remove(KEY_USUARIO_ACTUAL).apply()
    }

    // Verificar si hay sesión activa
    fun haySesionActiva(): Boolean {
        return obtenerUsuarioActual() != null
    }
}