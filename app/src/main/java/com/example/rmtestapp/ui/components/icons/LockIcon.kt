package com.example.rmtestapp.ui.components.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.rmtestapp.R

@Composable
fun LockIcon(
    color: Color,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_lock),
        contentDescription = "Lock",
        tint = color,
        modifier = modifier
    )
}