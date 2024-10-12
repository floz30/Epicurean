package fr.floz.epicurian.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsScreen(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    SettingsContent()
}


@Composable
private fun SettingsContent() {
    Text(text = "Liste des paramètres disponibles")
}