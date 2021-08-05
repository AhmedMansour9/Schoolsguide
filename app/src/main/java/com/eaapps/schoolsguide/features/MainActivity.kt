package com.eaapps.schoolsguide.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eaapps.schoolsguide.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}