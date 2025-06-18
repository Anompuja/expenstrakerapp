package com.example.expensetracker.presentation.screen.add_edit_transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.presentation.components.BackgroundWrapper
import com.example.expensetracker.presentation.theme.ExpenseTrackerTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen(
    // transactionId: String? = null, // Pass this if you are editing an existing transaction
    onNavigateBack: () -> Unit
) {
    // State for the form fields
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedExpenseType by remember { mutableStateOf(ExpenseType.OTHER) }
    var transactionDate by remember { mutableStateOf(Date()) } // Default to today
    var description by remember { mutableStateOf("") }

    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showExpenseTypeDropdown by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val dateFormatter = remember { SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault()) }

    // TODO: If transactionId is not null, load the transaction data into the states

    BackgroundWrapper {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(if (false /* TODO: check if editing */) "Edit Transaction" else "Add Transaction") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            // TODO: Validate and save the transaction
                            // val newTransaction = Transaction(
                            //     id = UUID.randomUUID().toString(), // or existing id if editing
                            //     title = title,
                            //     amount = amount.toDoubleOrNull() ?: 0.0,
                            //     type = selectedExpenseType,
                            //     date = transactionDate,
                            //     description = description.takeIf { it.isNotBlank() }
                            // )
                            // Then call a ViewModel/Repository method to save
                            onNavigateBack() // Navigate back after saving
                        }) {
                            Icon(Icons.Filled.Check, contentDescription = "Save Transaction")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()), // Make content scrollable
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title (e.g., Lunch, Groceries)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it.filter { char -> char.isDigit() || char == '.' } },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    leadingIcon = { Text("$", style = MaterialTheme.typography.bodyLarge) }
                )

                // Expense Type Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedExpenseType.name.lowercase().replaceFirstChar { it.titlecase(Locale.getDefault()) },
                        onValueChange = { /* Read Only */ },
                        label = { Text("Expense Type") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showExpenseTypeDropdown = true },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = showExpenseTypeDropdown)
                        }
                    )
                    DropdownMenu(
                        expanded = showExpenseTypeDropdown,
                        onDismissRequest = { showExpenseTypeDropdown = false },
                        modifier = Modifier.fillMaxWidth(0.8f) // Adjust width as needed
                    ) {
                        ExpenseType.values().forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.name.lowercase().replaceFirstChar { it.titlecase(Locale.getDefault()) }) },
                                onClick = {
                                    selectedExpenseType = type
                                    showExpenseTypeDropdown = false
                                }
                            )
                        }
                    }
                }


                // Date Picker
                OutlinedTextField(
                    value = dateFormatter.format(transactionDate),
                    onValueChange = { /* Read Only */ },
                    label = { Text("Date") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePickerDialog = true },
                    readOnly = true,
                    trailingIcon = {
                        Icon(Icons.Filled.CalendarToday, "Select Date", Modifier.clickable { showDatePickerDialog = true })
                    }
                )

                if (showDatePickerDialog) {
                    val calendar = Calendar.getInstance().apply { time = transactionDate }
                    DatePickerDialog(
                        onDismissRequest = { showDatePickerDialog = false },
                        confirmButton = {
                            TextButton(onClick = { showDatePickerDialog = false }) { Text("OK") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePickerDialog = false }) { Text("Cancel") }
                        }
                    ) {
                        DatePicker(
                            state = rememberDatePickerState(
                                initialSelectedDateMillis = calendar.timeInMillis
                            ),
                            dateValidator = { timestamp -> timestamp <= System.currentTimeMillis() } // Allow only past or today
                        )
                        // The selected date needs to be retrieved from the DatePickerState
                        // For simplicity, this example doesn't update `transactionDate` from the dialog directly.
                        // You would typically update `transactionDate` in the confirmButton's onClick.
                        // Example:
                        // val datePickerState = rememberDatePickerState(...)
                        // confirmButton = { TextButton(onClick = {
                        //    datePickerState.selectedDateMillis?.let { transactionDate = Date(it) }
                        //    showDatePickerDialog = false
                        // }) { Text("OK") } }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp), // Multi-line
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    maxLines = 3
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
fun AddEditTransactionScreenPreview() {
    ExpenseTrackerTheme {
        AddEditTransactionScreen(onNavigateBack = {})
    }
}