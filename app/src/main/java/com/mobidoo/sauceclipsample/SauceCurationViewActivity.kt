package com.mobidoo.sauceclipsample

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.mobidoo.sauceclip.SauceClip
import com.mobidoo.sauceclip.SauceCurationView

class SauceCurationViewActivity : Activity() {

    private lateinit var curationView: SauceCurationView
    private lateinit var sampleText: TextView
    private lateinit var sampleText2: TextView

    private var partnerId: String = ""
    private var curationId: String = ""
    private var stageMode: Boolean = true
    private var devMode: Boolean = false

    companion object {
        var onMoveBroadcast: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curationview)

        partnerId = intent.getStringExtra("partnerId") ?: ""
        curationId = intent.getStringExtra("curationId") ?: ""
        stageMode = intent.getBooleanExtra("stageMode", true)
        devMode = intent.getBooleanExtra("devMode", false)

        init()
    }

    private fun init() {
        curationView = findViewById(R.id.curation)
        curationView.setInit(partnerId, curationId)
        curationView.setStageMode(stageMode)
        curationView.setPvVisibility(false)
        curationView.setHorizontalPadding(10)
        curationView.setPreviewAutoplay(true)
        curationView.load()

        sampleText = findViewById(R.id.sample_text)
        sampleText.text = """<com.mobidoo.sauceclip.SauceCurationView
            android:id="@+id/curation"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>"""

        sampleText2 = findViewById(R.id.sample_text2)
        sampleText2.text = """YourCurationView.setInit("partner id", "curation id")
            YourCurationView.load()
        """.trimMargin()

        if (onMoveBroadcast) {
            curationView.setOnMoveBroadcast { message ->
                SauceClip.openClipActivity(
                    this,
                    message.partnerId,
                    "${message.clipId}",
                    "${message.curationId}",
                    true,
                    stageMode,
                    devMode,
                    {
//                        it.pipOn()
                    },
                    { it.finish() },
                    null,
                    { productInfo, clipActivity ->
                        clipActivity.pipOn()
                    },
                    { clipActivity -> },
                    { cartInfo, clipActivity -> },
                    { errorInfo ->

                    }
                )
            }

            curationView.setOnCollectionError { message ->

            }
        }

    }

}