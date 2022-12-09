package com.example.pauseandplay

const val GUESSING_POINTS : Int = 200
const val MINUTE_POINTS : Int = -1
const val LINE_POINTS : Int = -10
const val MINIGAME_LOST_POINTS : Int = -30
const val MISTAKE_POINTS : Int = -40
const val GIVINGUP_POINTS : Int = -500

class Points {
    var songGuessed = false
    var linesCollected : Int = 0
    var minigamesLost : Int = 0
    var solvingMistakes : Int = 0
    var givenUp = false

    private var minutesPoints : Int = 0
    private var linesPoints : Int = 0
    private var minigamesPoints : Int = 0
    private var mistakesPoints : Int = 0

    private var totalScore : Int = 0

    fun getNumberOfPoints(song: Song, minutes: Int) : Int {
        var points : Int = 0

        for (line in song.lyricLines)
            if (line.got)
                linesCollected++

        if (songGuessed)
            points += GUESSING_POINTS

        minutesPoints = (minutes * MINUTE_POINTS)
        linesPoints = (linesCollected * LINE_POINTS)
        minigamesPoints = (minigamesLost * MINIGAME_LOST_POINTS)
        mistakesPoints = (solvingMistakes * MISTAKE_POINTS)

        points += minutesPoints + linesPoints + minigamesPoints + mistakesPoints

        if (givenUp)
            points += GIVINGUP_POINTS

        return points
    }

    fun getStringInfoPoints(song: Song, minutes: Int) : String {
        var stringInfo : StringBuilder = StringBuilder()

        totalScore = getNumberOfPoints(song, minutes)

        stringInfo.append("These are your results:\n")
        stringInfo.append("$GUESSING_POINTS for having guessed the title and the artist of the song\n")
        stringInfo.append("$minutesPoints for spending $minutes minutes at the game.\n")
        stringInfo.append("$linesPoints for having collected $linesCollected lyric lines in total.\n")
        //stringInfo.append("$minigamesPoints for having lost $minigamesLost mini-games in total.\n")
        stringInfo.append("$mistakesPoints for having $solvingMistakes mistakes attempting to solve the song.\n")
        stringInfo.append("So, in total you have got $totalScore points in this game!\n")

        return stringInfo.toString()

    }
}