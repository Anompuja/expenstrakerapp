package com.example.expensetracker.presentation.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.presentation.components.BackgroundWrapper
import com.example.expensetracker.presentation.theme.ExpenseTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
    // Add callbacks for specific setting actions, e.g.:
    // onNavigateToProfile: () -> Unit,
    // onToggleNotifications: (Boolean) -> Unit,
    // onChangeTheme: () -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) } // Example state

    BackgroundWrapper {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Settings") },
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
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                // .verticalScroll(rememberScrollState()) // Add if content might overflow
            ) {
                // Profile Section (Example - can navigate to a dedicated profile screen)
                SettingsItem(
                    icon = Icons.Filled.Person,
                    title = "Profile",
                    subtitle = "Manage your account details",
                    onClick = { /* TODO: onNavigateToProfile() */ }
                )
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

                // Notifications Toggle
                SettingsToggleItem(
                    icon = Icons.Filled.Notifications,
                    title = "Enable Notifications",
                    checked = notificationsEnabled,
                    onCheckedChange = {
                        notificationsEnabled = it
                        // TODO: onToggleNotifications(it) - Persist this setting
                    }
                )
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

                // Theme Setting (Example)
                SettingsItem(
                    icon = Icons.Filled.ColorLens,
                    title = "Appearance",
                    subtitle = "Change app theme (Light/Dark)",
                    onClick = { /* TODO: onChangeTheme() - Implement theme switching logic */ }
                )
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

                // Add more settings items as needed:
                // - Currency
                // - Data Backup/Restore
                // - About
                // - Logout (if applicable)

                Spacer(modifier = Modifier.weight(1f)) // Pushes logout to bottom if added

                // Example: Logout Button
                // Button(
                //     onClick = { /* TODO: Handle logout */ },
                //     modifier = Modifier
                //         .fillMaxWidth()
                //         .padding(16.dp),
                //     colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                // ) {
                //     Text("Logout")
                // }
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            subtitle?.let {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = "Go to $title",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SettingsToggleItem(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) } // Click row to toggle
            .padding(horizontal = 16.dp, vertical = 12.dp), // Slightly less vertical padding for toggles
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 740)
@Composable
fun SettingsScreenPreview() {
    ExpenseTrackerTheme {
        SettingsScreen(onNavigateBack = {})
    }
}