package com.example.expensetracker.presentation.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// UI State for the Settings Screen
data class SettingsUIState(
    val isDarkThemeEnabled: Boolean = false, // Example setting
    val areNotificationsEnabled: Boolean = true, // Example setting
    val appVersion: String = "1.0.0" // Example static info
    // Add more settings as needed
)

class SettingsController(
    private val onNavigateBack: () -> Unit,
    // Potentially pass a preferences repository or similar for actual settings persistence
    // private val userPreferencesRepository: UserPreferencesRepository
) {
    var uiState by mutableStateOf(SettingsUIState())
        private set

    init {
        loadSettings()
    }

    private fun loadSettings() {
        // In a real app, load these from SharedPreferences, DataStore, or a database
        // For now, we'll use default values or mock them.
        // Example:
        // viewModelScope.launch {
        //     val isDark = userPreferencesRepository.isDarkTheme.first()
        //     val notifications = userPreferencesRepository.areNotificationsEnabled.first()
        //     uiState = uiState.copy(isDarkThemeEnabled = isDark, areNotificationsEnabled = notifications)
        // }
        uiState = SettingsUIState(
            isDarkThemeEnabled = false, // Default or fetched value
            areNotificationsEnabled = true, // Default or fetched value
            appVersion = "1.0.0-mvc" // Example
        )
    }

    fun onDarkThemeToggle(isEnabled: Boolean) {
        uiState = uiState.copy(isDarkThemeEnabled = isEnabled)
        // Persist this change
        // viewModelScope.launch { userPreferencesRepository.setDarkTheme(isEnabled) }
        // For now, this only affects UI state, not the actual app theme globally
        // Global theme changes require recomposing the root with the new theme setting.
        println("Dark theme toggled: $isEnabled (UI state only)")
    }

    fun onNotificationsToggle(isEnabled: Boolean) {
        uiState = uiState.copy(areNotificationsEnabled = isEnabled)
        // Persist this change
        // viewModelScope.launch { userPreferencesRepository.setNotificationsEnabled(isEnabled) }
        println("Notifications toggled: $isEnabled")
    }

    fun navigateBack() {
        onNavigateBack()
    }

    // Add other settings actions here, e.g., clear data, export data, etc.
    fun logoutUser() {
        // Perform logout logic (clear session, navigate to login)
        println("User logout requested.")
        // This would typically involve calling a method on an AuthController or similar
        // and then navigating to the SignInScreen.
        // For now, it's just a placeholder.
    }
}