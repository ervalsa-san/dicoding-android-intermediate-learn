package com.ervalsa.storyapp.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class UserEntity (
    val name: String,
    val token: String,
    val isLogin: Boolean
)