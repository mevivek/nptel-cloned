package com.nptel.courses.online.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nptel.courses.online.R;
import com.nptel.courses.online.activities.AboutUsActivity;
import com.nptel.courses.online.activities.SearchActivity;
import com.nptel.courses.online.activities.WebViewActivity;
import com.nptel.courses.online.databinding.MainActivityBinding;
import com.nptel.courses.online.ui.course.CourseSelectionActivity;
import com.nptel.courses.online.ui.login.LoginActivity;
import com.nptel.courses.online.ui.playlist.PlaylistActivity;
import com.nptel.courses.online.ui.playlist.PlaylistFragment;
import com.nptel.courses.online.utility.Constants;
import com.nptel.courses.online.utility.SharedPreferencesUtility;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;
    private ActivityResultLauncher<Intent> loginLauncher;

    public void setBottomNavigation(int itemId) {
        binding.bottomNavigation.setSelectedItemId(itemId);
    }

    public void toggleFullScreen(boolean fullScreen) {
        binding.bottomNavigation.setVisibility(fullScreen ? View.GONE : View.VISIBLE);
        binding.appBar.getChildAt(1).setVisibility(fullScreen ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK)
                Toast.makeText(this, "You have successfully signed in.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Sign In failed", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(findViewById(R.id.toolbar));
        binding.toolbar.setTitle(SharedPreferencesUtility.getSelectedCourse(this).getTitle());
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.course) {
                binding.toolbar.setTitle(SharedPreferencesUtility.getSelectedCourse(this).getTitle());
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, CourseFragment.newInstance()).commit();
                return true;
            } else if (item.getItemId() == R.id.playlists) {
                if (SharedPreferencesUtility.getUser(this) == null) {
                    showLoginDialog();
                    return false;
                }
                binding.toolbar.setTitle("Collections");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CollectionsFragment()).commit();
                return true;
            } else if (item.getItemId() == R.id.favourite) {
                if (SharedPreferencesUtility.getUser(this) == null) {
                    showLoginDialog();
                    return false;
                }
                binding.toolbar.setTitle("Favorites");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, PlaylistFragment.newInstance(PlaylistFragment.Mode.COLLECTION, "favorite", "Favorites", null)).commit();
                return true;
            }
            return false;
        });
    }

    private void showLoginDialog() {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
        dialogBuilder.setMessage("Login to bookmark, create your collections and share content with friends.");
        dialogBuilder.setPositiveButton("Login", (dialog, which) -> loginLauncher.launch(new Intent(this, LoginActivity.class)));
        dialogBuilder.setNegativeButton("Cancel", ((dialog, which) -> {
        }));
        dialogBuilder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int itemId = item.getItemId();
        if (itemId == R.id.search) {
            SearchActivity.start(this, SharedPreferencesUtility.getSelectedCourse(this));
            return true;
        } else if (itemId == R.id.share_app) {
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareText = "Get all NPTEL online course videos at one place.\nInstall the app now:\n"
                    + getResources().getString(R.string.play_store_dynamic_link);
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(intent, "Choose"));
            return true;
        } else if (itemId == R.id.favourite) {
            intent = new Intent(getApplicationContext(), PlaylistActivity.class);
            intent.putExtra(PlaylistActivity.MODE, PlaylistFragment.Mode.COLLECTION);
//            intent.putExtra(PlaylistActivity.ID, SharedPreferencesUtility.FAVOURITE_VIDEOS);
            intent.putExtra(PlaylistActivity.COURSE_NAME, "Favourites");
            startActivity(intent);
            return true;
        } else if (itemId == R.id.change_course) {
            startActivity(new Intent(this, CourseSelectionActivity.class));
            finish();
            return true;
        } else if (itemId == R.id.app_feedback) {
            intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("url", Constants.feedbackForm);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.appreciate) {
            intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("url", Constants.getAppreciatePaymentPage(this));
            startActivity(intent);
            return true;
        } else if (itemId == R.id.about_us) {
            intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}