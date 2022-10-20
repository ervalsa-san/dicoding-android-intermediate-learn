package com.ervalsa.storyapp.ui.story.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ervalsa.storyapp.R
import com.ervalsa.storyapp.databinding.ActivityAddStoryBinding

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}