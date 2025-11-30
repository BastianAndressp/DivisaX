package com.example.divisax.ui.screens.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Defines user intents coming from the landing UI.
 */
sealed interface LandingAction {
    data class RoleSelected(val role: LandingRole) : LandingAction
    data object ContinueClicked : LandingAction
    data object SeedRestoreClicked : LandingAction
    data object ErrorDisplayed : LandingAction
}

/**
 * One-off events used for navigation or transient UI feedback.
 */
sealed interface LandingEffect {
    data object NavigateToSeedRestore : LandingEffect
    data class NavigateToRole(val role: LandingRole) : LandingEffect
}

class LandingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LandingUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<LandingEffect>()
    val effects = _effects.asSharedFlow()

    fun onAction(action: LandingAction) {
        when (action) {
            is LandingAction.RoleSelected -> onRoleSelected(action.role)
            LandingAction.ContinueClicked -> onContinue()
            LandingAction.SeedRestoreClicked -> onSeedRestore()
            LandingAction.ErrorDisplayed -> clearError()
        }
    }

    private fun onRoleSelected(role: LandingRole) {
        _uiState.update { current ->
            current.copy(
                selectedRole = role,
                errorMessage = null
            )
        }
    }

    private fun onContinue() {
        val currentRole = _uiState.value.selectedRole
        if (currentRole == null) {
            _uiState.update { it.copy(errorMessage = "Selecciona una opción para continuar.") }
            return
        }

        viewModelScope.launch {
            _effects.emit(LandingEffect.NavigateToRole(currentRole))
        }
    }

    private fun onSeedRestore() {
        viewModelScope.launch {
            _effects.emit(LandingEffect.NavigateToSeedRestore)
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
