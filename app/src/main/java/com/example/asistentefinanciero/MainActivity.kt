package com.example.asistentefinanciero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.asistentefinanciero.ui.theme.AsistenteFinancieroTheme
import com.example.asistentefinanciero.ui.vistas.login.LoginVista

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AsistenteFinancieroTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LoginVista()
                }
            }
        }
    }
}