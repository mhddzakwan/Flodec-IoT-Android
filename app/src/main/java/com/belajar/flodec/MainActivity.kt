package com.belajar.flodec

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import org.json.JSONException
import org.json.JSONObject
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity() {

    private lateinit var dialog: AlertDialog

    // Meminta request notification permission ke user
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {

            } else {
                showNotifPermissPopup()
            }
        }
    private lateinit var bottomNavigationView: BottomNavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window?.decorView?.systemUiVisibility = window?.decorView?.systemUiVisibility?.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)!!
        }

        // jika versi android >= 13 maka minta notification permission ke user
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        val fragmentManager = supportFragmentManager
        val mapFragment = MapFragment()

        fragmentManager
            .beginTransaction()
            .replace(R.id.container, mapFragment, MapFragment::class.java.simpleName)
            .commit()

//        if (mapFragment != null) {
//            val info = mapFragment.view?.findViewById<TextView>(R.id.info_btn)
//            info?.setOnClickListener {
//                showNotifPermissPopup()
//            }
//        }

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener {item ->
            if (item.itemId == R.id.home){
                val mapFragment = MapFragment()

                fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, mapFragment, MapFragment::class.java.simpleName)
                    .commit()
            }else if(item.itemId  == R.id.lokasi){
                val lokasiFragment = DaftarLokasiFragment()

                fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, lokasiFragment, DaftarLokasiFragment::class.java.simpleName)
                    .commit()
            }
            true
        }
        registTopic()
    }
    fun changeButtonBackground() {
        val windowBackgroundColor = android.R.attr.windowBackground
        bottomNavigationView.setBackgroundColor(windowBackgroundColor)
    }
    fun changeButtonBackground2(color: Int) {
        bottomNavigationView.setBackgroundColor(color)
    }

    private fun showInfoPopup() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.popup_layout, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        val overlayView = View(this)
        overlayView.apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.TRANSPARENT)
            isClickable = true
            setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()

// Tambahkan overlay ke dalam layout utama
        val parentLayout = dialog.window?.decorView as ViewGroup
        parentLayout.addView(overlayView)
    }

    private fun showNotifPermissPopup() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_pop_up, null)

        dialogView.findViewById<TextView>(R.id.textViewTitle).text = "Peringatan!"
        dialogView.findViewById<TextView>(R.id.textViewDescription).text = "Jika Anda tidak mengizinkan notifikasi, sebagian fitur aplikasi kami mungkin tidak berfungsi secara optimal. Harap izinkan notifikasi untuk mendapatkan pengalaman terbaik."

        dialogView.findViewById<Button>(R.id.buttonPositive).setOnClickListener {
            val settingIntent = Intent()
            settingIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName,null )
            settingIntent.setData(uri)
            startActivity(settingIntent)
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.buttonNegative).setOnClickListener {
            dialog.dismiss()
        }
        builder.setView(dialogView)
        dialog = builder.create()
        dialog.show()
    }

    fun registTopic(){
        Firebase.messaging.subscribeToTopic("notif")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d("FCM-msg", msg)
            }
    }

//    fun fetchFCMToken(): String{
//        // Untuk mengambil token perangkat
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            token = task.result
//            // Log
//            Log.d("FCM", token)
//        })
//        return token
//    }

//    fun kirimData(token: String){
//        val url = "http://ummuhafidzah.sch.id/kehadiran/aquawatch/token.php"
//        val jsonObject = JSONObject()
//        jsonObject.put("token", token)
//        Log.d("token2",token)
//
//        val queue = Volley.newRequestQueue(this)
//
//        val jsonObjectRequest = JsonObjectRequest(
//            Request.Method.POST, url,jsonObject,
//            { response ->
//                Toast.makeText(this, "berhasil", Toast.LENGTH_LONG).show()
//            },
//            { error->
//                // Toast.makeText(this, "error volley : ${error.toString()}", Toast.LENGTH_LONG).show()
//                Log.d("StatusError" ,"error volley : ${error.toString()}")
//            })
//
//        queue.add(jsonObjectRequest)
//    }

}


