package com.nptel.courses.online.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.nptel.courses.online.BuildConfig;
import com.nptel.courses.online.databinding.LoginActivityBinding;
import com.nptel.courses.online.entities.User;
import com.nptel.courses.online.retrofit.RetrofitFactory;
import com.nptel.courses.online.utility.SharedPreferencesUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // Uploaded Key Client ID: 862491076156-o5oehl0vqauqhcnrqab7ofbe2lb0lulo.apps.googleusercontent.com
    // Google Play Key Client ID: 862491076156-o5oehl0vqauqhcnrqab7ofbe2lb0lulo.apps.googleusercontent.com

    private Intent signInIntent;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private LoginActivityBinding binding;

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(BuildConfig.BUILD_TYPE.equals("release") ? "862491076156-o5oehl0vqauqhcnrqab7ofbe2lb0lulo.apps.googleusercontent.com" : "862491076156-o5oehl0vqauqhcnrqab7ofbe2lb0lulo.apps.googleusercontent.com")
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        signInIntent = googleSignInClient.getSignInIntent();
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            handleSignInResult(task);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        binding.googleSignin.setOnClickListener(view -> {
            activityResultLauncher.launch(signInIntent);
            binding.googleSignin.showProgress();
        });
        binding.skip.setOnClickListener(view -> {
            SharedPreferencesUtility.setLoginSkipped(this);
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            FirebaseCrashlytics.getInstance().recordException(e);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            binding.googleSignin.hideProgress();
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        RetrofitFactory.getRetrofitService(this).updateUser("google", account.getIdToken()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                binding.googleSignin.hideProgress();
                if (response.isSuccessful()) {
                    SharedPreferencesUtility.saveUser(LoginActivity.this, response.body());
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                binding.googleSignin.hideProgress();
            }
        });
    }
}