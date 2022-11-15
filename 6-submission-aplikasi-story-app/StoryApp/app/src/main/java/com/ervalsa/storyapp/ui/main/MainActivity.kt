package com.ervalsa.storyapp.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
import com.ervalsa.storyapp.ui.story.AddStoryActivity

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

        supportActionBar?.hide()

        setupViewModel()
        val factory: StoryViewModelFactory = StoryViewModelFactory.getInstance(this)
        val storyViewModel: StoryViewModel by viewModels {
            factory
        }

        val storyAdapter = ListStoryAdapter()

        mainViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                binding.tvTitle.text = "Selamat datang,\n${user.name}"
                storyViewModel.getAllStories("Bearer ${user.token}").observe(this) { result ->
                    when(result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            val storyData = result.data
                            storyAdapter.submitList(storyData)
                        }

                        is Result.Error -> {
                            showLoading(false)
                        }
                    }
                }
            } else {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.rvListStory.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = storyAdapter
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this@MainActivity).apply {
                setTitle("Ingin keluar aplikasi?")
                setMessage("Apakah Anda yakin ingin mengeluarkan akun dari aplikasi?")
                setNegativeButton("Tidak jadi") {_, _ ->

                }
                setPositiveButton("Yakin") { _, _ ->
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    mainViewModel.logout()
                    finish()
                }
                create()
                show()
            }
        }

        binding.btnAddStory.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
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

    override fun onDestroy() {
        super.onDestroy()
    }
}