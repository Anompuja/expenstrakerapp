package com.example.expensetracker.presentation.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.data.model.Transaction
import com.example.expensetracker.data.repository.impl.InMemoryTransactionRepositoryImpl
import com.example.expensetracker.presentation.components.BackgroundWrapper
import com.example.expensetracker.presentation.controller.HomeController
import com.example.expensetracker.presentation.theme.ExpenseTrackerTheme
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    controller: HomeController
) {

    val uiState = controller.uiState

    LaunchedEffect(key1 = Unit) { // <--- REPLACEMENT BLOCK
        if (!uiState.isLoading &&
            uiState.recentTransactions.isEmpty() &&
            (uiState.totalBalance == 0.0 && uiState.totalIncome == 0.0 && uiState.totalExpenses == 0.0) &&
            uiState.errorMessage == null) {
            controller.loadHomeScreenData()
        }
    }
    BackgroundWrapper {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Dashboard") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    actions = {
                        IconButton(onClick = { controller.navigateToHistoryScreen() }) {
                            Icon(Icons.Filled.History, contentDescription = "Transaction History", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        IconButton(onClick = { controller.navigateToSettingsScreen() }) {
                            Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { controller.navigateToAddTransactionScreen() },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Transaction")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Adjusted vertical padding
                    .fillMaxSize()
            ) {
                if (uiState.isLoading && uiState.recentTransactions.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.errorMessage != null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    Text(
                        "Welcome, ${uiState.userName}!",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 16.dp, top = 8.dp) // Added top padding
                    )

                    SummarySection(
                        balance = uiState.totalBalance,
                        income = uiState.totalIncome,
                        expenses = uiState.totalExpenses
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        "Recent Transactions",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (uiState.recentTransactions.isEmpty()) {
                        Text(
                            "No transactions yet. Tap the '+' button to add one!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 16.dp)
                        )
                    } else {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(uiState.recentTransactions) { transaction ->
                                TransactionItem(transaction = transaction, onItemClick = {
                                    // TODO: Navigate to transaction detail or edit
                                    // controller.navigateToEditTransaction(transaction.id)
                                    println("Clicked on transaction: ${transaction.title}")
                                })
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummarySection(balance: Double, income: Double, expenses: Double) {
    Column {
        Row(
            // Balance card takes full width
            modifier = Modifier.fillMaxWidth(),
        ) {
            SummaryCard("Balance", balance, MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth())
        }
        Spacer(modifier = Modifier.height(12.dp)) // Reduced spacer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp) // Spacing between cards
        ) {
            SummaryCard("Income", income, Color(0xFF2E7D32), modifier = Modifier.weight(1f)) // Dark Green
            SummaryCard("Expenses", expenses, MaterialTheme.colorScheme.error, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun SummaryCard(title: String, amount: Double, amountColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier, // Removed fillMaxWidth here, applied by caller if needed
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // Reduced elevation
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)) // More subtle
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp), // Adjusted padding
            horizontalAlignment = Alignment.Start, // Align text to start
        ) {
            Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) // Smaller label
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                String.format(Locale.getDefault(), "$%.2f", amount), // Added currency symbol
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = amountColor,
                fontSize = 17.sp // Slightly adjusted
            )
        }
    }
}

// ... (previous code in HomeScreen.kt)

@Composable
fun TransactionItem(transaction: Transaction, onItemClick: (String) -> Unit) {
    val amountColor = if (transaction.isExpense) MaterialTheme.colorScheme.error else Color(0xFF2E7D32) // Dark Green for income
    val sign = if (transaction.isExpense) "-" else "+"
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    ListItem( // Using Material 3 ListItem
        modifier = Modifier.clickable { onItemClick(transaction.id) },
        headlineContent = { Text(transaction.title, fontWeight = FontWeight.SemiBold) },
        supportingContent = { // THIS IS WHERE WE LEFT OFF
            Text("${transaction.type} - ${dateFormat.format(transaction.date)}")
        },
        trailingContent = {
            Text(
                text = String.format(Locale.getDefault(), "%s$%.2f", sign, transaction.amount),
                color = amountColor,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent // So background shows through if desired
        )
    )
}

// Preview for HomeScreen
@Preview(showBackground = true, widthDp = 380, heightDp = 700)
@Composable
fun HomeScreenPreview() {
    ExpenseTrackerTheme {
        // For the preview, we create a dummy controller.
        // The InMemoryTransactionRepositoryImpl will provide sample data.
        val previewController = HomeController(
            onNavigateToHistory = {},
            onNavigateToAddTransaction = {},
            onNavigateToSettings = {},
            transactionRepository = InMemoryTransactionRepositoryImpl // Use the in-memory repo
        )
        // Manually trigger data load for preview if needed, or ensure constructor does it.
        // previewController.loadHomeScreenData() // Already called in init of HomeController

        HomeScreen(controller = previewController)
    }
}