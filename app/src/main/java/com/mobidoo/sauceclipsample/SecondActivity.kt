package com.mobidoo.sauceclipsample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.mobidoo.sauceclip.SauceClip

class SecondActivity : Activity() {
    private lateinit var mContext: Context
    private lateinit var btnSauceviewActivity: Button
    private lateinit var btnSauceactivityActivity: Button
    private lateinit var btnCurationviewActivity: Button

    private var partnerId: String = ""
    private var clipId: String? = null
    private var curationId: String? = null
    private var stageMode: Boolean = true
    private var devMode: Boolean = false

    private lateinit var onEnter: CheckBox
    private lateinit var onMoveExit: CheckBox
    private lateinit var onMoveLogin: CheckBox
    private lateinit var onShare: CheckBox
    private lateinit var onMoveProduct: CheckBox
    private lateinit var onMoveCart: CheckBox
    private lateinit var onAddCart: CheckBox
    private lateinit var onMoveBroadcast: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        partnerId = intent.getStringExtra("partnerId") ?: ""
        clipId = intent.getStringExtra("clipId")
        curationId = intent.getStringExtra("curationId")
        stageMode = intent.getBooleanExtra("stageMode", true)
        devMode = intent.getBooleanExtra("devMode", false)

        init()
    }

    fun init() {
        mContext = this
        btnSauceviewActivity = findViewById(R.id.btn_sauceview_activity)
        btnCurationviewActivity = findViewById(R.id.btn_curationview_activity)
        btnSauceactivityActivity = findViewById(R.id.btn_sauceactivity_activity)

        onEnter = findViewById(R.id.check_Enter)
        onMoveExit = findViewById(R.id.check_MoveExit)
        onMoveLogin = findViewById(R.id.check_MoveLogin)
        onShare = findViewById(R.id.check_OnShare)
        onMoveProduct = findViewById(R.id.check_MoveProduct)
        onMoveCart = findViewById(R.id.check_OnMoveCart)
        onAddCart = findViewById(R.id.check_OnAddCart)
        onMoveBroadcast = findViewById(R.id.check_MoveBroadcast)

        btnSauceviewActivity.setOnClickListener() {
            if (clipId != null && clipId!!.isNotBlank()) {
                val intent = Intent(this, SauceViewActivity::class.java)
                SauceViewActivity.onEnter = onEnter.isChecked
                SauceViewActivity.onMoveExit = onMoveExit.isChecked
                SauceViewActivity.onMoveLogin = onMoveLogin.isChecked
                SauceViewActivity.onShare = onShare.isChecked
                SauceViewActivity.onMoveProduct = onMoveProduct.isChecked
                SauceViewActivity.onMoveCart = onMoveCart.isChecked
                SauceViewActivity.onAddCart = onAddCart.isChecked
                intent.putExtra("partnerId", partnerId)
                intent.putExtra("clipId", clipId)
                intent.putExtra("curationId", curationId)
                intent.putExtra("stageMode", stageMode)
                intent.putExtra("devMode", devMode)
                startActivity(intent)
            } else {
                Toast.makeText(mContext, "clipId is null", Toast.LENGTH_SHORT).show()
            }

        }

        btnSauceactivityActivity.setOnClickListener {
            if (clipId != null && clipId!!.isNotBlank()) {
                SauceClip.openClipActivity(
                    mContext,
                    partnerId,
                    clipId!!,
                    if (curationId != null && curationId!!.isNotBlank()) curationId else null,
                    true,
                    stageMode,
                    devMode,
                    if (onEnter.isChecked) {
                        {
                            Toast.makeText(mContext, "onEnter", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        null
                    },
                    if (onMoveExit.isChecked) {
                        {
                            it.finish()
                            Toast.makeText(mContext, "onMoveExit", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        null
                    },
                    if (onShare.isChecked) {
                        {
                            Toast.makeText(mContext, "onShare", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        null
                    },
                    if (onMoveProduct.isChecked) {
                        { productInfo, clipActivity ->
                            Toast.makeText(mContext, "onMoveProduct", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        null
                    },
                    if (onMoveCart.isChecked) {
                        { clipActivity ->
                            Toast.makeText(mContext, "onMoveCart", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        null
                    },
                    if (onAddCart.isChecked) {
                        { cartInfo, clipActivity ->
                            Toast.makeText(mContext, "onAddCart", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        null
                    },
                    {
                        Toast.makeText(mContext, "onError", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(mContext, "clipId is null", Toast.LENGTH_SHORT).show()
            }
        }

        btnCurationviewActivity.setOnClickListener() {
            if (curationId != null && curationId!!.isNotBlank()) {
                val intent = Intent(this, SauceCurationViewActivity::class.java)
                SauceCurationViewActivity.onMoveBroadcast = onMoveBroadcast.isChecked
                intent.putExtra("partnerId", partnerId)
                intent.putExtra("curationId", curationId)
                intent.putExtra("stageMode", stageMode)
                intent.putExtra("devMode", devMode)
                startActivity(intent)
            } else {
                Toast.makeText(mContext, "curationId is null", Toast.LENGTH_SHORT).show()
            }
        }

    }
}