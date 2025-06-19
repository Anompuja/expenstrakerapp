package com.example.expensetracker.presentation.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.expensetracker.data.model.Transaction // For summary data later
import com.example.expensetracker.data.repository.TransactionRepository
import com.example.expensetracker.data.repository.impl.InMemoryTransactionRepositoryImpl

// Data class for UI state of the Home Screen
data class HomeUIState(
    val userName: String = "Degas", // Placeholder
    val totalBalance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HomeController(
    private val onNavigateToHistory: () -> Unit,
    private val onNavigateToAddTransaction: () -> Unit,
    private val onNavigateToSettings: () -> Unit,
    // Injecting the repository (Model access)
    private val transactionRepository: TransactionRepository = InMemoryTransactionRepositoryImpl
) {
    var uiState by mutableStateOf(HomeUIState(isLoading = true))
        private set

    init {
        loadHomeScreenData()
    }

    fun loadHomeScreenData() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        try {
            // In a real app, these would be suspend functions and run in a coroutine scope
            val allTransactions = transactionRepository.getAllTransactions()
            val income = allTransactions.filter { !it.isExpense }.sumOf { it.amount }
            val expenses = allTransactions.filter { it.isExpense }.sumOf { it.amount }
            val balance = income - expenses
            val recent = allTransactions.take(5) // Show latest 5 transactions

            uiState = uiState.copy(
                userName = "Degas", // Replace with actual user data if available
                totalBalance = balance,
                totalIncome = income,
                totalExpenses = expenses,
                recentTransactions = recent,
                isLoading = false
            )
        } catch (e: Exception) {
            // Handle exceptions, e.g., from data source
            uiState =
                uiState.copy(isLoading = false, errorMessage = "Error loading data: ${e.message}")
            println("Error loading home screen data: ${e.localizedMessage}")
        }
    }

    fun navigateToHistoryScreen() {
        onNavigateToHistory()
    }

    fun navigateToAddTransactionScreen() {
        onNavigateToAddTransaction()
    }

    fun navigateToSettingsScreen() {
        onNavigateToSettings()
    }
}