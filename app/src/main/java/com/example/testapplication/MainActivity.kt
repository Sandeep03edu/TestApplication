package com.example.testapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val mobSfAPIKey = "a51aa880c30337977d0107ba437c14cd41068be4f46f5c254acf0d7aa476ef23";
        println("mobSfAPI_Key: $mobSfAPIKey");
    }
}