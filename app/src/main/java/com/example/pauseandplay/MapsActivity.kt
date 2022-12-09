package com.example.pauseandplay

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.lang.Math.abs
import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random

const val TAG = "MapsActivity"
const val SOLVING_REQUEST_CODE = 10

class MapsActivity : BaseActivity(), OnMapReadyCallback {

    var mode = -1
    var master: String = ""
    var icon: Int = -1
    var PERMISSION_ID = 42

    val markers: ArrayList<Marker> = arrayListOf<Marker>()

    var lineGot: Boolean = false

    val minLat = 51.617282
    val maxLat = 51.619667
    val minLng = -3.883300
    val maxLng = -3.874803

    var linesInTheMap = 0

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastMarker: Marker
    private lateinit var chronometer: Chronometer

    private lateinit var song: Song

    private lateinit var points: Points

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        requestNewLocationData()

        val mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(mToolbar)
        supportActionBar?.title = ""

        points = Points()

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
        } else if (mode == CURRENT_MODE) {
            masterMenuImageView.setImageResource(R.drawable.play_master)
            icon = R.drawable.play_master
            master = "Play"
        }

        song = Song(assets, mode)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate((R.menu.toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        Log.i("myLocation", "Map Ready")
        mMap = googleMap

        try {
            var success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))

            if (!success) {
                Log.e(TAG, "Stile parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }

        for (i in 1..10) {

            var lyricLine = song.lyricLines.random()
            if (!lyricLine.inTheMap) {
                lyricLine.latitude = Random.nextDouble(minLat, maxLat)
                lyricLine.longitude = Random.nextDouble(minLng, maxLng)
                val position =
                    LatLng(lyricLine.latitude, lyricLine.longitude)
                var marker = mMap.addMarker(
                    MarkerOptions().position(position).icon(
                        BitmapDescriptorFactory.fromResource(
                            R.drawable.line
                        )
                    )
                )

                markers.add(marker)
                lyricLine.markerID = markers.indexOf(marker)

                lyricLine.inTheMap = true
                linesInTheMap = 10
            }

        }

        chronometer = findViewById<View>(R.id.chronometer) as Chronometer

        chronometer.start();

    }


    fun solveFunction(view: View) {

        MaterialAlertDialogBuilder(this, R.style.Theme_MaterialAlertDialog)
            .setTitle(master + " says...")
            .setMessage("Ok... Are you sure you wanna try to attempt the artist and the title of the song? You'll lose points if your answer is incorrect")
            .setPositiveButton("YES! I'M SURE") { dialog, which ->
                val intent = Intent(this, SolveActivity::class.java)
                intent.putExtra("mode", mode)
                intent.putExtra("title", song.title)
                intent.putExtra("artist", song.artist)
                startActivityForResult(intent, SOLVING_REQUEST_CODE)
            }
            .setNegativeButton("BETTER TO WAIT", null)
            .setIcon(icon)
            .show();
    }


    private fun lineFoundAlert(line: LyricLine) {
        MaterialAlertDialogBuilder(this, R.style.Theme_MaterialAlertDialog)
            .setTitle(master + " says...")
            .setMessage("Hey! You've found a lyric line. I challenge you a game to get the line!")
            .setPositiveButton("LET ME WIN!") { dialog, which ->
                val intent = Intent(this, WhoSaidThatActivity::class.java)
                intent.putExtra("mode", mode)
                intent.putExtra("lineID", line.id)
                startActivityForResult(intent, 10)
            }
            .setIcon(icon)
            .show();
    }

    private fun alert(message: String) {
        MaterialAlertDialogBuilder(this, R.style.Theme_MaterialAlertDialog)
            .setTitle("Master says...")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show();
    }

    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        var lat = location.latitude
                        var long = location.longitude

                        var accuracy = location.accuracy

                        val lastLoc = LatLng(lat, long)
                        val zoomLevel = 18.0f

                        lastMarker = mMap.addMarker(
                            MarkerOptions().position(lastLoc).title("You are here!").icon(
                                BitmapDescriptorFactory.fromResource(R.drawable.person)
                            )
                        )
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, zoomLevel))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, zoomLevel))
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestNewLocationData() {
        Log.i("myLocation", "request")
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 2000
        mLocationRequest.fastestInterval = 1000

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.i("myLocation", "Callback")
            var mLastLocation: Location = locationResult.lastLocation
            var lat = mLastLocation.latitude
            var long = mLastLocation.longitude

            val lastLoc = LatLng(lat, long)

            lastMarker.remove()
            lastMarker = mMap.addMarker(
                MarkerOptions().position(lastLoc).title("You are here!").icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.person)
                )
            )
            //mMap!!.animateCamera(CameraUpdateFactory.newLatLng(lastLoc))

            for (line in song.lyricLines)
                if (line.inTheMap && !lineGot)
                    if (abs(line.latitude - lastLoc.latitude) < 0.0001 &&
                        abs(line.longitude - lastLoc.longitude) < 0.0001
                    ) {
                        lineGot = true
                        line.got = true
                        alert("You've found a lyric line. It's: " + line.text)
                        markers[line.markerID].remove()
                        line.inTheMap = false
                        linesInTheMap--
                        lineGot = false

                        if (linesInTheMap < 5) {
                            var lyricLine = song.lyricLines.random()
                            if (!lyricLine.inTheMap) {

                                lyricLine.latitude = Random.nextDouble(minLat, maxLat)
                                lyricLine.longitude = Random.nextDouble(minLng, maxLng)

                                val position = LatLng(lyricLine.latitude, lyricLine.longitude)
                                var marker = mMap.addMarker(
                                    MarkerOptions().position(position).icon(
                                        BitmapDescriptorFactory.fromResource(
                                            R.drawable.line
                                        ))
                                )

                                markers.add(marker)
                                lyricLine.markerID = markers.indexOf(marker)

                                lyricLine.inTheMap = true
                                linesInTheMap++
                            }
                        }

                    }
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SOLVING_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!!.hasExtra("mistakes")) {
                    points.solvingMistakes += data.extras!!.getInt("mistakes")
                    points.songGuessed = true

                    chronometer.stop()
                    val minutes = chronometer.text.split(":")[0].toInt()

                    val sharedPreferences = getSharedPreferences(
                        "com.example.myapp.PREFERENCE_FILE_KEY",
                        Context.MODE_PRIVATE
                    ) ?: return
                    val lastPoints = sharedPreferences.getInt("points", 0)
                    val finalPoints = lastPoints + points.getNumberOfPoints(song, minutes)

                    with(sharedPreferences.edit()) {
                        putInt("points", finalPoints)
                        commit()
                    }

                    MaterialAlertDialogBuilder(this, R.style.Theme_MaterialAlertDialog)
                        .setTitle(master + " says...")
                        .setMessage(
                            "Oh! You guessed the song correctly. Of course, the title is " + song.title +
                                    " and the artist is " + song.artist +
                                    ". But don't trust, I'll win you next time..."
                        )
                        .setPositiveButton("SHOW ME MY POINTS!") { dialog, which ->
                            MaterialAlertDialogBuilder(this, R.style.Theme_MaterialAlertDialog)
                                .setTitle("This is your final score:")
                                .setMessage(
                                    points.getStringInfoPoints(song, minutes)
                                )
                                .setPositiveButton("OK") { dialog, which ->
                                    startActivity(Intent(this, MainActivity::class.java))
                                }
                                .setIcon(R.drawable.star_solve)
                                .show();
                        }
                        .setIcon(icon)
                        .show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (data!!.hasExtra("mistakes")) {
                    points.solvingMistakes += data.extras!!.getInt("mistakes")
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.lyrics -> {
                val intent = Intent(this, LyricLinesActivity::class.java)
                intent.putExtra("song", song)
                intent.putExtra("mode", mode)
                startActivity(intent)
                return true
            }
            R.id.give_up -> {
                MaterialAlertDialogBuilder(this, R.style.Theme_MaterialAlertDialog)
                    .setTitle(master + " says...")
                    .setMessage("Ohh... It looks like you're trying to give up the game. Do you really wanna? You'll lose 500 points of your total score.")
                    .setPositiveButton("YES, SEE YOU") { dialog, which ->
                        val sharedPreferences = getSharedPreferences(
                            "com.example.myapp.PREFERENCE_FILE_KEY",
                            Context.MODE_PRIVATE
                        )
                        val lastPoints = sharedPreferences.getInt("points", 0)
                        val finalPoints = lastPoints + GIVINGUP_POINTS

                        with(sharedPreferences.edit()) {
                            putInt("points", finalPoints)
                            commit()
                        }
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    .setNegativeButton("I'LL KEEP PLAYING", null)
                    .setIcon(icon)
                    .show();

                return true
            }
        }



        return super.onOptionsItemSelected(item)
    }
}
