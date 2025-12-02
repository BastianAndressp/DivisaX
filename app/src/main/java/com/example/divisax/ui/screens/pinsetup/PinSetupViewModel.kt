package com.example.divisax.ui.screens.pinsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface PinSetupAction {
    data class DigitPressed(val digit: Int) : PinSetupAction
    data object BackspacePressed : PinSetupAction
    data object ToggleBiometrics : PinSetupAction
    data object ConfirmPressed : PinSetupAction
    data object ErrorDismissed : PinSetupAction
}

sealed interface PinSetupEffect {
    data class PinReady(val pin: String, val biometricsEnabled: Boolean) : PinSetupEffect
}

class PinSetupViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PinSetupUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<PinSetupEffect>()
    val effects = _effects.asSharedFlow()

    fun onAction(action: PinSetupAction) {
        when (action) {
            is PinSetupAction.DigitPressed -> onDigitPressed(action.digit)
            PinSetupAction.BackspacePressed -> onBackspace()
            PinSetupAction.ToggleBiometrics -> onToggleBiometrics()
            PinSetupAction.ConfirmPressed -> onConfirm()
            PinSetupAction.ErrorDismissed -> clearError()
        }
    }

    private fun onDigitPressed(digit: Int) {
        if (digit !in 0..9) return
        _uiState.update { current ->
            if (current.pinInput.length >= PIN_REQUIRED_LENGTH || current.isSaving) current
            else current.copy(pinInput = current.pinInput + digit.toString(), errorMessage = null)
        }
    }

    private fun onBackspace() {
        _uiState.update { current ->
            if (current.pinInput.isEmpty() || current.isSaving) current
            else current.copy(pinInput = current.pinInput.dropLast(1), errorMessage = null)
        }
    }

    private fun onToggleBiometrics() {
        _uiState.update { current ->
            current.copy(isBiometricEnabled = !current.isBiometricEnabled)
        }
    }

    private fun onConfirm() {
        val current = _uiState.value
        if (current.pinInput.length < PIN_REQUIRED_LENGTH) {
            _uiState.update {
                it.copy(errorMessage = "El PIN debe tener $PIN_REQUIRED_LENGTH dígitos.")
            }
            return
        }

        when (current.stage) {
            PinSetupStage.Create -> {
                _uiState.update {
                    it.copy(
                        stage = PinSetupStage.Confirm,
                        firstPin = current.pinInput,
                        pinInput = "",
                        errorMessage = null
                    )
                }
            }

            PinSetupStage.Confirm -> {
                if (current.firstPin != current.pinInput) {
                    _uiState.update {
                        it.copy(
                            pinInput = "",
                            errorMessage = "El PIN no coincide, inténtalo nuevamente."
                        )
                    }
                    return
                }

                _uiState.update { it.copy(isSaving = true, errorMessage = null) }

                viewModelScope.launch {
                    _effects.emit(
                        PinSetupEffect.PinReady(current.pinInput, current.isBiometricEnabled)
                    )
                    _uiState.value = PinSetupUiState(isBiometricEnabled = current.isBiometricEnabled)
                }
            }
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
