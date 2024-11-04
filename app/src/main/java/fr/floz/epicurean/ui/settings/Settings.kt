package fr.floz.epicurean.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.floz.epicurean.R
import fr.floz.epicurean.ui.SettingsEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    onBackPressed: () -> Unit,
    content: List<SettingsEntry>
) {

    Surface {
        Column {
            SettingsTopBar(
                title = stringResource(R.string.settings),
                onBackPressed = onBackPressed
            )

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                content.forEach { f ->
                    SettingsRow(label = stringResource(f.label), icon = f.icon, onClick = f.onClick)
                }

                if (state.showLanguageDialog) {
                    LanguageDialog(onEvent, state)
                }

            }
        }
    }




}

@Composable
private fun LanguageDialog(
    onEvent: (SettingsEvent) -> Unit,
    state: SettingsState
) {
    Dialog(
        onDismissRequest = { onEvent(SettingsEvent.ShowLanguageDialog) }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            val radioOptions = listOf("Calls", "Missed", "Friends")

            Text(
                text = stringResource(R.string.settings_appearance_language),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )
            Column(Modifier.selectableGroup()) {
                radioOptions.forEach { text ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp)
                            .selectable(
                                selected = (text == state.currentLanguage),
                                onClick = { onEvent(SettingsEvent.SetLanguage(text)) },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == state.currentLanguage),
                            onClick = null
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopBar(
    title: String,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = { onBackPressed() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retour en arrière"
                )
            }
        }
    )
}

@Composable
private fun SettingsRow(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row {
        ListItem(
            headlineContent = { Text(text = label) },
            leadingContent = {
                Icon(
                    imageVector = icon,
                    contentDescription = ""
                )
            },
            modifier = Modifier.clickable { onClick() }
        )
    }
}




@Composable
@Preview
fun SettingsTopBarPreview() {
    SettingsTopBar(stringResource(R.string.settings)) { }
}

@Composable
@Preview
fun SettingsEntryPreview() {
    SettingsRow(stringResource(R.string.settings_about), Icons.Outlined.Info) {  }
}

@Composable
@Preview
fun SettingsScreenPreview() {
    SettingsScreen(state = SettingsState(), onEvent = {}, onBackPressed = { }, content = emptyList())
}