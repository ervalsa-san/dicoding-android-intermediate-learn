package com.ervalsa.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.ervalsa.storyapp.BuildConfig
import com.ervalsa.storyapp.data.remote.response.story.ListStoryItem
import com.ervalsa.storyapp.data.remote.retrofit.ApiService
import com.ervalsa.storyapp.utils.AppExecutors

//class StoryRepository private constructor(
//    private val apiService: ApiService,
//    private val appExecutors: AppExecutors
//) {
//    companion object {
//        @Volatile
//        private var instance: StoryRepository? = null
//        fun getInstance(
//            apiService: ApiService
//        ) : StoryRepository =
//            instance ?: synchronized(this) {
//                instance ?: StoryRepository(apiService, appExecutors)
//            }.also { instance = it }
//    }
//
//    private val result = MediatorLiveData<Result<List<ListStoryItem>>>()
//
//    fun getAllStory(): LiveData<Result<List<ListStoryItem>>> {
//        result.value = Result.Loading
//        val client = apiService.getAllStory(BuildConfig.BASE_URL)
//        return result
//    }
//}