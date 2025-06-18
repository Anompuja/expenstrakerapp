package com.example.expensetracker.presentation.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.expensetracker.data.model.Transaction
import com.example.expensetracker.data.repository.TransactionRepository
import com.example.expensetracker.data.repository.impl.InMemoryTransactionRepositoryImpl

// UI State for the History Screen
data class HistoryUIState(
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    // Future: Add filter/sort options here
    // val currentFilter: FilterType = FilterType.ALL,
    // val currentSortOrder: SortOrder = SortOrder.DATE_DESCENDING
)

class HistoryController(
    private val onNavigateBack: () -> Unit,
    private val onNavigateToEditTransaction: (String) -> Unit, // Takes transactionId
    private val transactionRepository: TransactionRepository = InMemoryTransactionRepositoryImpl
) {
    var uiState by mutableStateOf(HistoryUIState(isLoading = true))
        private set

    init {
        loadTransactionHistory()
    }

    fun loadTransactionHistory() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        try {
            // In a real app, apply sorting/filtering here based on uiState
            val allTransactions = transactionRepository.getAllTransactions().sortedByDescending { it.date }
            uiState = uiState.copy(
                transactions = allTransactions,
                isLoading = false
            )
        } catch (e: Exception) {
            uiState = uiState.copy(isLoading = false, errorMessage = "Error loading history: ${e.message}")
            println("Error loading transaction history: ${e.localizedMessage}")
        }
    }

    fun navigateBack() {
        onNavigateBack()
    }

    fun navigateToEditTransactionScreen(transactionId: String) {
        onNavigateToEditTransaction(transactionId)
    }

    fun deleteTransaction(transactionId: String) {
        try {
            transactionRepository.deleteTransaction(transactionId)
            loadTransactionHistory() // Refresh the list
        } catch (e: Exception) {
            uiState = uiState.copy(errorMessage = "Error deleting transaction: ${e.message}")
            println("Error deleting transaction: ${e.localizedMessage}")
        }
    }
}