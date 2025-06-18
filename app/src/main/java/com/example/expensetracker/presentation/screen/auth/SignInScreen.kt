package com.example.expensetracker.presentation.screen.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.presentation.components.BackgroundWrapper // We'll create this later
import com.example.expensetracker.presentation.controller.SignInController
import com.example.expensetracker.presentation.theme.ExpenseTrackerTheme // We'll create this later

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    controller: SignInController
) {
    // The BackgroundWrapper and Theme will be defined later.
    // For now, you might see errors related to them if you try to build.
    BackgroundWrapper {
        Scaffold(
            containerColor = Color.Transparent, // To see background
            topBar = {
                TopAppBar(
                    title = { Text("Sign In / Register") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Expense Tracker",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = controller.email,
                    onValueChange = { controller.onEmailChange(it) },
                    label = { Text("Email Address") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = controller.errorMessage != null // Show error state if message exists
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = controller.password,
                    onValueChange = { controller.onPasswordChange(it) },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = controller.errorMessage != null // Show error state if message exists
                )
                Spacer(modifier = Modifier.height(8.dp))

                controller.errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { controller.attemptSignIn() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Sign In")
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { /* TODO: Navigate to registration or handle registration flow */ }) {
                    Text("Don't have an account? Sign Up", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

// Preview for SignInScreen (won't fully work until Theme and BackgroundWrapper are set up)
@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
fun SignInScreenPreview() {
    // Dummy controller for preview
    val previewController = SignInController(onSignInSuccess = {})
    ExpenseTrackerTheme { // Assuming ExpenseTrackerTheme is defined
        SignInScreen(controller = previewController)
    }
}