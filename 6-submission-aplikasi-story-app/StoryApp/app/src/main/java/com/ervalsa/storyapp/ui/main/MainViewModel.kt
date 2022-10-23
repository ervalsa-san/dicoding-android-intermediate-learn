package com.ervalsa.storyapp.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.ervalsa.storyapp.data.entity.UserEntity
import com.ervalsa.storyapp.data.local.datastore.UserPreference
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

    fun getAllStories() : LiveData<List<StoryItem>> = listStory

    fun setListStory(token: String, page: Int? = null, size: Int? = null) {
        val client = ApiConfig.getApiService().getAllStory(token, page, size)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    listStory.postValue(responseBody.listStory)
                    Log.e(TAG, responseBody.message)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })
    }
}