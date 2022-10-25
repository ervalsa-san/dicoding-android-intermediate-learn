package com.ervalsa.storyapp.ui.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ervalsa.storyapp.R
import com.ervalsa.storyapp.databinding.ActivityAddStoryBinding
import com.ervalsa.storyapp.ui.main.MainActivity

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = ""
        actionBar?.elevation = 0f
    }
}