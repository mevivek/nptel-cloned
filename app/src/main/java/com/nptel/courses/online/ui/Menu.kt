package com.nptel.courses.online.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Menu(val label: String, val icon: ImageVector, onClick: () -> Unit) {
    object Search : Menu("Search", Icons.Default.Search, onClick = {

    })
}
