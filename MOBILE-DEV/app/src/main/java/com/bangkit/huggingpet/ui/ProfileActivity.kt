package com.bangkit.huggingpet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bangkit.huggingpet.R

class ProfileActivity : AppCompatActivity() {

    private lateinit var detailNameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Retrieve user's name from the intent
        val userName = intent.getStringExtra("userName")

        // Find the TextView by ID
        detailNameTextView = findViewById(R.id.detail_name)

        // Set the user's name to the TextView
        detailNameTextView.text = userName

        supportActionBar?.title = "Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}