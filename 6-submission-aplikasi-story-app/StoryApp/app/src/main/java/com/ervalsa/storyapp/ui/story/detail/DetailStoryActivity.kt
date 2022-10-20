package com.ervalsa.storyapp.ui.story.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ervalsa.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}