package com.nptel.courses.online.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nptel.courses.online.R;
import com.nptel.courses.online.databinding.AboutUsActivityBinding;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AboutUsActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.about_us_activity);
        binding.appName.setText(getString(R.string.app_name_full));
        try {
            binding.version.setText(String.format("App Version - %s", getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
        } catch (PackageManager.NameNotFoundException e) {
            binding.version.setText("App Version - 2.0");
        }
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, WebViewActivity.class);
        switch (view.getId()) {
            case R.id.privacy_policy:
                intent.putExtra("url", "https://sites.google.com/view/nptel-coursevideos/privacy-policy");
                startActivity(intent);
                break;
            case R.id.terms_condition:
                intent.putExtra("url", "https://sites.google.com/view/nptel-coursevideos/terms-and-conditions");
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
