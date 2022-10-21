package com.ervalsa.storyapp.data.model

data class Story(
    var id: String,
    var name: String,
    var description: String,
    var photoUrl: String,
    var createdAt: String,
    var lat: Float,
    var lon: Float
)
