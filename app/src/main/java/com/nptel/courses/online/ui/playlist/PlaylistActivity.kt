package com.nptel.courses.online.ui.playlist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.nptel.courses.online.R
import com.nptel.courses.online.databinding.PlaylistActivityBinding
import com.nptel.courses.online.ui.AboutUsActivity
import com.nptel.courses.online.ui.WebViewActivity
import com.nptel.courses.online.utility.Constants

class PlaylistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mode = PlaylistFragment.Mode.valueOf(intent.getStringExtra(MODE)!!)
        val id = intent.getStringExtra(ID)
        val courseName = intent.getStringExtra(COURSE_NAME)
        val playlistName = intent.getStringExtra(PLAYLIST_NAME)
        val binding = PlaylistActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PlaylistFragment.newInstance(mode, id, courseName, playlistName)).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menu.removeItem(R.id.change_course)
        menu.removeItem(R.id.playlists)
        menu.removeItem(R.id.search)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent: Intent
        when (item.itemId) {
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
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PlaylistFragment.newInstance(PlaylistFragment.Mode.COLLECTION, "favorite", "Favorites", null)).commit()
                return true
            }
            R.id.app_feedback -> {
                intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("url", Constants.feedbackForm)
                startActivity(intent)
                return true
            }
            R.id.about_us -> {
                intent = Intent(this, AboutUsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.appreciate -> {
                intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("url", Constants.getAppreciatePaymentPage(this))
                startActivity(intent)
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val MODE = "mode"
        const val ID = "id"
        const val COURSE_NAME = "course name"
        const val PLAYLIST_NAME = "playlist name"
    }
}