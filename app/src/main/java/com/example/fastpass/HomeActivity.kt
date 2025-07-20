package com.example.fastpass

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.firestore.FirebaseFirestore
import com.bumptech.glide.request.target.Target
import com.example.fastpass.databinding.ActivityHomeBinding

import com.google.firebase.storage.FirebaseStorage


class HomeActivity: AppCompatActivity() {

    private val suggestionMap = mapOf(
        "Food" to R.drawable.food_icon,
        "Competitions" to R.drawable.food_icon,
        "Art" to R.drawable.art_icon,
        "Sports" to R.drawable.food_icon,
        "Coding" to R.drawable.food_icon,
        "Competitiona" to R.drawable.food_icon,
        "Music" to R.drawable.music_icon,
        "Conferences" to R.drawable.food_icon,

        "Job Fairs" to R.drawable.food_icon,
        "Hackathons" to R.drawable.food_icon,
        "Entrepreneurship" to R.drawable.food_icon,
        "Battle of Bands" to R.drawable.music_icon
    )

    private lateinit var binding: ActivityHomeBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UpcomingEventAdapter
    private val eventList = mutableListOf<Event>()
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showInitialChipsWithRandomColors()


        recyclerView = findViewById(R.id.upcomingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        //adapter = UpcomingEventAdapter(eventList)
        adapter = UpcomingEventAdapter(eventList) { selectedEvent ->
            val intent = Intent(this, EventDetailActivity::class.java).apply {
                putExtra("name", selectedEvent.name)
                putExtra("poster", selectedEvent.poster)
                putExtra("desc", selectedEvent.desc)
                putExtra("ticket_price", selectedEvent.ticket_price)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        setupRecyclerView()

        fetchEventsFromFirestore()


        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val hamburgerIcon = findViewById<ImageView>(R.id.hamburger_icon)

        binding.hamburgerIcon.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }


    }

    private fun showInitialChipsWithRandomColors() {
        val chipGroup = findViewById<ChipGroup>(R.id.chip_group)

        val colorIds = listOf(
            R.color.green,
            R.color.yellow,
            R.color.light_orange,
            R.color.parrot_green,
            R.color.red,
            R.color.pinkish_red,
            R.color.blue,
            R.color.light_grey
        )

        val limitedSuggestions = suggestionMap.entries.take(8)

        limitedSuggestions.forEachIndexed { index, (label, iconRes) ->
            val colorId = colorIds[index % colorIds.size]
            val chipColor = ContextCompat.getColorStateList(this, colorId)
            val chipTextColor = ContextCompat.getColor(this, android.R.color.white)

            val chip = Chip(this).apply {
                text = label
                chipIcon = ContextCompat.getDrawable(context, iconRes)
                chipIconSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 18f, resources.displayMetrics
                )
                chipBackgroundColor = chipColor
                chipStrokeColor = chipColor
                chipStrokeWidth = 2f
                setTextColor(chipTextColor)
                isCloseIconVisible = false
                isCheckable = false
                chipCornerRadius = 40f
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = 8 // use an extension or convert manually
                }
            }

            chipGroup.addView(chip)
        }
    }

    private fun fetchEventsFromFirestore() {
        db.collection("events")
            .get()
            .addOnSuccessListener { result ->
                eventList.clear()
                for (document in result) {
                    val event = document.toObject(Event::class.java)
                    eventList.add(event)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Log or show error
            }
    }

    private fun setupRecyclerView() {
        adapter = UpcomingEventAdapter(eventList) { selectedEvent ->
            val intent = Intent(this, EventDetailActivity::class.java).apply {
                putExtra("name", selectedEvent.name)
                putExtra("poster", selectedEvent.poster)
                putExtra("desc", selectedEvent.desc)
                putExtra("ticket_price", selectedEvent.ticket_price)
            }
            startActivity(intent)
        }

        binding.upcomingRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.upcomingRecyclerView.adapter = adapter
    }



    class UpcomingEventAdapter(private val events: List<Event>, private val onItemClick: (Event) -> Unit) :
        RecyclerView.Adapter<UpcomingEventAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val eventImage: ImageView = view.findViewById(R.id.eventImage)
            val imageProgress: CircularProgressIndicator = view.findViewById(R.id.imageProgress)
            val eventTitle: TextView = view.findViewById(R.id.eventTitle)
            // Add more views if needed
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_upcoming_event, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = events.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val event = events[position]  // Assuming you updated your adapter for Event list

            holder.eventTitle.text = event.name
            holder.eventImage.visibility = View.INVISIBLE
            holder.imageProgress.visibility = View.VISIBLE

            Glide.with(holder.itemView.context)
                .load(event.poster)
                .centerCrop()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.imageProgress.visibility = View.GONE
                        holder.eventImage.visibility = View.VISIBLE
                        holder.eventImage.setImageResource(R.drawable.placeholder_image)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.imageProgress.visibility = View.GONE
                        holder.eventImage.visibility = View.VISIBLE
                        return false
                    }
                })

                .into(holder.eventImage)

            holder.itemView.setOnClickListener {
                onItemClick(event)
            }

        }

    }



}