package com.example.divisax

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.divisax.ui.navigation.AppDestination
import com.example.divisax.ui.screens.home.HomeRoute
import com.example.divisax.ui.screens.landing.LandingRole
import com.example.divisax.ui.screens.landing.LandingRoute
import com.example.divisax.ui.screens.merchant.MerchantComingSoonScreen
import com.example.divisax.ui.screens.pinsetup.PinSetupRoute
import com.example.divisax.ui.screens.seedphrase.SeedPhraseRoute

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NovaraApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = AppDestination.Landing.route,
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(220),
                    initialOffsetX = { it }
                ) + fadeIn(animationSpec = tween(220))
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(160),
                    targetOffsetX = { -it }
                ) + fadeOut(animationSpec = tween(160))
            },
            popEnterTransition = {
                slideInHorizontally(
                    animationSpec = tween(220),
                    initialOffsetX = { -it }
                ) + fadeIn(animationSpec = tween(220))
            },
            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(160),
                    targetOffsetX = { it }
                ) + fadeOut(animationSpec = tween(160))
            }
        ) {
            composable(AppDestination.Landing.route) {
                LandingRoute(
                    onNavigateToSeedRestore = {
                        Toast.makeText(
                            context,
                            "Pronto podrás restaurar tu billetera con tu frase semilla.",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onNavigateToRole = { role: LandingRole ->
                        when (role) {
                            LandingRole.Shopper -> navController.navigate(AppDestination.PinSetup.route)
                            LandingRole.Merchant -> navController.navigate(AppDestination.MerchantOnboarding.route)
                        }
                    }
                )
            }

            composable(AppDestination.PinSetup.route) {
                PinSetupRoute(
                    onBack = { navController.popBackStack() },
                    onPinCreated = { _, biometricsEnabled ->
                        val message = if (biometricsEnabled) {
                            "PIN configurado y biometría activada."
                        } else {
                            "PIN configurado."
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        navController.navigate(AppDestination.SeedPhrase.route) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(AppDestination.MerchantOnboarding.route) {
                MerchantComingSoonScreen(onBack = { navController.popBackStack() })
            }

            composable(AppDestination.SeedPhrase.route) {
                SeedPhraseRoute(
                    onBack = { navController.popBackStack() },
                    onContinue = {
                        Toast.makeText(
                            context,
                            "Frase guardada. Configuración inicial completa.",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(AppDestination.WalletHome.route) {
                            launchSingleTop = true
                            popUpTo(AppDestination.Landing.route) { inclusive = false }
                        }
                    }
                )
            }

            composable(AppDestination.WalletHome.route) {
                HomeRoute(
                    onReceiveClick = {
                        Toast.makeText(context, "Mostrando tu QR de recepción", Toast.LENGTH_SHORT).show()
                    },
                    onScanClick = {
                        Toast.makeText(context, "Abriendo cámara para escanear.", Toast.LENGTH_SHORT).show()
                    },
                    onTopUpClick = {
                        Toast.makeText(context, "Pronto podrás recargar con Solana o USDC.", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
