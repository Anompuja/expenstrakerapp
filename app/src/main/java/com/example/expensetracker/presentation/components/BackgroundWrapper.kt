package com.example.expensetracker.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.expensetracker.R // Make sure your_background.png is in res/drawable

@Composable
fun BackgroundWrapper(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.your_background), // YOUR BACKGROUND IMAGE
            contentDescription = null, // Decorative
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Or FillBounds, adjust as needed
        )
        content() // Content will be placed on top of the image
    }
}