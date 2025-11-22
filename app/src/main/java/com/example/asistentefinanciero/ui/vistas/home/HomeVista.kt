package com.example.asistentefinanciero.ui.vistas.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asistentefinanciero.data.repository.UsuarioRepository
import com.example.asistentefinanciero.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

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

    // Formato colombiano para números
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

            // Header con texto INICIO
            Text(
                text = "INICIO",
                fontSize = 14.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Card principal
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
                    // Fila superior: Usuario, Mes, Estadísticas
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icono usuario
                        IconButton(
                            onClick = { /* TODO */ },
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

                        // Selector de mes
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

                        // Icono estadísticas
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

                    // Botones Ingresos y Egresos
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Botón Ingresos
                        Button(
                            onClick = onRegistrarIngreso,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = "Ingresos",
                                    tint = Color(0xFF00E676),
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Ingresos",
                                    color = TextPrimary,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        // Botón Egresos
                        Button(
                            onClick = onRegistrarEgreso,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TrendingDown,
                                    contentDescription = "Egresos",
                                    tint = Color(0xFFFF1744),
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Egresos",
                                    color = TextPrimary,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Saldo total
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

                    // Card Salario mensual
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

                    // Card Arriendo
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

                    // Botón Ver historial
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

        // Botón flotante +
        FloatingActionButton(
            onClick = { /* TODO: Mostrar menú */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp),
            containerColor = PrimaryPurple,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
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

                // Inicio (activo)
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

                // Historial
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
    }
}