package com.example.fastpass

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fastpass.databinding.ActivityEventDetailBinding

class EventDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name") ?: "Untitled"
        val poster = intent.getStringExtra("poster") ?: ""
        val desc = intent.getStringExtra("desc") ?: ""
        val ticketPrice = intent.getIntExtra("ticket_price", 0)

        binding.eventTitleTextView.text = name
        binding.eventDesc.text = desc
        binding.ticketPriceTextView.text = "Rs $ticketPrice/-"

        Glide.with(this).load(poster).into(binding.posterImageView)
    }
}
