package com.nptel.courses.online.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nptel.courses.online.databinding.CourseFragmentBinding;
import com.nptel.courses.online.entities.Playlist;
import com.nptel.courses.online.retrofit.RetrofitFactory;
import com.nptel.courses.online.ui.playlist.PlaylistActivity;
import com.nptel.courses.online.ui.playlist.PlaylistFragment;
import com.nptel.courses.online.utility.SharedPreferencesUtility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Keep
public class CourseFragment extends Fragment {

    private CourseFragmentBinding binding;
    private PlaylistsAdapter adapter;

    public static CourseFragment newInstance() {
        return new CourseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CourseFragmentBinding.inflate(inflater);
        adapter = new PlaylistsAdapter(module -> {
            Intent intent = new Intent(requireContext(), PlaylistActivity.class);
            intent.putExtra(PlaylistActivity.MODE, PlaylistFragment.Mode.PLAYLIST.toString());
            intent.putExtra(PlaylistActivity.ID, module.getId());
            intent.putExtra(PlaylistActivity.COURSE_NAME, SharedPreferencesUtility.getSelectedCourse(requireContext()).getTitle());
            intent.putExtra(PlaylistActivity.PLAYLIST_NAME, module.getTitle());
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(adapter);

        if (SharedPreferencesUtility.getSelectedCourse(requireContext()) != null)
            fetch();
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void fetch() {
        binding.serverError.getRoot().setVisibility(View.GONE);
        RetrofitFactory.getRetrofitService(requireContext()).getPlaylists(SharedPreferencesUtility.getSelectedCourse(requireContext()).getId()).enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(@NonNull Call<List<Playlist>> call, @NonNull Response<List<Playlist>> response) {
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
            public void onFailure(@NonNull Call<List<Playlist>> call, @NonNull Throwable throwable) {
                adapter.submitList(null);
                binding.serverError.getRoot().setVisibility(View.VISIBLE);
                binding.serverError.retry.setOnClickListener(view -> {
                    fetch();
                    adapter.startShimmer();
                });
            }
        });
    }
}