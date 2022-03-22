package com.nptel.courses.online.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.nptel.courses.online.R
import com.nptel.courses.online.databinding.AboutUsActivityBinding
import com.nptel.courses.online.ui.WebViewActivity

class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: AboutUsActivityBinding = DataBindingUtil.setContentView(this, R.layout.about_us_activity)
        binding.appName.text = getString(R.string.app_name_full)
        try {
            binding.version.text = String.format("App Version - %s", packageManager.getPackageInfo(packageName, 0).versionName)
        } catch (e: PackageManager.NameNotFoundException) {
            binding.version.text = "App Version - 2.0"
        }
    }

    fun onClick(view: View) {
        val intent = Intent(this, WebViewActivity::class.java)
        when (view.id) {
            R.id.privacy_policy -> {
                intent.putExtra("url", "https://sites.google.com/view/nptel-coursevideos/privacy-policy")
                startActivity(intent)
            }
            R.id.terms_condition -> {
                intent.putExtra("url", "https://sites.google.com/view/nptel-coursevideos/terms-and-conditions")
                startActivity(intent)
            }
            R.id.back -> finish()
        }
    }
}