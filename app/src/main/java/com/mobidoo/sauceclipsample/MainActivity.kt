package com.mobidoo.sauceclipsample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private lateinit var mContext: Context
    private lateinit var editText: EditText
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        mContext = this
        editText = findViewById(R.id.editText)
        btnNext = findViewById(R.id.btnNext)

        editText.setText("https://stage.player.sauceclip.com/player?partnerId=1&clipId=8")
        editText.setHint("Enter URL")
        editText.inputType = InputType.TYPE_CLASS_TEXT
        editText.maxLines = 1



        btnNext.setOnClickListener {
            val url = editText.text.toString()
            val intent = Intent(mContext, SecondActivity::class.java)
            intent.putExtra("linkUrl", url)
            startActivity(intent)
        }
    }
}
