package com.example.expensetracker.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.expensetracker.R // Import your R class

@Composable
fun BackgroundWrapper(
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.app_background), // Replace with your actual drawable
            contentDescription = "Application background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Or ContentScale.FillBounds, etc.
        )
        // Content is drawn on top of the image
        content()
    }
}