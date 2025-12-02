package com.example.divisax.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.divisax.R

@Composable
fun HomeRoute(
    onReceiveClick: () -> Unit,
    onScanClick: () -> Unit,
    onTopUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    HomeScreen(
        snackbarHostState = snackbarHostState,
        onReceiveClick = onReceiveClick,
        onScanClick = onScanClick,
        onTopUpClick = onTopUpClick,
        modifier = modifier
    )
}

@Composable
fun HomeScreen(
    snackbarHostState: SnackbarHostState,
    onReceiveClick: () -> Unit,
    onScanClick: () -> Unit,
    onTopUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFF050A16),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FloatingActionButton(
                    onClick = onReceiveClick,
                    shape = CircleShape,
                    containerColor = Color(0xFF00E5FF),
                    elevation = FloatingActionButtonDefaults.elevation(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Download,
                        contentDescription = "Recibir",
                        tint = Color(0xFF021018)
                    )
                }
                FloatingActionButton(
                    onClick = onScanClick,
                    shape = CircleShape,
                    containerColor = Color(0xFF6C4BFF),
                    elevation = FloatingActionButtonDefaults.elevation(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.QrCodeScanner,
                        contentDescription = "Escanear",
                        tint = Color.White
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.landing_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.35f)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0A1324).copy(alpha = 0.9f),
                                Color(0xFF1A0930).copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                BalanceCard()

                EmptyState(onTopUpClick = onTopUpClick)
            }
        }
    }
}

@Composable
private fun BalanceCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0.35f)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFF00E5FF).copy(alpha = 0.45f), Color.Transparent)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Saldo disponible",
                    color = Color.White.copy(alpha = 0.65f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$0.00",
                    color = Color.White,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "USD",
                    color = Color.White.copy(alpha = 0.65f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun EmptyState(onTopUpClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.04f)),
        shape = CardDefaults.elevatedShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Tu billetera está vacía",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Recarga Solana o USDC aquí.",
                color = Color.White.copy(alpha = 0.75f),
                textAlign = TextAlign.Center
            )
            TextButton(
                onClick = onTopUpClick,
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF00E5FF)),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Recargar ahora",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
