package com.nptel.courses.online.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@Composable
fun SubmitButton(
    modifier: Modifier,
    text: String,
    icon: ImageVector? = null,
    style: SubmitButtonStyle = SubmitButtonStyle.Filled,
    onClick: () -> Unit = {}
) {
    var showProgress by remember {
        mutableStateOf(false)
    }
    when (style) {
        SubmitButtonStyle.Text -> TextButton(onClick = {
            onClick()
            showProgress = true
        }) {
            Content(text = text, icon, showProgress)
        }
        SubmitButtonStyle.Border -> OutlinedButton(onClick = {
            onClick()
            showProgress = true
        }) {
            Content(text = text, icon, showProgress)
        }
        else -> Button(onClick = {
            onClick()
            showProgress = true
        }) {
            Content(text = text, icon, showProgress)
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun Content(text: String, icon: ImageVector? = null, progress: Boolean = false) {
    val showProgress by remember {
        mutableStateOf(progress)
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = if (showProgress) "In Progress" else text)
        if (icon != null)
            Icon(imageVector = icon, contentDescription = "")
        AnimatedVisibility(visible = showProgress) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp),
                strokeWidth = 2.dp
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview()
@Composable
fun SubmitButtonPreview() {
    SubmitButton(modifier = Modifier, text = "Submit Button")
}

@ExperimentalAnimationApi
@Preview()
@Composable
fun SubmitButtonBorderPreview() {
    SubmitButton(modifier = Modifier, text = "Submit Button", style = SubmitButtonStyle.Border)
}

@ExperimentalAnimationApi
@Preview()
@Composable
fun SubmitButtonTextPreview() {
    SubmitButton(modifier = Modifier, text = "Submit Button", style = SubmitButtonStyle.Text)
}

enum class SubmitButtonStyle {
    Text, Border, Filled
}