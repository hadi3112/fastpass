package com.example.fastpass

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator

class OtpVerification: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_verif)

        val progressBar = findViewById<LinearProgressIndicator>(R.id.progressBar)

        // Set progress directly (no animation)
        progressBar.progress = 67

        val button: MaterialButton = findViewById(R.id.sendotp_button)
        button.setOnClickListener {
            val intent = Intent(this, SendOtpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}