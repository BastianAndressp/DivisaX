package com.example.divisax

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.divisax.ui.theme.DivisaXTheme
import com.example.divisax.ui.screens.landing.LandingRole
import com.example.divisax.ui.screens.landing.LandingRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DivisaXTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LandingRoute(
                        onNavigateToSeedRestore = {
                            Toast.makeText(
                                this,
                                "Pronto podrás restaurar tu billetera con tu frase semilla.",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onNavigateToRole = { role: LandingRole ->
                            val message = when (role) {
                                LandingRole.Shopper -> "Preparando la experiencia de usuario comprador."
                                LandingRole.Merchant -> "Preparando la experiencia de comercio."
                            }
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

