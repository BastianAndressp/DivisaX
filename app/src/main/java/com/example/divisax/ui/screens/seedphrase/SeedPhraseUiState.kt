package com.example.divisax.ui.screens.seedphrase

data class SeedPhraseUiState(
    val words: List<String> = SeedPhraseDefaults.randomSeed(),
    val isAcknowledged: Boolean = false,
    val showCopyWarning: Boolean = false,
    val isProcessing: Boolean = false,
    val errorMessage: String? = null
)

object SeedPhraseDefaults {
    private val seedPool = listOf(
        "oasis",
        "cristal",
        "satelite",
        "bosque",
        "luz",
        "atlantico",
        "modulo",
        "origen",
        "trueno",
        "vortice",
        "nexo",
        "aurora",
        "quantum",
        "marea",
        "naciente",
        "lienzo",
        "vector",
        "brisa",
        "mirador",
        "halcon"
    )

    fun randomSeed(): List<String> = seedPool.shuffled().take(12)
}
