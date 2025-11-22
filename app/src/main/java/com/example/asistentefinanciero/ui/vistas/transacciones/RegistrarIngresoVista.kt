package com.example.asistentefinanciero.ui.vistas.transacciones

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asistentefinanciero.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarIngresoVista(
    onVolver: () -> Unit = {},
    onVerHistorial: () -> Unit = {}
) {
    var cantidad by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Categoría") }
    var fecha by remember { mutableStateOf("Fecha") }
    var hora by remember { mutableStateOf("Hora") }
    var nombre by remember { mutableStateOf("") }
    var repetir by remember { mutableStateOf("No se repite") }

    var mostrarMenuCategoria by remember { mutableStateOf(false) }
    var mostrarMenuRepetir by remember { mutableStateOf(false) }

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
                    text = "REGISTAR INGRESO",
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

            Spacer(modifier = Modifier.height(32.dp))

            // Icono y título
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "Ingreso",
                    tint = Color(0xFF00E676),
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Ingreso",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Campo Cantidad
            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cantidad", color = Color(0xFFB4B4B4)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00E676),
                    unfocusedBorderColor = Color(0xFF424242),
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown Categoría
            Box {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF424242),
                        unfocusedBorderColor = Color(0xFF424242),
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        IconButton(onClick = { mostrarMenuCategoria = true }) {
                            Icon(Icons.Default.ArrowDropDown, null, tint = TextPrimary)
                        }
                    }
                )

                DropdownMenu(
                    expanded = mostrarMenuCategoria,
                    onDismissRequest = { mostrarMenuCategoria = false }
                ) {
                    listOf("Salario", "Freelance", "Inversiones", "Ventas", "Otro").forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                categoria = it
                                mostrarMenuCategoria = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fila Fecha y Hora
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {},
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF424242),
                        unfocusedBorderColor = Color(0xFF424242),
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, null, tint = TextPrimary)
                    }
                )

                OutlinedTextField(
                    value = hora,
                    onValueChange = {},
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF424242),
                        unfocusedBorderColor = Color(0xFF424242),
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, null, tint = TextPrimary)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Nombre", color = Color(0xFFB4B4B4)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF424242),
                    unfocusedBorderColor = Color(0xFF424242),
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown No se repite
            Box {
                OutlinedTextField(
                    value = repetir,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF424242),
                        unfocusedBorderColor = Color(0xFF424242),
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = {
                        IconButton(onClick = { mostrarMenuRepetir = true }) {
                            Icon(Icons.Default.ArrowDropDown, null, tint = TextPrimary)
                        }
                    }
                )

                DropdownMenu(
                    expanded = mostrarMenuRepetir,
                    onDismissRequest = { mostrarMenuRepetir = false }
                ) {
                    listOf("No se repite", "Diario", "Semanal", "Mensual", "Anual").forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                repetir = it
                                mostrarMenuRepetir = false
                            }
                        )
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Calendario",
                            tint = TextSecondary
                        )
                    }
                    Text("Calendario", color = TextSecondary, fontSize = 10.sp)
                }

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
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Inicio", color = TextPrimary, fontSize = 10.sp)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Historial",
                            tint = TextSecondary
                        )
                    }
                    Text("Historial", color = TextSecondary, fontSize = 10.sp)
                }
            }
        }
    }
}