package com.ervalsa.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ervalsa.storyapp.data.entity.UserEntity
import com.ervalsa.storyapp.data.local.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(private val preferences: UserPreference) : ViewModel() {

    fun login() {
        viewModelScope.launch {
            preferences.login()
        }
    }

    fun saveUser(user: UserEntity) {
        viewModelScope.launch {
            preferences.saveUser(user)
        }
    }
}