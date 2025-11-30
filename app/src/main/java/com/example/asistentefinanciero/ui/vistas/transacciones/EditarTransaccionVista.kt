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
import com.example.asistentefinanciero.viewmodel.EditarTransaccionViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarTransaccionVista(
    viewModel: EditarTransaccionViewModel,
    transaccionId: String,
    esIngreso: Boolean,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {}
) {
    val usuarioId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Estados del ViewModel
    val cantidad by viewModel.cantidad.collectAsState()
    val categoria by viewModel.categoria.collectAsState()
    val fecha by viewModel.fecha.collectAsState()
    val hora by viewModel.hora.collectAsState()
    val nombre by viewModel.nombre.collectAsState()
    val seRepite by viewModel.seRepite.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val mensajeExito by viewModel.mensajeExito.collectAsState()
    val mostrarDialogoEliminar by viewModel.mostrarDialogoEliminar.collectAsState()

    // Estados locales para UI
    var mostrarMenuCategoria by remember { mutableStateOf(false) }
    var mostrarMenuRepetir by remember { mutableStateOf(false) }
    var mostrarDatePicker by remember { mutableStateOf(false) }
    var mostrarTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    val colorPrincipal = if (esIngreso) Color(0xFF00E676) else Color(0xFFFF1744)
    val nombreTipo = if (esIngreso) "Ingreso" else "Egreso"

    // Cargar transacción al iniciar
    LaunchedEffect(Unit) {
        viewModel.cargarTransaccion(usuarioId, transaccionId, esIngreso)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                    text = "EDITAR ${nombreTipo.uppercase()}",
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

            // Icono y título
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (esIngreso) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                    contentDescription = nombreTipo,
                    tint = colorPrincipal,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Editar $nombreTipo",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            if (isLoading && cantidad.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colorPrincipal)
                }
            } else {
                // Campo Cantidad
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^[0-9]*\\.?[0-9]*$"))) {
                            viewModel.actualizarCantidad(newValue)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Cantidad", color = Color(0xFFB4B4B4)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorPrincipal,
                        unfocusedBorderColor = Color(0xFF424242),
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = colorPrincipal
                    ),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = {
                        Text(
                            text = "$",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    enabled = !isLoading
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
                            IconButton(
                                onClick = { mostrarMenuCategoria = true },
                                enabled = !isLoading
                            ) {
                                Icon(Icons.Default.ArrowDropDown, null, tint = TextPrimary)
                            }
                        },
                        enabled = !isLoading
                    )

                    DropdownMenu(
                        expanded = mostrarMenuCategoria,
                        onDismissRequest = { mostrarMenuCategoria = false },
                        modifier = Modifier.background(SurfaceDark).size(200.dp)
                    ) {
                        val categorias = if (esIngreso) {
                            listOf(
                                "Salario",
                                "Freelance",
                                "Inversiones",
                                "Ventas",
                                "Bonos",
                                "Regalos",
                                "Otro"
                            )
                        } else {
                            listOf(
                                "Arriendo",
                                "Comida",
                                "Transporte",
                                "Servicios",
                                "Entretenimiento",
                                "Salud",
                                "Educación",
                                "Ropa",
                                "Otro"
                            )
                        }

                        categorias.forEach {
                            DropdownMenuItem(
                                text = { Text(it, color = TextPrimary) },
                                onClick = {
                                    viewModel.actualizarCategoria(it)
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
                            unfocusedTextColor = TextPrimary,
                            disabledBorderColor = Color(0xFF424242),
                            disabledContainerColor = SurfaceDark,
                            disabledTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            IconButton(
                                onClick = { mostrarDatePicker = true },
                                enabled = !isLoading
                            ) {
                                Icon(Icons.Default.DateRange, null, tint = TextPrimary)
                            }
                        },
                        enabled = !isLoading
                    )

                    OutlinedTextField(
                        value = hora,
                        onValueChange = {},
                        modifier = Modifier.weight(0.8f),
                        readOnly = true,
                        placeholder = { Text("Hora", color = Color(0xFFB4B4B4)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF424242),
                            unfocusedBorderColor = Color(0xFF424242),
                            focusedContainerColor = SurfaceDark,
                            unfocusedContainerColor = SurfaceDark,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            disabledBorderColor = Color(0xFF424242),
                            disabledContainerColor = SurfaceDark,
                            disabledTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            IconButton(
                                onClick = { mostrarTimePicker = true },
                                enabled = !isLoading
                            ) {
                                Icon(Icons.Default.AccessTime, null, tint = TextPrimary)
                            }
                        },
                        enabled = !isLoading
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { viewModel.actualizarNombre(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Nombre/Descripción (opcional)",
                            color = Color(0xFFB4B4B4)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF424242),
                        unfocusedBorderColor = Color(0xFF424242),
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = colorPrincipal
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dropdown Repetir
                Box {
                    OutlinedTextField(
                        value = seRepite,
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
                            IconButton(
                                onClick = { mostrarMenuRepetir = true },
                                enabled = !isLoading
                            ) {
                                Icon(Icons.Default.ArrowDropDown, null, tint = TextPrimary)
                            }
                        },
                        enabled = !isLoading
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
                                    viewModel.actualizarSeRepite(it)
                                    mostrarMenuRepetir = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Mensajes de error
                mensaje?.let {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFF5252).copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Error",
                                tint = Color(0xFFFF5252)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = it,
                                color = Color(0xFFFF5252),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                // Mensajes de éxito
                mensajeExito?.let {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF00E676).copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Éxito",
                                tint = Color(0xFF00E676)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = it,
                                color = Color(0xFF00E676),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                // Botón Actualizar
                Button(
                    onClick = {
                        viewModel.actualizarTransaccion(usuarioId) {
                            onVolver()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorPrincipal,
                        disabledContainerColor = Color(0xFF424242)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = cantidad.isNotEmpty() && categoria.isNotEmpty() && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = if (esIngreso) Color.Black else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Actualizar",
                                tint = if (esIngreso) Color.Black else Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Actualizar $nombreTipo",
                                color = if (esIngreso) Color.Black else Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón Eliminar
                OutlinedButton(
                    onClick = { viewModel.mostrarDialogoEliminar() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFFF5252)
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFF5252))
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Eliminar $nombreTipo",
                            color = Color(0xFFFF5252),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Date Picker Dialog
        if (mostrarDatePicker) {
            DatePickerDialog(
                onDismissRequest = { mostrarDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = millis
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            viewModel.actualizarFecha(dateFormat.format(calendar.time))
                        }
                        mostrarDatePicker = false
                    }) {
                        Text("OK", color = colorPrincipal)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDatePicker = false }) {
                        Text("Cancelar", color = TextSecondary)
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        containerColor = SurfaceDark,
                        titleContentColor = TextPrimary,
                        headlineContentColor = TextPrimary,
                        weekdayContentColor = TextSecondary,
                        subheadContentColor = TextPrimary,
                        yearContentColor = TextPrimary,
                        currentYearContentColor = colorPrincipal,
                        selectedYearContentColor = if (esIngreso) Color.Black else Color.White,
                        selectedYearContainerColor = colorPrincipal,
                        dayContentColor = TextPrimary,
                        selectedDayContentColor = if (esIngreso) Color.Black else Color.White,
                        selectedDayContainerColor = colorPrincipal,
                        todayContentColor = colorPrincipal,
                        todayDateBorderColor = colorPrincipal
                    )
                )
            }
        }

        // Time Picker Dialog
        if (mostrarTimePicker) {
            AlertDialog(
                onDismissRequest = { mostrarTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val horaFormateada = String.format(
                            "%02d:%02d",
                            timePickerState.hour,
                            timePickerState.minute
                        )
                        viewModel.actualizarHora(horaFormateada)
                        mostrarTimePicker = false
                    }) {
                        Text("OK", color = colorPrincipal)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarTimePicker = false }) {
                        Text("Cancelar", color = TextSecondary)
                    }
                },
                title = { Text("Seleccionar Hora", color = TextPrimary) },
                text = {
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            clockDialColor = SurfaceDark,
                            selectorColor = colorPrincipal,
                            containerColor = SurfaceDark,
                            periodSelectorBorderColor = Color(0xFF424242),
                            clockDialSelectedContentColor = if (esIngreso) Color.Black else Color.White,
                            clockDialUnselectedContentColor = TextPrimary,
                            periodSelectorSelectedContainerColor = colorPrincipal,
                            periodSelectorUnselectedContainerColor = Color(0xFF424242),
                            periodSelectorSelectedContentColor = if (esIngreso) Color.Black else Color.White,
                            periodSelectorUnselectedContentColor = TextPrimary,
                            timeSelectorSelectedContainerColor = colorPrincipal,
                            timeSelectorUnselectedContainerColor = Color(0xFF424242),
                            timeSelectorSelectedContentColor = if (esIngreso) Color.Black else Color.White,
                            timeSelectorUnselectedContentColor = TextPrimary
                        )
                    )
                },
                containerColor = SurfaceDark
            )
        }

        // Diálogo de confirmación para eliminar
        if (mostrarDialogoEliminar) {
            AlertDialog(
                onDismissRequest = { viewModel.ocultarDialogoEliminar() },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Advertencia",
                        tint = Color(0xFFFF5252),
                        modifier = Modifier.size(48.dp)
                    )
                },
                title = {
                    Text(
                        text = "¿Eliminar $nombreTipo?",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Esta acción no se puede deshacer. El saldo de tu cuenta será ajustado automáticamente.",
                        color = TextSecondary
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.eliminarTransaccion(usuarioId) {
                                onVolver()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF5252)
                        )
                    ) {
                        Text("Eliminar", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.ocultarDialogoEliminar() }
                    ) {
                        Text("Cancelar", color = TextSecondary)
                    }
                },
                containerColor = SurfaceDark
            )
        }
    }
}