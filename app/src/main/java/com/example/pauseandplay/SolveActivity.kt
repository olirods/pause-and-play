package com.example.pauseandplay

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.security.AccessController.getContext

class SolveActivity : BaseActivity() {

    var mode = -1
    var icon:Int = -1
    var master:String = ""
    var mistakes : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solve)

        val mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        val masterMenuImageView = findViewById<View>(R.id.masterMenuImageView) as ImageView

        setSupportActionBar(mToolbar)
        supportActionBar?.title = getString(R.string.solve_activity_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras

        if (extras == null) {
            Log.d(TAG, "Oops, the bundle was empty")
            return
        }

        mode = extras.getInt("mode")

        if (mode == CLASSIC_MODE) {
            masterMenuImageView.setImageResource(R.drawable.pause_master)
            icon = R.drawable.pause_master
            master = "Pause"
        }
        else if (mode == CURRENT_MODE) {
            masterMenuImageView.setImageResource(R.drawable.play_master)
            icon = R.drawable.play_master
            master = "Play"
        }

        val artist:String = extras.getString("artist")
        val title:String = extras.getString("title")

        val artistEditText = findViewById<View>(R.id.artistEditText) as TextInputEditText
        val titleEditText = findViewById<View>(R.id.titleEditText) as TextInputEditText

        val artistEditTextLayout = findViewById<View>(R.id.artistEditTextLayout) as TextInputLayout
        val titleEditTextLayout = findViewById<View>(R.id.titleEditTextLayout) as TextInputLayout
        
        artistEditTextLayout.setCounterMaxLength(artist.length)
        titleEditTextLayout.setCounterMaxLength(title.length)

        val solveButton = findViewById<View>(R.id.solveButton) as Button
        solveButton.setOnClickListener {
            val view = this.getCurrentFocus()
            view.clearFocus()
            if (view != null)
            {
                val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }

            if (artistEditText.text.toString() == artist && titleEditText.text.toString() == title) {
                val data = Intent()
                data.putExtra("mistakes", mistakes)

                setResult(Activity.RESULT_OK, data)
                super.finish()
            } else {
                mistakes++

                if (artistEditText.text.toString() != artist)
                    artistEditTextLayout.error = getString(R.string.incorrect)
                if (titleEditText.text.toString() != title)
                    titleEditTextLayout.error = getString(R.string.incorrect)
            }
        }

        mToolbar.setNavigationOnClickListener{
            returnFunction()
        }

    }

    fun returnFunction() {
        MaterialAlertDialogBuilder(this, R.style.Theme_MaterialAlertDialog)
            .setTitle(master + getString(R.string.master_says))
            .setMessage(getString(R.string.exit_solving))
            .setPositiveButton(getString(R.string.ok_exit_solving)) { dialog, which ->
                mistakes++

                val data = Intent()
                data.putExtra("mistakes", mistakes)

                setResult(Activity.RESULT_CANCELED, data)
                super.finish()
            }
            .setNegativeButton(getString(R.string.cancel_exit_solving), null)
            .setIcon(icon)
            .show();
    }

    override fun onBackPressed() {
        returnFunction()
    }
}
