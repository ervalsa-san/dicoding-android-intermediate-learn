package com.ervalsa.storyapp.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ervalsa.storyapp.data.entity.UserEntity
import com.ervalsa.storyapp.data.remote.retrofit.ApiConfig
import com.ervalsa.storyapp.data.remote.response.auth.RegisterResponse
import com.ervalsa.storyapp.databinding.ActivityRegisterBinding
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

        binding.btnRegister.setOnClickListener { view ->
            postRegister()
        }
    }

    private fun postRegister() {
        showLoading(true)

        val name = binding.edtInputNama.toString()
        val email = binding.edtInputEmail.toString()
        val password = binding.edtInputPassword.toString()

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
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
//                    setRegisterData(User)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(
                call: Call<RegisterResponse>,
                t: Throwable
            ) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

//    private fun setRegisterData(user: UserEntity) {
//        val userData = ApiConfig.getApiService().postRegister(
//            user.name,
//            user.email,
//            user.password
//        )
//    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}