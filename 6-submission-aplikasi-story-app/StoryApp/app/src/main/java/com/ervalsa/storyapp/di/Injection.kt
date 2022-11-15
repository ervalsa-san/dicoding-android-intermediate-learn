package com.ervalsa.storyapp.di

import android.content.Context
import com.ervalsa.storyapp.data.StoryRepository
import com.ervalsa.storyapp.data.local.room.StoryDatabase
import com.ervalsa.storyapp.data.remote.retrofit.ApiConfig
import com.ervalsa.storyapp.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context) : StoryRepository{
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getInstance(context)
        val dao = database.storyDao()
        val appExecutors = AppExecutors()
        return StoryRepository.getInstance(apiService, dao, appExecutors)
    }
}