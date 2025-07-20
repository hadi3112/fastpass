package com.example.fastpass

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator

class SendOtpActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sendotp)

        val progressBar = findViewById<LinearProgressIndicator>(R.id.progressBar)

        // Set progress directly (no animation)
        progressBar.progress = 85

        val button: MaterialButton = findViewById(R.id.continue_button)
        button.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun setupOtpInputs() {
        val otp1 = findViewById<EditText>(R.id.otp1)
        val otp2 = findViewById<EditText>(R.id.otp2)
        val otp3 = findViewById<EditText>(R.id.otp3)
        val otp4 = findViewById<EditText>(R.id.otp4)

        otp1.addTextChangedListener { if (it?.length == 1) otp2.requestFocus() }
        otp2.addTextChangedListener { if (it?.length == 1) otp3.requestFocus() }
        otp3.addTextChangedListener { if (it?.length == 1) otp4.requestFocus() }
    }

}