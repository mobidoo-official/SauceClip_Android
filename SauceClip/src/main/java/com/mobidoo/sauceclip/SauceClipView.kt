package com.mobidoo.sauceclip

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout

class SauceClipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val webView: WebView
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

    }

    // 웹뷰에 로드할 URL을 설정하는 메서드
    fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

//    fun hidePlayerUi() {
//        webView.evaluateJavascript(
//            "window.postMessage(\"sauceflexPictureInPictureOn\", \"*\");",
//            null
//        )
//    }

//    fun showPlayerUi() {
//        webView.evaluateJavascript(
//            "window.postMessage(\"sauceflexPictureInPictureOff\", \"*\");",
//            null
//        )
//    }

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

    fun setOnMoveLoginListener(callback: (() -> Unit)?) {
        webView.removeJavascriptInterface("sauceclipMoveLogin")
        if (callback != null) {
            webView.addJavascriptInterface(
                SauceclipMoveLoginJavaScriptInterface(callback),
                "sauceclipMoveLogin"
            )
        }
    }

    fun setOnShareListener(callback: ((message: String) -> Unit)?) {
        webView.removeJavascriptInterface("sauceclipOnShare")
        if (callback != null) {
            webView.addJavascriptInterface(
                SauceclipOnShareJavaScriptInterface(callback),
                "sauceclipOnShare"
            )
        }
    }

    fun setOnMoveProductListener(callback: ((message: String) -> Unit)?) {
        webView.removeJavascriptInterface("sauceclipMoveProduct")
        if (callback != null) {
            webView.addJavascriptInterface(
                SauceclipMoveProductJavaScriptInterface(callback),
                "sauceclipMoveProduct"
            )
        }
    }

    fun setOnMoveCartListener(callback: ((message: String) -> Unit)?) {
        webView.removeJavascriptInterface("sauceclipMoveCart")
        if (callback != null) {
            webView.addJavascriptInterface(
                SauceclipMoveCartJavaScriptInterface(callback),
                "sauceclipMoveCart"
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

    private class SauceclipMoveLoginJavaScriptInterface(
        val sauceflexMoveLogin: (() -> Unit)
    ) {
        private val handler = Handler()

        @JavascriptInterface   // 로그인 팝업에서 확인시
        fun sauceclipMoveLogin() {
            handler.post {
                sauceflexMoveLogin.invoke()
            }
        }
    }

    private class SauceclipMoveProductJavaScriptInterface(
        val sauceflexOnShare: ((message: String) -> Unit)
    ) {
        private val handler = Handler()

        @JavascriptInterface   // 공유하기
        fun sauceclipMoveProduct(message: String) {
            handler.post {
                sauceflexOnShare.invoke(message)
            }
        }
    }

    private class SauceclipMoveCartJavaScriptInterface(
        val sauceflexOnShare: ((message: String) -> Unit)
    ) {
        private val handler = Handler()

        @JavascriptInterface   // 공유하기
        fun sauceclipMoveCart(message: String) {
            handler.post {
                sauceflexOnShare.invoke(message)
            }
        }
    }

    private class SauceclipOnShareJavaScriptInterface(
        val sauceflexOnShare: ((message: String) -> Unit)
    ) {
        private val handler = Handler()

        @JavascriptInterface   // 공유하기
        fun sauceclipOnShare(message: String) {
            handler.post {
                sauceflexOnShare.invoke(message)
            }
        }
    }


}