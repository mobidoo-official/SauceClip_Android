package com.mobidoo.sauceclipsample

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.mobidoo.sauceclip.SauceClipView
import com.mobidoo.sauceclip.SauceCurationView

class SauceCurationViewActivity : Activity() {

    private lateinit var curationView: SauceCurationView
    private lateinit var sampleText: TextView
    private lateinit var sampleText2: TextView

    companion object {
        var onMoveBroadcast: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curationview)
        init()
    }

    private fun init() {
        curationView = findViewById(R.id.curation)
        curationView.setInit("8", "99")
        curationView.setStageMode(true)
        curationView.load()

        sampleText = findViewById(R.id.sample_text)
        sampleText.text = """<com.mobidoo.sauceclip.SauceClipView
            android:id="@+id/sauceclip"
            android:layout_width="180dp"
            android:layout_height="320dp"/>"""

        sampleText2 = findViewById(R.id.sample_text2)
        sampleText2.text = "YourSauceView.loadUrl(\"live URL\")"

        if (onMoveBroadcast) {
            curationView.setOnMoveBroadcast { message ->
                Toast.makeText(
                    this,
                    "onMoveBroadcast",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

}