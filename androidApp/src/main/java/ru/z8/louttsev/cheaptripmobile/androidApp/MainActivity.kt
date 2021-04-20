package ru.z8.louttsev.cheaptripmobile.androidApp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.z8.louttsev.cheaptripmobile.shared.convertToString
import ru.z8.louttsev.cheaptripmobile.shared.model.data.TransportationType

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        convertToString = { toString(this@MainActivity) }

        setContentView(R.layout.activity_main)

        val tt = TransportationType.BUS
        val tv = findViewById<TextView>(R.id.text_view)
        val iv = findViewById<ImageView>(R.id.image_view)
        tv.text = tt.toString()
        iv.setImageResource(tt.imageResource.drawableResId)
    }
}
