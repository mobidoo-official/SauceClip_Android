package com.mobidoo.sauceclip

import android.app.Activity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class WebActivity : Activity() {
    private var link: String = ""
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        link = intent.getStringExtra("link") ?: ""

        webView = findViewById(R.id.webview)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webChromeClient = WebChromeClient()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null){
                    webView.loadUrl(url)
                }
                return true // 현재 WebView에서 처리하도록 true 반환
            }
        }

        webView.loadUrl(link)
    }
}