package com.example.asistentefinanciero

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.asistentefinanciero.data.repository.UsuarioRepository
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.OptIn
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import com.example.asistentefinanciero.viewmodel.AuthViewModel

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelAndroidTest {

    private lateinit var auth: FirebaseAuth
    private lateinit var usuarioRepository: UsuarioRepository
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        FirebaseApp.initializeApp(
            ApplicationProvider.getApplicationContext()
        )

        auth = FirebaseAuth.getInstance()
        usuarioRepository = UsuarioRepository()
        viewModel = AuthViewModel(auth, usuarioRepository)
    }

    @After
    fun tearDown() {
        auth.signOut()
    }

    @Test
    fun login_real_con_firebase_no_muestra_error() = runBlocking {
        val correo = "test@test.com"
        val contrasena = "123456"

        viewModel.login(correo, contrasena) {}

        delay(2000)

        assertNotNull(auth.currentUser)
        assertNull(viewModel.errorMensaje.value)
    }
}
