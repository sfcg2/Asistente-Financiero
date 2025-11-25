package com.example.asistentefinanciero.ui.vistas.transacciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asistentefinanciero.data.repository.UsuarioRepository
import com.example.asistentefinanciero.ui.theme.*
import com.example.asistentefinanciero.ui.componentes.MenuPerfilDialog
import java.text.NumberFormat
import java.util.Locale

enum class TipoHistorial {
    TODOS, INGRESOS, EGRESOS
}

data class Transaccion(
    val nombre: String,
    val monto: Double,
    val esIngreso: Boolean
)

@Composable
fun HistorialVista(
    onVolver: () -> Unit = {},
    onCerrarSesion: () -> Unit = {},
    onVerCalendario: () -> Unit = {},
    onIrPerfil: () -> Unit = {},
    onIrSeguridad: () -> Unit = {},
    onIrNotificaciones: () -> Unit = {},
    onIrTerminos: () -> Unit = {}
) {
    val context = LocalContext.current
    val repository = remember { UsuarioRepository(context) }
    val usuario = remember { repository.obtenerUsuarioActual() }

    var tipoSeleccionado by remember { mutableStateOf(TipoHistorial.TODOS) }
    var mostrarMenuPerfil by remember { mutableStateOf(false) }

    // Datos de ejemplo
    val transaccionesTodas = listOf(
        Transaccion("Salario mensual", 2500000.0, true),
        Transaccion("Arriendo", 900000.0, false),
        Transaccion("Freelance diseño web", 400000.0, true),
        Transaccion("Servicios públicos", 250000.0, false),
        Transaccion("Bono de productividad", 300000.0, true),
        Transaccion("Transporte", 180000.0, false)
    )

    val transaccionesIngresos = transaccionesTodas.filter { it.esIngreso }
    val transaccionesEgresos = transaccionesTodas.filter { !it.esIngreso }

    val transaccionesMostrar = when (tipoSeleccionado) {
        TipoHistorial.TODOS -> transaccionesTodas
        TipoHistorial.INGRESOS -> transaccionesIngresos
        TipoHistorial.EGRESOS -> transaccionesEgresos
    }

    val formatoMoneda = remember {
        NumberFormat.getNumberInstance(Locale("es", "CO")).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 0
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (tipoSeleccionado) {
                        TipoHistorial.TODOS -> "HISTORIAL"
                        TipoHistorial.INGRESOS -> "HISTORIAL INGRESOS"
                        TipoHistorial.EGRESOS -> "HISTORIAL EGRESOS"
                    },
                    fontSize = 14.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Normal
                )

                IconButton(
                    onClick = { mostrarMenuPerfil = true },
                    modifier = Modifier
                        .size(40.dp)
                        .background(PrimaryPurple, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Usuario",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pestañas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Pestaña Todos
                TextButton(
                    onClick = { tipoSeleccionado = TipoHistorial.TODOS },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (tipoSeleccionado == TipoHistorial.TODOS)
                            TextPrimary else TextSecondary
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Todos",
                            fontSize = 16.sp,
                            fontWeight = if (tipoSeleccionado == TipoHistorial.TODOS)
                                FontWeight.Bold else FontWeight.Normal
                        )
                        if (tipoSeleccionado == TipoHistorial.TODOS) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(3.dp)
                                    .background(PrimaryPurple, RoundedCornerShape(2.dp))
                            )
                        }
                    }
                }

                // Pestaña Ingresos
                TextButton(
                    onClick = { tipoSeleccionado = TipoHistorial.INGRESOS },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (tipoSeleccionado == TipoHistorial.INGRESOS)
                            Color(0xFF00E676) else TextSecondary
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Ingresos",
                            fontSize = 16.sp,
                            fontWeight = if (tipoSeleccionado == TipoHistorial.INGRESOS)
                                FontWeight.Bold else FontWeight.Normal
                        )
                        if (tipoSeleccionado == TipoHistorial.INGRESOS) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(3.dp)
                                    .background(Color(0xFF00E676), RoundedCornerShape(2.dp))
                            )
                        }
                    }
                }

                // Pestaña Egresos
                TextButton(
                    onClick = { tipoSeleccionado = TipoHistorial.EGRESOS },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (tipoSeleccionado == TipoHistorial.EGRESOS)
                            Color(0xFFFF1744) else TextSecondary
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Egresos",
                            fontSize = 16.sp,
                            fontWeight = if (tipoSeleccionado == TipoHistorial.EGRESOS)
                                FontWeight.Bold else FontWeight.Normal
                        )
                        if (tipoSeleccionado == TipoHistorial.EGRESOS) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(3.dp)
                                    .background(Color(0xFFFF1744), RoundedCornerShape(2.dp))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de transacciones
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(20.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(transaccionesMostrar) { transaccion ->
                        TransaccionItem(
                            transaccion = transaccion,
                            formatoMoneda = formatoMoneda
                        )
                    }

                    // Espaciado final
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        // Barra de navegación inferior
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(30.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Calendario
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = onVerCalendario) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Calendario",
                            tint = TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "Calendario",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }

                // Inicio
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",
                            tint = TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "Inicio",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }

                // Historial (activo)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(PrimaryPurple, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Historial",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Historial",
                        color = TextPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Menú de perfil
        if (mostrarMenuPerfil) {
            MenuPerfilDialog(
                nombreUsuario = usuario?.nombre ?: "Usuario",
                onDismiss = { mostrarMenuPerfil = false },
                onDatosPersonales = {
                    mostrarMenuPerfil = false
                    onIrPerfil()
                },
                onNotificaciones = {
                    mostrarMenuPerfil = false
                    onIrNotificaciones()
                },
                onSeguridad = {
                    mostrarMenuPerfil = false
                    onIrSeguridad()
                },
                onTerminos = {
                    mostrarMenuPerfil = false
                    onIrTerminos()
                },
                onCerrarSesion = {
                    mostrarMenuPerfil = false
                    repository.cerrarSesion()
                    onCerrarSesion()
                }
            )
        }
    }
}

@Composable
fun TransaccionItem(
    transaccion: Transaccion,
    formatoMoneda: NumberFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = transaccion.nombre,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${formatoMoneda.format(transaccion.monto)}",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Icon(
                imageVector = if (transaccion.esIngreso)
                    Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                contentDescription = null,
                tint = if (transaccion.esIngreso)
                    Color(0xFF00E676) else Color(0xFFFF1744),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}