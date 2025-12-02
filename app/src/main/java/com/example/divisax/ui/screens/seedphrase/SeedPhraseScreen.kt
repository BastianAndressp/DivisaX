package com.example.divisax.ui.screens.seedphrase

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.divisax.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun SeedPhraseRoute(
    onBack: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SeedPhraseViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is SeedPhraseEffect.CopyToClipboard -> {
                    clipboardManager.setText(AnnotatedString(effect.words.joinToString(" ")))
                    launch {
                        snackbarHostState.showSnackbar("Frase copiada. Guardala en un lugar offline y seguro.")
                    }
                }
                SeedPhraseEffect.Continue -> onContinue()
            }
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onAction(SeedPhraseAction.ErrorDismissed)
        }
    }

    SeedPhraseScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction,
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
fun SeedPhraseScreen(
    uiState: SeedPhraseUiState,
    snackbarHostState: SnackbarHostState,
    onAction: (SeedPhraseAction) -> Unit,
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
                                Color(0xFF150A24).copy(alpha = 0.92f),
                                Color(0xFF1F0B2E).copy(alpha = 0.97f)
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
                TopBar(onBack = onBack)

                WarningHeader()

                SeedPhraseCard(words = uiState.words)

                KycNotice()

                ActionButtons(
                    isAcknowledged = uiState.isAcknowledged,
                    isProcessing = uiState.isProcessing,
                    onAction = onAction
                )
            }
        }
    }

    if (uiState.showCopyWarning) {
        CopyWarningDialog(onAction = onAction)
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
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
private fun WarningHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.size(70.dp),
            shape = CircleShape,
            color = Color(0xFFFF9800).copy(alpha = 0.18f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Text(
            text = "Esta es tu llave maestra",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Si la pierdes, pierdes tu dinero. Nosotros no podemos recuperarla.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.78f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SeedPhraseCard(words: List<String>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(alpha = 0.06f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            words.chunked(3).forEachIndexed { rowIndex, triple ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    triple.forEachIndexed { columnIndex, word ->
                        val position = rowIndex * 3 + columnIndex
                        SeedWordItem(
                            index = position,
                            word = word,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    repeat(3 - triple.size) {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .height(SeedWordHeight)
                        )
                    }
                }
            }
        }
    }
}

private val SeedWordHeight = 48.dp

@Composable
private fun SeedWordItem(index: Int, word: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .height(SeedWordHeight),
        shape = RoundedCornerShape(14.dp),
        color = Color.Black.copy(alpha = 0.25f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = String.format("%02d", index + 1),
                color = Color.White.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
            Text(
                text = word,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun KycNotice() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.05f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Importante",
                color = Color(0xFFFFC107),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "Sin completar KYC podrás operar hasta 100 USD. Para montos superiores deberás verificar tu identidad.",
                color = Color.White.copy(alpha = 0.78f),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Text(
                text = "No te pediremos reescribir las palabras ahora. Hazlo cuando tengas más de 10 USD almacenados.",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun ActionButtons(
    isAcknowledged: Boolean,
    isProcessing: Boolean,
    onAction: (SeedPhraseAction) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        OutlinedButton(
            onClick = { onAction(SeedPhraseAction.CopyRequested) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
            contentPadding = PaddingValues(horizontal = 18.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.ContentCopy,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Copiar al portapapeles",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
        val toggleAcknowledgement = {
            onAction(SeedPhraseAction.AcknowledgementChanged(!isAcknowledged))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = toggleAcknowledgement)
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Checkbox(
                checked = isAcknowledged,
                onCheckedChange = { toggleAcknowledgement() },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF00F0FF))
            )
            Text(
                text = "Ya las guardé en un lugar seguro",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp
            )
        }

        FilledTonalButton(
            onClick = { onAction(SeedPhraseAction.ContinuePressed) },
            enabled = isAcknowledged && !isProcessing,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF00F0FF)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isProcessing) "Procesando..." else "Continuar",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CopyWarningDialog(onAction: (SeedPhraseAction) -> Unit) {
    AlertDialog(
        onDismissRequest = { onAction(SeedPhraseAction.CopyDismissed) },
        title = {
            Text(
                text = "Copia solo si estás en un lugar seguro",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Evita compartir tu frase en apps o notas en la nube. Cópiala únicamente si estás desconectado de internet o puedes almacenarla cifrada.",
                lineHeight = 18.sp
            )
        },
        confirmButton = {
            TextButton(onClick = { onAction(SeedPhraseAction.CopyConfirmed) }) {
                Text(text = "Entendido")
            }
        },
        dismissButton = {
            TextButton(onClick = { onAction(SeedPhraseAction.CopyDismissed) }) {
                Text(text = "Cancelar")
            }
        }
    )
}
