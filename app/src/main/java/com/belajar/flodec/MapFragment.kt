package com.belajar.flodec

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.app.ActivityCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import kotlin.properties.Delegates
import org.json.JSONObject

class MapFragment : Fragment() {
    private var locationProviderClient: FusedLocationProviderClient? = null
    private lateinit var alamat: TextView
    private lateinit var status: TextView
    private lateinit var ketinggian: TextView
    private lateinit var statusBanjir: TextView
    private lateinit var keterangan: TextView
    private lateinit var saran: TextView
    private var prediksi1 = ""

    private lateinit var newView: View
    lateinit var overLayout: FrameLayout
    lateinit var progressBar: ProgressBar
    private var animator: ValueAnimator? = null


    //varibel realtime
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        newView = view
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = requireActivity()
        // important! set your user agent to prevent getting banned from the osm servers
        getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

        val bottomSheet= view.findViewById<FrameLayout>(R.id.design_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        // Animasi bottom sheet
        if(arguments != null){
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheet.post {
                val height = bottomSheet.height.toFloat()
                val animator = ObjectAnimator.ofFloat(bottomSheet, "translationY", height, 0f)
                animator.duration = 350
                animator.start()
            }
        }else{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val mapView = view.findViewById<MapView>(R.id.map)
        var isFull = false

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Mendapatkan tinggi layar
                val displayMetrics = resources.displayMetrics
                val screenHeight = displayMetrics.heightPixels

                // Menghitung tinggi BottomSheet setelah diubah
                val bottomSheetHeight = (bottomSheet.height * slideOffset).toInt()

                // Mengatur tinggi dari elemen MapView
                val params = mapView.layoutParams
                params.height = screenHeight - bottomSheetHeight
                mapView.layoutParams = params

                if (slideOffset == 0f) {
                    // BottomSheet di-close, mengatur tinggi MapView ke tinggi layar penuh
                    params.height = screenHeight
                    mapView.layoutParams = params
                }
            }
        })

        progressBar = view.findViewById(R.id.progressBar)
        overLayout = view.findViewById(R.id.overlayLayout)
        val btnMap = view.findViewById<AppCompatImageButton>(R.id.btnMap)
        val btnRefresh = view.findViewById<AppCompatImageButton>(R.id.btnRefresh)
        val btnDetail = view.findViewById<AppCompatButton>(R.id.btnDetail)

        locationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        progressBar.setVisibility(View.VISIBLE)
        getLocation(view)
        progressBar.setVisibility(View.INVISIBLE)

        alamat = view.findViewById(R.id.alamat)
        status = view.findViewById(R.id.status)
        ketinggian = view.findViewById(R.id.ketinggian_air)
        statusBanjir = view.findViewById(R.id.status_banjir)
        keterangan = view.findViewById(R.id.keterangan)
        saran = view.findViewById(R.id.saran)


        if (arguments != null) {
            status.text = arguments?.getString("status")
            ketinggian.text = arguments?.getString("ketinggian")
        }

        btnMap.setOnClickListener {
            val MapFragment = Intent(requireActivity(), MapPercobaan::class.java)
            startActivity(MapFragment)
        }

        btnDetail.setOnClickListener {
            val DetailActivity = Intent(requireActivity(), DetailActivity::class.java)
            startActivity(DetailActivity)

        }

        btnRefresh.setOnClickListener {
            val mapFragment = MapFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.container, mapFragment, MapFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
        val mainActivity = activity as? MainActivity
        val bottomNavigationView = mainActivity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        progressBar.visibility = View.VISIBLE
        overLayout.visibility = View.VISIBLE
        btnDetail.isEnabled = false
        bottomNavigationView?.menu?.forEach { menuItem ->
            menuItem.isEnabled = false
        }

        (activity as? MainActivity)?.changeButtonBackground2(Color.parseColor("#80000000"))
        // Jalankan fetchData() di dalam coroutine
        GlobalScope.launch(Dispatchers.IO) {
            fetchData()
            delay(1500)
            // Setelah selesai, kembali ke thread utama untuk menyembunyikan ProgressBar
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.INVISIBLE
                overLayout.visibility = View.INVISIBLE
                (activity as? MainActivity)?.changeButtonBackground()
                bottomNavigationView?.menu?.forEach { menuItem ->
                    menuItem.isEnabled = true
                }
                btnDetail.isEnabled = true
            }
        }

        // fetching data real-time
        Log.d("sebelum", "tes" )
        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                Log.d("real-time", "success")
                fetchData()

                // Menjadwalkan pemanggilan ulang setiap 5 detik
                handler!!.postDelayed(this, 1500)
            }
        }

        val info = view.findViewById<ImageView>(R.id.info_btn)
        val popup = view.findViewById<LinearLayout>(R.id.popup)
        info.setOnClickListener {
            overLayout.visibility = View.VISIBLE
            popup.visibility = View.VISIBLE

            overLayout.setOnClickListener {
                overLayout.visibility = View.INVISIBLE
                popup.visibility = View.INVISIBLE
            }
        }

    }

    fun fetchData(){
        val url = "http://ummuhafidzah.sch.id/kehadiran/aquawatch/map.php"
        //val url = "http://192.168.1.5/sensorjarak/map.php"
        val queue = Volley.newRequestQueue(requireContext())
        val jsonObjectRequest = StringRequest(
            Request.Method.GET, url,
            { response ->

                try {
                    val responseJSON = JSONObject(response)

                    var statusResp = responseJSON.getString("status")
                    var ketinggianResp = responseJSON.getDouble("ketinggian").toInt().toString()
                    var curahResp = responseJSON.getInt("curah").toString()
                    var prediksiMenit = responseJSON.getInt("prediksiMenit")
                    if (responseJSON.has("prediksiNaik")){
                        prediksi1 = responseJSON.getString("prediksiNaik")
                        statusBanjir.text = "Sedang naik"
                    }else if (responseJSON.has("prediksiTurun")){
                        prediksi1 = responseJSON.getString("prediksiTurun")
                        statusBanjir.text = "Sedang turun"
                    }else if (responseJSON.has("sudahBanjir"))  {
                        prediksi1 = responseJSON.getString("sudahBanjir")
                        statusBanjir.text = "Stagnan"
                    }else if (responseJSON.has("sudahSurut"))  {
                        prediksi1 = responseJSON.getString("sudahSurut")
                        statusBanjir.text = "Tidak Banjir"
                    }else {
                        prediksi1 = responseJSON.getString("prediksiNone")
                        statusBanjir.text = "Tidak banjir"
                    }

                    if (prediksiMenit >= 30){
                        statusBanjir.text = "Stagnan"
                    }

                    if (statusResp == "Tidak banjir" || statusResp == "0"){
                        saran.text= "Tidak perlu alternatif jalan lain untuk saat ini"
                        saran.setTextColor(Color.parseColor("#4c4c4c"))
                    }else {
                        saran.text="Anda dapat melewati Jl. Sei Serayu dari arah utara sebagai alternatif"
                        saran.setTextColor(Color.parseColor("#95672b"))
                    }
                    var prediksi = prediksi1

                    //Log.d("hasil", "fetchData: $statusResp $ketinggianResp $curahResp")
                    //Toast.makeText(context, "fetchData: $statusResp $ketinggianResp $curahResp", Toast.LENGTH_LONG).show()

                    ketinggian.text = "$ketinggianResp cm"
                    keterangan.text = "$prediksi"

                    if (responseJSON.has("prediksiNaik") && statusResp == "Tidak banjir") {
                        status.text = "Belum banjir"
                    }else {
                        status.text = "$statusResp"
                    }

                }catch (error: JSONException){
                    error.printStackTrace();
                    Toast.makeText(requireActivity(), error.message.toString(), Toast.LENGTH_SHORT).show()
                }
            },
            { error->
                Toast.makeText(context, "error volley : ${error.toString()}", Toast.LENGTH_LONG).show()
                Log.d("StatusError" ,"fetchingData: ")
            })

        queue.add(jsonObjectRequest)
    }

    public override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        getInstance().load(requireActivity(), PreferenceManager.getDefaultSharedPreferences(requireActivity()))

        // Memulai pembaruan gambar secara berkala saat aktivitas aktif
        handler!!.post(runnable!!)
    }

    override fun onPause() {
        super.onPause()
        // Menghentikan pembaruan gambar saat aktivitas tidak aktif
        handler!!.removeCallbacks(runnable!!)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //Toast.makeText(requireActivity(),"Izin lokasi tidak di aktifkan!", Toast.LENGTH_SHORT).show()
                // meminta permission
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 10
                )
            } else {
                getLocation(newView)
            }
        }
    }

    fun getLocation(view: View){
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // meminta Permission
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
                    val latitude = location.latitude
                    val longitude = location.longitude

                    val lat = location.latitude.toString()
                    val longi = location.longitude.toString()
                    Log.d("koordinat String", "$lat $longi")

                    val map = view.findViewById<MapView>(R.id.map)
                    map.setTileSource(TileSourceFactory.MAPNIK)

                    map.setBuiltInZoomControls(false)
                    map.setMultiTouchControls(true)


                    val mapController = map.controller
                    mapController.setZoom(16)
                    val startPoint = GeoPoint(latitude, longitude)
                    val startPoint2 = GeoPoint(3.567347, 98.659992)
                    mapController.setCenter(startPoint2)
                    mapController.setZoom(18)

//                    val compassOverlay = CompassOverlay(requireActivity(), InternalCompassOrientationProvider(requireActivity()), map)
//                    compassOverlay.enableCompass()
//                    map.overlays.add(compassOverlay)



                    val startMarker = Marker(map)
                    startMarker.setPosition(startPoint)
                    startMarker.setAnchor(
                        Marker.ANCHOR_CENTER,
                        Marker.ANCHOR_BOTTOM
                    )
                    startMarker.title = "Tempat Anda Berada"
                    val customDrawable = getResources().getDrawable(R.drawable.baseline_location_on_50)
                    startMarker.icon = customDrawable
                    map.overlays.add(startMarker)

                    val startMarker2 = Marker(map)
                    startMarker2.setPosition(startPoint2)
                    startMarker2.setAnchor(
                        Marker.ANCHOR_CENTER,
                        Marker.ANCHOR_BOTTOM
                    )
                    startMarker2.title = "Jalan Dr. Mansyur, Medan Selayang"
                    val customDrawable2 = getResources().getDrawable(R.drawable.baseline_location_pin_50)
                    startMarker2.icon = customDrawable2
                    map.overlays.add(startMarker2)

//                    startMarker.setOnMarkerClickListener { marker, mapView ->
//                        alamat.text = "Jalan Sakti Lubis Gg. Pegawai, Kecamatan Medan Kota"
//                        status.text = "Tidak Banjir"
//                        ketinggian.text = "1.5 cm"
//
//                        // Mengembalikan true untuk mengindikasikan bahwa event telah ditangani
//                        true
//                    }

                } else {
                    Toast.makeText(requireActivity(), "Lokasi tidak aktif!", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(
                    requireActivity(),
                    e.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }



}