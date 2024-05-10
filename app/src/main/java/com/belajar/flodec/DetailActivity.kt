package com.belajar.flodec

import android.content.Intent
import android.graphics.Color
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.Date
import java.util.Locale
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class DetailActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private lateinit var xValues: ArrayList<String>
    private lateinit var descText: TextView
    private lateinit var videoCam: ImageView
    private lateinit var circle: ImageView
    private lateinit var refreshBtn: Button
    private lateinit var handler: Handler
    private lateinit var handler2: Handler
    private lateinit var runnable: Runnable
    private lateinit var runnable2: Runnable
    private var prediksi1 = ""
    private lateinit var progressBar: ProgressBar
    private lateinit var overLayout: FrameLayout
    private lateinit var pilih: TextView
    //variabel TV fetch data
    private lateinit var status: TextView
    private lateinit var ketinggianTV: TextView
    private lateinit var intensitasTV: TextView
    private lateinit var debit: TextView
    private lateinit var keterangan: TextView
    var isIntensitasChart = false
    var isKetinggianChart = false

    //variabel realtime photo
    private lateinit var camerajpg: ShapeableImageView

    //variabel chart
    lateinit var jamKetinggian: FloatArray
    lateinit var dataKetinggian: FloatArray
    lateinit var jamCurah: FloatArray
    lateinit var dataCurah: FloatArray
    var tempKetinggian: Int = 0
    var tempCurah: Int =0
    var keluar = false

    // Meminta request permission ke user
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {

            } else {
                //Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        // jika versi android >= 13 maka minta permission notifikasi ke user
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        overLayout = findViewById(R.id.overlayLayout)
        progressBar = findViewById(R.id.progressBar)
        status = findViewById(R.id.status)
        ketinggianTV = findViewById(R.id.ketinggianTV)
        intensitasTV = findViewById(R.id.intensitasTV)
        debit = findViewById(R.id.debit)
        keterangan = findViewById(R.id.keterangan)
        videoCam = findViewById(R.id.videocam)
        circle = findViewById(R.id.circle)
        camerajpg = findViewById(R.id.shapeableImageView)
        pilih = findViewById(R.id.pilih)

        lineChart = findViewById(R.id.chart)
        descText = findViewById(R.id.desc_text)
        refreshBtn = findViewById(R.id.refreshButton)
        val btnIntensitas = findViewById<MaterialButton>(R.id.intensitas)
        val btnKetinggian = findViewById<MaterialButton>(R.id.ketinggian)
        realTimeImage()
        // Menampilkan ProgressBar sebelum memulai fetch
        progressBar.visibility = View.VISIBLE
        overLayout.visibility = View.VISIBLE
        btnIntensitas.isEnabled = false
        btnKetinggian.isEnabled = false
        refreshBtn.isEnabled = false
        // Jalankan fetchData() di dalam coroutine
        GlobalScope.launch(Dispatchers.IO) {
            fetchData()
            fetchChartData()
            delay(1500)
            // Setelah selesai, kembali ke thread utama untuk menyembunyikan ProgressBar
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.INVISIBLE
                overLayout.visibility = View.INVISIBLE
                btnIntensitas.isEnabled = true
                btnKetinggian.isEnabled = true
                refreshBtn.isEnabled = true
                keluar = true
            }
        }

        setInfoWaktu()

        val banjir = findViewById<LinearLayout>(R.id.status_pu)
        val ketinggian = findViewById<LinearLayout>(R.id.ketinggian_pu)
        val debit = findViewById<LinearLayout>(R.id.debit_pu)
        val intensitas = findViewById<LinearLayout>(R.id.intensitas_pu)
        val cardView = findViewById<MaterialCardView>(R.id.materialCardView)
        val mainLayout = findViewById<FrameLayout>(R.id.frame_layout)

        banjir.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    showFloatingTextView(mainLayout, banjir, cardView, "Status wilayah", 45)
                }
                MotionEvent.ACTION_UP -> {
                    removeFloatingTextView(mainLayout)
                }
            }
            true
        }

        ketinggian.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    showFloatingTextView(mainLayout, ketinggian, cardView, "ketinggian air", 58)
                }
                MotionEvent.ACTION_UP -> {
                    removeFloatingTextView(mainLayout)
                }
            }
            true
        }

        debit.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    showFloatingTextView(mainLayout, debit, cardView, "Perubahan tinggi",40)
                }
                MotionEvent.ACTION_UP -> {
                    removeFloatingTextView(mainLayout)
                }
            }
            true
        }

        intensitas.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    showFloatingTextView(mainLayout, intensitas, cardView, "Intensitas hujan", 45)
                }
                MotionEvent.ACTION_UP -> {
                    removeFloatingTextView(mainLayout)
                }
            }
            true
        }

        btnIntensitas.setBackgroundColor(Color.BLACK)
        btnIntensitas.setTextColor(Color.WHITE)
        btnKetinggian.setBackgroundColor(Color.WHITE)
        btnKetinggian.setTextColor(Color.BLACK)
        descText.visibility=View.VISIBLE
        lineChart.visibility=View.VISIBLE
        pilih.visibility = View.INVISIBLE
        isIntensitasChart = true

        btnIntensitas.setOnClickListener {
            if(isIntensitasChart){
                btnIntensitas.setBackgroundColor(Color.WHITE)
                btnIntensitas.setTextColor(Color.BLACK)
                descText.visibility=View.INVISIBLE
                lineChart.visibility=View.INVISIBLE
                pilih.visibility = View.VISIBLE
                isIntensitasChart = false
            }else{
                btnIntensitas.setBackgroundColor(Color.BLACK)
                btnIntensitas.setTextColor(Color.WHITE)
                btnKetinggian.setBackgroundColor(Color.WHITE)
                btnKetinggian.setTextColor(Color.BLACK)
                descText.visibility=View.VISIBLE
                lineChart.visibility=View.VISIBLE
                pilih.visibility = View.INVISIBLE
                isIntensitasChart = true
                isKetinggianChart = false
                intensitasHujanChart()
            }
        }
        btnKetinggian.setOnClickListener {
            if (isKetinggianChart){
                btnKetinggian.setBackgroundColor(Color.WHITE)
                btnKetinggian.setTextColor(Color.BLACK)
                descText.visibility=View.INVISIBLE
                lineChart.visibility=View.INVISIBLE
                pilih.visibility = View.VISIBLE
                isKetinggianChart = false
            }else{
                btnIntensitas.setBackgroundColor(Color.WHITE)
                btnIntensitas.setTextColor(Color.BLACK)
                btnKetinggian.setBackgroundColor(Color.BLACK)
                btnKetinggian.setTextColor(Color.WHITE)
                descText.visibility=View.VISIBLE
                lineChart.visibility=View.VISIBLE
                pilih.visibility = View.INVISIBLE
                isKetinggianChart = true
                isIntensitasChart = false
                ketinggianAirChart()
            }
        }

        refreshBtn.setOnClickListener {
            recreate()
        }

        val info = findViewById<ImageView>(R.id.info_btn)
        val popup = findViewById<LinearLayout>(R.id.popup)
        val overLayout = findViewById<FrameLayout>(R.id.overlayLayout)
        info.setOnClickListener {
            overLayout.visibility = View.VISIBLE
            popup.visibility = View.VISIBLE

            overLayout.setOnClickListener {
                overLayout.visibility = View.INVISIBLE
                popup.visibility = View.INVISIBLE
            }
        }


    }

    private fun showFloatingTextView(parent: ViewGroup, view: LinearLayout, cardView: MaterialCardView, pesan: String, n: Int) {
        val floatingTextView = TextView(this)
        floatingTextView.text = pesan
        floatingTextView.setTextColor(Color.parseColor("#808080"))
//        floatingTextView.setBackgroundResource(R.drawable.dialog)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )

        val location = IntArray(2)
        cardView.getLocationOnScreen(location)

        val viewLocation = IntArray(2)
        view.getLocationOnScreen(viewLocation)

        val parentLocation = IntArray(2)
        parent.getLocationInWindow(parentLocation)

        val y = location[1] - parentLocation[1] - 36
        val x = viewLocation[0] - n

        params.leftMargin = x
        params.topMargin = y
        floatingTextView.layoutParams = params
        parent.addView(floatingTextView)
    }

    private fun removeFloatingTextView(parent: ViewGroup) {
        if (parent.childCount > 0) {
            parent.removeViewAt(parent.childCount - 1)
        }
    }

    override fun onBackPressed() {
        // Tampilkan dialog konfirmasi sebelum keluar dari aplikasi
        if (keluar){
            super.onBackPressed()
        }else{
            AlertDialog.Builder(this)
                .setMessage("Sedang memuat data, Harap tunggu!")
                .setNegativeButton("Iya", null)
                .show()
        }
    }

    fun realTimeImage(){
        handler2 = Handler()
        runnable2 = object : Runnable {
            override fun run() {
                Log.d("tes", "tes12345")
                // Memuat gambar dari URL menggunakan Glide
                Glide.with(this@DetailActivity)
                    .load("http://ummuhafidzah.sch.id/kehadiran/aquawatch/uploads/esp32-cam.jpg")
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(camerajpg)


                // Menjadwalkan pemanggilan ulang setiap 3 detik
                handler2!!.postDelayed(this, 5000)
            }
        }
        handler2.post(runnable2)
    }



    fun fetchChartData(){
        //isi data ketinggian dengan NaN (biar mula" gda data)
        //entriest.add(Entry(10f, Float.NaN))
        for ( i in 0..23){
            jamKetinggian = FloatArray(25)
            jamCurah = FloatArray(25)
            dataKetinggian = FloatArray(25)
            dataCurah = FloatArray(25)

            jamKetinggian[i] = 10f
            dataKetinggian[i] = Float.NaN
            jamCurah[i] = 10f
            dataCurah[i] = Float.NaN

        }
        val url = "http://ummuhafidzah.sch.id/kehadiran/aquawatch/chart.php"
        //val url = "http://192.168.1.5/sensorjarak/chart.php"
        val jsonObject = JSONObject()
        jsonObject.put("tanggal", "04-04-2024")

        val queue = Volley.newRequestQueue(this)
        Log.d("tesawal0", "fetchKetinggian: ")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url,jsonObject,
            { response ->
                Log.d("tesawal", "fetchKetinggian: ")
                //fetch dataKetinggian
                var arrayKetinggian = response.getJSONArray("dataKetinggian")
                Log.d("tesawal2", "fetchKetinggian: ")
                for (i in 0 until arrayKetinggian.length()) {
                    val itemKetinggian = arrayKetinggian.getJSONObject(i)
                    jamKetinggian[itemKetinggian.getInt("jam")] = itemKetinggian.getInt("jam").toFloat()
                    dataKetinggian[itemKetinggian.getInt("jam")] = itemKetinggian.getInt("ketinggian_air").toFloat()
                    tempKetinggian = itemKetinggian.getInt("jam")
                }
                val tes = jamKetinggian[1].toString()
                Log.d("jamKetinggian : ", "tes")
                //fetch dataCUrah
                var arrayCurah = response.getJSONArray("dataCurah")
                //Log.d("tesawal2", "fetchKetinggian: ")
                for (i in 0 until arrayCurah.length()) {
                    val itemCurah = arrayCurah.getJSONObject(i)
                    jamCurah[itemCurah.getInt("jam")] = itemCurah.getInt("jam").toFloat()
                    dataCurah[itemCurah.getInt("jam")] = itemCurah.getInt("curah_jam").toFloat()
                    tempCurah = itemCurah.getInt("jam")
                }

            },
            { error->
                //Toast.makeText(this, "error volley : ${error.toString()}", Toast.LENGTH_LONG).show()
                //Log.d("StatusError" ,"fetchingData: ")
            })

        queue.add(jsonObjectRequest)
        intensitasHujanChart()
    }

    fun fetchData(){
        val url2 = "http://ummuhafidzah.sch.id/kehadiran/aquawatch/map.php"
        //val url = "http://192.168.1.5/sensorjarak/map.php"
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url2,null,
            { response ->
                var statusResp = response.getString("status")
                var ketinggianResp = response.getDouble("ketinggian").toInt().toString()
                var curahResp = response.getInt("curah").toString()
                var prediksiMenit = response.getInt("prediksiMenit")
                var berikutnya = response.getString("berikutnya")

                var bawah = prediksiMenit-1
                var atas = prediksiMenit+1

                if (response.has("prediksiNaik")){
                    keterangan.text = "$berikutnya dalam $bawah - $atas Menit"
                    keterangan.setBackgroundResource(R.drawable.corner_radius)
                    videoCam.setImageResource(R.drawable.videocam2)
                    circle.setImageResource(R.drawable.orange_circle)
                }else if (response.has("prediksiTurun")){
                    keterangan.text = "Surut ke $berikutnya dalam $bawah - $atas Menit"
                    keterangan.setBackgroundResource(R.drawable.corner_radius_3)
                    videoCam.setImageResource(R.drawable.videocam2)
                    circle.setImageResource(R.drawable.circle3)
                }else if (response.has("sudahBanjir")){
                    keterangan.text = "Sudah Banjir Tinggi"
                    keterangan.setBackgroundResource(R.drawable.corner_radius_2)
                    videoCam.setImageResource(R.drawable.videocam2)
                    circle.setImageResource(R.drawable.circle2)
                }else if (response.has("sudahSurut")){
                    keterangan.text = "Tidak ada prediksi surut"
                    keterangan.setBackgroundResource(R.drawable.corner_radius_hitam)
                    videoCam.setImageResource(R.drawable.videocam2)
                    circle.setImageResource(R.drawable.circle_hitam)
                }
                else {
                    keterangan.text = "Tidak ada prediksi"
                    keterangan.setBackgroundResource(R.drawable.corner_radius_hitam)
                    videoCam.setImageResource(R.drawable.videocam2)
                    circle.setImageResource(R.drawable.circle_hitam)
                }
                var debitResp = response.getDouble("debit")

                ketinggianTV.text = "$ketinggianResp"
                intensitasTV.text = "$curahResp"
                debit.text = String.format("%.2f", debitResp)

                if (response.has("prediksiNaik") && statusResp == "Tidak banjir"){
                    status.text = "Belum banjir"
                }else {
                    status.text = "$statusResp"
                }

            },
            { error->
                //Toast.makeText(this, "error volley : ${error.toString()}", Toast.LENGTH_LONG).show()
                //Log.d("StatusError" ,"fetchingData: ")
            })

        queue.add(jsonObjectRequest)

    }

    fun intensitasHujanChart(){

        descText.text = "mm/jam"
        descText.textSize = 10f
        Log.d("Masuk intetnistas", "intensitasHujanChart: ")
        val description = Description()
        description.text = "Jam"
        description.setPosition(1015f, 465f)
        description.textSize = 10f
        lineChart.description = description
        lineChart.getAxisRight().setDrawLabels(false)

        //xValues = arrayListOf("0","2","5","7","9","12","14","16","19","24")

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisMinimum = 0f
        xAxis.axisMaximum = tempCurah.toFloat()
        //xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
        xAxis.axisLineWidth = 2f
        xAxis.axisLineColor = Color.BLACK
        xAxis.setLabelCount(8)
        xAxis.granularity = 1f
        xAxis.textSize = 13f


        val yAxis = lineChart.axisLeft
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 20f
        yAxis.axisLineWidth = 2f
        yAxis.axisLineColor = Color.BLACK
        yAxis.setLabelCount(5)
        yAxis.setDrawGridLines(false)
        yAxis.textSize = 13f

        lineChart.description.isEnabled = true

        val entriest =  ArrayList<Entry>()
        for(i in 0..tempCurah){
            entriest.add(Entry(jamCurah[i], dataCurah[i]))
        }

        val dataSet = LineDataSet(entriest, "Intensitas hujan (mm/jam)")
        dataSet.setColors(Color.CYAN)
        dataSet.valueTextSize = 10f

        val lineData = LineData(dataSet)

        lineChart.data = lineData
        lineChart.invalidate()
    }

    fun ketinggianAirChart(){
        descText.text = "cm"
        descText.textSize = 11f

        val description = Description()
        description.text = "Jam"
        description.setPosition(1015f, 465f)
        description.textSize = 10f
        lineChart.description = description
        lineChart.description = description
        lineChart.getAxisRight().setDrawLabels(false)

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisMinimum = 0f
        xAxis.axisMaximum = tempKetinggian.toFloat()
        //xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
        xAxis.axisLineWidth = 2f
        xAxis.axisLineColor = Color.BLACK
        xAxis.setLabelCount(8)
        xAxis.granularity = 1f
        xAxis.textSize = 13f

        val yAxis = lineChart.axisLeft
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 50f
        yAxis.axisLineWidth = 2f
        yAxis.axisLineColor = Color.BLACK
        yAxis.setLabelCount(5)
        yAxis.setDrawGridLines(false)
        yAxis.textSize = 13f

        lineChart.description.isEnabled = true

        val entriest =  ArrayList<Entry>()
        for(i in 0..tempKetinggian){
            entriest.add(Entry(jamKetinggian[i], dataKetinggian[i]))
        }

        val dataSet = LineDataSet(entriest, "Ketinggian Air (cm)")
        dataSet.setColors(Color.CYAN)
        dataSet.valueTextSize = 10f

        val lineData = LineData(dataSet)

        lineChart.data = lineData
        lineChart.invalidate()
    }


    fun setInfoWaktu(){
        val infoWaktuTV = findViewById<TextView>(R.id.info_waktu)

        // Buat instance Handler
        handler = Handler()

        // Buat Runnable untuk memperbarui waktu setiap detik
        runnable = object : Runnable {
            override fun run() {
                // Dapatkan waktu saat ini
                val currentTime = Calendar.getInstance().time

                // Format waktu sesuai dengan format yang diinginkan (misalnya, HH:mm:ss untuk waktu)
                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val formattedTime = timeFormat.format(currentTime)

                // Dapatkan tanggal
                val dateFormatDate = DateFormat.getDateInstance()
                val currentDateAndTime = Date()
                val formattedDate = dateFormatDate.format(currentDateAndTime)

                // Tampilkan waktu yang diformat dalam TextView
                infoWaktuTV.text = "$formattedTime WIB, $formattedDate"
                if (isIntensitasChart){intensitasHujanChart()}
                if (isKetinggianChart){ketinggianAirChart()}
                fetchData()

                // Jadwalkan diri sendiri untuk dieksekusi lagi setelah 1 detik
                handler.postDelayed(this, 1000)
            }
        }

        // Mulai memperbarui waktu
        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hentikan pembaruan waktu saat Activity dihancurkan
        handler.removeCallbacks(runnable)
        handler2.removeCallbacks(runnable2)
    }

}