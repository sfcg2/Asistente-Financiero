package com.example.asistentefinanciero.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.asistentefinanciero.ui.vistas.egreso.RegistrarEgresoVista
import com.example.asistentefinanciero.ui.vistas.transacciones.HistorialVista
import com.example.asistentefinanciero.ui.vistas.home.HomeVista
import com.example.asistentefinanciero.ui.vistas.transacciones.RegistrarIngresoVista
import com.example.asistentefinanciero.viewmodel.EgresoViewModel
import com.example.asistentefinanciero.viewmodel.HistorialViewModel
import com.example.asistentefinanciero.viewmodel.IngresoViewModel

enum class Pantalla {
    HOME,
    REGISTRAR_INGRESO,
    REGISTRAR_EGRESO,
    HISTORIAL,
    CALENDARIO,
    ESTADISTICAS
}

@Composable
fun AppNavigation(
    usuarioId: String = "K6Tr9DTjDIMGf7PFG4MH"
) {
    var pantallaActual by remember { mutableStateOf(Pantalla.HOME) }

    when (pantallaActual) {
        Pantalla.HOME -> {
            HomeVista(
                onRegistrarIngreso = { pantallaActual = Pantalla.REGISTRAR_INGRESO },
                onRegistrarEgreso = { pantallaActual = Pantalla.REGISTRAR_EGRESO },
                onVerCalendario = { pantallaActual = Pantalla.CALENDARIO },
                onVerEstadisticas = { pantallaActual = Pantalla.ESTADISTICAS },
                onVerHistorial = { pantallaActual = Pantalla.HISTORIAL },
                onCerrarSesion = { /* Implementar logout */ },
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
                usuarioId = usuarioId,
                onVolver = { pantallaActual = Pantalla.HOME },
                onVerHistorial = { pantallaActual = Pantalla.HISTORIAL }
            )
        }

        Pantalla.REGISTRAR_EGRESO -> {
            val viewModel: EgresoViewModel = viewModel()
            RegistrarEgresoVista(
                viewModel = viewModel,
                usuarioId = usuarioId,
                onVolver = { pantallaActual = Pantalla.HOME },
                onVerHistorial = { pantallaActual = Pantalla.HISTORIAL }
            )
        }

        Pantalla.HISTORIAL -> {
            val viewModel: HistorialViewModel = viewModel()
            HistorialVista(
                viewModel = viewModel,
                usuarioId = usuarioId,
                onVolver = { pantallaActual = Pantalla.HOME },
                onVerCalendario = { pantallaActual = Pantalla.CALENDARIO },
                onVerInicio = { pantallaActual = Pantalla.HOME }
            )
        }

        Pantalla.CALENDARIO -> {
            // TODO: Implementar vista de calendario
            LaunchedEffect(Unit) {
                pantallaActual = Pantalla.HOME
            }
        }

        Pantalla.ESTADISTICAS -> {
            // TODO: Implementar vista de estadísticas
            LaunchedEffect(Unit) {
                pantallaActual = Pantalla.HOME
            }
        }
    }
}