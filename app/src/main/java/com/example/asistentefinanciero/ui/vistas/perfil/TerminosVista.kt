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
fun TerminosVista(
    onVolver: () -> Unit = {}
) {
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
                    text = "TÉRMINOS Y PRIVACIDAD",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Card con contenido
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Términos de Servicio
                    Text(
                        text = "Términos de Servicio",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Al usar ControlCash, aceptas los siguientes términos:",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TerminoItem(
                        numero = "1.",
                        texto = "La aplicación es para uso personal y gestión de finanzas individuales."
                    )

                    TerminoItem(
                        numero = "2.",
                        texto = "Los datos ingresados son responsabilidad del usuario."
                    )

                    TerminoItem(
                        numero = "3.",
                        texto = "La aplicación no se hace responsable de pérdidas financieras derivadas de su uso."
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Divider(color = Color(0xFF424242))

                    Spacer(modifier = Modifier.height(24.dp))

                    // Política de Privacidad
                    Text(
                        text = "Política de Privacidad",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Respetamos tu privacidad y protegemos tus datos:",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TerminoItem(
                        numero = "•",
                        texto = "Tus datos se almacenan localmente en tu dispositivo."
                    )

                    TerminoItem(
                        numero = "•",
                        texto = "No compartimos tu información con terceros."
                    )

                    TerminoItem(
                        numero = "•",
                        texto = "No recopilamos datos personales sin tu consentimiento."
                    )

                    TerminoItem(
                        numero = "•",
                        texto = "Puedes eliminar tu cuenta y datos en cualquier momento."
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Divider(color = Color(0xFF424242))

                    Spacer(modifier = Modifier.height(24.dp))

                    // Información de contacto
                    Text(
                        text = "Contacto",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Si tienes preguntas sobre estos términos, contáctanos en:",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "soporte@controlcash.com",
                        color = PrimaryPurple,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Última actualización: Noviembre 2025",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun TerminoItem(
    numero: String,
    texto: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = numero,
            color = TextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.width(24.dp)
        )
        Text(
            text = texto,
            color = TextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
    }
}