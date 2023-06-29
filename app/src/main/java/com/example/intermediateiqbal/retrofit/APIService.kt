package com.example.intermediateiqbal.retrofit

import com.example.intermediateiqbal.retrofit.response.AddStoryResponse
import com.example.intermediateiqbal.retrofit.response.LoginResponse
import com.example.intermediateiqbal.retrofit.response.RegisterResponse
import com.example.intermediateiqbal.retrofit.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    @FormUrlEncoded
    @POST("register")
    fun registerUser(@Field("name") name: String, @Field("email") email: String, @Field("password") password: String) : Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(@Field("email") email: String, @Field("password") password: String): Call<LoginResponse>

    @GET("stories")
    fun getStories(@Header("Authorization") token: String): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun uploadStory(@Part file: MultipartBody.Part, @Part("description") description: RequestBody, @Header("Authorization") token: String): Call<AddStoryResponse>

}