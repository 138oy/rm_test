package com.example.rmtestapp.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ImageCard(
    snapshot: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Image(url = snapshot, modifier = Modifier.fillMaxHeight())
    }
}
