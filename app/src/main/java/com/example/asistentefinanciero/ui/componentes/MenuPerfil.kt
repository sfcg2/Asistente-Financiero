package com.example.asistentefinanciero.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.asistentefinanciero.ui.theme.*

/**
 * Diálogo del menú de perfil de usuario
 * Componente reutilizable para todas las pantallas de la aplicación
 */
@Composable
fun MenuPerfilDialog(
    nombreUsuario: String,
    onDismiss: () -> Unit,
    onDatosPersonales: () -> Unit = {},
    onNotificaciones: () -> Unit = {},
    onSeguridad: () -> Unit = {},
    onTerminos: () -> Unit = {},
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
                // Avatar del usuario
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(PrimaryPurple, CircleShape),
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

                // Nombre del usuario
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

/**
 * Botón de opción del menú de perfil
 * Componente interno reutilizable para cada opción del menú
 */
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