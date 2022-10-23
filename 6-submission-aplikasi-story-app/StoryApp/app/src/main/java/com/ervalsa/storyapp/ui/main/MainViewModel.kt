package com.ervalsa.storyapp.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.ervalsa.storyapp.data.StoryRepository
import com.ervalsa.storyapp.data.entity.UserEntity
import com.ervalsa.storyapp.data.local.UserPreference
import com.ervalsa.storyapp.data.remote.response.story.StoryItem
import com.ervalsa.storyapp.data.remote.response.story.StoryResponse
import com.ervalsa.storyapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val preference: UserPreference) : ViewModel() {

    val listStory = MutableLiveData<List<StoryItem>>()

    fun getUser(): LiveData<UserEntity> =
        preference.getUser().asLiveData()

    fun logout() {
        viewModelScope.launch {
            preference.logout()
        }
    }
}