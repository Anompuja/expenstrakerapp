package com.example.expensetracker.data.repository.impl

import com.example.expensetracker.data.model.Transaction
import com.example.expensetracker.data.repository.TransactionRepository
// import kotlinx.coroutines.flow.Flow // For later
// import kotlinx.coroutines.flow.MutableStateFlow // For later
// import kotlinx.coroutines.flow.asStateFlow // For later
// import kotlinx.coroutines.flow.map // For later
import java.util.Collections
import java.util.Date
import com.example.expensetracker.data.model.ExpenseType // Make sure this is imported

// Singleton to hold our in-memory data and act as a simple data source.
// In a real app, this would be replaced by a database (e.g., Room).
object InMemoryTransactionRepositoryImpl : TransactionRepository {

    private val transactions = mutableListOf<Transaction>(
        // Sample data for initial testing
        Transaction(title = "Groceries", amount = 55.75, type = ExpenseType.FOOD, date = Date(System.currentTimeMillis() - 86400000L * 2), isExpense = true, description = "Weekly groceries"),
        Transaction(title = "Salary", amount = 2500.00, type = ExpenseType.OTHER, date = Date(System.currentTimeMillis() - 86400000L * 5), isExpense = false),
        Transaction(title = "Gas Bill", amount = 70.20, type = ExpenseType.UTILITIES, date = Date(System.currentTimeMillis() - 86400000L * 1), isExpense = true),
        Transaction(title = "Movie Tickets", amount = 25.00, type = ExpenseType.ENTERTAINMENT, date = Date(System.currentTimeMillis() - 86400000L * 3), isExpense = true, description = "Cinema night")
    )

    // For reactive updates later with Flow
    // private val _transactionsFlow = MutableStateFlow(transactions.toList())
    // fun getTransactionsListFlow() = _transactionsFlow.asStateFlow()

    override fun getAllTransactions(): List<Transaction> {
        // Return an immutable copy sorted by date descending
        return Collections.unmodifiableList(transactions.sortedByDescending { it.date })
    }

    override fun getTransactionById(id: String): Transaction? {
        return transactions.find { it.id == id }
    }

    override fun addTransaction(transaction: Transaction) {
        transactions.add(transaction)
        // _transactionsFlow.value = transactions.toList().sortedByDescending { it.date } // For Flow
    }

    override fun updateTransaction(transaction: Transaction) {
        val index = transactions.indexOfFirst { it.id == transaction.id }
        if (index != -1) {
            transactions[index] = transaction
            // _transactionsFlow.value = transactions.toList().sortedByDescending { it.date } // For Flow
        }
    }

    override fun deleteTransaction(transactionId: String) {
        transactions.removeAll { it.id == transactionId }
        // _transactionsFlow.value = transactions.toList().sortedByDescending { it.date } // For Flow
    }

    override fun getExpenses(): List<Transaction> {
        return Collections.unmodifiableList(transactions.filter { it.isExpense }.sortedByDescending { it.date })
    }

    override fun getIncome(): List<Transaction> {
        return Collections.unmodifiableList(transactions.filter { !it.isExpense }.sortedByDescending { it.date })
    }

    // Example for Flow if we were to use it now
    // override fun getAllTransactionsFlow(): Flow<List<Transaction>> {
    //     return getTransactionsListFlow()
    // }
}