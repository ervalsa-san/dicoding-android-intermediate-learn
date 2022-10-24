package com.ervalsa.storyapp.ui.main

import androidx.lifecycle.ViewModel
import com.ervalsa.storyapp.data.StoryRepository


class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getAllStories(token: String) = storyRepository.getAllStories(token)
}