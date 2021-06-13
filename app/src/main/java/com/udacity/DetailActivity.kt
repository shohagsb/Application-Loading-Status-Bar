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

        Log.d("DetailTAG", "onCreate: file name: $fileName\n Status: $status")
        file_name_text.text = fileName
        status_text.text = status
    }

}
