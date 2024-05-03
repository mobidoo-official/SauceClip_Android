package com.mobidoo.sauceclipsample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private lateinit var mContext: Context
    private lateinit var partnerIdEdit: EditText
    private lateinit var clipIdEdit: EditText
    private lateinit var curationIdEdit: EditText
    private lateinit var btnNext: Button
    private lateinit var stageModeCheck: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        mContext = this
        partnerIdEdit = findViewById(R.id.partner_id_edit)
        clipIdEdit = findViewById(R.id.clip_id_edit)
        curationIdEdit = findViewById(R.id.curation_id_edit)
        stageModeCheck = findViewById(R.id.check_stage)
        btnNext = findViewById(R.id.btnNext)

        partnerIdEdit.setHint("Enter Partner ID")
        partnerIdEdit.inputType = InputType.TYPE_CLASS_NUMBER
        partnerIdEdit.maxLines = 1

        clipIdEdit.setHint("Enter Clip ID")
        clipIdEdit.inputType = InputType.TYPE_CLASS_NUMBER
        clipIdEdit.maxLines = 1

        curationIdEdit.setHint("Enter Curation ID")
        curationIdEdit.inputType = InputType.TYPE_CLASS_NUMBER
        curationIdEdit.maxLines = 1


        btnNext.setOnClickListener {
            if (partnerIdEdit.text.toString().isNotBlank()) {
                val partnerId = partnerIdEdit.text.toString()
                val clipId = clipIdEdit.text.toString()
                val curationId = curationIdEdit.text.toString()
                val intent = Intent(mContext, SecondActivity::class.java)
                intent.putExtra("partnerId", partnerId)
                intent.putExtra("clipId", clipId)
                intent.putExtra("curationId", curationId)
                intent.putExtra("stageMode", stageModeCheck.isChecked)
                startActivity(intent)
            }else {
                Toast.makeText(mContext, "Please enter Partner ID", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
