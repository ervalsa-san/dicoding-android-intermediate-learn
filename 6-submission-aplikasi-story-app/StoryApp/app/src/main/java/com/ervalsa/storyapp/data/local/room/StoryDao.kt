package com.ervalsa.storyapp.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ervalsa.storyapp.data.remote.response.story.StoryItem

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStories(stories: List<StoryItem>)
}