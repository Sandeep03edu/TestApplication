package com.example.testapplication

import android.content.Context
import android.content.SharedPreferences
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


        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("API_KEY", mobSfAPIKey)
        editor.apply()

        var key = sharedPreferences.getString("API_KEY", null)
        key += "11";
    }
}