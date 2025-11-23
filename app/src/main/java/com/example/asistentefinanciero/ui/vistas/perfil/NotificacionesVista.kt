package com.example.asistentefinanciero.ui.vistas.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@Composable
fun NotificacionesVista(
    onVolver: () -> Unit = {}
) {
    var notificacionesGenerales by remember { mutableStateOf(true) }
    var notificacionesIngresos by remember { mutableStateOf(true) }
    var notificacionesEgresos by remember { mutableStateOf(true) }
    var notificacionesRecordatorios by remember { mutableStateOf(false) }
    var notificacionesEmail by remember { mutableStateOf(false) }

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
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onVolver,
                    modifier = Modifier
                        .size(40.dp)
                        .background(SurfaceDark, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "NOTIFICACIONES",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Opciones de notificaciones
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Notificaciones generales
                    NotificationOption(
                        titulo = "Notificaciones generales",
                        descripcion = "Recibe alertas sobre cambios en tu cuenta",
                        activado = notificacionesGenerales,
                        onCambio = { notificacionesGenerales = it }
                    )

                    Divider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color(0xFF424242)
                    )

                    // Ingresos
                    NotificationOption(
                        titulo = "Ingresos",
                        descripcion = "Notificación cuando registres un ingreso",
                        activado = notificacionesIngresos,
                        onCambio = { notificacionesIngresos = it }
                    )

                    Divider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color(0xFF424242)
                    )

                    // Egresos
                    NotificationOption(
                        titulo = "Egresos",
                        descripcion = "Notificación cuando registres un egreso",
                        activado = notificacionesEgresos,
                        onCambio = { notificacionesEgresos = it }
                    )

                    Divider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color(0xFF424242)
                    )

                    // Recordatorios
                    NotificationOption(
                        titulo = "Recordatorios",
                        descripcion = "Alertas de pagos próximos a vencer",
                        activado = notificacionesRecordatorios,
                        onCambio = { notificacionesRecordatorios = it }
                    )

                    Divider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color(0xFF424242)
                    )

                    // Notificaciones por correo
                    NotificationOption(
                        titulo = "Notificaciones por correo",
                        descripcion = "Recibe resumen mensual en tu email",
                        activado = notificacionesEmail,
                        onCambio = { notificacionesEmail = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun NotificationOption(
    titulo: String,
    descripcion: String,
    activado: Boolean,
    onCambio: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = titulo,
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = descripcion,
                color = TextSecondary,
                fontSize = 13.sp
            )
        }


        Switch(
            checked = activado,
            onCheckedChange = onCambio,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryPurple,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFF424242)
            )
        )
    }
}