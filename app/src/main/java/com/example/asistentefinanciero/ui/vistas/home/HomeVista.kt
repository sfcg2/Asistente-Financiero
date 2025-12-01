package com.example.asistentefinanciero.ui.vistas.home

import com.example.asistentefinanciero.ui.componentes.MenuPerfilDialog
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.asistentefinanciero.ui.theme.*
import com.example.asistentefinanciero.viewmodel.HomeViewModel
import com.example.asistentefinanciero.viewmodel.HistorialViewModel
import com.example.asistentefinanciero.viewmodel.TransaccionItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeVista(
    homeViewModel: HomeViewModel = viewModel(),
    historialViewModel: HistorialViewModel = viewModel(),
    modifier: Modifier = Modifier,
    onCerrarSesion: () -> Unit = {},
    onRegistrarIngreso: () -> Unit = {},
    onRegistrarEgreso: () -> Unit = {},
    onVerCalendario: () -> Unit = {},
    onVerEstadisticas: () -> Unit = {},
    onVerHistorial: () -> Unit = {},
    onIrPerfil: () -> Unit = {},
    onIrSeguridad: () -> Unit = {},
    onIrNotificaciones: () -> Unit = {},
    onIrTerminos: () -> Unit = {}
) {
    val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var nombreUsuario by remember { mutableStateOf("Usuario") }
    var mesSeleccionado by remember { mutableStateOf("Mes") }
    var mostrarMenuMes by remember { mutableStateOf(false) }
    var mostrarMenuPerfil by remember { mutableStateOf(false) }

    val saldo by homeViewModel.saldoActual.collectAsState()
    val isLoadingHome by homeViewModel.isLoading.collectAsState()

    // OBTENER TRANSACCIONES Y FILTRAR A LAS ÚLTIMAS DOS
    val todasLasTransacciones by historialViewModel.transacciones.collectAsState()
    val ultimasDosTransacciones = remember(todasLasTransacciones) {
        todasLasTransacciones.take(2)
    }
    val isLoadingTransacciones by historialViewModel.isLoading.collectAsState()

    LaunchedEffect(usuarioId) {
        if(usuarioId.isNotEmpty()){
            val db = FirebaseFirestore.getInstance()
            val doc = db.collection("usuarios").document(usuarioId).get().await()
            nombreUsuario = doc.getString("nombre") ?: "Usuario"

            // Solo cargar saldo en HomeViewModel
            homeViewModel.cargarDatos(usuarioId)

            // HistorialViewModel carga las transacciones (evita duplicación)
            historialViewModel.cargarTransacciones(usuarioId, mes = null)
        }
    }


    val formatoMoneda = remember {
        NumberFormat.getNumberInstance(Locale("es", "CO")).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 0
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.verticalScroll(rememberScrollState())
                .padding(horizontal = 5.dp)
        ) {
            Spacer(modifier = Modifier.height(35.dp))
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = BackgroundDark),
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

                        // Menú desplegable para el mes
                        Box {
                            Text(text = "Control", color = TextPrimary, fontSize = 16.sp,fontWeight = FontWeight.Bold)
                            Text(text = "             Cash", color = TextPrimary, fontSize = 16.sp)
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

                    // --- BOTONES INGRESOS Y EGRESOS ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Botón Ingresos
                        Card(
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = CardDefaults.cardColors(containerColor = CardDark),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Button(
                                onClick = onRegistrarIngreso,
                                modifier = Modifier.fillMaxSize(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
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
                                    Text(text = "Ingresos", color = TextPrimary, fontSize = 14.sp)
                                }
                            }
                        }

                        // Botón Egresos
                        Card(
                            modifier = Modifier.weight(1f).height(50.dp),
                            colors = CardDefaults.cardColors(containerColor = CardDark),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Button(
                                onClick = onRegistrarEgreso,
                                modifier = Modifier.fillMaxSize(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
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
                                    Text(text = "Egresos", color = TextPrimary, fontSize = 14.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // --- SALDO ACTUAL ---
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (isLoadingHome) {
                            // ✅ NUEVO: Mostrar indicador mientras carga
                            CircularProgressIndicator(
                                color = PrimaryPurple,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Cargando saldo...",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        } else {
                            Text(
                                text = "\$ ${formatoMoneda.format(saldo)}",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Saldo actual",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    } // Separación grande después del saldo

                    // --- INICIO: ÚLTIMAS DOS TRANSACCIONES ---
                    Text(
                        text = "Últimas Transacciones",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp)) // Separación después del título

                    if (isLoadingTransacciones) {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(140.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = PrimaryPurple)
                        }
                    } else if (ultimasDosTransacciones.isEmpty()) {
                        Text(
                            text = "No hay transacciones recientes registradas.",
                            color = TextSecondary,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.background(SurfaceDark, RoundedCornerShape(20.dp))
                                    .padding(16.dp)           )
                            {

                            ultimasDosTransacciones.forEach { transaccion ->
                                // USANDO LA FUNCIÓN LOCAL
                                TransaccionCard(
                                    transaccion = transaccion,
                                    formatoMoneda = formatoMoneda
                                )
                            }
                        }
                    }
                    // --- FIN: ÚLTIMAS DOS TRANSACCIONES ---


                    Spacer(modifier = Modifier.height(12.dp)) // Separación antes del botón Historial

                    // --- BOTÓN VER HISTORIAL ---
                    TextButton(
                        onClick = onVerHistorial,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = "Ver historial completo",
                            color = PrimaryPurple,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(35.dp))
        }

        // Menú de perfil (Diálogo)
        if (mostrarMenuPerfil) {
            MenuPerfilDialog(
                nombreUsuario = nombreUsuario,
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
                    onCerrarSesion()
                }
            )
        }


        // --- BARRA DE NAVEGACIÓN INFERIOR ---
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(20.dp),
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
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Calendario",
                            tint = TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(text = "Calendario", color = TextSecondary, fontSize = 10.sp)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(PrimaryPurple),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",
                            tint = TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(text = "Inicio", color = TextSecondary, fontSize = 10.sp)
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
                    Text(text = "Historial", color = TextSecondary, fontSize = 10.sp)
                }
            }
        }
    }
}

// --- FUNCIÓN TRANSACCIONCARD DUPLICADA LOCALMENTE (MODIFICADA PARA SER MÁS COMPACTA) ---
@Composable
fun TransaccionCard(
    transaccion: TransaccionItem,
    formatoMoneda: NumberFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = transaccion.nombre,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "$${formatoMoneda.format(transaccion.monto)}",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = "${transaccion.fecha} • ${transaccion.hora}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = if (transaccion.esIngreso)
                    Icons.Default.TrendingUp
                else
                    Icons.Default.TrendingDown,
                contentDescription = if (transaccion.esIngreso) "Ingreso" else "Egreso",
                tint = if (transaccion.esIngreso)
                    Color(0xFF00E676)
                else
                    Color(0xFFFF1744),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}