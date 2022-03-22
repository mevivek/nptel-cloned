package com.nptel.courses.online.ui.playlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.nptel.courses.online.R;
import com.nptel.courses.online.activities.AboutUsActivity;
import com.nptel.courses.online.activities.WebViewActivity;
import com.nptel.courses.online.databinding.PlaylistActivityBinding;
import com.nptel.courses.online.utility.Constants;

public class PlaylistActivity extends AppCompatActivity {

    public static final String MODE = "mode";
    public static final String ID = "id";
    public static final String COURSE_NAME = "course name";
    public static final String PLAYLIST_NAME = "playlist name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlaylistFragment.Mode mode = PlaylistFragment.Mode.valueOf(getIntent().getStringExtra(MODE));
        String id = getIntent().getStringExtra(ID);
        String courseName = getIntent().getStringExtra(COURSE_NAME);
        String playlistName = getIntent().getStringExtra(PLAYLIST_NAME);

        PlaylistActivityBinding binding = PlaylistActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(view -> onBackPressed());

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, PlaylistFragment.newInstance(mode, id, courseName, playlistName)).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.removeItem(R.id.change_course);
        menu.removeItem(R.id.playlists);
        menu.removeItem(R.id.search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int itemId = item.getItemId();
        if (itemId == R.id.share_app) {
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareText = "Get all NPTEL online course videos at one place.\nInstall the app now:\n"
                    + getResources().getString(R.string.play_store_dynamic_link);
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(intent, "Choose"));
            return true;
        } else if (itemId == R.id.favourite) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, PlaylistFragment.newInstance(PlaylistFragment.Mode.COLLECTION, "favorite", "Favorites", null)).commit();
            return true;
        } else if (itemId == R.id.app_feedback) {
            intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("url", Constants.feedbackForm);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.about_us) {
            intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.appreciate) {
            intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("url", Constants.getAppreciatePaymentPage(this));
            startActivity(intent);

            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);

    }
}