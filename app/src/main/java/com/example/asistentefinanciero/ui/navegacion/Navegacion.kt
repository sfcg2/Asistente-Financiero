package com.example.asistentefinanciero.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.asistentefinanciero.ui.vistas.transacciones.RegistrarEgresoVista
import com.example.asistentefinanciero.ui.vistas.transacciones.HistorialVista
import com.example.asistentefinanciero.ui.vistas.home.HomeVista
import com.example.asistentefinanciero.ui.vistas.login.LoginVista
import com.example.asistentefinanciero.ui.vistas.login.RegisterVista
import com.example.asistentefinanciero.ui.vistas.transacciones.RegistrarIngresoVista
import com.example.asistentefinanciero.ui.vistas.transacciones.CalendarioVista
import com.example.asistentefinanciero.ui.vistas.transacciones.EstadisticaVista
import com.example.asistentefinanciero.viewmodel.AuthViewModel
import com.example.asistentefinanciero.viewmodel.EgresoViewModel
import com.example.asistentefinanciero.viewmodel.EstadisticasViewModel
import com.example.asistentefinanciero.viewmodel.HistorialViewModel
import com.example.asistentefinanciero.viewmodel.IngresoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

enum class Pantalla {
    HOME,

    LOGIN,

    REGISTER,

    REGISTRAR_INGRESO,
    REGISTRAR_EGRESO,
    HISTORIAL,
    CALENDARIO,
    ESTADISTICAS
}

data class NavState(
    val pantalla: Pantalla,
    val mesFiltro: Int? = null // Argumento para filtrar el historial por mes (1-12)
)

@Composable
fun AppNavigation(
    //usuarioId: String = "K6Tr9DTjDIMGf7PFG4MH"
) {

    val historialViewModel: HistorialViewModel = viewModel() // Creamos el ViewModel una sola vez aquí
    val estadisticasViewModel: EstadisticasViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    //val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val pantallaInicial = if (authViewModel.haySesionActiva()) {
        Pantalla.HOME
    } else {
        Pantalla.LOGIN
    }

    var navState by remember { mutableStateOf(NavState(pantallaInicial)) }


    when (navState.pantalla) {
        Pantalla.HOME -> {
            HomeVista(
                onRegistrarIngreso = { navState = NavState(Pantalla.REGISTRAR_INGRESO) },
                onRegistrarEgreso = { navState = NavState(Pantalla.REGISTRAR_EGRESO) },
                onVerCalendario = { navState = NavState(Pantalla.CALENDARIO) },
                onVerEstadisticas = { navState = NavState(Pantalla.ESTADISTICAS) },
                // Cuando vamos al historial desde el Home, el filtro de mes es NULL (todos)
                onVerHistorial = { navState = NavState(Pantalla.HISTORIAL, mesFiltro = null) },
                onCerrarSesion = {
                    authViewModel.logout()
                    navState = NavState(Pantalla.LOGIN) },

                onIrPerfil = { /* Implementar perfil */ },
                onIrSeguridad = { /* Implementar seguridad */ },
                onIrNotificaciones = { /* Implementar notificaciones */ },
                onIrTerminos = { /* Implementar términos */ }
            )
        }

        Pantalla.REGISTRAR_INGRESO -> {
            val viewModel: IngresoViewModel = viewModel()
            RegistrarIngresoVista(
                viewModel = viewModel,
                //usuarioId = usuarioId,
                onVolver = { navState = NavState(Pantalla.HOME) },
                onVerCalendario = { navState = NavState(Pantalla.CALENDARIO) },
                onVerInicio = { navState = NavState(Pantalla.HOME) },
                onVerHistorial = { navState = NavState(Pantalla.HISTORIAL) }
            )
        }

        Pantalla.REGISTRAR_EGRESO -> {
            val viewModel: EgresoViewModel = viewModel()
            RegistrarEgresoVista(
                viewModel = viewModel,
                //usuarioId = usuarioId,
                onVolver = { navState = NavState(Pantalla.HOME) },
                onVerCalendario = { navState = NavState(Pantalla.CALENDARIO) },
                onVerInicio = { navState = NavState(Pantalla.HOME) },
                onVerHistorial = { navState = NavState(Pantalla.HISTORIAL) }
            )
        }

        Pantalla.HISTORIAL -> {
            HistorialVista(
                viewModel = historialViewModel,
                //usuarioId = usuarioId,
                // Le pasamos el filtro de mes actual al Composable
                mesFiltroInicial = navState.mesFiltro,
                onVolver = { navState = NavState(Pantalla.HOME) },
                onVerCalendario = { navState = NavState(Pantalla.CALENDARIO) },
                onVerInicio = { navState = NavState(Pantalla.HOME) }
            )
        }

        Pantalla.CALENDARIO -> {
            CalendarioVista(
                onVolver = { navState = NavState(Pantalla.HOME) },
                onMesSeleccionado = { mes ->
                    navState = NavState(Pantalla.HISTORIAL, mesFiltro = mes)},
                    onVerInicio = { navState = NavState(Pantalla.HOME) },
                    onVerHistorial = { navState = NavState(Pantalla.HISTORIAL, mesFiltro = null) }
            )
        }

        Pantalla.ESTADISTICAS -> {
            EstadisticaVista(
                viewModel = estadisticasViewModel,
                onVolver = { navState = NavState(Pantalla.HOME) },
                onVerCalendario = { navState = NavState(Pantalla.CALENDARIO) },
                onVerInicio = { navState = NavState(Pantalla.HOME) },
                onVerHistorial = { navState = NavState(Pantalla.HISTORIAL) }
            )
        }

        Pantalla.LOGIN -> {
            LoginVista(
                viewModel = authViewModel,
                onLoginExitoso = {navState = NavState(Pantalla.HOME)},
                onIrARegistro = {navState = NavState(Pantalla.REGISTER)}
            )
        }
        Pantalla.REGISTER -> {
            RegisterVista(
                viewModel = authViewModel,
                onRegistroExitoso = {navState = NavState(Pantalla.HOME)},
                onIrALogin = {navState = NavState(Pantalla.LOGIN)}
            )

        }
    }
}