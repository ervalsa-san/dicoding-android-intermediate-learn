package com.ervalsa.storyapp.data

import android.content.ContentValues.TAG
import android.nfc.Tag
import android.util.Log
import androidx.datastore.dataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.ervalsa.storyapp.BuildConfig
import com.ervalsa.storyapp.data.local.room.StoryDao
import com.ervalsa.storyapp.data.remote.response.story.StoryItem
import com.ervalsa.storyapp.data.remote.response.story.StoryResponse
import com.ervalsa.storyapp.data.remote.retrofit.ApiService
import com.ervalsa.storyapp.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val storyDao: StoryDao,
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<Result<List<StoryItem>>>()

    fun getAllStories(token: String): LiveData<Result<List<StoryItem>>> {
        result.value = Result.Loading
        val client = apiService.getAllStory(token, null, null)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful) {
                    val story = response.body()?.listStory
                    val storiesList = ArrayList<StoryItem>()
                    appExecutors.diskIO.execute {
                        story?.forEach { story ->
                            val stories = StoryItem(
                                story.id,
                                story.name,
                                story.description,
                                story.photoUrl,
                                story.createdAt,
                                story.lat,
                                story.lon
                            )
                            storiesList.add(stories)
                        }
                        storyDao.deleteAll()
                        storyDao.insertStories(storiesList)
                    }
                }
                Log.e(TAG, response.message())
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
                t.message?.let { Log.e(TAG, it) }
            }
        })

        val localData = storyDao.getStories()
        result.addSource(localData) { storyData: List<StoryItem> ->
            result.value = Result.Success(storyData)
        }
        return result
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            storyDao: StoryDao,
            appExecutors: AppExecutors
        ) : StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, storyDao, appExecutors)
            }.also { instance = it }
    }
}