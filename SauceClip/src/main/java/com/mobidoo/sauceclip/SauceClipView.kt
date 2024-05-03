package com.mobidoo.sauceclip

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import com.google.gson.Gson

class SauceClipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val webView: WebView
    private var partnerId: String? = null
    private var clipId: String? = null
    private var curationId: String? = null
    private var stageMode: Boolean = false
    private var openProductActivity: Boolean = true
    private lateinit var mContext: Context

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

        mContext = context
    }

    fun setInit(partnerId: String, clipId: String, curationId: String? = null) {
        this.partnerId = partnerId
        this.clipId = clipId
        this.curationId = curationId
    }

    fun setStageMode(on: Boolean) {
        stageMode = on
    }

    fun setProductActivity(on: Boolean) {
        openProductActivity = on
    }

    fun load() {

        if (partnerId == null) {
            throw Error("partnerId is required")
        }

        if (clipId == null) {
            throw Error("clipId is required")
        }

        webView.addJavascriptInterface(
            SauceclipMoveProductJavaScriptInterface { productInfo ->
                if (openProductActivity) {
                    val intent = Intent(mContext, WebActivity::class.java)
                    intent.putExtra("link", productInfo.linkUrl)
                    mContext.startActivity(intent)
                }
            },
            "sauceclipMoveProduct"
        )

        var url = ""

        if (stageMode) {
            url = "https://stage.player.sauceclip.com/player?partnerId=$partnerId&clipId=$clipId"
        } else {
            url = "https://player.sauceclip.com/player?partnerId=$partnerId&clipId=$clipId"
        }

        if (curationId != null) {
            url += "&curationId=$curationId"
        }

        webView.loadUrl(url)
    }

    fun setOnEnterListener(callback: (() -> Unit)?) {
        webView.removeJavascriptInterface("sauceclipEnter")
        if (callback != null) {
            webView.addJavascriptInterface(
                SauceclipEnterJavaScriptInterface(callback),
                "sauceclipEnter"
            )
        }
    }

    fun setOnMoveExitListener(callback: (() -> Unit)?) {
        webView.removeJavascriptInterface("sauceclipMoveExit")
        if (callback != null) {
            webView.addJavascriptInterface(
                SauceclipMoveExitJavaScriptInterface(callback),
                "sauceclipMoveExit"
            )
        }
    }

    fun setOnShareListener(callback: ((message: SauceShareInfo) -> Unit)?) {
        webView.removeJavascriptInterface("sauceclipOnShare")
        if (callback != null) {
            webView.addJavascriptInterface(
                SauceclipOnShareJavaScriptInterface(callback),
                "sauceclipOnShare"
            )
        }
    }

    fun setOnMoveProductListener(callback: ((message: SauceProductInfo) -> Unit)?) {
        webView.removeJavascriptInterface("sauceclipMoveProduct")
        webView.addJavascriptInterface(
            SauceclipMoveProductJavaScriptInterface { productInfo ->
                callback?.invoke(productInfo)
                if (openProductActivity) {
                    val intent = Intent(mContext, WebActivity::class.java)
                    intent.putExtra("link", productInfo.linkUrl)
                    mContext.startActivity(intent)
                }
            },
            "sauceclipMoveProduct"
        )
    }

    fun setOnMoveCartListener(callback: ((message: SauceCartInfo) -> Unit)?) {
        webView.removeJavascriptInterface("sauceclipMoveCart")
        if (callback != null) {
            webView.addJavascriptInterface(
                SauceclipMoveCartJavaScriptInterface(callback),
                "sauceclipMoveCart"
            )
        }
    }

    fun setOnErrorListener(callback: ((message: SauceErrorInfo) -> Unit)?) {
        webView.removeJavascriptInterface("sauceclipPlayerError")
        if (callback != null) {
            webView.addJavascriptInterface(
                SauceclipOnErrorJavaScriptInterface(callback),
                "sauceclipPlayerError"
            )
        }
    }

    private class SauceclipEnterJavaScriptInterface(
        val sauceflexEnter: (() -> Unit),
    ) {
        private val handler = Handler()

        @JavascriptInterface   // 처음 플레이어 진입시
        fun sauceclipEnter() {
            handler.post {
                sauceflexEnter.invoke()
            }
        }
    }

    private class SauceclipMoveExitJavaScriptInterface(
        val sauceflexMoveExit: (() -> Unit)
    ) {
        private val handler = Handler()

        @JavascriptInterface   // 닫기 버튼 눌러 팝업에서 나가기시
        fun sauceclipMoveExit() {
            handler.post {
                sauceflexMoveExit.invoke()
            }
        }
    }

    private class SauceclipMoveProductJavaScriptInterface(
        val sauceflexOnShare: ((message: SauceProductInfo) -> Unit)
    ) {
        private val handler = Handler()

        @JavascriptInterface   // 공유하기
        fun sauceclipMoveProduct(message: String) {
            val gson = Gson()
            val productInfo = gson.fromJson(message, SauceProductInfo::class.java)
            handler.post {
                sauceflexOnShare.invoke(productInfo)
            }
        }
    }

    private class SauceclipMoveCartJavaScriptInterface(
        val sauceflexOnShare: ((message: SauceCartInfo) -> Unit)
    ) {
        private val handler = Handler()

        @JavascriptInterface   // 공유하기
        fun sauceclipMoveCart(message: String) {
            val gson = Gson()
            val cartInfo = gson.fromJson(message, SauceCartInfo::class.java)
            handler.post {
                sauceflexOnShare.invoke(cartInfo)
            }
        }
    }

    private class SauceclipOnShareJavaScriptInterface(
        val sauceflexOnShare: ((message: SauceShareInfo) -> Unit)
    ) {
        private val handler = Handler()

        @JavascriptInterface   // 공유하기
        fun sauceclipOnShare(message: String) {
            val gson = Gson()
            val shareInfo = gson.fromJson(message, SauceShareInfo::class.java)
            handler.post {
                sauceflexOnShare.invoke(shareInfo)
            }
        }
    }

    private class SauceclipOnErrorJavaScriptInterface(
        val sauceflexOnMovebroadcast: ((message: SauceErrorInfo) -> Unit)
    ) {
        private val handler = Handler()

        @JavascriptInterface   // 클립 이동
        fun sauceclipPlayerError(message: String) {
            val gson = Gson()
            val sauceclipErrorInfo = gson.fromJson(message, SauceErrorInfo::class.java)
            handler.post {
                sauceflexOnMovebroadcast.invoke(sauceclipErrorInfo)
            }
        }
    }

}