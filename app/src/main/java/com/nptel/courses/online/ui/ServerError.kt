package com.nptel.courses.online.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nptel.courses.online.components.SubmitButton

@ExperimentalAnimationApi
@Composable
fun ServerError(text: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {


        Spacer(modifier = Modifier.height(IntrinsicSize.Max))
        Text(text = text, color = MaterialTheme.colors.error)
        SubmitButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(50)),
            text = "Try Again"
        )
    }
}

@ExperimentalAnimationApi
@Preview()
@Composable
fun ServerErrorPreview() {
    ServerError("Something Went Wrong") {}
}