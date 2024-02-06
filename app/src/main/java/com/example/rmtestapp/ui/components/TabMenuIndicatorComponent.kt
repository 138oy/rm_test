package com.example.rmtestapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomIndicator(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .height(2.dp)
            .fillMaxWidth()
            .border(BorderStroke(2.dp, color), RoundedCornerShape(5.dp))
    )
}
