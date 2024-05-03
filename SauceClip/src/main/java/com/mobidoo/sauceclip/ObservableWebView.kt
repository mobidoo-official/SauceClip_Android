package com.mobidoo.sauceclip

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.webkit.WebView

internal class ObservableWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {
    private var isScrolling = false
    private var initialPosition: Int = 0
    private val checkDelay: Long = 300
    private lateinit var scrollerTask: Runnable

    init {
        scrollerTask = Runnable {
            val newPosition = scrollX
            isScrolling = if (initialPosition - newPosition == 0) {
                false // 스크롤이 멈춘 경우
            } else {
                initialPosition = scrollX
                postDelayed(scrollerTask, checkDelay)
                true // 스크롤 중인 경우
            }
        }
        overScrollMode = OVER_SCROLL_NEVER
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (!isScrolling) {
            initialPosition = scrollX
            postDelayed(scrollerTask, checkDelay)
            isScrolling = true
        }
    }

    fun isCurrentlyScrolling(): Boolean = isScrolling
}
