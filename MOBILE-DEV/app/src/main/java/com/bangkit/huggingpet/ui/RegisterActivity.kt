package com.bangkit.huggingpet.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.huggingpet.R
import com.bangkit.huggingpet.databinding.ActivityRegisterBinding
import com.bangkit.huggingpet.dataclass.LoginDataAccount
import com.bangkit.huggingpet.dataclass.RegisterDataAccount
import com.bangkit.huggingpet.preference.Preference
import com.bangkit.huggingpet.viewmodel.DataStoreViewModel
import com.bangkit.huggingpet.viewmodel.MainViewModel
import com.bangkit.huggingpet.viewmodel.MainViewModelFactory
import com.bangkit.huggingpet.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }

    private val loginViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.createAccount)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ifClicked()

        val pref = Preference.getInstance(dataStore)
        val dataStoreViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[DataStoreViewModel::class.java]
        dataStoreViewModel.getLoginSession().observe(this) { sessionTrue ->
            if (sessionTrue) {
                val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        registerViewModel.message.observe(this) { messageRegist ->
            responseRegister(
                messageRegist
            )
        }

        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        loginViewModel.message.observe(this) { messageLogin ->
            responseLogin(
                messageLogin,
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
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun responseRegister(
        message: String,
    ) {
        if (message == "Account Created Successfully") {
            Toast.makeText(
                this,
                resources.getString(R.string.accountSuccessCreated),
                Toast.LENGTH_SHORT
            ).show()
            val userLogin = LoginDataAccount(
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString()
            )
            loginViewModel.login(userLogin)
        } else {
            if (message.contains("Email yang anda masukan sudah terdaftar")) {
                binding.inputEmail.setErrorMessage(
                    resources.getString(R.string.emailTaken),
                    binding.inputPassword.text.toString()
                )
                Toast.makeText(this, resources.getString(R.string.emailTaken), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ifClicked() {
        binding.btnRegister.setOnClickListener {
            binding.apply {
                inputName.clearFocus()
                inputEmail.clearFocus()
                inputPassword.clearFocus()
                inputPasswordsame.clearFocus()
            }

            if (binding.inputName.isNameValid && binding.inputEmail.isEmailValid && binding.inputPassword.isPasswordValid && binding.inputPasswordsame.isPasswordSameValid) {
                val dataRegisterAccount = RegisterDataAccount(
                    name = binding.inputName.text.toString().trim(),
                    email = binding.inputEmail.text.toString().trim(),
                    password = binding.inputPassword.text.toString().trim()
                )

                registerViewModel.register(dataRegisterAccount)
            } else {
                if (!binding.inputName.isNameValid) binding.inputName.error =
                    resources.getString(R.string.nameNone)
                if (!binding.inputEmail.isEmailValid) binding.inputEmail.error =
                    resources.getString(R.string.emailNone)
                if (!binding.inputPassword.isPasswordValid) binding.inputPassword.error =
                    resources.getString(R.string.passwordNone)
                if (!binding.inputPasswordsame.isPasswordSameValid) binding.inputPasswordsame.error =
                    resources.getString(R.string.passwordConfirmNone)

                Toast.makeText(this, R.string.invalidLogin, Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        finish()
        return super.onSupportNavigateUp()
    }
}