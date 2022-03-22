package com.nptel.courses.online.ui.course

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.nptel.courses.online.R
import com.nptel.courses.online.Status
import com.nptel.courses.online.entities.Course
import com.nptel.courses.online.retrofit.RetrofitServices
import com.nptel.courses.online.ui.*
import com.nptel.courses.online.ui.main.MainActivity
import com.nptel.courses.online.ui.playlist.PlaylistActivity
import com.nptel.courses.online.ui.playlist.PlaylistFragment
import com.nptel.courses.online.utility.ColorUtility
import com.nptel.courses.online.utility.Constants
import com.nptel.courses.online.utility.setSelectedCourse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CourseSelectionActivity : ComponentActivity() {

    @Inject
    lateinit var retrofitServices: RetrofitServices

    private val viewModel: CourseSelectionViewModel by viewModels()

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CourseSelectionScreen()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menu.removeItem(R.id.change_course)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent: Intent
        when (item.itemId) {
            R.id.search -> {
                SearchActivity.start(this, null)
                return true
            }
            R.id.share_app -> {
                intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                val shareText = """
                Get all NPTEL online course videos at one place.
                Install the app now:
                ${resources.getString(R.string.play_store_dynamic_link)}
                """.trimIndent()
                intent.putExtra(Intent.EXTRA_TEXT, shareText)
                startActivity(Intent.createChooser(intent, "Choose"))
                return true
            }
            R.id.favourite -> {
                intent = Intent(applicationContext, PlaylistActivity::class.java)
                intent.putExtra(PlaylistActivity.MODE, PlaylistFragment.Mode.COLLECTION)
                //            intent.putExtra(PlaylistActivity.ID, SharedPreferencesUtility.FAVOURITE_VIDEOS);
                intent.putExtra(PlaylistActivity.COURSE_NAME, "Favourites")
                startActivity(intent)
                return true
            }
            R.id.change_course -> {
                startActivity(Intent(this, CourseSelectionActivity::class.java))
                finish()
                return true
            }
            R.id.app_feedback -> {
                intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("url", Constants.feedbackForm)
                startActivity(intent)
                return true
            }
            R.id.appreciate -> {
                intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("url", Constants.getAppreciatePaymentPage(this))
                startActivity(intent)
                return true
            }
            R.id.about_us -> {
                intent = Intent(this, AboutUsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @Composable
    fun CourseSelectionScreen() {
        val dark = isSystemInDarkTheme()
        val courses by viewModel.courses.observeAsState()
        val networkRequestStatus by viewModel.networkRequestStatus.observeAsState()
        AppTheme {
            Scaffold(topBar = {
                TopAppBar(
                    title = { Text(text = "Select Course") },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_search_white_24dp),
                                contentDescription = "Search"
                            )
                        }
                    },
                    backgroundColor = MaterialTheme.colors.surface,
                    contentColor = MaterialTheme.colors.onSurface,
                    elevation = 0.dp
                )
            }) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.background)
                        .fillMaxSize()
                ) {
                    when (networkRequestStatus?.status) {
                        Status.Loading -> CircularProgressIndicator()
                        Status.Error -> ServerError(
                            networkRequestStatus?.error!!
                        ) { viewModel.getCourses() }
                        else -> LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp)
                        ) {
                            val colors =
                                ColorUtility.getRandomColors(150, courses!!.size, dark).map {
                                    Color(it)
                                }
                            items(courses!! zip colors) { (course, color) ->
                                Course(course, color)
                            }
                        }
                    }
                }
            }
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @Preview(showSystemUi = true)
    @Composable
    fun CourseSelectionReview() {
        val courses = mutableListOf<Course>()
        repeat(10) {
            courses.add(Course.dummy())
        }
        CourseSelectionScreen()
    }

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @Composable
    fun Course(course: Course, color: Color = Color(ColorUtility.primaryRandomColor)) {
        Box(modifier = Modifier.padding(horizontal = 8.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                elevation = 0.dp,
                onClick = {
                    setSelectedCourse(this@CourseSelectionActivity, course = course)
                    startActivity(
                        Intent(
                            this@CourseSelectionActivity,
                            MainActivity::class.java
                        )
                    )
                    finish()
                }) {
                Row(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .background(color = color.copy(alpha = 0.15f))
                        .padding(16.dp)
                ) {
                    if (course.image.isNullOrBlank())
                        Text(
                            text = course.title[0].toString().uppercase(),
                            color = color,
                            fontSize = 22.sp,
                            modifier = Modifier
                                .background(
                                    color = color.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(100)
                                )
                                .size(48.dp),
                            textAlign = TextAlign.Center
                        )
                    else Image(
                        painter = rememberImagePainter(data = course.image, builder = {
                            transformations(CircleCropTransformation())
                            crossfade(true)
                        }),
                        contentDescription = "",
                        modifier = Modifier
                            .background(
                                color = color.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(100)
                            )
                            .size(72.dp)
                    )
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = course.title,
                            color = color,
                            modifier = Modifier.width(IntrinsicSize.Max)
                        )
                        Row(modifier = Modifier.padding(top = 16.dp)) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = color.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_outline_folder_24),
                                    contentDescription = "",
                                    tint = color,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "${course.playlistsCount}",
                                    color = color,
                                    modifier = Modifier.padding(start = 2.dp),
                                    fontSize = 12.sp
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .offset(x = 16.dp)
                                    .background(
                                        color = color.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_baseline_video_library_24),
                                    contentDescription = "",
                                    tint = color,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "${course.videosCount}",
                                    color = color,
                                    modifier = Modifier.padding(start = 2.dp),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                }
            }
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @Preview
    @Composable
    fun CoursePreview() {
        Course(
            course = Course.dummy(),
            Color(ColorUtility.getRandomColor(150, 150, 150, isSystemInDarkTheme()))
        )
    }
}