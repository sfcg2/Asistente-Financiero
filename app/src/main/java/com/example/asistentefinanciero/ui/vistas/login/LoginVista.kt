package com.example.asistentefinanciero.ui.vistas.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asistentefinanciero.R
import com.example.asistentefinanciero.ui.theme.*
import com.example.asistentefinanciero.viewmodel.AuthViewModel




@Composable
fun LoginVista(
    viewModel: AuthViewModel,
    onLoginExitoso: () -> Unit,
    onIrARegistro: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val cargando by viewModel.cargando.collectAsState()
    val errorMensaje by viewModel.errorMensaje.collectAsState()

    // Limpiar error al entrar a la vista
    LaunchedEffect(Unit) {
        viewModel.limpiarError()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo), // Usa el nombre que le diste
                contentDescription = "Logo SVG",
                modifier = Modifier.size(80.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Título
            Box{
                Text(
                    text = "Control",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "             Cash",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Card blanca con inputs
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Bienvenido",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo Correo
                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            viewModel.limpiarError()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "Correo",
                                color = Color(0xFFB4B4B4)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = PrimaryPurple
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !cargando,
                        singleLine = true,
                        isError = errorMensaje != null
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Contraseña
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = {
                            contrasena = it
                            viewModel.limpiarError()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "Contraseña",
                                color = Color(0xFFB4B4B4)
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = PrimaryPurple
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !cargando,
                        singleLine = true,
                        isError = errorMensaje != null
                    )

                    // Mostrar error si existe
                    if (errorMensaje != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMensaje ?: "",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón Ingresar
                    Button(
                        onClick = {
                            when {
                                correo.isBlank() -> {
                                    viewModel.setError("Ingrese su correo electrónico")
                                }
                                contrasena.isBlank() -> {
                                    viewModel.setError("Ingrese su contraseña")
                                }
                                else -> {
                                    viewModel.login(correo, contrasena) {
                                        onLoginExitoso()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryPurple
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !cargando
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "Ingresar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Link Registrarse
            TextButton(
                onClick = {
                    viewModel.limpiarError()
                    onIrARegistro()
                }
            ) {
                Text(
                    text = "¿Aún no tienes cuenta? Registrate",
                    color = TextPrimary,
                    fontSize = 16.sp
                )
            }
        }
    }
}