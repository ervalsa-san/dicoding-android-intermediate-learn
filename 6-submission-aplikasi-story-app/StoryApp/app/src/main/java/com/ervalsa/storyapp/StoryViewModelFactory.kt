package com.ervalsa.storyapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ervalsa.storyapp.data.StoryRepository
import com.ervalsa.storyapp.di.Injection
import com.ervalsa.storyapp.ui.main.StoryViewModel

class StoryViewModelFactory(
    private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory(){

    companion object {
        @Volatile
        private var instance: StoryViewModelFactory? = null

        fun getInstance(context: Context): StoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoryViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unkown ViewModel class: " + modelClass.name)
        }
    }
}