package com.example.asistentefinanciero.ui.vistas.home

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.asistentefinanciero.data.repository.UsuarioRepository
import com.example.asistentefinanciero.ui.theme.*
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
fun HomeVista(
    onCerrarSesion: () -> Unit = {},
    onRegistrarIngreso: () -> Unit = {},
    onRegistrarEgreso: () -> Unit = {},
    onVerCalendario: () -> Unit = {},
    onVerEstadisticas: () -> Unit = {},
    onVerHistorial: () -> Unit = {}
) {
    val context = LocalContext.current
    val repository = remember { UsuarioRepository(context) }
    val usuario = remember { repository.obtenerUsuarioActual() }

    var mesSeleccionado by remember { mutableStateOf("Mes") }
    var mostrarMenuMes by remember { mutableStateOf(false) }
    var mostrarMenuPerfil by remember { mutableStateOf(false) }

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
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "INICIO",
                fontSize = 14.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Botón de usuario con menú
                        IconButton(
                            onClick = { mostrarMenuPerfil = true },
                            modifier = Modifier
                                .size(48.dp)
                                .background(PrimaryPurple, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Usuario",
                                tint = Color.White
                            )
                        }

                        Box {
                            TextButton(
                                onClick = { mostrarMenuMes = !mostrarMenuMes }
                            ) {
                                Text(
                                    text = mesSeleccionado,
                                    color = TextPrimary,
                                    fontSize = 14.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Seleccionar mes",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = mostrarMenuMes,
                                onDismissRequest = { mostrarMenuMes = false }
                            ) {
                                listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                                    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
                                ).forEach { mes ->
                                    DropdownMenuItem(
                                        text = { Text(mes) },
                                        onClick = {
                                            mesSeleccionado = mes
                                            mostrarMenuMes = false
                                        }
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = onVerEstadisticas,
                            modifier = Modifier
                                .size(48.dp)
                                .background(PrimaryPurple, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = "Estadísticas",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botones Ingresos y Egresos con fondo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Botón Ingresos
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = CardDark
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Button(
                                onClick = onRegistrarIngreso,
                                modifier = Modifier.fillMaxSize(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TrendingUp,
                                        contentDescription = "Ingresos",
                                        tint = Color(0xFF00E676),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Ingresos",
                                        color = TextPrimary,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        // Botón Egresos
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = CardDark
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Button(
                                onClick = onRegistrarEgreso,
                                modifier = Modifier.fillMaxSize(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                ),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TrendingDown,
                                        contentDescription = "Egresos",
                                        tint = Color(0xFFFF1744),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Egresos",
                                        color = TextPrimary,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$ 0.0",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

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
                                    text = "Salario mensual",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "$${formatoMoneda.format(usuario?.saldoInicial ?: 0.0)}",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = Color(0xFF00E676),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

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
                                    text = "Arriendo",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "$900,000",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.TrendingDown,
                                contentDescription = null,
                                tint = Color(0xFFFF1744),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = onVerHistorial,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = "Ver historial",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }



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

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(PrimaryPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Inicio",
                        color = TextPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = onVerHistorial) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Historial",
                            tint = TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "Historial",
                        color = TextSecondary,
                        fontSize = 10.sp
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
                    // TODO: Navegar a datos personales
                },
                onNotificaciones = {
                    mostrarMenuPerfil = false
                    // TODO: Navegar a notificaciones
                },
                onSeguridad = {
                    mostrarMenuPerfil = false
                    // TODO: Navegar a seguridad
                },
                onTerminos = {
                    mostrarMenuPerfil = false
                    // TODO: Navegar a términos
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
fun MenuPerfilDialog(
    nombreUsuario: String,
    onDismiss: () -> Unit,
    onDatosPersonales: () -> Unit,
    onNotificaciones: () -> Unit,
    onSeguridad: () -> Unit,
    onTerminos: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar y nombre
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(PrimaryPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = nombreUsuario,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Opciones del menú
                MenuOption(
                    icon = Icons.Default.Person,
                    text = "Datos personales",
                    onClick = onDatosPersonales
                )

                Spacer(modifier = Modifier.height(12.dp))

                MenuOption(
                    icon = Icons.Default.Notifications,
                    text = "Notificaciones",
                    onClick = onNotificaciones
                )

                Spacer(modifier = Modifier.height(12.dp))

                MenuOption(
                    icon = Icons.Default.Lock,
                    text = "Seguridad",
                    onClick = onSeguridad
                )

                Spacer(modifier = Modifier.height(12.dp))

                MenuOption(
                    icon = Icons.Default.Description,
                    text = "Términos y privacidad",
                    onClick = onTerminos
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón cerrar sesión
                Button(
                    onClick = onCerrarSesion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = Color(0xFFFF1744),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cerrar sesión",
                            color = Color(0xFFFF1744),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuOption(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CardDark
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = text,
                    color = TextPrimary,
                    fontSize = 14.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Ir",
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
