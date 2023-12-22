package com.bangkit.huggingpet.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bangkit.huggingpet.R
import com.bangkit.huggingpet.databinding.ActivityHomeBinding
import com.bangkit.huggingpet.preference.Preference
import com.bangkit.huggingpet.viewmodel.DataStoreViewModel
import com.bangkit.huggingpet.viewmodel.MainViewModel
import com.bangkit.huggingpet.viewmodel.MainViewModelFactory
import com.bangkit.huggingpet.viewmodel.ViewModelFactory

class HomeActivity : AppCompatActivity() {
    private val pref by lazy {
        Preference.getInstance(dataStore)
    }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var token: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Home Pet"
        ifClicked()

        val dataStoreViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[DataStoreViewModel::class.java]

        dataStoreViewModel.observeLoginSession().observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                // User is logged in, proceed with the usual flow
                dataStoreViewModel.getToken().observe(this) {
                    token = it
                }
            } else {
                // User is not logged in, redirect to login screen
                redirectToLogin()
            }
        }
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Close the current activity
    }

    private fun logout() {
        val loginViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[DataStoreViewModel::class.java]
        loginViewModel.clearDataLogin()
        Toast.makeText(this, R.string.SuccessLogout, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                showAlertDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun ifClicked() {
        binding.chatnow.setOnClickListener {
            startActivity(Intent(this, ConsultActivity::class.java))
        }
        binding.scanpet.setOnClickListener {
            startActivity(Intent(this, ScanPetActivity::class.java))
        }
        binding.findclinic.setOnClickListener {
            startActivity(Intent(this, ClinicActivity::class.java))
        }
        binding.petarticle.setOnClickListener {
            startActivity(Intent(this, PetArticleActivity::class.java))
        }
        binding.ivprofile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

    }
    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        val alert = builder.create()
        builder
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.logoutDescription))
            .setPositiveButton(getString(R.string.cancelLogout)) { _, _ ->
                alert.cancel()
            }
            .setNegativeButton(getString(R.string.yes)) { _, _ ->
                logout()
            }
            .show()
    }
}