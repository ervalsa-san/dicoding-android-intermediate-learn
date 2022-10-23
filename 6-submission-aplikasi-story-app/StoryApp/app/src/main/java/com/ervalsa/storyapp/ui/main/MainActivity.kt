package com.ervalsa.storyapp.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ervalsa.storyapp.ViewModelFactory
import com.ervalsa.storyapp.data.local.UserPreference
import com.ervalsa.storyapp.databinding.ActivityMainBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainviewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        loginCheck()

        binding.btnLogout.setOnClickListener {
            mainviewModel.logout()
        }
    }

    private fun loginCheck() {
        mainviewModel.getUser().observe(this) { user->
            if (user.isLogin) {
                binding.tvTitle.text = "Selamat datang, ${user.name}"
            }
        }
    }

    private fun setupViewModel() {
        mainviewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                UserPreference.getInstance(dataStore)))[MainViewModel::class.java]
    }
}