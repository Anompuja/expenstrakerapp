package com.example.expensetracker.presentation.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.expensetracker.data.model.ExpenseType
import com.example.expensetracker.data.model.Transaction
import com.example.expensetracker.data.repository.TransactionRepository
import com.example.expensetracker.data.repository.impl.InMemoryTransactionRepositoryImpl
import java.util.Calendar
import java.util.Date
import java.util.UUID

// UI State for the Add/Edit Transaction Screen
data class AddEditTransactionUIState(
    val transactionId: String? = null, // Null for new, non-null for edit
    val title: String = "",
    val amount: String = "", // Store as String for TextField, convert to Double on save
    val selectedExpenseType: ExpenseType = ExpenseType.OTHER, // Default type
    val transactionDate: Date = Date(), // Default to current date
    val description: String = "",
    val isExpense: Boolean = true, // Default to expense

    val availableExpenseTypes: List<ExpenseType> = ExpenseType.values().toList(),
    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val titleError: String? = null,
    val amountError: String? = null
)

class AddEditTransactionController(
    private val transactionId: String?, // If null, it's an "Add" operation, otherwise "Edit"
    private val onTransactionSaved: () -> Unit,
    private val onNavigateBack: () -> Unit,
    private val transactionRepository: TransactionRepository = InMemoryTransactionRepositoryImpl
) {
    var uiState by mutableStateOf(AddEditTransactionUIState(isLoading = true))
        private set

    init {
        if (transactionId != null) {
            loadTransactionDetails(transactionId)
        } else {
            // New transaction, set defaults
            uiState = AddEditTransactionUIState(
                isLoading = false,
                isEditing = false,
                transactionDate = Calendar.getInstance().time // Sensible default
            )
        }
    }

    private fun loadTransactionDetails(id: String) {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        val transaction = transactionRepository.getTransactionById(id)
        if (transaction != null) {
            uiState = AddEditTransactionUIState(
                transactionId = transaction.id,
                title = transaction.title,
                amount = transaction.amount.toString(),
                selectedExpenseType = transaction.type,
                transactionDate = transaction.date,
                description = transaction.description ?: "",
                isExpense = transaction.isExpense,
                isEditing = true,
                isLoading = false
            )
        } else {
            uiState = uiState.copy(isLoading = false, errorMessage = "Transaction not found.")
        }
    }

    fun onTitleChange(newTitle: String) {
        uiState = uiState.copy(title = newTitle, titleError = null, errorMessage = null)
    }

    fun onAmountChange(newAmount: String) {
        // Allow only numbers and a single decimal point
        if (newAmount.isEmpty() || newAmount.matches(Regex("^\\d*\\.?\\d*\$"))) {
            uiState = uiState.copy(amount = newAmount, amountError = null, errorMessage = null)
        }
    }

    fun onExpenseTypeChange(newType: ExpenseType) {
        uiState = uiState.copy(selectedExpenseType = newType, errorMessage = null)
    }

    fun onDateChange(newDateMillis: Long) {
        uiState = uiState.copy(transactionDate = Date(newDateMillis), errorMessage = null)
    }

    fun onDescriptionChange(newDescription: String) {
        uiState = uiState.copy(description = newDescription, errorMessage = null)
    }

    fun onIsExpenseChange(isExpense: Boolean) {
        uiState = uiState.copy(isExpense = isExpense, errorMessage = null)
    }

    private fun validateInput(): Boolean {
        var isValid = true
        if (uiState.title.isBlank()) {
            uiState = uiState.copy(titleError = "Title cannot be empty")
            isValid = false
        }
        val amountDouble = uiState.amount.toDoubleOrNull()
        if (amountDouble == null || amountDouble <= 0) {
            uiState = uiState.copy(amountError = "Enter a valid positive amount")
            isValid = false
        }
        return isValid
    }

    fun saveTransaction() {
        if (!validateInput()) {
            return
        }

        val amountValue = uiState.amount.toDoubleOrNull() ?: return // Should be validated by now

        val transactionToSave = Transaction(
            id = uiState.transactionId ?: UUID.randomUUID().toString(),
            title = uiState.title.trim(),
            amount = amountValue,
            type = uiState.selectedExpenseType,
            date = uiState.transactionDate,
            description = uiState.description.trim().takeIf { it.isNotEmpty() },
            isExpense = uiState.isExpense
        )

        try {
            if (uiState.isEditing) {
                transactionRepository.updateTransaction(transactionToSave)
            } else {
                transactionRepository.addTransaction(transactionToSave)
            }
            onTransactionSaved() // Navigate back or to a confirmation
        } catch (e: Exception) {
            uiState = uiState.copy(errorMessage = "Failed to save transaction: ${e.message}")
            println("Error saving transaction: ${e.localizedMessage}")
        }
    }

    fun navigateBack() {
        onNavigateBack()
    }
}