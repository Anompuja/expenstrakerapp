package com.example.expensetracker.presentation.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.expensetracker.presentation.components.BackgroundWrapper
import com.example.expensetracker.presentation.controller.SignInController
import com.example.expensetracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    controller: SignInController
) {
    BackgroundWrapper {
        Scaffold(
            containerColor = Color.Transparent, // To see background
            topBar = {
                TopAppBar(
                    title = { Text("Sign In | Register")},
                    modifier = Modifier
                        .padding(horizontal = 105.dp),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,

                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 10.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo_moneting),
                    contentDescription = "App Logo",
                    modifier = Modifier.height(200.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))

                OutlinedTextField(
                    value = controller.email,
                    onValueChange = { controller.onEmailChange(it) },
                    label = { Text("Email Address") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = controller.errorMessage != null
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = controller.password,
                    onValueChange = { controller.onPasswordChange(it) },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = controller.errorMessage != null
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
