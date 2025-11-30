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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asistentefinanciero.ui.theme.* // Asegúrate de que tus imports de tema son correctos
import com.example.asistentefinanciero.viewmodel.FiltroHistorial
import com.example.asistentefinanciero.viewmodel.HistorialViewModel
import com.example.asistentefinanciero.viewmodel.TransaccionItem
import com.google.firebase.auth.FirebaseAuth
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HistorialVista(
    viewModel: HistorialViewModel,
    //usuarioId: String = "K6Tr9DTjDIMGf7PFG4MH",
    // ✨ MODIFICACIÓN CLAVE: Parámetro para recibir el filtro de mes.
    mesFiltroInicial: Int? = null,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {},
    onVerCalendario: () -> Unit = {},
    onVerInicio: () -> Unit = {}
) {
    val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val transacciones by viewModel.transacciones.collectAsState()
    val filtroActual by viewModel.filtroActual.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val formatoMoneda = remember {
        NumberFormat.getNumberInstance(Locale("es", "CO")).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 0
        }
    }

    LaunchedEffect(mesFiltroInicial) {
        viewModel.cargarTransacciones(usuarioId, mes = mesFiltroInicial)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "HISTORIAL",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Normal
                )

                IconButton(
                    onClick = onVolver,
                    modifier = Modifier
                        .size(40.dp)
                        .background(SurfaceDark, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Filtros (Todos, Ingresos, Egresos)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FiltroButton(
                    texto = "Todos",
                    seleccionado = filtroActual == FiltroHistorial.TODOS,
                    onClick = { viewModel.cambiarFiltro(FiltroHistorial.TODOS) },
                    modifier = Modifier.weight(1f)
                )

                FiltroButton(
                    texto = "Ingresos",
                    seleccionado = filtroActual == FiltroHistorial.INGRESOS,
                    onClick = { viewModel.cambiarFiltro(FiltroHistorial.INGRESOS) },
                    modifier = Modifier.weight(1f)
                )

                FiltroButton(
                    texto = "Egresos",
                    seleccionado = filtroActual == FiltroHistorial.EGRESOS,
                    onClick = { viewModel.cambiarFiltro(FiltroHistorial.EGRESOS) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de transacciones
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryPurple)
                }
            } else if (transacciones.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay transacciones",
                            color = TextSecondary,
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(transacciones) { transaccion ->
                        TransaccionCard(
                            transaccion = transaccion,
                            formatoMoneda = formatoMoneda
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        // Barra de navegación inferior
// Barra de navegación inferior
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 35.dp),
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
                    //Text(text = "Calendario", color = TextSecondary, fontSize = 10.sp)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = onVerInicio) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",
                            tint = TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    //Text(text = "Inicio", color = TextSecondary, fontSize = 10.sp)
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
                    ){
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Historial",
                            tint = TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            //Text(text = "Historial", color = TextSecondary, fontSize = 10.sp)
        }
    }
}

// --- Componibles Reutilizados (FiltroButton y TransaccionCard) ---

@Composable
fun FiltroButton(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (seleccionado) PrimaryPurple else SurfaceDark,
            contentColor = if (seleccionado) Color.White else TextSecondary
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = texto,
            fontSize = if (seleccionado) 13.sp else 14.sp ,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun TransaccionCard(
    transaccion: TransaccionItem,
    formatoMoneda: NumberFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${formatoMoneda.format(transaccion.monto)}",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(8.dp))
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