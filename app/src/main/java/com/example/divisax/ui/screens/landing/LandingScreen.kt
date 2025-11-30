package com.example.divisax.ui.screens.landing

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.divisax.R
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun LandingRoute(
    onNavigateToSeedRestore: () -> Unit,
    onNavigateToRole: (LandingRole) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LandingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                LandingEffect.NavigateToSeedRestore -> onNavigateToSeedRestore()
                is LandingEffect.NavigateToRole -> onNavigateToRole(effect.role)
            }
        }
    }

    LandingScreen(
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
fun LandingScreen(
    snackbarHostState: SnackbarHostState,
    onAction: (LandingAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFF0A0E17),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Fondo Base
            Image(
                painter = painterResource(id = R.drawable.landing_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().alpha(0.4f)
            )

            // Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF05182D).copy(alpha = 0.8f),
                                Color(0xFF1A0B2E).copy(alpha = 0.95f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    // PUNTO MEDIO: Padding más relajado que el anterior, pero controlado
                    .padding(top = 32.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                // PUNTO MEDIO: 20dp da aire sin separar demasiado las cosas
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                LandingLogo()

                BrandHeader()

                // Ilustración equilibrada
                LandingIllustration()

                // Sección de botones
                RoleSelectorSection(
                    onOptionSelected = { role ->
                        onAction(LandingAction.RoleSelected(role))
                        onAction(LandingAction.ContinueClicked)
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                LoginFooter(
                    onLoginClick = { onAction(LandingAction.SeedRestoreClicked) }
                )
            }
        }
    }
}

@Composable
private fun LandingLogo(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logo_nfc),
            contentDescription = "Logo Novara",
            tint = Color(0xFF00F0FF),
            // Tamaño medio (ni 40 ni 48)
            modifier = Modifier.size(44.dp)
        )
        Text(
            text = "NOVARA",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp, // Ajuste fino de tamaño
            color = Color.White
        )
    }
}

@Composable
private fun BrandHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "El futuro de los pagos,\nen tu bolsillo.",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )

        Text(
            text = "Paga con cripto en tu tienda local favorita usando solo tu celular. Rápido, seguro y sin contacto.",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp, // Mantenemos 13sp para que quepa bien
                lineHeight = 19.sp // Un poco más de aire entre líneas para leer mejor
            ),
            color = Color.White.copy(alpha = 0.75f), // Un poco más brillante
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
    }
}

@Composable
private fun LandingIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            // PUNTO MEDIO: 135dp. Se ve bien el detalle sin ocupar media pantalla.
            .height(135.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.pago_nfc),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun RoleSelectorSection(
    onOptionSelected: (LandingRole) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        // Separación elegante entre el título y las tarjetas
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "¿Cómo quieres empezar?",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 19.sp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
        )

        RoleCard(
            title = "Usuario / Comprador",
            description = "Quiero pagar mis compras diarias con criptomonedas.",
            accentColor = Color(0xFF00E5FF),
            icon = Icons.Rounded.Person,
            onClick = { onOptionSelected(LandingRole.Shopper) }
        )

        RoleCard(
            title = "Comercio / Vendedor",
            description = "Quiero aceptar pagos cripto en mi negocio.",
            accentColor = Color(0xFFD500F9),
            icon = Icons.Rounded.Store,
            onClick = { onOptionSelected(LandingRole.Merchant) }
        )
    }
}

@Composable
private fun RoleCard(
    title: String,
    description: String,
    accentColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "scale")
    val shape = RoundedCornerShape(20.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            // PUNTO MEDIO: 100dp. Da espacio para que el texto respire.
            .height(100.dp)
            .scale(scale)
            .shadow(
                elevation = 12.dp,
                shape = shape,
                spotColor = Color.Black.copy(alpha = 0.5f),
                ambientColor = Color.Black
            )
            .border(
                BorderStroke(1.dp, accentColor.copy(alpha = 0.6f)),
                shape = shape
            )
            .clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.35f),
                            accentColor.copy(alpha = 0.10f)
                        )
                    )
                )
                // Padding interno cómodo
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Icono tamaño medio (48dp)
                IconBadge(accentColor = accentColor, icon = icon, size = 48.dp)

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.95f),
                        lineHeight = 15.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun IconBadge(
    accentColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    size: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(
                color = accentColor.copy(alpha = 0.25f),
                shape = RoundedCornerShape(13.dp)
            )
            .border(
                width = 1.dp,
                color = accentColor.copy(alpha = 0.4f),
                shape = RoundedCornerShape(13.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(size * 0.55f)
        )
    }
}

@Composable
private fun LoginFooter(onLoginClick: () -> Unit) {
    TextButton(
        onClick = onLoginClick,
        modifier = Modifier.fillMaxWidth(),
        // Un poco más de área touch
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Key,
                contentDescription = null,
                tint = Color(0xFF00F0FF),
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "¿Ya tienes tu frase semilla? ",
                style = MaterialTheme.typography.bodyMedium, // Volvemos a bodyMedium
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = "Restaura aquí.",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
            )
        }
    }
}