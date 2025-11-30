package com.example.divisax.ui.screens.landing

import androidx.compose.ui.graphics.Color
import com.example.divisax.ui.theme.BuyerAccent
import com.example.divisax.ui.theme.MerchantAccent

/**
 * UI state representation for the landing screen. Additional properties can be
 * appended here as the onboarding flow grows (e.g. remote config flags or A/B experiments).
 */
data class LandingUiState(
    val isLoading: Boolean = false,
    val selectedRole: LandingRole? = null,
    val options: List<RoleOption> = LandingDefaults.roleOptions,
    val errorMessage: String? = null
)

/**
 * Defines the type of onboarding path the user can take from the landing screen.
 */
enum class LandingRole {
    Shopper,
    Merchant
}

/**
 * Lightweight icon abstraction so we can map to Compose vectors without storing
 * framework types in the state layer.
 */
enum class LandingIcon {
    Shopper,
    Merchant
}

/**
 * Describes the content shown for each role option.
 */
data class RoleOption(
    val role: LandingRole,
    val title: String,
    val description: String,
    val accentColor: Color,
    val badgeLabel: String,
    val icon: LandingIcon
)

/**
 * Default configuration for the landing screen. Keeping defaults centralized
 * simplifies testing and allows future overrides (e.g. remote values).
 */
object LandingDefaults {
    val roleOptions: List<RoleOption> = listOf(
        RoleOption(
            role = LandingRole.Shopper,
            title = "Usuario / Comprador",
            description = "Paga tus compras diarias usando cripto de forma rápida y segura.",
            accentColor = BuyerAccent,
            badgeLabel = "Pagos diarios",
            icon = LandingIcon.Shopper
        ),
        RoleOption(
            role = LandingRole.Merchant,
            title = "Comercio / Vendedor",
            description = "Acepta cripto en tu negocio con liquidación transparente.",
            accentColor = MerchantAccent,
            badgeLabel = "Nuevo canal",
            icon = LandingIcon.Merchant
        )
    )
}
