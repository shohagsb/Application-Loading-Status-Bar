package com.udacity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        val fileName = intent.getStringExtra("file_name")
        val status = intent.getStringExtra("status")
        if (fileName != null && status != null) {
            file_name_text.text = fileName.toString()
            status_text.text = status.toString()
        }

        ok_button.setOnClickListener {
            onBackPressed()
        }
    }

}
