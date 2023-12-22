package com.bangkit.huggingpet.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.huggingpet.R
import com.bangkit.huggingpet.databinding.ActivityLoginBinding
import com.bangkit.huggingpet.dataclass.LoginDataAccount
import com.bangkit.huggingpet.preference.Preference
import com.bangkit.huggingpet.viewmodel.DataStoreViewModel
import com.bangkit.huggingpet.viewmodel.MainViewModel
import com.bangkit.huggingpet.viewmodel.MainViewModelFactory
import com.bangkit.huggingpet.viewmodel.ViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ifClicked()

        val preferences = Preference.getInstance(dataStore)
        val dataStoreViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[DataStoreViewModel::class.java]

        dataStoreViewModel.getLoginSession().observe(this) { sessionTrue ->
            if (sessionTrue) {
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        loginViewModel.message.observe(this) { message ->
            responseLogin(
                message,
                dataStoreViewModel
            )
        }

        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun responseLogin(
        message: String,
        dataStoreViewModel: DataStoreViewModel
    ) {
        if (message.contains("Hello")) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            val user = loginViewModel.userlogin.value
            dataStoreViewModel.saveLoginSession(true)
            dataStoreViewModel.saveToken(user?.loginResult!!.token)
            dataStoreViewModel.saveName(user.loginResult.userId)

            // Start ProfileActivity and pass user's name
            val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
            intent.putExtra("userName", user.loginResult.userId)
            startActivity(intent)
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun ifClicked() {
        binding.btnLogin.setOnClickListener {
            binding.inputEmail.clearFocus()
            binding.inputPassword.clearFocus()

            if (isDataValid()) {
                val requestLogin = LoginDataAccount(
                    binding.inputEmail.text.toString().trim(),
                    binding.inputPassword.text.toString().trim()
                )
                loginViewModel.login(requestLogin)
            } else {
                if (!binding.inputEmail.isEmailValid) binding.inputEmail.error =
                    getString(R.string.emailNone)
                if (!binding.inputPassword.isPasswordValid) binding.inputPassword.error =
                    getString(R.string.passwordNone)

                Toast.makeText(this, R.string.invalidLogin, Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isDataValid(): Boolean {
        return binding.inputEmail.isEmailValid && binding.inputPassword.isPasswordValid
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}