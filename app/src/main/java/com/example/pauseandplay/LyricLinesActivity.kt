package com.example.pauseandplay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LyricLinesActivity : BaseActivity() {

    private lateinit var song : Song
    var mode = -1
    var icon:Int = -1
    var master:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyric_lines)

        val mToolbar = findViewById<View>(R.id.toolbar) as Toolbar

        setSupportActionBar(mToolbar)
        supportActionBar?.title = "Full lyrics of the song"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras

        if (extras == null) {
            Log.d(TAG, "Oops, the bundle was empty")
            return
        }

        mode = extras.getInt("mode")

        if (mode == CLASSIC_MODE) {
            icon = R.drawable.pause_master
            master = "Pause"
        }
        else if (mode == CURRENT_MODE) {
            icon = R.drawable.play_master
            master = "Play"
        }

        song = extras.getSerializable("song") as Song

        var imageModelArrayList: ArrayList<LyricLine> = song.lyricLines

        val recyclerView = findViewById<View>(R.id.my_recycler_view) as RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val mAdapter = MyAdapter(imageModelArrayList)
        recyclerView.adapter = mAdapter


    }

}
