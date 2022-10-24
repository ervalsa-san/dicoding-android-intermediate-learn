package com.ervalsa.storyapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ervalsa.storyapp.data.remote.response.story.StoryItem

@Dao
interface StoryDao {

    @Query("SELECT * FROM story")
    fun getStories(): LiveData<List<StoryItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStories(stories: List<StoryItem>)
}