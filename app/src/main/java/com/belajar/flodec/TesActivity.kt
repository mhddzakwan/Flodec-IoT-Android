package com.belajar.flodec

//import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat

import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tes)

        val mainLayout = findViewById<FrameLayout>(R.id.main_layout)
        val textView = findViewById<TextView>(R.id.textView)

        textView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Menampilkan TextView baru di atasnya
                    showFloatingTextView(mainLayout, event.rawX, event.rawY, textView)
                }
                MotionEvent.ACTION_UP -> {
                    // Menghapus TextView yang ditampilkan
                    removeFloatingTextView(mainLayout)
                }
            }
            true
        }
    }

    private fun showFloatingTextView(parent: ViewGroup, x: Float, y: Float, textView: TextView) {
        val floatingTextView = TextView(this)
        floatingTextView.text = "Text yang ingin Anda tampilkan"
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        val location = IntArray(2)
        textView.getLocationOnScreen(location)

        val x = location[0]
        val y = location[1]

        params.leftMargin = 200
        params.topMargin = y-textView.height-50// Menampilkan di atas TextView yang di-hover
        floatingTextView.layoutParams = params
        parent.addView(floatingTextView)
    }

    private fun removeFloatingTextView(parent: ViewGroup) {
        if (parent.childCount > 0) {
            parent.removeViewAt(parent.childCount - 1)
        }
    }
}
