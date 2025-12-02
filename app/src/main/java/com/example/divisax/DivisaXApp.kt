package com.example.divisax

import androidx.compose.runtime.Composable

/**
 * Temporary shim to keep legacy references compiling while the project adopta el nuevo nombre NOVARA.
 * Delegates directly to [NovaraApp].
 */
@Composable
fun DivisaXApp() {
    NovaraApp()
}
