package com.example.pauseandplay

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById<View>(R.id.startButton) as Button
        startButton.setOnClickListener {
            val intent = Intent(this, ModeSelectionActivity::class.java)
            startActivity(intent)
        }

        val flagButton = findViewById<View>(R.id.flagButton) as ImageButton
        val language = Locale.getDefault().language

        if (language == "es")
            flagButton.setImageResource(R.drawable.uk_flag)
        else
            flagButton.setImageResource(R.drawable.spain_flag)

        flagButton.setOnClickListener {
            MaterialAlertDialogBuilder(this, R.style.Theme_MaterialAlertDialog)
                .setTitle(getString(R.string.change_language_title))
                .setMessage(getString(R.string.change_language_message))
                .setPositiveButton(getString(R.string.ok_button_text)) { dialog, which ->
                    if (language == "es")
                        dLocale = Locale("uk")
                    else
                        dLocale = Locale("es")
                    recreate()
                }
                .setNegativeButton(getString(R.string.cancel_button_text), null)
                .show();
        }

        val sharedPreferences =
            getSharedPreferences("com.example.myapp.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
                ?: return
        val totalPoints = sharedPreferences.getInt("points", 0)

        val pointTextView = findViewById<View>(R.id.pointsTextView) as TextView
        pointTextView.text = totalPoints.toString()


    }


}
