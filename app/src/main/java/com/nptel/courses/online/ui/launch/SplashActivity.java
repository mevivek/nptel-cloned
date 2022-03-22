package com.nptel.courses.online.ui.launch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.nptel.courses.online.BuildConfig;
import com.nptel.courses.online.databinding.SplashActivityBinding;
import com.nptel.courses.online.retrofit.RetrofitFactory;
import com.nptel.courses.online.ui.course.CourseSelectionActivity;
import com.nptel.courses.online.ui.login.LoginActivity;
import com.nptel.courses.online.ui.main.MainActivity;
import com.nptel.courses.online.ui.playlist.PlaylistActivity;
import com.nptel.courses.online.ui.playlist.PlaylistFragment;
import com.nptel.courses.online.utility.SharedPreferencesUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private SplashActivityBinding binding;
    private ActivityResultLauncher<Intent> loginActivityLauncher;
    private Update update;

    @Override
    protected void onStart() {
        super.onStart();
        loginActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> checkUser());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SplashActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.skip.setOnClickListener(view -> checkForDynamic());
        binding.retry.setOnClickListener(view -> checkForUpdate());
        binding.update.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(update.updateLink)));
        });
        checkForUpdate();
    }

    private void checkForDynamic() {
        FirebaseDynamicLinks
                .getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    if (pendingDynamicLinkData != null) {
                        Uri deepLink = pendingDynamicLinkData.getLink();
                        if (deepLink == null)
                            checkUser();
                        else {
                            String url = deepLink.toString();
                            if (url.contains("youtu.be")) {
                                String id = url.replace("https://youtu.be/", "");
                                Intent intent = new Intent(this, PlaylistActivity.class);
                                intent.putExtra(PlaylistActivity.MODE, PlaylistFragment.Mode.SHARED_VIDEO.toString());
                                intent.putExtra(PlaylistActivity.COURSE_NAME, "Shared");
                                intent.putExtra(PlaylistActivity.ID, id);
                                startActivity(intent);
                                getIntent().setData(null);
                            } else {
                                checkUser();
                            }
                        }
                    } else {
                        checkUser();
                    }
                }).addOnFailureListener(this, exception -> checkUser());
    }

    private void checkForUpdate() {
        binding.retry.setVisibility(View.GONE);
        binding.progress.setVisibility(View.VISIBLE);
        binding.checkingUpdate.setText("Checking for updates");
        RetrofitFactory.getRetrofitService(this).checkForUpdate().enqueue(new Callback<Update>() {
            @Override
            public void onResponse(@NonNull Call<Update> call, @NonNull Response<Update> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    update = response.body();
                    if (update.checkAvailable())
                        binding.setUpdate(update);
                    else checkForDynamic();
                } else {
                    Toast.makeText(SplashActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    binding.retry.setVisibility(View.VISIBLE);
                    binding.progress.setVisibility(View.GONE);
                    binding.checkingUpdate.setText("Something went wrong.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Update> call, @NonNull Throwable throwable) {
                Toast.makeText(SplashActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                binding.retry.setVisibility(View.VISIBLE);
                binding.progress.setVisibility(View.GONE);
                binding.checkingUpdate.setText("Something went wrong.");
            }
        });
    }

    private void checkUser() {
        if (SharedPreferencesUtility.getUser(this) == null && !SharedPreferencesUtility.isLoginSkipped(this)) {
            loginActivityLauncher.launch(new Intent(SplashActivity.this, LoginActivity.class));
        } else {
            if (SharedPreferencesUtility.getSelectedCourse(SplashActivity.this) == null)
                startActivity(new Intent(SplashActivity.this, CourseSelectionActivity.class));
            else startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Keep
    public static class Update {
        private Integer current;
        private Integer minimum;
        private String updateLink;
        private boolean mandatory;

        public boolean checkAvailable() {
            mandatory = BuildConfig.VERSION_CODE < minimum;
            return BuildConfig.VERSION_CODE < current;
        }

        public Integer getCurrent() {
            return current;
        }

        public void setCurrent(Integer current) {
            this.current = current;
        }

        public Integer getMinimum() {
            return minimum;
        }

        public void setMinimum(Integer minimum) {
            this.minimum = minimum;
        }

        public String getUpdateLink() {
            return updateLink;
        }

        public void setUpdateLink(String updateLink) {
            this.updateLink = updateLink;
        }

        public boolean isMandatory() {
            return mandatory;
        }

        public void setMandatory(boolean mandatory) {
            this.mandatory = mandatory;
        }
    }
}
