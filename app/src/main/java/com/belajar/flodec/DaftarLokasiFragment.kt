package com.belajar.flodec

import android.content.Intent
import android.graphics.Color
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.forEach
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Locale

class DaftarLokasiFragment : Fragment() {

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var textViewTime: TextView
    private lateinit var textViewDate: TextView
    private lateinit var status: TextView
    private lateinit var ketinggian: TextView
    lateinit var overLayout: FrameLayout
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daftar_lokasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewTime = view.findViewById(R.id.waktu)
        textViewDate = view.findViewById(R.id.tanggal)
        status = view.findViewById(R.id.status)
        ketinggian = view.findViewById(R.id.ketinggian_air)

        progressBar = view.findViewById(R.id.progressBar)
        overLayout = view.findViewById(R.id.overlayLayout)
        val mainActivity = activity as? MainActivity
        val bottomNavigationView = mainActivity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        progressBar.visibility = View.VISIBLE
        overLayout.visibility = View.VISIBLE
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
            }
        }
        setWaktu()
        setTanggal()

        val btnDetail = view.findViewById<AppCompatButton>(R.id.btnDetail)

        btnDetail.setOnClickListener {
            val DetailActivity = Intent(requireActivity(), DetailActivity::class.java)
            startActivity(DetailActivity)

        }

    }

    fun fetchData(){
        val url = "http://ummuhafidzah.sch.id/kehadiran/aquawatch/map.php"
        //val url = "http://192.168.1.5/sensorjarak/map.php"
        val queue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url,null,
            { response ->

                var statusResp = response.getString("status")
                var ketinggianResp = response.getDouble("ketinggian").toInt().toString()
                if (statusResp == "Tidak banjir"){
                    status.backgroundTintList = resources.getColorStateList(R.color.green)
                }else if (statusResp == "Banjir rendah"){
                    status.backgroundTintList = resources.getColorStateList(R.color.yellow)
                }else{
                    status.backgroundTintList = resources.getColorStateList(R.color.red2)
                }
                //Log.d("hasil", "fetchData: $statusResp $ketinggianResp $curahResp")
                //Toast.makeText(context, "fetchData: $statusResp $ketinggianResp $curahResp", Toast.LENGTH_LONG).show()
                ketinggian.text = "$ketinggianResp cm"
                status.text = "$statusResp"
            },
            { error->
                Toast.makeText(context, "error volley : ${error.toString()}", Toast.LENGTH_LONG).show()
                Log.d("StatusError" ,"fetchingData: ") })

        queue.add(jsonObjectRequest)
    }

    fun setWaktu(){
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

                // Tampilkan waktu yang diformat dalam TextView
                textViewTime.text = formattedTime

                // Jadwalkan diri sendiri untuk dieksekusi lagi setelah 1 detik
                handler.postDelayed(this, 1000)
            }
        }
        // Mulai memperbarui waktu
        handler.post(runnable)
    }

    fun setTanggal(){
        // Dapatkan instance dari DateFormat untuk menampilkan tanggal dan waktu sesuai pengaturan lokal perangkat
        val dateFormatDate = DateFormat.getDateInstance()

        // Dapatkan tanggal dan waktu saat ini
        val currentDateAndTime = Date()

        // Format tanggal dan waktu sesuai dengan pengaturan lokal perangkat
        val formattedDate = dateFormatDate.format(currentDateAndTime)

        // Tampilkan tanggal dan waktu yang diformat dalam TextView yang berbeda
        textViewDate.text = formattedDate
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hentikan pembaruan waktu saat Activity dihancurkan
        handler.removeCallbacks(runnable)
    }

}