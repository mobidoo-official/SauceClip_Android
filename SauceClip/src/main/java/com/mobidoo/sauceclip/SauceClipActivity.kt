package com.mobidoo.sauceclip

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import androidx.annotation.RequiresApi

class SauceClipActivity: Activity() {
    // 파트너 ID
    private var partnerId: String = ""
    // 클립 ID
    private var clipId: String = ""
    // 큐레이션 ID
    private var curationId: String? = null
    // 스테이지 모드 여부
    private var stageMode: Boolean = false
    // 제품 활동 열기 여부
    private var openProductActivity: Boolean = true
    // SauceClipView 인스턴스
    private lateinit var sauceclipView: SauceClipView

    companion object {
        var sauceclipEnter: ((clipActivity: SauceClipActivity) -> Unit)? = null
        var sauceclipMoveExit: ((clipActivity: SauceClipActivity) -> Unit)? = null
        var sauceclipOnShare: ((message: SauceShareInfo) -> Unit)? = null
        var sauceclipMoveProduct: ((message: SauceProductInfo, clipActivity: SauceClipActivity) -> Unit)? = null
        var sauceclipMoveCart: ((message: SauceCartInfo, clipActivity: SauceClipActivity) -> Unit)? = null
        var sauceclipError: ((message: SauceErrorInfo) -> Unit)? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sauceclip)
        partnerId = intent.getStringExtra("partnerId") ?: ""
        clipId = intent.getStringExtra("clipId") ?: ""
        curationId = intent.getStringExtra("curationId") ?: null
        stageMode = intent.getBooleanExtra("stageMode", false)
        openProductActivity = intent.getBooleanExtra("openProductActivity", true)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        init()
    }

    private fun init() {
        sauceclipView = findViewById(R.id.sauceclip)
        sauceclipView.setInit(partnerId, clipId, curationId)
        sauceclipView.setStageMode(stageMode)
        sauceclipView.setProductActivity(openProductActivity)
        sauceclipView.load()

        initPlayerCallback()
    }

    // 플레이어 콜백 초기화
    private fun initPlayerCallback() {
        sauceclipView.setOnEnterListener {
            sauceclipEnter?.invoke(this)
        }
        sauceclipView.setOnMoveExitListener {
            sauceclipMoveExit?.invoke(this)
        }
        sauceclipView.setOnShareListener(sauceclipOnShare)
        sauceclipView.setOnMoveProductListener{sauceProductInfo ->
            sauceclipMoveProduct?.invoke(sauceProductInfo, this)
        }
        sauceclipView.setOnMoveCartListener{sauceCartInfo ->
            sauceclipMoveCart?.invoke(sauceCartInfo, this)
        }
        sauceclipView.setOnErrorListener(sauceclipError)
    }

    // PIP 모드 활성화
    @RequiresApi(Build.VERSION_CODES.O)
    fun pipOn() {
        var pipWidth = 9
        var pipHeight = 16

        var pipBuilder = PictureInPictureParams.Builder()
        pipBuilder.setAspectRatio(Rational(pipWidth, pipHeight))

        enterPictureInPictureMode(pipBuilder.build())
    }

    // PIP 모드 변경 시 UI 변경
    @Deprecated("Deprecated in Java")
    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        if (isInPictureInPictureMode) {
            sauceclipView.hidePlayerUi()
        } else {
            sauceclipView.showPlayerUi()
        }
    }

}