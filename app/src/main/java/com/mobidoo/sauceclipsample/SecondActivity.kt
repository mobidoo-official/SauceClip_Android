package com.mobidoo.sauceclipsample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast

class SecondActivity : Activity() {
    private lateinit var mContext: Context
    private lateinit var btnSauceviewActivity: Button
    private lateinit var btnCurationviewActivity: Button

    private lateinit var onEnter: CheckBox
    private lateinit var onMoveExit: CheckBox
    private lateinit var onMoveLogin: CheckBox
    private lateinit var onShare: CheckBox
    private lateinit var onMoveProduct: CheckBox
    private lateinit var onMoveCart: CheckBox
    private lateinit var onMoveBroadcast: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        init()
    }

    fun init() {
        mContext = this
        btnSauceviewActivity = findViewById(R.id.btn_sauceview_activity)
        btnCurationviewActivity = findViewById(R.id.btn_curationview_activity)

        onEnter = findViewById(R.id.check_Enter)
        onMoveExit = findViewById(R.id.check_MoveExit)
        onMoveLogin = findViewById(R.id.check_MoveLogin)
        onShare = findViewById(R.id.check_OnShare)
        onMoveProduct = findViewById(R.id.check_MoveProduct)
        onMoveCart = findViewById(R.id.check_OnMoveCart)
        onMoveBroadcast = findViewById(R.id.check_MoveBroadcast)

        btnSauceviewActivity.setOnClickListener() {
            val intent = Intent(this, SauceViewActivity::class.java)
            SauceViewActivity.onEnter = onEnter.isChecked
            SauceViewActivity.onMoveExit = onMoveExit.isChecked
            SauceViewActivity.onMoveLogin = onMoveLogin.isChecked
            SauceViewActivity.onShare = onShare.isChecked
            SauceViewActivity.onMoveProduct = onMoveProduct.isChecked
            SauceViewActivity.onMoveCart = onMoveCart.isChecked
            startActivity(intent)
        }

        btnCurationviewActivity.setOnClickListener() {
            val intent = Intent(this, SauceCurationViewActivity::class.java)
            SauceCurationViewActivity.onMoveBroadcast = onMoveBroadcast.isChecked
            startActivity(intent)
        }

    }
}