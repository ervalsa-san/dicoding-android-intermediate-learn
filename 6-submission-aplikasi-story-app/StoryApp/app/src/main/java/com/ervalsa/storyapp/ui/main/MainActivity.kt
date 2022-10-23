package com.ervalsa.storyapp.ui.main

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ervalsa.storyapp.ViewModelFactory
import com.ervalsa.storyapp.data.local.UserPreference
import com.ervalsa.storyapp.data.remote.response.story.StoryResponse
import com.ervalsa.storyapp.data.remote.retrofit.ApiConfig
import com.ervalsa.storyapp.databinding.ActivityMainBinding
import com.ervalsa.storyapp.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ListStoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        binding.btnLogout.setOnClickListener {
            mainViewModel.logout()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun setListStory(token: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getAllStory(token)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    mainViewModel.listStory.postValue(response.body()?.listStory)
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure : ${t.message}")
            }

        })
    }

    private fun setDataUser() {
        mainViewModel.getUser().observe(this) { user->
            if (user.isLogin) {
                binding.tvTitle.text = "Selamat datang,\n${user.name}"
                val token = "Bearer ${user.token}"
                Log.e(TAG, user.token)
                val storyAdapter = ListStoryAdapter()
                binding.apply {
                    rvListStory.layoutManager = LinearLayoutManager(this@MainActivity)
                    rvListStory.adapter = storyAdapter
                    setListStory(token)
                }
            }
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                UserPreference.getInstance(dataStore)))[MainViewModel::class.java]

        setDataUser()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}