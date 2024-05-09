package com.belajar.flodec

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.MinimapOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MapPercobaan : AppCompatActivity() {

    private var locationProviderClient: FusedLocationProviderClient? = null
    var latitude = 0.0
    var longitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        setContentView(R.layout.activity_map_percobaan)

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this@MapPercobaan)

        getLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    applicationContext,
                    "Izin lokasi tidak di aktifkan!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                getLocation()
            }
        }
    }

    fun getLocation(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // get Permission
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 10
            )
        } else {
            // get Location
            locationProviderClient!!.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude

                    val lat = location.latitude.toString()
                    val longi = location.longitude.toString()
                    Log.d("koordinat String", "$lat $longi")

                    val map = findViewById<MapView>(R.id.map)
                    map.setTileSource(TileSourceFactory.MAPNIK)

                    // untuk menampilkan posisi saat ini dengan radius beberapa meter
//                    val mLocationOverlay = MyLocationNewOverlay(
//                        GpsMyLocationProvider(this),
//                        map
//                    )
//                    mLocationOverlay.enableMyLocation()
//                    map.overlays.add(mLocationOverlay)

                    //Enable rotasi peta
                    val rotationGestureOverlay = RotationGestureOverlay(map)
                    rotationGestureOverlay.isEnabled
                    map.setMultiTouchControls(true)
                    map.overlays.add(rotationGestureOverlay)

                    map.setBuiltInZoomControls(true)
                    map.setMultiTouchControls(true)

                    val mapController = map.controller
                    mapController.setZoom(16)
                    val startPoint = GeoPoint(latitude, longitude)
                    val startPoint2 = GeoPoint(3.567347, 98.659992)
                    mapController.setCenter(startPoint)

                    val compassOverlay = CompassOverlay(this, InternalCompassOrientationProvider(this), map)
                    compassOverlay.enableCompass()
                    map.overlays.add(compassOverlay)

                    val startMarker = Marker(map)
                    startMarker.setPosition(startPoint)
                    startMarker.setAnchor(
                        Marker.ANCHOR_CENTER,
                        Marker.ANCHOR_BOTTOM
                    )
                    startMarker.title = "Tempat kamu Berada"
                    startMarker.icon = getResources().getDrawable(R.drawable.baseline_location_on_50)
                    map.overlays.add(startMarker)

                    val startMarker2 = Marker(map)
                    startMarker2.setPosition(startPoint2)
                    startMarker2.setAnchor(
                        Marker.ANCHOR_CENTER,
                        Marker.ANCHOR_BOTTOM
                    )
                    startMarker2.title = "Jalan Dr. Mansyur, Medan Selayang"
                    startMarker2.icon = getResources().getDrawable(R.drawable.baseline_location_pin_50)
                    map.overlays.add(startMarker2)

                    val minimapOverlay = MinimapOverlay(this, map.tileRequestCompleteHandler)
                    minimapOverlay.setWidth(400)
                    minimapOverlay.setHeight(400)
                    //optionally, you can set the minimap to a different tile source
                    //minimapOverlay.setTileSource(....)
                    map.overlays.add(minimapOverlay)

//                    startMarker.setOnMarkerClickListener { marker, mapView -> // Menanggapi klik marker
//                        // Buka halaman baru atau lakukan aksi lain sesuai kebutuhan Anda
//                        Log.d("MarkerClick", "Marker clicked!")
//                        val intent: Intent = Intent(
//                            this@Hadir,
//                            Home::class.java
//                        )
//                        startActivity(intent)
//                        finish()
//                        Log.d("MarkerClick", "Marker pindah")
//
//                        // Mengembalikan true untuk mengindikasikan bahwa event telah ditangani
//                        true
//                    }
                } else {
                    Toast.makeText(applicationContext, "Lokasi tidak aktif!", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(
                    applicationContext,
                    e.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}