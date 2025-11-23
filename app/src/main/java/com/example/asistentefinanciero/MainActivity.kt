package com.example.asistentefinanciero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.asistentefinanciero.ui.theme.AsistenteFinancieroTheme
import com.example.asistentefinanciero.ui.vistas.home.HomeVista
import com.example.asistentefinanciero.ui.vistas.login.LoginVista
import com.example.asistentefinanciero.ui.vistas.login.RegistrarseVista
import com.example.asistentefinanciero.ui.vistas.transacciones.RegistrarIngresoVista
import com.example.asistentefinanciero.ui.vistas.transacciones.RegistrarEgresoVista
import com.example.asistentefinanciero.ui.vistas.transacciones.HistorialVista
import com.example.asistentefinanciero.ui.vistas.transacciones.CalendarioVista
import com.example.asistentefinanciero.ui.vistas.perfil.PerfilVista
import com.example.asistentefinanciero.ui.vistas.perfil.SeguridadVista
import com.example.asistentefinanciero.ui.vistas.perfil.NotificacionesVista
import com.example.asistentefinanciero.ui.vistas.perfil.TerminosVista

sealed class Pantalla {
    object Login : Pantalla()
    object Registro : Pantalla()
    object Home : Pantalla()
    object RegistrarIngreso : Pantalla()
    object RegistrarEgreso : Pantalla()
    object Historial : Pantalla()
    object Calendario : Pantalla()
    object Perfil : Pantalla()
    object Seguridad : Pantalla()
    object Notificaciones : Pantalla()
    object Terminos : Pantalla()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AsistenteFinancieroTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }

    }
}

@Composable
fun AppNavigation() {
    var pantallaActual by remember { mutableStateOf<Pantalla>(Pantalla.Login) }
    var pantallaAnterior by remember { mutableStateOf<Pantalla?>(null) }

    when (pantallaActual) {
        is Pantalla.Login -> {
            LoginVista(
                onLoginExitoso = {
                    pantallaActual = Pantalla.Home
                },
                onIrRegistro = {
                    pantallaActual = Pantalla.Registro
                }
            )
        }

        is Pantalla.Registro -> {
            RegistrarseVista(
                onRegistroExitoso = {
                    pantallaActual = Pantalla.Home
                },
                onVolverLogin = {
                    pantallaActual = Pantalla.Login
                }
            )
        }

        is Pantalla.Home -> {
            HomeVista(
                onCerrarSesion = {
                    pantallaActual = Pantalla.Login
                },
                onRegistrarIngreso = {
                    pantallaActual = Pantalla.RegistrarIngreso
                },
                onRegistrarEgreso = {
                    pantallaActual = Pantalla.RegistrarEgreso
                },
                onVerCalendario = {
                    pantallaActual = Pantalla.Calendario
                },
                onVerEstadisticas = {
                    // TODO: Implementar estadísticas
                },
                onVerHistorial = {
                    pantallaActual = Pantalla.Historial
                }
            )
        }

        is Pantalla.RegistrarIngreso -> {
            RegistrarIngresoVista(
                onVolver = {
                    pantallaActual = Pantalla.Home
                },
                onVerHistorial = {
                    pantallaActual = Pantalla.Historial
                }
            )
        }

        is Pantalla.RegistrarEgreso -> {
            RegistrarEgresoVista(
                onVolver = {
                    pantallaActual = Pantalla.Home
                },
                onVerHistorial = {
                    pantallaActual = Pantalla.Historial
                }
            )
        }

        is Pantalla.Historial -> {
            HistorialVista(
                onVolver = {
                    pantallaActual = Pantalla.Home
                },
                onCerrarSesion = {
                    pantallaActual = Pantalla.Login
                },
                onVerCalendario = {
                    pantallaActual = Pantalla.Calendario
                }
            )
        }

        is Pantalla.Calendario -> {
            CalendarioVista(
                onVolver = {
                    pantallaActual = Pantalla.Home
                },
                onVerHistorial = {
                    pantallaActual = Pantalla.Historial
                },
                onCerrarSesion = {
                    pantallaActual = Pantalla.Login
                }
            )
        }

        is Pantalla.Perfil -> {
            PerfilVista(
                onVolver = {
                    pantallaActual = pantallaAnterior ?: Pantalla.Home
                }
            )
        }

        is Pantalla.Seguridad -> {
            SeguridadVista(
                onVolver = {
                    pantallaActual = pantallaAnterior ?: Pantalla.Home
                }
            )
        }

        is Pantalla.Notificaciones -> {
            NotificacionesVista(
                onVolver = {
                    pantallaActual = pantallaAnterior ?: Pantalla.Home
                }
            )
        }

        is Pantalla.Terminos -> {
            TerminosVista(
                onVolver = {
                    pantallaActual = pantallaAnterior ?: Pantalla.Home
                }
            )
        }
    }
}