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
import com.example.asistentefinanciero.ui.vistas.transacciones.RegistrarIngresoVista
import com.example.asistentefinanciero.ui.vistas.transacciones.CalendarioVista
import com.example.asistentefinanciero.ui.vistas.transacciones.EstadisticaVista
import com.example.asistentefinanciero.viewmodel.EgresoViewModel
import com.example.asistentefinanciero.viewmodel.EstadisticasViewModel
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

data class NavState(
    val pantalla: Pantalla,
    val mesFiltro: Int? = null // Argumento para filtrar el historial por mes (1-12)
)

@Composable
fun AppNavigation(
    usuarioId: String = "K6Tr9DTjDIMGf7PFG4MH"
) {
    var navState by remember { mutableStateOf(NavState(Pantalla.HOME)) }
    val historialViewModel: HistorialViewModel = viewModel() // Creamos el ViewModel una sola vez aquí
    val estadisticasViewModel: EstadisticasViewModel = viewModel()

    when (navState.pantalla) {
        Pantalla.HOME -> {
            HomeVista(
                onRegistrarIngreso = { navState = NavState(Pantalla.REGISTRAR_INGRESO) },
                onRegistrarEgreso = { navState = NavState(Pantalla.REGISTRAR_EGRESO) },
                onVerCalendario = { navState = NavState(Pantalla.CALENDARIO) },
                onVerEstadisticas = { navState = NavState(Pantalla.ESTADISTICAS) },
                // Cuando vamos al historial desde el Home, el filtro de mes es NULL (todos)
                onVerHistorial = { navState = NavState(Pantalla.HISTORIAL, mesFiltro = null) },
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
                usuarioId = usuarioId,
                onVolver = { navState = NavState(Pantalla.HOME) },
                onVerCalendario = { navState = NavState(Pantalla.CALENDARIO) },
                onVerInicio = { navState = NavState(Pantalla.HOME) },
                onVerHistorial = { navState = NavState(Pantalla.HISTORIAL) }
            )
        }

        Pantalla.HISTORIAL -> {
            HistorialVista(
                viewModel = historialViewModel,
                usuarioId = usuarioId,
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
                usuarioId = usuarioId,
                onVolver = { navState = NavState(Pantalla.HOME) },
                onVerCalendario = { navState = NavState(Pantalla.CALENDARIO) },
                onVerInicio = { navState = NavState(Pantalla.HOME) },
                onVerHistorial = { navState = NavState(Pantalla.HISTORIAL) }
            )
        }
    }
}