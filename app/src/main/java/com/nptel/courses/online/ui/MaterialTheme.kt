package com.nptel.courses.online.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.nptel.courses.online.R

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val lightColors = lightColors(
        primary = colorResource(id = R.color.colorPrimary)
    )

    val darkColors = darkColors(
        primary = colorResource(id = R.color.colorPrimary),
        primaryVariant = Color.Black
    )

    MaterialTheme(
        colors = if (isSystemInDarkTheme()) darkColors else lightColors,
        content = content
    )
}