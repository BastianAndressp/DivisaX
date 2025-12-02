package com.example.divisax.ui.navigation

sealed class AppDestination(val route: String) {
    data object Landing : AppDestination("landing")
    data object PinSetup : AppDestination("pin_setup")
    data object MerchantOnboarding : AppDestination("merchant_onboarding")
    data object SeedRestore : AppDestination("seed_restore")
    data object SeedPhrase : AppDestination("seed_phrase")
    data object WalletHome : AppDestination("wallet_home")
}
