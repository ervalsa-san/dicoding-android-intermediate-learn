package com.ervalsa.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.ervalsa.storyapp.R
import com.ervalsa.storyapp.data.remote.response.story.StoryItem
import com.ervalsa.storyapp.databinding.ActivityDetailBinding
import com.ervalsa.storyapp.utils.withDateFormat

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private lateinit var binding: ActivityDetailBinding
    private var story: StoryItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = ""
        actionBar?.elevation = 0f
        actionBar?.setDisplayHomeAsUpEnabled(true)

        story = intent.getParcelableExtra(EXTRA_DATA)
        populateStory()

    }

    private fun populateStory() {
        binding.tvTitle.text = story?.name
        binding.tvDescription.text = story?.description
        story?.let { binding.tvCreateAt.withDateFormat(it.createdAt) }
        Glide.with(this)
            .load(story?.photoUrl)
            .placeholder(R.drawable.ic_place_holder)
            .into(binding.imgPhotoStory)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}