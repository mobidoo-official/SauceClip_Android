package com.mobidoo.sauceclipsample

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.mobidoo.sauceclip.SauceClipView

class SauceViewActivity : Activity() {

    private lateinit var linkUrl: String
    private lateinit var sauceview: SauceClipView
    private lateinit var sampleText: TextView
    private lateinit var sampleText2: TextView

    companion object {
        var onEnter: Boolean = false
        var onMoveExit: Boolean = false
        var onMoveLogin: Boolean = false
        var onShare: Boolean = false
        var onMoveProduct: Boolean = false
        var onMoveCart: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sauceview)
        linkUrl = intent.getStringExtra("linkUrl") ?: ""
        init()
    }

    private fun init() {
        sauceview = findViewById(R.id.sauceclip)
        sauceview.loadUrl(linkUrl)

        sampleText = findViewById(R.id.sample_text)
        sampleText.text = """<com.mobidoo.sauceclip.SauceClipView
            android:id="@+id/sauceclip"
            android:layout_width="180dp"
            android:layout_height="320dp"/>"""

        sampleText2 = findViewById(R.id.sample_text2)
        sampleText2.text = "YourSauceView.loadUrl(\"live URL\")"


        if (onEnter){
            sauceview.setOnEnterListener { Toast.makeText(this, "onEnter", Toast.LENGTH_SHORT).show() }
        }

        if (onMoveExit){
            sauceview.setOnMoveExitListener { Toast.makeText(this, "onMoveExit", Toast.LENGTH_SHORT).show() }
        }

        if (onMoveLogin){
            sauceview.setOnMoveLoginListener { Toast.makeText(this, "onMoveLogin", Toast.LENGTH_SHORT).show() }
        }

        if (onShare){
            sauceview.setOnShareListener { message -> Toast.makeText(this, "onShare", Toast.LENGTH_SHORT).show() }
        }

        if (onMoveProduct){
            sauceview.setOnMoveProductListener { message -> Toast.makeText(this, "onMoveProduct", Toast.LENGTH_SHORT).show() }
        }

        if (onMoveCart){
            sauceview.setOnMoveCartListener { message -> Toast.makeText(this, "onMoveCart", Toast.LENGTH_SHORT).show() }
        }

    }

}