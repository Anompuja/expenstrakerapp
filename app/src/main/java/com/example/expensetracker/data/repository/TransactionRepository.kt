package com.example.expensetracker.data.repository

import com.example.expensetracker.data.model.Transaction
import kotlinx.coroutines.flow.Flow // For reactive updates if needed later

interface TransactionRepository {
    // In a real app, these would likely be suspend functions
    // and Flow would be used for observing changes.
    // For simplicity with MVC and no complex coroutines in controllers yet,
    // we'll start with direct return types.

    fun getAllTransactions(): List<Transaction>
    fun getTransactionById(id: String): Transaction?
    fun addTransaction(transaction: Transaction)
    fun updateTransaction(transaction: Transaction)
    fun deleteTransaction(transactionId: String)
    fun getExpenses(): List<Transaction>
    fun getIncome(): List<Transaction>

    // If we want reactive updates for the UI (good for Compose)
    // fun getAllTransactionsFlow(): Flow<List<Transaction>>
}