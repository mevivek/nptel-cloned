package com.nptel.courses.online.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Course : Screen("course", "Course", Icons.Default.Folder)
    object Collections : Screen("collections", "Collections", Icons.Default.Folder)
    object Favorites : Screen("favorite", "Favorite", Icons.Default.Star)
}

val mainScreens = listOf(Screen.Course, Screen.Collections, Screen.Favorites)