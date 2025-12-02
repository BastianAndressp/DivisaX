package com.example.divisax.ui.screens.pinsetup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.divisax.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PinSetupRoute(
    onBack: () -> Unit,
    onPinCreated: (pin: String, biometricsEnabled: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PinSetupViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is PinSetupEffect.PinReady -> onPinCreated(effect.pin, effect.biometricsEnabled)
            }
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        val message = uiState.errorMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.onAction(PinSetupAction.ErrorDismissed)
        }
    }

    PinSetupScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction,
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
fun PinSetupScreen(
    uiState: PinSetupUiState,
    snackbarHostState: SnackbarHostState,
    onAction: (PinSetupAction) -> Unit,
    onBack: () -> Unit,
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
                                Color(0xFF081427).copy(alpha = 0.92f),
                                Color(0xFF120B24).copy(alpha = 0.97f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                TopActionBar(onBack = onBack)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = Color(0xFF00F0FF),
                        modifier = Modifier.size(42.dp)
                    )
                    StageBadge(stage = uiState.stage)
                    Text(
                        text = when (uiState.stage) {
                            PinSetupStage.Create -> "Crea un PIN de acceso"
                            PinSetupStage.Confirm -> "Confirma tu PIN"
                        },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )
                    Text(
                        text = when (uiState.stage) {
                            PinSetupStage.Create -> "Protegemos tus llaves privadas cifrándolas directamente en tu dispositivo."
                            PinSetupStage.Confirm -> "Ingresa nuevamente el PIN para asegurarnos de que lo recuerdas."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.75f),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                PinDots(pinLength = uiState.pinInput.length)

                BiometricToggleCard(
                    enabled = uiState.isBiometricEnabled,
                    onToggle = { onAction(PinSetupAction.ToggleBiometrics) }
                )

                NumericKeypad(
                    onDigitPressed = { onAction(PinSetupAction.DigitPressed(it)) },
                    onBackspace = { onAction(PinSetupAction.BackspacePressed) },
                    onBiometricShortcut = if (uiState.stage == PinSetupStage.Create) {
                        { onAction(PinSetupAction.ToggleBiometrics) }
                    } else null
                )

                FilledTonalButton(
                    onClick = { onAction(PinSetupAction.ConfirmPressed) },
                    enabled = uiState.isPinComplete && !uiState.isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = if (uiState.isPinComplete) Color(0xFF00F0FF) else Color.White.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = when {
                            uiState.isSaving -> "Guardando..."
                            uiState.stage == PinSetupStage.Create -> "Continuar"
                            else -> "Confirmar"
                        },
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun TopActionBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(44.dp),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.08f)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun StageBadge(stage: PinSetupStage) {
    val (label, accent) = when (stage) {
        PinSetupStage.Create -> "Paso 1 de 2" to Color(0xFF00F0FF)
        PinSetupStage.Confirm -> "Paso 2 de 2" to Color(0xFFFFC107)
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = accent.copy(alpha = 0.15f)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun PinDots(pinLength: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(PIN_REQUIRED_LENGTH) { index ->
            val isFilled = index < pinLength
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .sizeIn(maxWidth = 52.dp)
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.18f),
                        shape = CircleShape
                    )
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isFilled) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(0.45f)
                            .clip(CircleShape)
                            .background(Color(0xFF00F0FF))
                    )
                }
            }
        }
    }
}

@Composable
private fun BiometricToggleCard(enabled: Boolean, onToggle: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(width = 1.dp, color = Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.06f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White.copy(alpha = 0.12f)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Fingerprint,
                        contentDescription = null,
                        tint = Color(0xFF00E5FF),
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Activar FaceID/TouchID",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Usaremos biometría para desbloquear la app más rápido.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Switch(checked = enabled, onCheckedChange = { onToggle() })
        }
    }
}

@Composable
private fun NumericKeypad(
    onDigitPressed: (Int) -> Unit,
    onBackspace: () -> Unit,
    onBiometricShortcut: (() -> Unit)?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val rows = listOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6),
            listOf(7, 8, 9)
        )

        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { digit ->
                    NumericKeyButton(
                        label = digit.toString(),
                        onClick = { onDigitPressed(digit) }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBiometricShortcut != null) {
                NumericKeyButton(
                    label = "",
                    icon = Icons.Rounded.Fingerprint,
                    onClick = onBiometricShortcut
                )
            } else {
                NumericKeyPlaceholder()
            }
            NumericKeyButton(
                label = "0",
                onClick = { onDigitPressed(0) }
            )
            NumericKeyButton(
                label = "",
                icon = Icons.AutoMirrored.Rounded.Backspace,
                onClick = onBackspace
            )
        }
    }
}

@Composable
private fun NumericKeyButton(
    label: String,
    onClick: () -> Unit,
    icon: ImageVector? = null
) {
    Surface(
        modifier = Modifier
            .width(78.dp)
            .height(62.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.06f)
    ) {
        TextButton(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(0.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = if (label.isEmpty()) null else label,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            } else {
                Text(
                    text = label,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun NumericKeyPlaceholder() {
    Surface(
        modifier = Modifier
            .width(78.dp)
            .height(62.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.02f)
    ) {}
}
