package com.example.pauseandplay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.card.MaterialCardView

const val CLASSIC_MODE = 0
const val CURRENT_MODE = 1

class ModeSelectionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_selection)

        val classicMaterialCard = findViewById<View>(R.id.classicMaterialCard) as MaterialCardView
        classicMaterialCard.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("mode", CLASSIC_MODE)
            startActivity(intent)
        }

        val currentMaterialCard = findViewById<View>(R.id.currentMaterialCard) as MaterialCardView
        currentMaterialCard.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("mode", CURRENT_MODE)
            startActivity(intent)
        }
    }
}
