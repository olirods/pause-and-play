package com.example.pauseandplay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_who_said_that.*

class WhoSaidThatActivity : BaseActivity() {

    var mode = -1
    var lineID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_who_said_that)

        val mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        val masterImageView = findViewById<View>(R.id.imageView) as ImageView

        setSupportActionBar(mToolbar)
        supportActionBar?.title = "Who Said That?"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras

        if (extras == null) {
            Log.d(TAG, "Oops, the bundle was empty")
            return
        }

        mode = extras.getInt("mode")
        lineID = extras.getInt("lineID")

        if (mode == CLASSIC_MODE)
            masterImageView.setImageResource(R.drawable.pause_master)
        else if (mode == CURRENT_MODE)
            masterImageView.setImageResource(R.drawable.play_master)
    }
}
