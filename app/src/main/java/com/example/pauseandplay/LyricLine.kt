package com.example.pauseandplay

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import java.io.Serializable
import kotlin.random.Random

class LyricLine : Serializable{
    val id: Int
    var latitude = 0.0
    var longitude = 0.0
    var markerID: Int = -1
    var inTheMap: Boolean = false
    var got: Boolean = false
    var text: String

    constructor(line: String, id: Int) {
        this.text = line
        this.id = id
    }

    fun getNames(): String {
        if (got)
            return text
        else {
            var unknownContent = StringBuilder()
            for (i in 1..text.length)
                unknownContent.append("x")
            return unknownContent.toString()
        }
    }

    fun getImage_drawables(): Int {
        if (got)
            return R.drawable.line
        else
            return R.drawable.baseline_help_24
    }

}