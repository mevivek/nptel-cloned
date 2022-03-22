package com.nptel.courses.online.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.nptel.courses.online.BuildConfig
import com.nptel.courses.online.R
import com.nptel.courses.online.ui.WebViewActivity
import com.nptel.courses.online.ui.WebViewActivity.WebAppInterface

class WebViewActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialog? = null
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = intent.getStringExtra(TITLE)
        toolbar.setNavigationOnClickListener { view: View? -> onBackPressed() }
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading Page")
        val url = intent.getStringExtra(URL)
        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(WebAppInterface(this), "Android")
        webView.settings.setSupportMultipleWindows(true)
        WebView.setWebContentsDebuggingEnabled(true)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                progressDialog!!.show()
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressDialog!!.dismiss()
            }
        }
        webView.loadUrl(url!!)
        if (BuildConfig.DEBUG) Toast.makeText(this, url, Toast.LENGTH_LONG).show()
    }

    internal class WebAppInterface(private val context: Activity) {
        @JavascriptInterface
        fun paymentCompleted(message: String?) {
            context.setResult(RESULT_OK)
            context.finish()
        }

        @JavascriptInterface
        fun paymentCancelled(message: String?) {
            context.setResult(RESULT_CANCELED)
            context.finish()
        }
    }

    companion object {
        const val TITLE = "title"
        const val URL = "url"
    }
}