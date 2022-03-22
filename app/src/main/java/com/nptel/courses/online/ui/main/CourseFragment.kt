package com.nptel.courses.online.ui.main

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.nptel.courses.online.R
import com.nptel.courses.online.entities.Playlist
import com.nptel.courses.online.retrofit.RetrofitFactory
import com.nptel.courses.online.ui.AppTheme
import com.nptel.courses.online.ui.playlist.PlaylistActivity
import com.nptel.courses.online.ui.playlist.PlaylistFragment
import com.nptel.courses.online.utility.ColorUtility
import com.nptel.courses.online.utility.getSelectedCourse

class CourseFragment : Fragment() {

    lateinit var composeView: ComposeView
    private lateinit var adapter: PlaylistsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (getSelectedCourse(requireContext()) != null) fetch()
        composeView = ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        return composeView
    }

    private fun fetch() {
        lifecycleScope.launchWhenResumed {
            val playlists = RetrofitFactory.getRetrofitService(requireContext()).getPlaylists(
                getSelectedCourse(requireContext())!!.id
            )
            composeView.setContent { CourseFragment(playlists = playlists) }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): CourseFragment {
            return CourseFragment()
        }
    }

    @Composable
    fun CourseFragment(playlists: List<Playlist>) {
        AppTheme {
            Box(modifier = Modifier.background(color = MaterialTheme.colors.background)) {
                val dark = isSystemInDarkTheme()
                LazyColumn(contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)) {
                    val colors = ColorUtility.getRandomColors(150, playlists.size, dark).map {
                        Color(it)
                    }

                    items(playlists zip colors) { (playlist, color) ->
                        Playlist(playlist, color)
                    }
                }
            }
        }
    }

    @Preview(group = "Main", uiMode = UI_MODE_NIGHT_YES)
    @Composable
    fun CourseFragmentDarkPreview() {
        val playlists = mutableListOf<Playlist>()
        repeat(10) {
            playlists.add(Playlist.dummy())
        }
        CourseFragment(playlists = playlists)
    }

    @Preview(group = "Main")
    @Composable
    fun CourseFragmentPreview() {
        val playlists = mutableListOf<Playlist>()
        repeat(10) {
            playlists.add(Playlist.dummy())
        }
        CourseFragment(playlists = playlists)
    }

    @Composable
    fun Playlist(playlist: Playlist, color: Color) {
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 16.dp)
        ) {
            Card(
                modifier = Modifier.clickable {
                    val intent = Intent(requireContext(), PlaylistActivity::class.java)
                    intent.putExtra(
                        PlaylistActivity.MODE,
                        PlaylistFragment.Mode.PLAYLIST.toString()
                    )
                    intent.putExtra(PlaylistActivity.ID, playlist.id)
                    intent.putExtra(
                        PlaylistActivity.COURSE_NAME,
                        getSelectedCourse(requireContext())!!.title
                    )
                    intent.putExtra(PlaylistActivity.PLAYLIST_NAME, playlist.title)
                    startActivity(intent)
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

    @Preview(group = "Playlist", uiMode = UI_MODE_NIGHT_YES)
    @Composable
    fun PlaylistPreview() {
        Playlist(
            playlist = Playlist.dummy(),
            color = Color(ColorUtility.getRandomColor(150, 150, 150, isSystemInDarkTheme()))
        )
    }

    @Preview(group = "Playlist")
    @Composable
    fun PlaylistDarkPreview() {
        PlaylistPreview()
    }
}