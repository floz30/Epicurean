package fr.floz.epicurian.ui.settings

/**
 * Evénements possibles lorsque l'utilisateur se trouve dans l'écran de paramétrage de l'application.
 */
sealed interface SettingsEvent {
    /**
     * L'utilisateur clique sur le paramétrage de l'apparance de l'application.
     */
    data object ShowAppearance: SettingsEvent

    /**
     * L'utilisateur clique sur le paramétrage du stockage des données.
     */
    data object ShowStorage: SettingsEvent

    /**
     * L'utilisateur clique sur le l'affichage des informations de l'application.
     */
    data object ShowAbout: SettingsEvent

    data object BackPressed: SettingsEvent

    data object ShowLanguageDialog: SettingsEvent

    data class SetLanguage(val language: String): SettingsEvent
}