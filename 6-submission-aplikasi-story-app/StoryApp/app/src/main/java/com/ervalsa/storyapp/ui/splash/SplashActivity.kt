package com.ervalsa.storyapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.ervalsa.storyapp.R
import com.ervalsa.storyapp.databinding.ActivitySplashBinding
import com.ervalsa.storyapp.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    companion object {
        const val timer = 2000
    }

    lateinit var handler: Handler

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, timer.toLong())
    }
}