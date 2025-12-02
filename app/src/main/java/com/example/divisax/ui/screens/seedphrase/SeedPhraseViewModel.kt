package com.example.divisax.ui.screens.seedphrase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface SeedPhraseAction {
    data object CopyRequested : SeedPhraseAction
    data object CopyDismissed : SeedPhraseAction
    data object CopyConfirmed : SeedPhraseAction
    data class AcknowledgementChanged(val checked: Boolean) : SeedPhraseAction
    data object ContinuePressed : SeedPhraseAction
    data object ErrorDismissed : SeedPhraseAction
}

sealed interface SeedPhraseEffect {
    data class CopyToClipboard(val words: List<String>) : SeedPhraseEffect
    data object Continue : SeedPhraseEffect
}

class SeedPhraseViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SeedPhraseUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SeedPhraseEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effects = _effects.asSharedFlow()

    fun onAction(action: SeedPhraseAction) {
        when (action) {
            SeedPhraseAction.CopyRequested -> onCopyRequested()
            SeedPhraseAction.CopyDismissed -> _uiState.update { it.copy(showCopyWarning = false) }
            SeedPhraseAction.CopyConfirmed -> onCopyConfirmed()
            is SeedPhraseAction.AcknowledgementChanged -> onAcknowledgementChanged(action.checked)
            SeedPhraseAction.ContinuePressed -> onContinuePressed()
            SeedPhraseAction.ErrorDismissed -> clearError()
        }
    }

    private fun onCopyRequested() {
        _uiState.update { it.copy(showCopyWarning = true, errorMessage = null) }
    }

    private fun onCopyConfirmed() {
        val current = _uiState.value
        _uiState.update {
            it.copy(
                showCopyWarning = false,
                isAcknowledged = true,
                errorMessage = null
            )
        }

        _effects.tryEmit(SeedPhraseEffect.CopyToClipboard(current.words))
    }

    private fun onAcknowledgementChanged(checked: Boolean) {
        _uiState.update { it.copy(isAcknowledged = checked, errorMessage = null) }
    }

    private fun onContinuePressed() {
        val current = _uiState.value
        if (!current.isAcknowledged) {
            _uiState.update {
                it.copy(errorMessage = "Confirma que guardaste la frase semilla antes de continuar.")
            }
            return
        }

        _uiState.update { it.copy(isProcessing = true, errorMessage = null) }

        viewModelScope.launch {
            _effects.emit(SeedPhraseEffect.Continue)
            _uiState.update { it.copy(isProcessing = false) }
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
