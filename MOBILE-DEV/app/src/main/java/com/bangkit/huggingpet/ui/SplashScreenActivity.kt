package com.bangkit.huggingpet.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bangkit.huggingpet.databinding.ActivitySplashScreenBinding
import com.bangkit.huggingpet.preference.Preference
import com.bangkit.huggingpet.viewmodel.DataStoreViewModel
import com.bangkit.huggingpet.viewmodel.ViewModelFactory

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = Preference.getInstance(dataStore)
        val loginViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[DataStoreViewModel::class.java]

        loginViewModel.getLoginSession().observe(this) { isLoggedIn ->

            val intent = if (isLoggedIn) {
                Intent(this, HomeActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }

            startActivity(intent)
            finish()
           }
      }
}