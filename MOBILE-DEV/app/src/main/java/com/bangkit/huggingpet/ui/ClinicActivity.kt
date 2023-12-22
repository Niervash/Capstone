package com.bangkit.huggingpet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.huggingpet.R

class ClinicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinic)
        supportActionBar?.title = "Veterinary Clinic"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}