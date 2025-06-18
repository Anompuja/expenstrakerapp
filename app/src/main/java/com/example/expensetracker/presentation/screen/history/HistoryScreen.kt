package com.example.expensetracker.presentation.screen.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Make sure to import this
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category // Example icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.presentation.components.BackgroundWrapper
import com.example.expensetracker.presentation.theme.ExpenseTrackerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

// Dummy data for now - replace with actual data from a ViewModel/Repository
val sampleTransactions = listOf(
    Transaction(UUID.randomUUID().toString(), "Lunch at Cafe", 15.75, ExpenseType.FOOD, Date(System.currentTimeMillis() - 80000000L), "With colleagues"),
    Transaction(UUID.randomUUID().toString(), "Monthly Bus Pass", 60.00, ExpenseType.TRANSPORTATION, Date(System.currentTimeMillis() - 160000000L)),
    Transaction(UUID.randomUUID().toString(), "Movie Tickets", 25.00, ExpenseType.ENTERTAINMENT, Date(System.currentTimeMillis() - 240000000L), "Weekend movie"),
    Transaction(UUID.randomUUID().toString(), "Groceries", 75.20, ExpenseType.FOOD, Date(System.currentTimeMillis() - 320000000L)),
    Transaction(UUID.randomUUID().toString(), "New T-shirt", 30.00, ExpenseType.APPAREL, Date(System.currentTimeMillis() - 400000000L))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit
    // In a real app, you'd pass a list of transactions, likely from a ViewModel
    // transactions: List<Transaction> = emptyList()
) {
    // For now, using sampleTransactions. Replace with ViewModel state later.
    val transactions by remember { mutableStateOf(sampleTransactions) }

    BackgroundWrapper {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Transaction History") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        ) { paddingValues ->
            if (transactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No transactions recorded yet.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(transactions, key = { it.id }) { transaction ->
                        TransactionHistoryItem(transaction = transaction)
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionHistoryItem(transaction: Transaction) {
    val dateFormatter = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)) // Semi-transparent card
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Category, // Replace with type-specific icons later
                contentDescription = transaction.type.name,
                modifier = Modifier.size(40.dp).padding(end = 12.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = transaction.type.name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                transaction.description?.let {
                    if (it.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$%.2f".format(transaction.amount), // Basic currency formatting
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.amount >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error // Example for income vs expense
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = dateFormatter.format(transaction.date),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
fun HistoryScreenPreview() {
    ExpenseTrackerTheme {
        HistoryScreen(onNavigateBack = {})
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun TransactionHistoryItemPreview() {
    ExpenseTrackerTheme {
        TransactionHistoryItem(
            transaction = Transaction(
                "1",
                "Lunch Special",
                12.99,
                ExpenseType.FOOD,
                Date(),
                "Quick bite at the new cafe downtown with a friend."
            )
        )
    }
}