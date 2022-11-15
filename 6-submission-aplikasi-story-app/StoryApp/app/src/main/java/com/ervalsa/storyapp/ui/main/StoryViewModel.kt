package com.ervalsa.storyapp.ui.main

import androidx.lifecycle.ViewModel
import com.ervalsa.storyapp.data.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody


class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getAllStories(token: String) = storyRepository.getAllStories(token)

    fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ) = storyRepository.addStory(token, file, description)
}