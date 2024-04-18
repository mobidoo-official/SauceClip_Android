package com.mobidoo.sauceclip

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
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
    private var pvVisibility: Boolean = true
    private var horizontalPadding: Int = 0
    private var previewAutoplay: Boolean = false
    private var lastX = 0f
    private var lastY = 0f
    private var deltaX = 0f
    private var deltaY = 0f

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

        /// 웹뷰에서 가로 스크롤이 있을 경우 가로 스크롤중 상위의 스크롤뷰에서 세로스크롤이 발생하지 않도록 설정
        webView.setOnTouchListener { v, ev -> when (ev.getAction()) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = ev.getX()
                    lastY = ev.getY()
                    // ACTION_DOWN에서는 기본적으로 이벤트를 가로채지 않음
                    v.parent.requestDisallowInterceptTouchEvent(false)
                    false
                }

                MotionEvent.ACTION_MOVE -> {
                    deltaX = Math.abs(ev.getX() - lastX)
                    deltaY = Math.abs(ev.getY() - lastY)

                    // X축 이동이 Y축 이동보다 클 때 true를 반환하여 수평 스크롤 이벤트를 가로챈다
                    v.parent.requestDisallowInterceptTouchEvent(deltaX > deltaY)
                    deltaX > deltaY
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
  <div id="sauce_clip_curation"></div>
  <script>
    window.addEventListener('load', () => {
      const partnerId = '$partnerId'
      window.SauceClipCollectionLib.setInit({ partnerId })
      $pvOption
      $paddingOption     
      $previewAutoplayOption
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
        } else {
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
      $pvOption
      $paddingOption
      $previewAutoplayOption
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