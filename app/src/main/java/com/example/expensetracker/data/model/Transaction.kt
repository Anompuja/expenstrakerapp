package com.example.expensetracker.data.model

import java.util.Date
import java.util.UUID // For generating unique IDs

data class Transaction(
    val id: String = UUID.randomUUID().toString(), // Unique identifier
    val title: String,
    val amount: Double, // Can be positive (income) or negative (expense)
    val type: ExpenseType,
    val date: Date,
    val description: String? = null, // Optional notes
    val isExpense: Boolean = true // True for expense, false for income
)