package com.ervalsa.storyapp.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ervalsa.storyapp.data.entity.UserEntity
import com.ervalsa.storyapp.data.local.UserPreference
import kotlinx.coroutines.launch

class RegisterViewModel(private val preference: UserPreference) : ViewModel() {

    fun saveUser(user: UserEntity) {
        viewModelScope.launch {
            preference.saveUser(user)
        }
    }
}