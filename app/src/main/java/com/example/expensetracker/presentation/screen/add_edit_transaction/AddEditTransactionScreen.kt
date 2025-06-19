package com.example.expensetracker.presentation.screen.add_edit_transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.data.model.ExpenseType
import com.example.expensetracker.data.model.Transaction
import com.example.expensetracker.data.repository.impl.InMemoryTransactionRepositoryImpl
import com.example.expensetracker.presentation.components.BackgroundWrapper
import com.example.expensetracker.presentation.controller.AddEditTransactionController
import com.example.expensetracker.presentation.theme.ExpenseTrackerTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen(
    controller: AddEditTransactionController
) {
    val uiState = controller.uiState
    val context = LocalContext.current
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showExpenseTypeDropdown by remember { mutableStateOf(false) }

    val screenTitle = if (uiState.isEditing) "Edit Transaction" else "Add New Transaction"

    // Date Formatter
    val dateFormatter = remember { SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault()) }

    if (showDatePickerDialog) {
        val calendar = Calendar.getInstance()
        calendar.time = uiState.transactionDate

        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePickerDialog = false
                    // DatePicker returns time in UTC millis, adjust if needed or ensure consistency
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = calendar.timeInMillis,
                    // yearRange = IntRange(2000, Calendar.getInstance().get(Calendar.YEAR)) // Optional: constrain year
                ),
                // onDateSelected is part of the state now, no direct callback here
                // The selected date is obtained from datePickerState.selectedDateMillis
                // We'll update the controller when "OK" is clicked.
                // For this example, let's assume the state is updated directly for simplicity
                // In a real scenario, you'd get the selectedDateMillis from the state
                // when the dialog is confirmed.
                // For now, we'll update the controller when the user clicks OK.
                // This requires a slight refactor of how the DatePickerState is handled
                // or passing the state up.
                // Let's make it simpler: update on confirm.
            )
        }
        // To update the date when OK is clicked, we need to access the DatePickerState
        // This is typically done by hoisting the state or passing a callback.
        // For this example, let's assume a simplified DatePicker where the state is managed internally
        // and we get the result upon confirmation.
        // A more robust solution would involve rememberDatePickerState hoisted or a callback.
        // The Material 3 DatePickerDialog's DatePicker's state is self-contained.
        // We need to get the selectedDateMillis from the state when "OK" is pressed.
        // This is a bit tricky without hoisting the DatePickerState.
        // Let's use a temporary state for the DatePicker and update on confirm.
        val tempDatePickerState = rememberDatePickerState(initialSelectedDateMillis = uiState.transactionDate.time)
        if (showDatePickerDialog) { // Re-check because it might be dismissed above
            DatePickerDialog(
                onDismissRequest = { showDatePickerDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDatePickerDialog = false
                        tempDatePickerState.selectedDateMillis?.let {
                            controller.onDateChange(it)
                        }
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePickerDialog = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = tempDatePickerState)
            }
        }
    }


    BackgroundWrapper {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text(screenTitle) },
                    navigationIcon = {
                        IconButton(onClick = { controller.navigateBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { controller.saveTransaction() }) {
                            Icon(Icons.Filled.Check, contentDescription = "Save Transaction")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValues ->
            if (uiState.isLoading) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()) // Make content scrollable
                ) {
                    // Title Field
                    OutlinedTextField(
                        value = uiState.title,
                        onValueChange = { controller.onTitleChange(it) },
                        label = { Text("Title*") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        isError = uiState.titleError != null,
                        supportingText = { uiState.titleError?.let { Text(it) } }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Amount Field
                    OutlinedTextField(
                        value = uiState.amount,
                        onValueChange = { controller.onAmountChange(it) },
                        label = { Text("Amount*") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        leadingIcon = { Text("$", style = MaterialTheme.typography.bodyLarge) },
                        isError = uiState.amountError != null,
                        supportingText = { uiState.amountError?.let { Text(it) } }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Expense Type Dropdown (using ExposedDropdownMenuBox)
                    // ... (previous code in AddEditTransactionScreen.kt)
                    // Expense Type Dropdown (using ExposedDropdownMenuBox)
                    ExposedDropdownMenuBox(
                        expanded = showExpenseTypeDropdown,
                        onExpandedChange = { showExpenseTypeDropdown = !showExpenseTypeDropdown },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = uiState.selectedExpenseType.name.replace("_", " ").replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                            onValueChange = {}, // Not directly editable
                            readOnly = true,
                            label = { Text("Category*") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showExpenseTypeDropdown) },
                            colors = OutlinedTextFieldDefaults.colors(), // Default colors
                            modifier = Modifier
                                .menuAnchor() // This is important for ExposedDropdownMenuBox
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = showExpenseTypeDropdown,
                            onDismissRequest = { showExpenseTypeDropdown = false },
                            modifier = Modifier.fillMaxWidth() // Make dropdown match width
                        ) {
                            uiState.availableExpenseTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.name.replace("_", " ").replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }) },
                                    onClick = {
                                        controller.onExpenseTypeChange(type)
                                        showExpenseTypeDropdown = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Date Picker Field
                    OutlinedTextField(
                        value = dateFormatter.format(uiState.transactionDate),
                        onValueChange = {}, // Not directly editable
                        readOnly = true,
                        label = { Text("Date*") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePickerDialog = true },
                        trailingIcon = {
                            Icon(
                                Icons.Filled.CalendarToday,
                                contentDescription = "Select Date",
                                modifier = Modifier.clickable { showDatePickerDialog = true }
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Description Field
                    OutlinedTextField(
                        value = uiState.description,
                        onValueChange = { controller.onDescriptionChange(it) },
                        label = { Text("Description (Optional)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp), // Multi-line
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Is Expense / Is Income Toggle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Type:", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.width(16.dp))
                        FilterChip(
                            selected = uiState.isExpense,
                            onClick = { controller.onIsExpenseChange(true) },
                            label = { Text("Expense") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        FilterChip(
                            selected = !uiState.isExpense,
                            onClick = { controller.onIsExpenseChange(false) },
                            label = { Text("Income") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    uiState.errorMessage?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f)) // Push button to bottom if content is short

                    Button(
                        onClick = { controller.saveTransaction() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .height(48.dp),
                        enabled = !uiState.isLoading // Disable button while loading/saving
                    ) {
                        Text(if (uiState.isEditing) "Update Transaction" else "Save Transaction")
                    }
                }
            }
        }
    }
}


// Preview for AddEditTransactionScreen
@Preview(showBackground = true, name = "Add New Transaction")
@Composable
fun AddTransactionScreenPreview() {
    ExpenseTrackerTheme {
        val previewController = AddEditTransactionController(
            transactionId = null, // For "Add" mode
            onTransactionSaved = {},
            onNavigateBack = {},
            transactionRepository = InMemoryTransactionRepositoryImpl
        )
        AddEditTransactionScreen(controller = previewController)
    }
}

@Preview(showBackground = true, name = "Edit Transaction")
@Composable
fun EditTransactionScreenPreview() {
    ExpenseTrackerTheme {
        // To preview edit mode, we'd ideally have a sample transaction ID
        // from InMemoryTransactionRepositoryImpl. For simplicity, we can mock it
        // or ensure the repository has a known ID.
        // Let's assume InMemoryTransactionRepositoryImpl has some data.
        val sampleRepo = InMemoryTransactionRepositoryImpl
        if (sampleRepo.getAllTransactions().isEmpty()) {
            // Add a temporary transaction if repo is empty for preview
            sampleRepo.addTransaction(
                Transaction(
                    id = "preview-id-123",
                    title = "Sample Edit Item",
                    amount = 75.50,
                    type = ExpenseType.FOOD,
                    date = Date(),
                    description = "This is a sample for editing.",
                    isExpense = true
                )
            )
        }
        val transactionToEditId = sampleRepo.getAllTransactions().firstOrNull()?.id

        val previewController = AddEditTransactionController(
            transactionId = transactionToEditId, // For "Edit" mode
            onTransactionSaved = {},
            onNavigateBack = {},
            transactionRepository = sampleRepo
        )
        AddEditTransactionScreen(controller = previewController)
    }
}