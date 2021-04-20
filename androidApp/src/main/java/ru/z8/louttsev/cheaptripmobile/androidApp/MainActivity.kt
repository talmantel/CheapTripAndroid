package ru.z8.louttsev.cheaptripmobile.androidApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.z8.louttsev.cheaptripmobile.shared.convertToString

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        convertToString = { toString(this@MainActivity) }

        setContentView(R.layout.activity_main)
    }
}
