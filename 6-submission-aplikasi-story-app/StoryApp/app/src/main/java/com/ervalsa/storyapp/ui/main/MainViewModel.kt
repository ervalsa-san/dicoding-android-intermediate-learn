package com.ervalsa.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ervalsa.storyapp.data.entity.UserEntity
import com.ervalsa.storyapp.data.local.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(private val preference: UserPreference) : ViewModel() {

    fun getUser(): LiveData<UserEntity> =
        preference.getUser().asLiveData()

    fun logout() {
        viewModelScope.launch {
            preference.logout()
        }
    }
}