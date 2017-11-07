package com.tomerrosenfeld.lost.demo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    public fun lostForm(v: View) {
        startActivity(Intent(this, FormActivity::class.java))
    }

    public fun foundForm(v: View) {
        startActivity(Intent(this, FoundFormActivity::class.java))
    }
}
