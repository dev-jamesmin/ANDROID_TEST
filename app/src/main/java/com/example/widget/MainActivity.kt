package com.example.widget

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity() {

    private lateinit var createQRBtn: Button
    private val scanQRBtn: Button? = null
    lateinit var option: SharedPreferences
    lateinit var userInfo: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        window.addFlags(windowManager.updateViewLayout())


//        MyApplication.prefs.setString("email", "abcd@gmail.com")
//        prefs.setString("email", "abcd@gmail.com")

        var dataEmail:String = MyApplication.prefs.getString("email", "no email")
        println("[SP_DEBUG] dataEmail MainActivity--->:"+ dataEmail)
        createQRBtn = findViewById(R.id.createQR)

        createQRBtn.setOnClickListener(View.OnClickListener {
            println("[SP_DEBUG] CreateQR")
            val intent = Intent(this@MainActivity, CreateQR::class.java)
            startActivity(intent)
        })



        // Create URL
        // Create URL
        val githubEndpoint = URL("https://api.github.com/")

// Create connection

// Create connection
        val myConnection: HttpsURLConnection = githubEndpoint.openConnection() as HttpsURLConnection

        myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");

        myConnection.setRequestProperty("Accept",
            "application/vnd.github.v3+json");
        myConnection.setRequestProperty("Contact-Me",
            "hathibelagal@example.com");


    }

}