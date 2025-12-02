package com.example.divisax.ui.screens.pinsetup

const val PIN_REQUIRED_LENGTH = 6

enum class PinSetupStage {
    Create,
    Confirm
}

data class PinSetupUiState(
    val stage: PinSetupStage = PinSetupStage.Create,
    val pinInput: String = "",
    val firstPin: String? = null,
    val isBiometricEnabled: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
) {
    val isPinComplete: Boolean get() = pinInput.length == PIN_REQUIRED_LENGTH
}
