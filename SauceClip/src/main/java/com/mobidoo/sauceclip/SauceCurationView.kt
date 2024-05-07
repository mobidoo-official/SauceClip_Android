package com.mobidoo.sauceclip

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.google.gson.Gson


class SauceCurationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val webView: ObservableWebView
    private var curatioinId: String? = null
    private var partnerId: String? = null
    private var stageMode: Boolean = false
    private var pvVisibility: Boolean = true
    private var horizontalPadding: Int = 0
    private var previewAutoplay: Boolean = false


    var webViewClient: WebViewClient = WebViewClient()
        set(value) {
            field = value
            webView.webViewClient = value
        }

    init {
        // 웹뷰를 생성하고 설정
        webView = ObservableWebView(context)

        // 웹뷰를 FrameLayout에 추가
        addView(webView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        webView.webChromeClient = WebChromeClient()
        webView.settings.userAgentString = webView.settings.userAgentString + " sauce-sdk-android"

        webView.setOnTouchListener { v, ev ->
            when (ev.getAction()) {
                MotionEvent.ACTION_DOWN -> {
                    false
                }
                MotionEvent.ACTION_MOVE -> {
                    // X축 이동이 Y축 이동보다 클 때 true를 반환하여 수평 스크롤 이벤트를 가로챈다
                    // v.parent.requestDisallowInterceptTouchEvent(deltaX > deltaY)
                    if (webView.isCurrentlyScrolling()){
                        v.parent.requestDisallowInterceptTouchEvent(true)
                    }else {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
            }
            false
        }
    }

    fun setInit(partnerId: String, curatioinId: String) {
        this.partnerId = partnerId
        this.curatioinId = curatioinId
    }

    fun setStageMode(on: Boolean) {
        stageMode = on
    }

    fun setPvVisibility(on: Boolean) {
        pvVisibility = on
    }

    fun setHorizontalPadding(padding: Int) {
        val pixels = dpToPx(padding, context)
        horizontalPadding = pixels
    }

    fun setPreviewAutoplay(on: Boolean) {
        previewAutoplay = on
    }

    private fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    fun load() {

        if (partnerId == null) {
            throw Error("partnerId is required")
        }

        if (curatioinId == null) {
            throw Error("curatioinId is required")
        }

        var pvOption = ""
        if (!pvVisibility) {
            pvOption =
                "window.SauceClipCollectionLib.setCurationClipPvStyle('{\"display\": \"none\"}')"
        }

        var paddingOption = ""
        if (horizontalPadding > 0) {
            paddingOption =
                "window.SauceClipCollectionLib.setCurationHorizontalContentsStyle('{\"padding-left\": \"${horizontalPadding}px\", \"padding-right\": \"${horizontalPadding}px\"}')"
        }

        var previewAutoplayOption = ""
        if (previewAutoplay) {
            previewAutoplayOption =
                "window.SauceClipCollectionLib.setCurationClipPreviewAutoplay(true)"
        } else {
            previewAutoplayOption =
                "window.SauceClipCollectionLib.setCurationClipPreviewAutoplay(false)"
        }

        if (stageMode) {
            webView.loadData(
                """<!DOCTYPE html>
<html lang="en">
<head>
  <script src="https://stage.showcase.sauceclip.com/static/js/SauceClipCollectionLib.js"></script>
</head>
<body>
  <div id="sauce_clip_curation_sdk_aos"></div>
  <script>
    window.addEventListener('load', () => {
      const partnerId = '$partnerId'
      window.SauceClipCollectionLib.setInit({ partnerId })
      $pvOption
      $paddingOption
      $previewAutoplayOption
      window.SauceClipCollectionLib.loadCuration({ curationId: '$curatioinId', elementId: 'sauce_clip_curation_sdk_aos' })
    })
  </script>
</body>
<style>
  html, body {
    padding: 0;
    margin: 0;
    height: fit-content;
  }
</style>
</html>""", "text/html", "UTF-8"
            )
        } else {
            webView.loadData(
                """<!DOCTYPE html>
<html lang="en">
<head>
  <script src="https://showcase.sauceclip.com/static/js/SauceClipCollectionLib.js"></script>
</head>
<body>
  <div id="sauce_clip_curation_sdk_aos"></div>
  <script>
    window.addEventListener('load', () => {
      const partnerId = '$partnerId'
      window.SauceClipCollectionLib.setInit({ partnerId })
      $pvOption
      $paddingOption
      $previewAutoplayOption
      window.SauceClipCollectionLib.loadCuration({ curationId: '$curatioinId', elementId: 'sauce_clip_curation_sdk_aos' })
    })
  </script>
</body>
<style>
  html, body {
    padding: 0;
    margin: 0;
    height: fit-content;
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

    fun setOnCollectionError(callback: ((message: SauceErrorInfo) -> Unit)?) {
        webView.removeJavascriptInterface("sauceclipCollectionError")
        if (callback != null) {
            webView.addJavascriptInterface(
                SauceclipOnCollectionErrorJavaScriptInterface { errorInfo ->
                    callback.invoke(errorInfo)
                },
                "sauceclipCollectionError"
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

    private class SauceclipOnCollectionErrorJavaScriptInterface(
        val sauceflexOnCollectionError: ((message: SauceErrorInfo) -> Unit)
    ) {
        private val handler = Handler()

        @JavascriptInterface   // 클립 이동
        fun sauceclipCollectionError(message: String) {
            val gson = Gson()
            val errorInfo = gson.fromJson(message, SauceErrorInfo::class.java)
            handler.post {
                sauceflexOnCollectionError.invoke(errorInfo)
            }
        }
    }

}