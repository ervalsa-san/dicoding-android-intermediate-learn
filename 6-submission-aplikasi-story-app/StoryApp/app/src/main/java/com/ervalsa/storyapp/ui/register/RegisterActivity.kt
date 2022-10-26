package com.ervalsa.storyapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.ervalsa.storyapp.data.remote.retrofit.ApiConfig
import com.ervalsa.storyapp.data.remote.response.auth.RegisterResponse
import com.ervalsa.storyapp.databinding.ActivityRegisterBinding
import com.ervalsa.storyapp.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    companion object {
        private val TAG = "RegisterActivity"
    }

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        animation()

        binding.btnRegister.setOnClickListener {
            setupRegister()
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRegister() {
        val name = binding.edtInputNama.text.toString()
        val email = binding.edtInputEmail.text.toString()
        val password = binding.edtInputPassword.text.toString()

        when {
            name.isEmpty() -> {
                binding.edtInputNama.error = "Nama tidak boleh kosong"
            }

            email.isEmpty() -> {
                binding.edtInputEmail.error = "Email tidak boleh kosong"
            }

            password.isEmpty() -> {
                binding.edtInputPassword.error = "Password tidak boleh kosong"
            }

            else -> {
                registerAuthentication()
            }
        }
    }

    private fun registerAuthentication() {
        val name = binding.edtInputNama.text.toString()
        val email = binding.edtInputEmail.text.toString()
        val password = binding.edtInputPassword.text.toString()

        showLoading(true)
        val client = ApiConfig
            .getApiService()
            .postRegister(
                name,
                email,
                password
            )

        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    showLoading(false)
                    AlertDialog.Builder(this@RegisterActivity).apply {
                        setTitle("Selamat!")
                        setMessage("Akun anda sudah jadi, mari login dan buat cerita menarik.")
                        setPositiveButton("Lanjut") { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                } else {
                    showLoading(false)
                    Log.e(TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showLoading(false)
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

    private fun animation() {
        val title = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)
        val cardView = ObjectAnimator.ofFloat(binding.cardView, View.ALPHA, 1f).setDuration(500)
        val illustration = ObjectAnimator.ofFloat(binding.imgIllustration, View.ALPHA, 1f).setDuration(500)
        val tvTitleName = ObjectAnimator.ofFloat(binding.tvTitleName, View.ALPHA, 1f).setDuration(500)
        val layoutName = ObjectAnimator.ofFloat(binding.layoutInputNama, View.ALPHA, 1f).setDuration(500)
        val tvTitleEmail = ObjectAnimator.ofFloat(binding.tvTitleEmail, View.ALPHA, 1f).setDuration(500)
        val layoutEmail = ObjectAnimator.ofFloat(binding.layoutInputEmail, View.ALPHA, 1f).setDuration(500)
        val tvTitlePassword = ObjectAnimator.ofFloat(binding.tvTitlePassword, View.ALPHA, 1f).setDuration(500)
        val layoutPassword = ObjectAnimator.ofFloat(binding.layoutInputPassword, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                cardView,
                illustration,
                tvTitleName,
                layoutName,
                tvTitleEmail,
                layoutEmail,
                tvTitlePassword,
                layoutPassword,
                btnRegister,
                btnLogin
            )
            start()
        }
    }
}