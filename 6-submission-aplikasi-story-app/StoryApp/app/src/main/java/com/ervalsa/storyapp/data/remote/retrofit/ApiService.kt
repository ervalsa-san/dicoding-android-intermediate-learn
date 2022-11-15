package com.ervalsa.storyapp.data.remote.retrofit

import com.ervalsa.storyapp.data.remote.response.auth.LoginResponse
import com.ervalsa.storyapp.data.remote.response.auth.RegisterResponse
import com.ervalsa.storyapp.data.remote.response.story.FileUploadResponse
import com.ervalsa.storyapp.data.remote.response.story.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<RegisterResponse>

    @GET("stories")
    fun getAllStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ) : Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>
}