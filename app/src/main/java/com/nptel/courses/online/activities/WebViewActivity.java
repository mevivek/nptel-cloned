package com.nptel.courses.online.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.nptel.courses.online.BuildConfig;
import com.nptel.courses.online.R;

public class WebViewActivity extends AppCompatActivity {

    public static final String TITLE = "title";
    public static final String URL = "url";

    private ProgressDialog progressDialog;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getIntent().getStringExtra(TITLE));
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Page");
        String url = getIntent().getStringExtra(URL);
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        webView.getSettings().setSupportMultipleWindows(true);

        WebView.setWebContentsDebuggingEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        });
        webView.loadUrl(url);
        if (BuildConfig.DEBUG) Toast.makeText(this, url, Toast.LENGTH_LONG).show();
    }

    static class WebAppInterface {
        private final Activity context;

        WebAppInterface(Activity context) {
            this.context = context;
        }

        @JavascriptInterface
        public void paymentCompleted(String message) {
            context.setResult(RESULT_OK);
            context.finish();
        }

        @JavascriptInterface
        public void paymentCancelled(String message) {
            context.setResult(RESULT_CANCELED);
            context.finish();
        }
    }
}
