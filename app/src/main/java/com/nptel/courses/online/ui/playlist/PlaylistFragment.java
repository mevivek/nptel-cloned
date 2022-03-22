package com.nptel.courses.online.ui.playlist;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.nptel.courses.online.BuildConfig;
import com.nptel.courses.online.R;
import com.nptel.courses.online.adapters.QuickCollectionListAdapter;
import com.nptel.courses.online.databinding.AddToCollectionBinding;
import com.nptel.courses.online.databinding.PlaylistFragmentBinding;
import com.nptel.courses.online.entities.Collection;
import com.nptel.courses.online.entities.Video;
import com.nptel.courses.online.interfaces.ListItemClickListener;
import com.nptel.courses.online.retrofit.RetrofitFactory;
import com.nptel.courses.online.ui.login.LoginActivity;
import com.nptel.courses.online.ui.main.MainActivity;
import com.nptel.courses.online.utility.SharedPreferencesUtility;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistFragment extends Fragment {

    private static final String ARG_PARAM_MODE = "mode";
    private static final String ARG_PARAM_ID = "id";
    private static final String ARG_PARAM_COURSE_NAME = "coursename";
    private static final String ARG_PARAM_PLAYLIST_NAME = "playlist name";

    private PlaylistFragmentBinding binding;
    private ActionBar actionBar;
    private VideoListAdapter adapter;
    private YouTubePlayer youTubePlayer;

    private Configuration screenConfiguration;

    private Mode mode;
    private String id;
    private String courseName;
    private String playlistName;

    private ActivityResultLauncher<Intent> loginLauncher;

    private void showLoginDialog() {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        dialogBuilder.setTitle("Login to bookmark, create your collections and share content with friends.");
        dialogBuilder.setPositiveButton("Login", (dialog, which) -> loginLauncher.launch(new Intent(requireContext(), LoginActivity.class)));
        dialogBuilder.setNegativeButton("Cancel", ((dialog, which) -> {
        }));
        dialogBuilder.show();
    }

    private final ListItemClickListener<Video> videoListItemClickListener = new ListItemClickListener<Video>() {
        @Override
        public void onClick(Video video) {
            playVideo(video);
        }

        @Override
        public void onOptionClicked(Video video, int optionId) {
            if (optionId == R.id.share_video) {
                shareVideo(video);
            } else if (optionId == R.id.favourite) {
                if (SharedPreferencesUtility.getUser(requireContext()) == null) {
                    showLoginDialog();
                    return;
                }

                if (video.isFavorite()) {
                    RetrofitFactory.getRetrofitService(requireContext()).removeFromCollection("favorite", video.getId()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                            fetch();
                            if (response.isSuccessful()) {
                                Toast.makeText(requireContext(), "Video removed from favorite", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(requireContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            fetch();
                            Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    RetrofitFactory.getRetrofitService(requireContext()).addToCollection("favorite", video.getId()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                            fetch();
                            if (response.isSuccessful()) {
                                Toast.makeText(requireContext(), "Video marked favorite", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(requireContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            fetch();
                            Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                video.setFavorite(!video.isFavorite());
            } else if (optionId == R.id.add_to_playlist) {
                if (SharedPreferencesUtility.getUser(requireContext()) == null) {
                    showLoginDialog();
                    return;
                }
                addToPlaylist(video);
            } else if (optionId == R.id.delete_video) {
                RetrofitFactory.getRetrofitService(requireContext()).removeFromCollection(id, video.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        fetch();
                        if (response.isSuccessful()) {
                            Toast.makeText(requireContext(), "Video removed from the collection.", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                        fetch();
                        Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };


    public PlaylistFragment() {
    }

    public static PlaylistFragment newInstance(Mode mode, String id, String courseName, String playlistName) {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_MODE, mode.toString());
        args.putString(ARG_PARAM_ID, id);
        args.putString(ARG_PARAM_COURSE_NAME, courseName);
        args.putString(ARG_PARAM_PLAYLIST_NAME, playlistName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = Mode.valueOf(getArguments().getString(ARG_PARAM_MODE));
            id = getArguments().getString(ARG_PARAM_ID);
            courseName = getArguments().getString(ARG_PARAM_COURSE_NAME);
            playlistName = getArguments().getString(ARG_PARAM_PLAYLIST_NAME);
        }
        loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK)
                Toast.makeText(requireContext(), "You have successfully signed in.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(requireContext(), "Sign In failed", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = PlaylistFragmentBinding.inflate(inflater);

        binding.youtubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> this.youTubePlayer = youTubePlayer);
        getLifecycle().addObserver(binding.youtubePlayerView);

        adapter = new VideoListAdapter(videoListItemClickListener);
        binding.recyclerView.setAdapter(adapter);

        if (mode == null) {
            Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            if (requireActivity() instanceof PlaylistActivity)
                requireActivity().finish();
        }
        actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        fetch();
        return binding.getRoot();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.screenConfiguration = newConfig;
        if (binding.youtubePlayerContainer.getVisibility() == View.GONE)
            return;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.youtubePlayerView.enterFullScreen();
            requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            if (requireActivity() instanceof MainActivity)
                ((MainActivity) requireActivity()).toggleFullScreen(true);
            if (actionBar != null)
                actionBar.hide();
        } else {
            binding.youtubePlayerView.exitFullScreen();
            requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            if (requireActivity() instanceof MainActivity)
                ((MainActivity) requireActivity()).toggleFullScreen(false);
            if (actionBar != null)
                actionBar.show();
        }
    }

    private void fetch() {
        switch (mode) {
            case PLAYLIST:
                showYoutubePlaylist();
                break;
            case COLLECTION:
                showCustomPlaylist();
                break;
            case SHARED_VIDEO:
                if (actionBar != null)
                    actionBar.setTitle("Shared Video");
                showSharedVideo();
                break;
            case SEARCHED:
                if (actionBar != null)
                    actionBar.setTitle("Searched Video");
                showSearchedVideo();
                break;
            default:
                Toast.makeText(requireContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                if (requireActivity() instanceof PlaylistActivity)
                    requireActivity().finish();
        }
    }

    private void setVideoList(List<Video> videos) {
        setVideoList(videos, null);
    }

    private void setVideoList(List<Video> videos, Runnable runnable) {
        adapter.submitList(videos, runnable);
    }

    private void showYoutubePlaylist() {
        if (actionBar != null) {
            actionBar.setTitle(courseName);
            actionBar.setSubtitle(playlistName);
        }
        RetrofitFactory.getRetrofitService(requireContext()).getVideos(id, "position ASC").enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                if (response.isSuccessful()) {
                    setVideoList(response.body());
                    if (response.body().size() == 0) {
                        binding.nothingFound.getRoot().setVisibility(View.VISIBLE);
                        binding.nothingFound.retry.setOnClickListener(view -> {
                            binding.nothingFound.getRoot().setVisibility(View.GONE);
                            adapter.startShimmer();
                            showYoutubePlaylist();
                        });
                    }
                } else {
                    adapter.submitList(null);
                    binding.serverError.getRoot().setVisibility(View.VISIBLE);
                    binding.serverError.retry.setOnClickListener(view -> {
                        binding.serverError.getRoot().setVisibility(View.GONE);
                        showYoutubePlaylist();
                        adapter.startShimmer();
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Video>> call, @NonNull Throwable throwable) {
                adapter.submitList(null);
                binding.serverError.getRoot().setVisibility(View.VISIBLE);
                binding.serverError.retry.setOnClickListener(view -> {
                    binding.serverError.getRoot().setVisibility(View.GONE);
                    showYoutubePlaylist();
                    adapter.startShimmer();
                });
            }
        });
    }

    private void showCustomPlaylist() {
        if (actionBar != null)
            actionBar.setTitle(courseName);

        if (!id.equals("favorite"))
            adapter.setShowDelete();
        RetrofitFactory.getRetrofitService(requireContext()).getCollection(id).enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                if (response.isSuccessful()) {
                    setVideoList(response.body());
                    if (response.body().size() == 0) {
                        binding.nothingFound.getRoot().setVisibility(View.VISIBLE);
                        binding.nothingFound.retry.setOnClickListener(view -> {
                            binding.nothingFound.getRoot().setVisibility(View.GONE);
                            adapter.startShimmer();
                            showCustomPlaylist();
                        });
                    }
                } else {
                    adapter.submitList(null);
                    binding.serverError.getRoot().setVisibility(View.VISIBLE);
                    binding.serverError.retry.setOnClickListener(view -> {
                        binding.serverError.getRoot().setVisibility(View.GONE);
                        showCustomPlaylist();
                        adapter.startShimmer();
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Video>> call, @NonNull Throwable throwable) {
                adapter.submitList(null);
                binding.serverError.getRoot().setVisibility(View.VISIBLE);
                binding.serverError.retry.setOnClickListener(view -> {
                    binding.serverError.getRoot().setVisibility(View.GONE);
                    showCustomPlaylist();
                    adapter.startShimmer();
                });
            }
        });
    }

    private void showSharedVideo() {
        RetrofitFactory.getRetrofitService(requireContext()).getSharedVideos(id).enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(@NonNull Call<List<Video>> call, @NonNull Response<List<Video>> response) {
                if (response.isSuccessful()) {
                    adapter.submitList(response.body());
                    if (response.body().size() == 0) {
                        binding.nothingFound.getRoot().setVisibility(View.VISIBLE);
                        binding.nothingFound.retry.setOnClickListener(view -> {
                            binding.nothingFound.getRoot().setVisibility(View.GONE);
                            adapter.startShimmer();
                            showYoutubePlaylist();
                        });
                    }
                } else {
                    adapter.submitList(null);
                    binding.serverError.getRoot().setVisibility(View.VISIBLE);
                    binding.serverError.retry.setOnClickListener(view -> {
                        binding.serverError.getRoot().setVisibility(View.GONE);
                        showYoutubePlaylist();
                        adapter.startShimmer();
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Video>> call, @NonNull Throwable throwable) {
                adapter.submitList(null);
                binding.serverError.getRoot().setVisibility(View.VISIBLE);
                binding.serverError.retry.setOnClickListener(view -> {
                    binding.serverError.getRoot().setVisibility(View.GONE);
                    showYoutubePlaylist();
                    adapter.startShimmer();
                });
            }
        });
    }

    private void showSearchedVideo() {
        RetrofitFactory.getRetrofitService(requireContext()).getSearchedVideo(id).enqueue(new Callback<Video>() {
            @Override
            public void onResponse(@NonNull Call<Video> call, @NonNull Response<Video> response) {
                if (response.isSuccessful()) {
                    adapter.submitList(Collections.singletonList(response.body()));
                    if (response.body() == null) {
                        binding.nothingFound.getRoot().setVisibility(View.VISIBLE);
                        binding.nothingFound.retry.setOnClickListener(view -> {
                            binding.nothingFound.getRoot().setVisibility(View.GONE);
                            adapter.startShimmer();
                            showYoutubePlaylist();
                        });
                    }
                } else {
                    adapter.submitList(null);
                    binding.serverError.getRoot().setVisibility(View.VISIBLE);
                    binding.serverError.retry.setOnClickListener(view -> {
                        binding.serverError.getRoot().setVisibility(View.GONE);
                        showYoutubePlaylist();
                        adapter.startShimmer();
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<Video> call, @NonNull Throwable throwable) {
                adapter.submitList(null);
                binding.serverError.getRoot().setVisibility(View.VISIBLE);
                binding.serverError.retry.setOnClickListener(view -> {
                    binding.serverError.getRoot().setVisibility(View.GONE);
                    showYoutubePlaylist();
                    adapter.startShimmer();
                });
            }
        });
    }

    public void playVideo(Video video) {
        binding.youtubePlayerContainer.setVisibility(View.VISIBLE);
        if (youTubePlayer == null)
            new Handler().postDelayed(() -> playVideo(video), 100);
        else youTubePlayer.loadVideo(video.getYoutubeVideoId(), 0);
        binding.recyclerView.smoothScrollToPosition(adapter.getCurrentList().indexOf(video));
        if (screenConfiguration != null)
            onConfigurationChanged(screenConfiguration);
    }

    public void shareVideo(Video video) {
        String url = "https://youtu.be/" + video.getYoutubeVideoId();
        Toast.makeText(requireContext(), "Creating share link...", Toast.LENGTH_SHORT).show();
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(url))
                .setDomainUriPrefix("https://nptel.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Uri shortLink = task.getResult().getShortLink();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, shortLink != null ? shortLink.toString() : "error");
                        startActivity(Intent.createChooser(intent, "Share URL"));
                    } else {
                        if (BuildConfig.DEBUG)
                            Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(requireContext(), "Some error occurred. Can't share now.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addToPlaylist(Video video) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        AddToCollectionBinding binding = AddToCollectionBinding.inflate(bottomSheetDialog.getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.show();
        binding.video.setText(Html.fromHtml("Add <b>" + video.getTitle() + "</b> to"));
        QuickCollectionListAdapter adapter = new QuickCollectionListAdapter(video.getId());
        binding.recyclerView.setAdapter(adapter);
        RetrofitFactory.getRetrofitService(requireContext()).getCollections().enqueue(new Callback<List<Collection>>() {
            @Override
            public void onResponse(@NonNull Call<List<Collection>> call, @NonNull Response<List<Collection>> response) {
                if (response.isSuccessful())
                    adapter.submitList(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Collection>> call, @NonNull Throwable throwable) {

            }
        });
        binding.save.setOnClickListener(view -> {
            String playlistName;
            if (adapter.getSelectedCollection() == null) {
                playlistName = binding.collectionName.getText().toString();
                if (playlistName.isEmpty()) {
                    binding.collectionName.setError("Provide playlist name" + (adapter.getCurrentList().size() > 0 ? " or choose from above list" : ""));
                    return;
                }
            } else playlistName = adapter.getSelectedCollection().getName();
            RetrofitFactory.getRetrofitService(requireContext()).addToCollection(playlistName, video.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Video added to playlist", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    } else
                        Toast.makeText(requireContext(), "Can't save video. Encountered some error.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                    Toast.makeText(requireContext(), "Can't save video. Encountered some error.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public enum Mode {
        PLAYLIST, COLLECTION, SHARED_VIDEO, SHARED_COLLECTION, SEARCHED, ERROR
    }
}