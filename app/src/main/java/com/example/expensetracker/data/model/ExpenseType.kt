package com.example.expensetracker.data.model

enum class ExpenseType {
    FOOD,
    TRANSPORTATION,
    HOUSING, // Rent, mortgage, utilities related to housing
    UTILITIES, // Phone, internet, electricity (if not under housing)
    APPAREL, // Clothing, shoes
    HEALTHCARE, // Doctor visits, medication
    EDUCATION,
    ENTERTAINMENT,
    GIFTS_DONATIONS,
    PERSONAL_CARE, // Toiletries, haircuts
    SAVINGS_INVESTMENTS,
    DEBT_PAYMENT,
    OTHER
}