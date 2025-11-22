package com.example.asistentefinanciero.ui.vistas.transacciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asistentefinanciero.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarEgresoVista(
    onVolver: () -> Unit = {},
    onVerHistorial: () -> Unit
) {
    var cantidad by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var repetir by remember { mutableStateOf("No se repite") }

    var mostrarMenuCategoria by remember { mutableStateOf(false) }
    var mostrarMenuRepetir by remember { mutableStateOf(false) }
    var mostrarDatePicker by remember { mutableStateOf(false) }
    var mostrarTimePicker by remember { mutableStateOf(false) }

    // Establecer fecha y hora actuales al inicio
    LaunchedEffect(Unit) {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        fecha = dateFormat.format(calendar.time)
        hora = timeFormat.format(calendar.time)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                    text = "REGISTRAR EGRESO",
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
                    imageVector = Icons.Default.TrendingDown,
                    contentDescription = "Egreso",
                    tint = Color(0xFFFF1744),
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Egreso",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Campo Cantidad
            OutlinedTextField(
                value = cantidad,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^[0-9.,]*$"))) {
                        cantidad = newValue
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cantidad", color = Color(0xFFB4B4B4)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF1744),
                    unfocusedBorderColor = Color(0xFF424242),
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = Color(0xFFFF1744)
                ),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Text(
                        text = "$",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown Categoría
            Box {
                OutlinedTextField(
                    value = if (categoria.isEmpty()) "Categoría" else categoria,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF424242),
                        unfocusedBorderColor = Color(0xFF424242),
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedTextColor = if (categoria.isEmpty()) Color(0xFFB4B4B4) else TextPrimary,
                        unfocusedTextColor = if (categoria.isEmpty()) Color(0xFFB4B4B4) else TextPrimary
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
                    onDismissRequest = { mostrarMenuCategoria = false },
                    modifier = Modifier.background(SurfaceDark)
                ) {
                    listOf("Arriendo", "Comida", "Transporte", "Servicios", "Entretenimiento", "Salud", "Otro").forEach {
                        DropdownMenuItem(
                            text = { Text(it, color = TextPrimary) },
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
                    placeholder = { Text("Fecha", color = Color(0xFFB4B4B4)) },
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
                        IconButton(onClick = { mostrarDatePicker = true }) {
                            Icon(Icons.Default.CalendarToday, null, tint = TextPrimary)
                        }
                    }
                )

                OutlinedTextField(
                    value = hora,
                    onValueChange = {},
                    modifier = Modifier.weight(1f),
                    readOnly = true,
                    placeholder = { Text("Hora", color = Color(0xFFB4B4B4)) },
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
                        IconButton(onClick = { mostrarTimePicker = true }) {
                            Icon(Icons.Default.Schedule, null, tint = TextPrimary)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Nombre/Descripción", color = Color(0xFFB4B4B4)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF424242),
                    unfocusedBorderColor = Color(0xFF424242),
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = Color(0xFFFF1744)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown Repetir
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
                    onDismissRequest = { mostrarMenuRepetir = false },
                    modifier = Modifier.background(SurfaceDark)
                ) {
                    listOf("No se repite", "Diario", "Semanal", "Mensual", "Anual").forEach {
                        DropdownMenuItem(
                            text = { Text(it, color = TextPrimary) },
                            onClick = {
                                repetir = it
                                mostrarMenuRepetir = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Guardar Egreso
            Button(
                onClick = {
                    // TODO: Guardar el egreso
                    onVolver()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF1744)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = cantidad.isNotEmpty() && categoria.isNotEmpty()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Guardar",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Guardar Egreso",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
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
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",
                            tint = TextSecondary
                        )
                    }
                    Text("Inicio", color = TextSecondary, fontSize = 10.sp)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = onVerHistorial) {
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

        // Date Picker Dialog
        if (mostrarDatePicker) {
            DatePickerDialog(
                onDismissRequest = { mostrarDatePicker = false },
                confirmButton = {
                    TextButton(onClick = { mostrarDatePicker = false }) {
                        Text("OK", color = Color(0xFFFF1744))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDatePicker = false }) {
                        Text("Cancelar", color = TextSecondary)
                    }
                }
            ) {
                DatePicker(
                    state = rememberDatePickerState(),
                    colors = DatePickerDefaults.colors(
                        containerColor = SurfaceDark
                    )
                )
            }
        }

        // Time Picker Dialog
        if (mostrarTimePicker) {
            AlertDialog(
                onDismissRequest = { mostrarTimePicker = false },
                confirmButton = {
                    TextButton(onClick = { mostrarTimePicker = false }) {
                        Text("OK", color = Color(0xFFFF1744))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarTimePicker = false }) {
                        Text("Cancelar", color = TextSecondary)
                    }
                },
                title = { Text("Seleccionar Hora", color = TextPrimary) },
                text = {
                    Text("Hora actual: $hora", color = TextPrimary)
                },
                containerColor = SurfaceDark
            )
        }
    }
}