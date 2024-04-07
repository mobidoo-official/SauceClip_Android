package com.mobidoo.sauceclip

import android.app.Activity
import android.os.Bundle

class SauceClipActivity: Activity() {
    private var partnerId: String = ""
    private var clipId: String = ""
    private var stageMode: Boolean = false
    private var openProductActivity: Boolean = true
    private lateinit var sauceclipView: SauceClipView

    companion object {
        var sauceclipEnter: (() -> Unit)? = null
        var sauceclipMoveExit: (() -> Unit)? = null
        var sauceclipOnShare: ((message: SauceShareInfo) -> Unit)? = null
        var sauceclipMoveProduct: ((message: SauceProductInfo) -> Unit)? = null
        var sauceclipMoveCart: ((message: SauceCartInfo) -> Unit)? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sauceclip)
        partnerId = intent.getStringExtra("partnerId") ?: ""
        clipId = intent.getStringExtra("clipId") ?: ""
        stageMode = intent.getBooleanExtra("stageMode", false)
        openProductActivity = intent.getBooleanExtra("openProductActivity", true)
        init()
    }

    private fun init() {
        sauceclipView = findViewById(R.id.sauceclip)
        sauceclipView.setInit(partnerId, clipId)
        sauceclipView.setStageMode(stageMode)
        sauceclipView.setProductActivity(openProductActivity)
        sauceclipView.load()

        initPlayerCallback()
    }

    private fun initPlayerCallback() {
        sauceclipView.setOnEnterListener(sauceclipEnter)
        sauceclipView.setOnMoveExitListener(sauceclipMoveExit)
        sauceclipView.setOnShareListener(sauceclipOnShare)
        sauceclipView.setOnMoveProductListener(sauceclipMoveProduct)
        sauceclipView.setOnMoveCartListener(sauceclipMoveCart)
    }

}