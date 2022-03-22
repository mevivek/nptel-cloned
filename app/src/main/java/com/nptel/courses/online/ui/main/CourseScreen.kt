package com.nptel.courses.online.ui.main

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.nptel.courses.online.R
import com.nptel.courses.online.entities.Playlist
import com.nptel.courses.online.ui.AppTheme
import com.nptel.courses.online.utility.ColorUtility

@Composable
fun CourseScreen(viewModel: MainViewModel, onClick: (playlist: Playlist) -> Unit) {
    val playlists by viewModel.playlists.observeAsState()
    val status by viewModel.status.observeAsState()
    AppTheme {
        if (playlists == null)
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        else Playlists(playlists!!, onClick)
    }
}


@Composable
fun Playlists(playlists: List<Playlist>, onClick: (playlist: Playlist) -> Unit) {
    Box(modifier = Modifier.background(color = MaterialTheme.colors.background)) {
        val dark = isSystemInDarkTheme()
        LazyColumn(contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)) {
            val colors = ColorUtility.getRandomColors(150, playlists.size, dark).map {
                Color(it)
            }

            items(playlists zip colors) { (playlist, color) ->
                Playlist(playlist, color, onClick)
            }
        }
    }
}

@Composable
fun Playlist(playlist: Playlist, color: Color, onClick: (playlist: Playlist) -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(bottom = 16.dp)
    ) {
        Card(
            modifier = Modifier.clickable {
                onClick(playlist)
            },
            elevation = 0.dp,
            shape = RoundedCornerShape(8.dp),
        ) {
            Box {
                Image(
                    painter = rememberImagePainter(data = playlist.image, builder = {

                    }),
                    contentDescription = "",
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .height(IntrinsicSize.Max)
                )
                Column(modifier = Modifier.background(color = color.copy(alpha = 0.15f))) {
                    Row {
                        Image(
                            painter = rememberImagePainter(data = playlist.image, builder = {
                                transformations(RoundedCornersTransformation(8f))
                            }),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(4.dp)
                                .size(128.dp, 96.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(IntrinsicSize.Max)
                        ) {
                            Text(
                                text = playlist.title,
                                modifier = Modifier.width(IntrinsicSize.Max),
                                color = color,
                                maxLines = 3,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                modifier = Modifier
                                    .padding(top = 24.dp)
                                    .background(
                                        color.copy(alpha = 0.15f),
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_baseline_video_library_24),
                                    contentDescription = "",
                                    tint = color,
                                    modifier = Modifier.size(12.5.dp)
                                )
                                Text(
                                    text = "${playlist.videosCount}",
                                    color = color,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 2.dp),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    Text(
                        text = playlist.description.toString(),
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .padding(8.dp),
                        color = color,
                        fontWeight = FontWeight.Thin,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CourseFragmentDarkPreview() {
    CourseFragmentPreview()
}

@Preview(showSystemUi = true)
@Composable
fun CourseFragmentPreview() {
    val playlists = mutableListOf<Playlist>()
    repeat(10) {
        playlists.add(Playlist.dummy())
    }
    Playlists(playlists, {})
}
