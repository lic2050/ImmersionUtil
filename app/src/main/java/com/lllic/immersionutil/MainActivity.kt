package com.lllic.immersionutil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lllic.immersion.immersive

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.immersive()
        setContentView(R.layout.activity_main)

    }
}