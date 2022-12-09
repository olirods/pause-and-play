package com.example.pauseandplay

import android.content.Context
import android.content.res.AssetManager
import com.google.android.gms.maps.model.LatLng
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class Song : Serializable {
    var artist: String = ""
    var title: String = ""
    val lyricLines: ArrayList<LyricLine> = arrayListOf<LyricLine>()

    constructor(assets: AssetManager, mode: Int) {
        var path: String = "songs"

        if (Locale.getDefault().language == "es")
            path += "-es"

        if (mode == CURRENT_MODE)
            path += "/current"
        else if (mode == CLASSIC_MODE)
            path += "/classic"

        val assetsFiles = assets.list(path)
        val arrayOfSongsFilenames: ArrayList<String> = arrayListOf<String>()

        if (assetsFiles != null)
            for (file in assetsFiles)
                if (file.endsWith("txt"))
                    arrayOfSongsFilenames.add(file)

        val songFilename = arrayOfSongsFilenames[Random.nextInt(0, arrayOfSongsFilenames.size - 1)]

        val artistList = songFilename.split("(")[0].split("_")
        val titleList = songFilename.split("(")[1].split(")")[0].split("_")

        for (word in artistList) {
            artist = artist + " " + word.toUpperCase()
        }

        artist = artist.substring(1)

        for (word in titleList) {
            title = title + " " + word.toUpperCase()
        }

        title = title.substring(1)

        var inputStreamReader = InputStreamReader(assets.open(path + "/" + songFilename))
        val bufferedReader = BufferedReader(inputStreamReader)
        var text: String? = null

        val lyricStrings: ArrayList<String> = arrayListOf<String>()

        while ({ text = bufferedReader.readLine(); text }() != null) {
            if (text != "" && text != "\n")
                lyricStrings.add(text.toString())
        }

        for (line in lyricStrings) {
            var lyricLine = LyricLine(line, lyricStrings.indexOf(line))
            lyricLines.add(lyricLine)
        }
    }

}