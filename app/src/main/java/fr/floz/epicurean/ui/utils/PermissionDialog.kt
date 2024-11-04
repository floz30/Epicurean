package fr.floz.epicurean.ui.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = {
            Text("Demande de permission")
        },
        text = {
            Text(
                text = permissionTextProvider.getDescription(isPermanentlyDeclined)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isPermanentlyDeclined) onGoToAppSettingsClick()
                    else onConfirm()
                }
            ) {
                Text(
                    text = if (isPermanentlyDeclined) {
                        "Accorder la permission"
                    } else {
                        "OK"
                    }
                )
            }
        },
        dismissButton = {
            if (isPermanentlyDeclined) {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text("Ignorer")
                }
            }
        }
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class LocationPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "Il semble vous avez refusé définitivement la permission de géocalisation. " +
                    "Vous pouvez vous rendre dans les paramètres pour l'activer."
        } else {
            "Cette application utilise votre position pour centrer la carte sur vous et " +
                    "ainsi voir les éléments proches."
        }
    }
}