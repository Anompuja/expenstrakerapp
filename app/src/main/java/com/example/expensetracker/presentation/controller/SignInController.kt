package com.example.expensetracker.presentation.controller

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SignInController(
    private val onSignInSuccess: () -> Unit
) {
    // State for the email field
    var email by mutableStateOf("")
        private set // View can read, only Controller can write

    // State for the password field
    var password by mutableStateOf("")
        private set

    // State for error messages
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
        if (errorMessage != null) { // Clear error when user types
            errorMessage = null
        }
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        if (errorMessage != null) { // Clear error when user types
            errorMessage = null
        }
    }

    fun attemptSignIn() {
        // Basic validation (can be expanded)
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Email and password cannot be empty."
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = "Please enter a valid email address."
            return
        }

        // TODO: Implement actual sign-in logic here (e.g., check credentials against a source)
        // For now, we'll just simulate a successful sign-in.
        println("Simulating sign-in for email: $email") // Log for debugging
        errorMessage = null // Clear any previous errors
        onSignInSuccess()
    }
}