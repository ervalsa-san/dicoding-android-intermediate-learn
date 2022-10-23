package com.ervalsa.storyapp.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ervalsa.storyapp.StoryViewModelFactory
import com.ervalsa.storyapp.ViewModelFactory
import com.ervalsa.storyapp.data.Result
import com.ervalsa.storyapp.data.local.datastore.UserPreference
import com.ervalsa.storyapp.databinding.ActivityMainBinding
import com.ervalsa.storyapp.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setDataUser()

        binding.btnLogout.setOnClickListener {
            mainViewModel.logout()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setToken(token: String) {
        binding.apply {
            showLoading(true)
            mainViewModel.setListStory(token, null, null)
        }
    }

    private fun setDataUser() {
        mainViewModel.getUser().observe(this) { user->
            if (user.isLogin) {
                showLoading(true)
                binding.tvTitle.text = "Selamat datang,\n${user.name}"
                val token = "Bearer ${user.token}"
                setToken(token)
                mainViewModel.getAllStories().observe(this) { stories ->
                    if (stories != null) {
                        showLoading(false)
                        with(binding.rvListStory) {
                            val storyAdapter = ListStoryAdapter()
                            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                            adapter = storyAdapter
                            setHasFixedSize(true)
                        }
                    }
                }
                Log.e(TAG, user.token)
            }
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                UserPreference.getInstance(dataStore)))[MainViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}