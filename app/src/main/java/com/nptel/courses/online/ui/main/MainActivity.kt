package com.nptel.courses.online.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nptel.courses.online.R
import com.nptel.courses.online.entities.Course
import com.nptel.courses.online.entities.Playlist
import com.nptel.courses.online.ui.*
import com.nptel.courses.online.ui.course.CourseSelectionActivity
import com.nptel.courses.online.ui.playlist.PlaylistActivity
import com.nptel.courses.online.ui.playlist.PlaylistFragment
import com.nptel.courses.online.utility.Constants
import com.nptel.courses.online.utility.getSelectedCourse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var selectedCourse: Course
    private val viewModel: MainViewModel by viewModels()
    private lateinit var loginLauncher: ActivityResultLauncher<Intent>

    fun toggleFullScreen(fullScreen: Boolean) {
//        binding.bottomNavigation.visibility = if (fullScreen) View.GONE else View.VISIBLE
//        binding.appBar.getChildAt(1).visibility = if (fullScreen) View.GONE else View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        loginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) Toast.makeText(
                    this,
                    "You have successfully signed in.",
                    Toast.LENGTH_SHORT
                ).show() else Toast.makeText(this, "Sign In failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
//        binding = MainActivityBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        setSupportActionBar(findViewById(R.id.toolbar))
//        binding.toolbar.title = getSelectedCourse(this)?.title
        /*binding.bottomNavigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.course -> {
                    binding.toolbar.title = getSelectedCourse(this)?.title
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CourseFragment.newInstance()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.playlists -> {
                    if (getUser(this) == null) {
                        showLoginDialog()
                        return@setOnNavigationItemSelectedListener false
                    }
                    binding.toolbar.title = "Collections"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CollectionsFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.favourite -> {
                    if (getUser(this) == null) {
                        showLoginDialog()
                        return@setOnNavigationItemSelectedListener false
                    }
                    binding.toolbar.title = "Favorites"
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container,
                        PlaylistFragment.newInstance(
                            PlaylistFragment.Mode.COLLECTION,
                            "favorite",
                            "Favorites",
                            null
                        )
                    ).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }*/
    }

    private fun showLoginDialog() {
        val dialogBuilder = MaterialAlertDialogBuilder(this)
        dialogBuilder.setMessage("Login to bookmark, create your collections and share content with friends.")
        dialogBuilder.setPositiveButton("Login") { _: DialogInterface?, _: Int ->
            /*loginLauncher.launch(
                Intent(this, LoginActivity::class.java)
            )*/
        }
        dialogBuilder.setNegativeButton("Cancel") { _: DialogInterface?, _: Int -> }
        dialogBuilder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent: Intent
        when (item.itemId) {
            R.id.search -> {
                SearchActivity.start(this, getSelectedCourse(this))
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
            else -> return super.onOptionsItemSelected(item)
        }
    }

    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        AppTheme {
            Scaffold(topBar = {
                TopAppBar(
                    title = {
                        Text(text = selectedCourse.title)
                    },
                    actions = {
                        MainMenu()
                    },
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 0.dp
                )
            }, bottomBar = {
                BottomNavigation(backgroundColor = MaterialTheme.colors.surface) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    mainScreens.forEach { screen ->
                        BottomNavigationItem(
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(imageVector = screen.icon, contentDescription = "")
                            },
                            label = {
                                Text(text = screen.title)
                            })
                    }
                }
            }) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Course.route,
                    modifier = Modifier.padding(it)
                ) {
                    composable(Screen.Course.route) {
                        CourseScreen(viewModel, ::startPlaylistActivity)
                    }
                    composable(Screen.Collections.route) {
                        CourseScreen(viewModel, ::startPlaylistActivity)
                    }
                    composable(Screen.Favorites.route) {
                        CourseScreen(viewModel, ::startPlaylistActivity)
                    }
                }
            }
        }
    }

    @Composable
    fun MainMenu() {
        IconButton(onClick = {
            startActivity(Intent(this, CourseSelectionActivity::class.java))
            finish()
        }) {
            Icon(
                imageVector = Icons.Default.ChangeCircle,
                contentDescription = "Change Course",
                Modifier
                    .background(
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.04f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 4.dp)
            )
        }
        IconButton(onClick = {
            SearchActivity.start(
                this@MainActivity,
                selectedCourse
            )
        }) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        }
        Box {
            var showMenu by remember {
                mutableStateOf(false)
            }
            IconButton(onClick = { showMenu = true }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                modifier = Modifier.padding(horizontal = 2.dp)
            ) {
                DropdownMenuItem(onClick = {
                    intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    val shareText =
                        "Get all NPTEL online course videos at one place. Install the app now:${
                            resources.getString(R.string.play_store_dynamic_link)
                        }"
                    intent.putExtra(Intent.EXTRA_TEXT, shareText)
                    startActivity(Intent.createChooser(intent, "Choose"))
                }) {
                    Text(
                        text = "Share App",
                        modifier = Modifier.padding(start = 12.dp, end = 24.dp),
                        maxLines = 1
                    )
                }
                Divider()
                DropdownMenuItem(onClick = {
                    intent = Intent(this@MainActivity, WebViewActivity::class.java)
                    intent.putExtra(WebViewActivity.URL, Constants.feedbackForm)
                    startActivity(intent)
                }) {
                    Text(
                        text = "Feedback",
                        modifier = Modifier.padding(start = 12.dp, end = 24.dp),
                        maxLines = 1
                    )
                }
                Divider()
                DropdownMenuItem(onClick = {
                    intent = Intent(this@MainActivity, WebViewActivity::class.java)
                    intent.putExtra(
                        "url",
                        Constants.getAppreciatePaymentPage(this@MainActivity)
                    )
                    startActivity(intent)
                }) {
                    Text(
                        text = "Appreciate",
                        modifier = Modifier.padding(start = 12.dp, end = 24.dp),
                        maxLines = 1
                    )
                }
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun MainScreenPreview() {
        MainScreen()
    }

    private fun startPlaylistActivity(playlist: Playlist) {
        val intent = Intent(this, PlaylistActivity::class.java)
        intent.putExtra(
            PlaylistActivity.MODE,
            PlaylistFragment.Mode.PLAYLIST.toString()
        )
        intent.putExtra(PlaylistActivity.ID, playlist.id)
        intent.putExtra(
            PlaylistActivity.COURSE_NAME,
            getSelectedCourse(this)!!.title
        )
        intent.putExtra(PlaylistActivity.PLAYLIST_NAME, playlist.title)
        startActivity(intent)
    }
}