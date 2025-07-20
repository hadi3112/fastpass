package com.example.fastpass

import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.ChipGroup
import com.google.android.material.chip.Chip
import androidx.core.content.ContextCompat
import android.view.View
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator


class PreferencesActivity: AppCompatActivity() {
    private val suggestionMap = mapOf(
        "Food" to R.drawable.food_icon,
        "Competitions" to R.drawable.food_icon,
        "Art" to R.drawable.food_icon,
        "Sports" to R.drawable.food_icon,
        "Coding" to R.drawable.food_icon,
        "Competitiona" to R.drawable.food_icon,
        "Music" to R.drawable.food_icon,
        "Conferences" to R.drawable.food_icon,

        "Job Fairs" to R.drawable.food_icon,
        "Hackathons" to R.drawable.food_icon,
        "Entrepreneurship" to R.drawable.food_icon,
        "Battle of Bands" to R.drawable.food_icon
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        showInitialChipsWithRandomColors()

        val progressBar = findViewById<LinearProgressIndicator>(R.id.progressBar)

        // Set progress directly (no animation)
        progressBar.progress = 33 // Set to 50%


        val searchBar = findViewById<AutoCompleteTextView>(R.id.searchAutoComplete)
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup)


        // Show all suggestions immediately
        searchBar.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) searchBar.showDropDown()
        }

        // Optional: Listen for item selection
        searchBar.setOnItemClickListener { parent, view, position, id ->
            val selected = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, "Selected: $selected", Toast.LENGTH_SHORT).show()
        }


        val suggestionList = suggestionMap.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, suggestionList)
        searchBar.setAdapter(adapter)

        // Handle selection
        searchBar.setOnItemClickListener { _, _, position, _ ->
            val selectedText = adapter.getItem(position)
            selectedText?.let {
                addChip(it)
                searchBar.text.clear()
            }
        }

        val button: MaterialButton = findViewById(R.id.continue_button)
        button.setOnClickListener {
            val intent = Intent(this, OtpVerification::class.java)
            startActivity(intent)

        }

    }
    private fun addChip(label: String) {
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup)
        val iconRes = suggestionMap[label] ?: R.drawable.profile

        val chip = Chip(this).apply {
            text = label
            chipIcon = ContextCompat.getDrawable(context, iconRes)
            chipIconSize = 18f
            chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.light_grey)
            chipStrokeColor = ContextCompat.getColorStateList(context, R.color.light_grey)
            isCloseIconVisible = true
            isCheckable = false
            chipCornerRadius = 40f
            setTextColor(ContextCompat.getColorStateList(context, R.color.white))
            setOnCloseIconClickListener { chipGroup.removeView(this) }
        }

        chip.chipIconSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            18f,  // change this value as needed
            resources.displayMetrics
        )
        chipGroup.addView(chip)
    }

    private fun showInitialChipsWithRandomColors() {
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup2)

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

        for ((index, entry) in limitedSuggestions.withIndex()) {
            val (label, iconRes) = entry
            val colorIndex = index % colorIds.size
            val colorId = colorIds[colorIndex]

            val colorStateList = ContextCompat.getColorStateList(this, colorId)
            val textColor = ContextCompat.getColor(this, android.R.color.white)

            val chip = Chip(this).apply {
                text = label
                chipIcon = ContextCompat.getDrawable(context, iconRes)
                chipIconSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    18f, // slightly larger than default
                    resources.displayMetrics
                )
                chipBackgroundColor = colorStateList
                chipStrokeColor = colorStateList
                chipStrokeWidth = 2f
                setTextColor(textColor)

                isCloseIconVisible = false
                isCheckable = false
                chipCornerRadius = 40f
            }

            chipGroup.addView(chip)
        }
    }

}