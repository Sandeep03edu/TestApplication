package com.example.testapplication

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.content.Context
import android.content.Intent
import android.net.Uri
import java.io.File
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.security.cert.CertificateException
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

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

        fetchDataInsecurely("https://google.com")
        saveSensitiveDataInsecurely(applicationContext, "sensitive.txt", "This is sensitive data")
        fetchDataIgnoringSsl("https://example.com")
        openUrlInsecurely("http://example.com", applicationContext)
    }


    fun fetchDataInsecurely(urlString: String): String? {
        var result: String? = null
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()

            result = response.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }


    fun saveSensitiveDataInsecurely(context: Context, filename: String, data: String) {
        try {
            val file = File(context.filesDir, filename)
            file.writeText(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun fetchDataIgnoringSsl(url: String): String? {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        val client = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier(HostnameVerifier { _, _ -> true })
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

        val request = Request.Builder().url(url).build()
        return client.newCall(request).execute().body?.string()
    }

    fun openUrlInsecurely(url: String, context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}