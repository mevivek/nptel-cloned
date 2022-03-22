package com.nptel.courses.online.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.nptel.courses.online.R;
import com.nptel.courses.online.activities.AboutUsActivity;
import com.nptel.courses.online.activities.SearchActivity;
import com.nptel.courses.online.activities.WebViewActivity;
import com.nptel.courses.online.databinding.CourseSelectionActivityBinding;
import com.nptel.courses.online.entities.Course;
import com.nptel.courses.online.retrofit.RetrofitFactory;
import com.nptel.courses.online.ui.main.CourseListAdapter;
import com.nptel.courses.online.ui.main.MainActivity;
import com.nptel.courses.online.ui.playlist.PlaylistActivity;
import com.nptel.courses.online.ui.playlist.PlaylistFragment;
import com.nptel.courses.online.utility.Constants;
import com.nptel.courses.online.utility.SharedPreferencesUtility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseSelectionActivity extends AppCompatActivity {

    private CourseSelectionActivityBinding binding;
    private CourseListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CourseSelectionActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        adapter = new CourseListAdapter(course -> {
            SharedPreferencesUtility.setSelectedCourse(this, course);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        binding.recyclerView.setAdapter(adapter);
        fetch();
        binding.serverError.retry.setOnClickListener(view -> fetch());
    }

    private void fetch() {
        RetrofitFactory.getRetrofitService(this).getCourses().enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(@NonNull Call<List<Course>> call, @NonNull Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    adapter.submitList(response.body());
                } else {
                    adapter.submitList(null);
                    binding.serverError.getRoot().setVisibility(View.VISIBLE);
                    binding.serverError.retry.setOnClickListener(view -> {
                        fetch();
                        adapter.startShimmer();
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Course>> call, @NonNull Throwable throwable) {
                adapter.submitList(null);
                binding.serverError.getRoot().setVisibility(View.VISIBLE);
                binding.serverError.retry.setOnClickListener(view -> {
                    fetch();
                    adapter.startShimmer();
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.removeItem(R.id.change_course);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int itemId = item.getItemId();
        if (itemId == R.id.search) {
            SearchActivity.start(this, null);
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