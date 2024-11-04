package fr.floz.epicurian.ui.settings

data class SettingsState (
    val showSettings: Boolean = false,
    val showAppearance: Boolean = false,
    val showStorage: Boolean = false,
    val showAbout: Boolean = false,
    /**
     *
     */
    val showLanguageDialog: Boolean = false,
    val currentLanguage: String = ""
)