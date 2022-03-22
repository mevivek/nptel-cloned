package com.nptel.courses.online.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.nptel.courses.online.databinding.SearchActivityBinding;
import com.nptel.courses.online.entities.Course;
import com.nptel.courses.online.entities.Video;
import com.nptel.courses.online.interfaces.ListItemClickListener;
import com.nptel.courses.online.retrofit.RetrofitFactory;
import com.nptel.courses.online.ui.playlist.PlaylistActivity;
import com.nptel.courses.online.ui.playlist.PlaylistFragment;
import com.nptel.courses.online.ui.playlist.SearchVideoListAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private SearchActivityBinding binding;
    private SearchVideoListAdapter videoAdapter;
    private List<Video> videos;
    private String searchResultFor;
    private String courseId;
    private String courseName;
    private Call<List<Video>> currentCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SearchActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        videoAdapter = new SearchVideoListAdapter(new ListItemClickListener<Video>() {
            @Override
            public void onClick(Video video) {
                Intent intent = new Intent(SearchActivity.this, PlaylistActivity.class);
                intent.putExtra(PlaylistActivity.ID, video.getId());
                intent.putExtra(PlaylistActivity.COURSE_NAME, "Searched Video");
                intent.putExtra(PlaylistActivity.MODE, PlaylistFragment.Mode.SEARCHED.toString());
                startActivity(intent);
            }

            @Override
            public void onOptionClicked(Video video, int optionId) {

            }
        });
        binding.recyclerView.setAdapter(videoAdapter);
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetch(newText);
                return false;
            }
        });
        binding.search.requestFocus();
        binding.search.setIconifiedByDefault(false);

        courseId = getIntent().getStringExtra("course_id");
        courseName = getIntent().getStringExtra("course_name");

        if (courseName != null)
            binding.courseName.setChecked(true);

        if (courseName != null)
            binding.courseName.setText(Html.fromHtml("In course <b>" + courseName + "</b>"));
        else binding.courseName.setVisibility(View.GONE);

        binding.courseName.setOnCheckedChangeListener((buttonView, isChecked) -> fetch(binding.search.getQuery().toString()));
    }

    private void fetch(String query) {
        if (currentCall != null)
            currentCall.cancel();
        if (query.length() == 0) {
            videoAdapter.submitList(null);
            binding.searchResultFor.setText(null);
            binding.hint.setText("Your results will appear here.");
            binding.hint.setVisibility(View.VISIBLE);
            return;
        }
        currentCall = RetrofitFactory.getRetrofitService(this).searchVideos(query, binding.courseName.isChecked() ? courseId : null);
        currentCall.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                if (response.isSuccessful()) {
                    binding.searchResultFor.setText(Html.fromHtml("Results for <b>" + query + "</b>"));
                    videos = response.body();
                    videoAdapter.submitList(videos);
                    if (videos.size() == 0) {
                        binding.hint.setText("No video found.");
                        binding.hint.setVisibility(View.VISIBLE);
                    } else binding.hint.setVisibility(View.GONE);
                } else
                    Toast.makeText(SearchActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<List<Video>> call, @NonNull Throwable throwable) {
                if (throwable.getMessage() != null && !throwable.getMessage().equalsIgnoreCase("canceled"))
                    Toast.makeText(SearchActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void start(Context context, Course course) {
        Intent intent = new Intent(context, SearchActivity.class);
        if (course != null) {
            intent.putExtra("course_id", course.getId());
            intent.putExtra("course_name", course.getTitle());
        }
        context.startActivity(intent);
    }
}
