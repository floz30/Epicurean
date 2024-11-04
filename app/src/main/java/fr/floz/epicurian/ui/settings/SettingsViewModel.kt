package fr.floz.epicurian.ui.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(): ViewModel() {

    val state = MutableStateFlow(SettingsState())

    val selectedLanguage = MutableStateFlow("")
    val onSelectedLanguage: (String) -> Unit = {}


    fun onEvent(event: SettingsEvent) {
        when(event) {
            SettingsEvent.ShowAbout -> {
                state.update { it.copy(
                    showAbout = true
                ) }
            }
            SettingsEvent.ShowAppearance -> {
                state.update { it.copy(
                    showAppearance = true
                ) }
            }
            SettingsEvent.ShowStorage -> {
                state.update { it.copy(
                    showStorage = true
                ) }
            }
            SettingsEvent.BackPressed -> {
                state.update { it.copy(
                    showAppearance = false,
                    showAbout = false,
                    showStorage = false,
                    showSettings = true
                ) }
            }

            SettingsEvent.ShowLanguageDialog -> {
                state.update { it.copy(
                    showLanguageDialog = !it.showLanguageDialog
                ) }
            }

            is SettingsEvent.SetLanguage -> {
                state.update { it.copy(
                    currentLanguage = event.language
                ) }
            }
        }
    }
}