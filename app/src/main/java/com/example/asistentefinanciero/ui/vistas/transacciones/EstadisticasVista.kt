package com.example.asistentefinanciero.ui.vistas.transacciones

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asistentefinanciero.ui.theme.*
import com.example.asistentefinanciero.viewmodel.EstadisticasViewModel
import com.example.asistentefinanciero.viewmodel.FiltroEstadisticas
import com.google.accompanist.flowlayout.FlowRow
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.sqrt
import kotlin.math.atan2

@Composable
fun EstadisticaVista(
    viewModel: EstadisticasViewModel,
    usuarioId: String = "K6Tr9DTjDIMGf7PFG4MH",
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {},
    onVerCalendario: () -> Unit = {},
    onVerInicio: () -> Unit = {},
    onVerHistorial: () -> Unit = {},
) {
    var mesSeleccionado by remember { mutableStateOf("Mes") }
    var mostrarMenuMes by remember { mutableStateOf(false) }
    val filtroActual by viewModel.filtroActual.collectAsState()
    val datosGrafico by viewModel.datosGrafico.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Cargar datos al iniciar
    LaunchedEffect(Unit) {
        viewModel.cargarIngresos(usuarioId)
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
                    text = "ESTADÍSTICAS",
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

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), // Asegura que ocupe todo el ancho
                horizontalArrangement = Arrangement.Center,

            )

            {
                // Menu desplegable para el mes
                Box {
                    TextButton(
                        onClick = { mostrarMenuMes = !mostrarMenuMes },
                        //modifier = Modifier.background(CardDark, RoundedCornerShape(10.dp))

                    ) {
                        Text(
                            text = mesSeleccionado,
                            color = TextPrimary,
                            fontSize = 14.sp,
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
                        onDismissRequest = { mostrarMenuMes = false },
                        modifier = Modifier
                            .background(SurfaceDark, RoundedCornerShape(12.dp))
                            .size(200.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Todos los meses", color = TextPrimary) },
                            onClick = {
                                mesSeleccionado = "Todos los meses"
                                mostrarMenuMes = false
                                viewModel.seleccionarMes(null, usuarioId)
                            }
                        )
                        listOf(
                            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
                        ).forEachIndexed { index, mes ->
                            DropdownMenuItem(
                                text = { Text(mes, color = TextPrimary) },
                                onClick = {
                                    mesSeleccionado = mes
                                    mostrarMenuMes = false
                                    viewModel.seleccionarMes(index + 1, usuarioId)
                                }
                            )
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(20.dp))

            // Filtros (Ingresos, Egresos)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón Ingresos
                Card(
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (filtroActual == FiltroEstadisticas.INGRESOS) PrimaryPurple else CardDark
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.cambiarFiltro(FiltroEstadisticas.INGRESOS) },
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
                                tint = if (filtroActual == FiltroEstadisticas.INGRESOS) Color.White else Color(0xFF00E676),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Ingresos",
                                color = if (filtroActual == FiltroEstadisticas.INGRESOS) Color.White else TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = if (filtroActual == FiltroEstadisticas.INGRESOS) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Botón Egresos
                Card(
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (filtroActual == FiltroEstadisticas.EGRESOS) PrimaryPurple else CardDark
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.cambiarFiltro(FiltroEstadisticas.EGRESOS) },
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
                                tint = if (filtroActual == FiltroEstadisticas.EGRESOS) Color.White else Color(0xFFFF1744),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Egresos",
                                color = if (filtroActual == FiltroEstadisticas.EGRESOS) Color.White else TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = if (filtroActual == FiltroEstadisticas.EGRESOS) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 📊 ÁREA DEL GRÁFICO (arriba) Y CATEGORÍAS (abajo)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // 📊 Card del Gráfico Circular (ARRIBA)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(color = PrimaryPurple)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Cargando...",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        } else if (datosGrafico.isEmpty()) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = TextSecondary,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Sin datos para mostrar",
                                    color = TextSecondary,
                                    fontSize = 14.sp
                                )
                            }
                        } else {
                            // 🎨 GRÁFICO CIRCULAR
                            GraficoCircular(
                                datos = datosGrafico,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 📋 Categorías horizontales con FlowRow (ABAJO)
                if (datosGrafico.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        mainAxisSpacing = 12.dp,
                        crossAxisSpacing = 8.dp
                    ) {
                        datosGrafico.forEach { dato ->
                            ChipCategoria(
                                nombreCategoria = dato.categoria,
                                color = dato.color
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(120.dp)) // Espacio para navbar
        }

        // Barra de navegación inferior
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 10.dp),
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
                    IconButton(onClick = onVerInicio) {
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

// 🏷️ Chip de categoría (solo nombre + color)
@Composable
fun ChipCategoria(
    nombreCategoria: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SurfaceDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = nombreCategoria,
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// 🎨 Componente del Gráfico Circular
@Composable
fun GraficoCircular(
    datos: List<com.example.asistentefinanciero.viewmodel.DatoGrafico>,
    modifier: Modifier = Modifier
) {
    val totalMonto = datos.sumOf { it.montoTotal }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(220.dp)
        ) {
            val radius = size.minDimension / 2f
            val center = Offset(size.width / 2, size.height / 2)
            val innerRadius = radius * 0.55f
            val textRadius = radius * 0.75f

            var startAngle = -90f

            // Pintura para texto dinámico
            val textPaint = android.graphics.Paint().apply {
                textSize = 40f
                textAlign = android.graphics.Paint.Align.CENTER
                isFakeBoldText = true
            }

            // --- DIBUJAR ARCOS ---
            datos.forEach { dato ->
                val sweepAngle = (dato.porcentajeTotal / 100f) * 360f

                drawArc(
                    color = dato.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )

                startAngle += sweepAngle
            }

            // --- TEXTO CENTRADO ---
            startAngle = -90f

            datos.forEach { dato ->
                val sweepAngle = (dato.porcentajeTotal / 100f) * 360f
                val midAngle = startAngle + sweepAngle / 2f

                val rad = Math.toRadians(midAngle.toDouble())
                val x = (center.x + textRadius * cos(rad)).toFloat()
                val y = (center.y + textRadius * sin(rad)).toFloat()

                // 💡 Contraste automático del texto
                val (r, g, b) = listOf(
                    dato.color.red * 255,
                    dato.color.green * 255,
                    dato.color.blue * 255
                )

                val luminance = (0.299 * r + 0.587 * g + 0.114 * b)

                textPaint.color = android.graphics.Color.WHITE
                    if (luminance < 128) android.graphics.Color.WHITE
                    else android.graphics.Color.BLACK

                // Dibujar porcentaje
                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText(
                        "${dato.porcentajeTotal.toInt()}%",
                        x,
                        y,
                        textPaint
                    )
                }

                startAngle += sweepAngle
            }

            // --- HUECO DONUT ---
            drawCircle(
                color = Color(0xFF252B48),
                radius = innerRadius,
                center = center
            )
        }

        // --- TEXTO CENTRAL (TOTAL) ---
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Total", color = TextSecondary, fontSize = 12.sp)
            Text(
                "$${String.format("%,.0f", totalMonto)}",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}