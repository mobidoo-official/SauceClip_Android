package com.mobidoo.sauceclip

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.AttributeSet
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.google.gson.Gson

class SauceCurationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val webView: WebView
    private var curatioinId: String? = null
    private var partnerId: String? = null
    private var stageMode: Boolean = false

    var webViewClient: WebViewClient = WebViewClient()
        set(value) {
            field = value
            webView.webViewClient = value
        }

    init {
        // 웹뷰를 생성하고 설정
        webView = WebView(context)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true // JavaScript를 사용할 수 있도록 설정

        // 웹뷰를 FrameLayout에 추가
        addView(webView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        webView.webChromeClient = WebChromeClient()
        webView.settings.userAgentString = webView.settings.userAgentString + " sauce-sdk-android"
        webView.setOnLongClickListener { true }
    }

    fun setInit(partnerId: String, curatioinId: String) {
        this.partnerId = partnerId
        this.curatioinId = curatioinId
    }

    fun setStageMode(on: Boolean) {
        stageMode = on
    }

    fun load() {

        if (partnerId == null) {
            throw Error("partnerId is required")
        }

        if (curatioinId == null) {
            throw Error("curatioinId is required")
        }

        if (stageMode) {
            webView.loadData(
                """<!DOCTYPE html>
<html lang="en">
<head>
  <script src="https://stage.showcase.sauceclip.com/static/js/SauceClipCollectionLib.js"></script>
</head>
<body>
  <div id="sauce_clip_curation"></div>
  <script>
    window.addEventListener('load', () => {
      const partnerId = '$partnerId'
      window.SauceClipCollectionLib.setInit({ partnerId })
      window.SauceClipCollectionLib.loadCuration({ curationId: '$curatioinId', elementId: 'sauce_clip_curation' })
    })
  </script>
</body>
<style>
  html, body {
    padding: 0;
    margin: 0;
    height: fit-content;
    overflow-x: hidden;
  }
</style>
</html>""", "text/html", "UTF-8"
            )
        }else {
            webView.loadData(
                """<!DOCTYPE html>
<html lang="en">
<head>
  <script src="https://showcase.sauceclip.com/static/js/SauceClipCollectionLib.js"></script>
</head>
<body>
  <div id="sauce_clip_curation"></div>
  <script>
    window.addEventListener('load', () => {
      const partnerId = '$partnerId'
      window.SauceClipCollectionLib.setInit({ partnerId })
      window.SauceClipCollectionLib.loadCuration({ curationId: '$curatioinId', elementId: 'sauce_clip_curation' })
    })
  </script>
</body>
<style>
  html, body {
    padding: 0;
    margin: 0;
    height: fit-content;
    overflow-x: hidden;
  }
</style>
</html>""", "text/html", "UTF-8"
            )
        }
    }

    fun setOnMoveBroadcast(callback: ((message: SauceBroadcastInfo) -> Unit)?) {
        webView.removeJavascriptInterface("sauceclipMoveBroadcast")
        if (callback != null) {
            webView.addJavascriptInterface(
                SauceclipOnMoveBroadcastJavaScriptInterface { broadcastInfo ->
                    callback.invoke(broadcastInfo)
                },
                "sauceclipMoveBroadcast"
            )
        }
    }

    private class SauceclipOnMoveBroadcastJavaScriptInterface(
        val sauceflexOnMovebroadcast: ((message: SauceBroadcastInfo) -> Unit)
    ) {
        private val handler = Handler()

        @JavascriptInterface   // 클립 이동
        fun sauceclipMoveBroadcast(message: String) {
            val gson = Gson()
            val broadcastInfo = gson.fromJson(message, SauceBroadcastInfo::class.java)
            handler.post {
                sauceflexOnMovebroadcast.invoke(broadcastInfo)
            }
        }
    }


}