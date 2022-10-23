package com.ervalsa.storyapp.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.ervalsa.storyapp.ui.main.MainActivity
import com.ervalsa.storyapp.ViewModelFactory
import com.ervalsa.storyapp.data.entity.UserEntity
import com.ervalsa.storyapp.data.local.UserPreference
import com.ervalsa.storyapp.data.remote.response.auth.LoginResponse
import com.ervalsa.storyapp.data.remote.retrofit.ApiConfig
import com.ervalsa.storyapp.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    companion object {
        private val TAG = "LoginActivity"
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        binding.btnLogin.setOnClickListener {
            setupLogin()
        }
    }

    private fun setupLogin() {
        val email = binding.edtInputEmail.text.toString()
        val password = binding.edtInputPassword.text.toString()
        when {
            email.isEmpty() -> {
                binding.edtInputEmail.error = "Email tidak boleh kosong"
            }

            password.isEmpty() -> {
                binding.edtInputPassword.error = "Password tidak boleh kosong"
            }

            else -> {
                loginViewModel.login()
                loginAuthentication()
            }
        }
    }

    private fun loginAuthentication() {
        showLoading(true)
        val client = ApiConfig
            .getApiService()
            .postLogin(
                binding.edtInputEmail.text.toString(),
                binding.edtInputPassword.text.toString()
            )

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    showLoading(false)
                    loginViewModel.saveUser(
                        UserEntity(
                            responseBody.loginResult.name,
                            responseBody.loginResult.token,
                            isLogin = true
                        )
                    )
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                    Log.e(TAG, responseBody.loginResult.token)
                } else {
                    showLoading(false)
                    Toast.makeText(
                        this@LoginActivity,
                        "Login gagal, Email atau Password salah",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(
                    this@LoginActivity,
                    "Login gagal, Email atau Password salah",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "onFailure ${t.message}")
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                UserPreference
                    .getInstance(dataStore)))[LoginViewModel::class.java]
    }
}