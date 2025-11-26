package com.example.asistentefinanciero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.asistentefinanciero.ui.theme.AsistenteFinancieroTheme
import com.example.asistentefinanciero.ui.vistas.egreso.RegistrarEgresoVista
import com.example.asistentefinanciero.ui.vistas.transacciones.RegistrarIngresoVista
import com.example.asistentefinanciero.viewmodel.EgresoViewModel
import com.example.asistentefinanciero.viewmodel.IngresoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AsistenteFinancieroTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: EgresoViewModel = viewModel()
                    RegistrarEgresoVista(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}