package com.example.expensetracker.presentation.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.expensetracker.data.repository.impl.InMemoryTransactionRepositoryImpl
import com.example.expensetracker.presentation.controller.AddEditTransactionController
import com.example.expensetracker.presentation.controller.HistoryController
import com.example.expensetracker.presentation.controller.HomeController
import com.example.expensetracker.presentation.controller.SettingsController
import com.example.expensetracker.presentation.controller.SignInController
import com.example.expensetracker.presentation.screen.add_edit_transaction.AddEditTransactionScreen
import com.example.expensetracker.presentation.screen.auth.SignInScreen
import com.example.expensetracker.presentation.screen.history.HistoryScreen
import com.example.expensetracker.presentation.screen.home.HomeScreen
import com.example.expensetracker.presentation.screen.settings.SettingsScreen

// Define your screen identifiers
enum class Screen {
    SIGN_IN,
    HOME,
    HISTORY,
    ADD_TRANSACTION,
    // EDIT_TRANSACTION, // Can be handled by ADD_TRANSACTION with an ID
    SETTINGS
}

// A simple navigator composable
@Composable
fun AppNavigator() {
    var currentScreen by remember { mutableStateOf(Screen.SIGN_IN) }
    var currentTransactionIdForEdit by remember { mutableStateOf<String?>(null) } // For editing

    // Navigation actions
    val navigateTo: (Screen, String?) -> Unit = { screen, transactionId ->
        currentScreen = screen
        currentTransactionIdForEdit = transactionId
    }

    // Here, we can instantiate controllers or they can be singletons if appropriate
    // For simplicity now, let's assume they are created/retrieved as needed.

    when (currentScreen) {
        Screen.SIGN_IN -> {
            val signInController = remember { SignInController(
                onSignInSuccess = { navigateTo(Screen.HOME, null) }
            ) }
            SignInScreen(controller = signInController)
        }
        Screen.HOME -> {
            val homeController = remember { HomeController(
                onNavigateToHistory = { navigateTo(Screen.HISTORY, null) },
                onNavigateToAddTransaction = { navigateTo(Screen.ADD_TRANSACTION, null) },
                onNavigateToSettings = { navigateTo(Screen.SETTINGS, null) }
            ) }
            HomeScreen(controller = homeController)
        }
// Inside AppNavigation.kt, within the AppNavigator composable's when(currentScreen) block:

// In AppNavigation.kt

        Screen.HISTORY -> {
            // Correct way to get the singleton instance of your object
            val transactionRepository = remember { InMemoryTransactionRepositoryImpl }

            val historyController = remember {
                HistoryController(
                    transactionRepository = transactionRepository, // Pass the repository
                    onNavigateBack = { navigateTo(Screen.HOME, null) },
                    onNavigateToEditTransaction = { transactionId ->
                        navigateTo(Screen.ADD_TRANSACTION, transactionId)
                    }
                )
            }
            HistoryScreen(controller = historyController)
        }
        Screen.ADD_TRANSACTION -> {
            val addEditTransactionController = remember { AddEditTransactionController(
                transactionId = currentTransactionIdForEdit,
                onTransactionSaved = { navigateTo(Screen.HOME, null) }, // Or Screen.HISTORY
                onNavigateBack = { navigateTo(Screen.HOME, null) } // Or previous screen
                // TODO: Pass transaction data source (Model access) to controller
            ) }
            AddEditTransactionScreen(controller = addEditTransactionController)
        }
        Screen.SETTINGS -> {
            val settingsController = remember { SettingsController(
                onNavigateBack = { navigateTo(Screen.HOME, null) }
            ) }
            SettingsScreen(controller = settingsController)
        }
    }
}