package com.ervalsa.storyapp.ui.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.ervalsa.storyapp.ViewModelFactory
import com.ervalsa.storyapp.data.local.datastore.UserPreference
import com.ervalsa.storyapp.databinding.ActivitySplashBinding
import com.ervalsa.storyapp.ui.login.LoginActivity
import com.ervalsa.storyapp.ui.login.LoginViewModel
import com.ervalsa.storyapp.ui.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SplashActivity : AppCompatActivity() {

    companion object {
        const val timer = 2000
    }

    lateinit var handler: Handler

    private lateinit var binding: ActivitySplashBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler()
        handler.postDelayed({
            setupViewModel()
        }, timer.toLong())
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                UserPreference
                    .getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginCheck()
    }

    private fun loginCheck() {
        loginViewModel.getUser().observe(this) { user->
            if (user.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}