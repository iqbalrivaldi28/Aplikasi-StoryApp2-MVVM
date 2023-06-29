package com.example.intermediateiqbal.retrofit.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class RegisterResponse(
    val error: Boolean,
    val message: String
)

data class LoginResponse(
    val error: Boolean,
    val loginResult: LoginResult,
    val message: String
)

data class LoginResult(
    val userId: String,
    val name: String,
    val token: String
)

data class StoryResponse(
    val listStory: List<StoryItem>,
    val error: Boolean,
    val message: String
)

data class AddStoryResponse(
    val error: Boolean,
    val message: String,
)

@Parcelize
data class StoryItem(
    val id: String,
    val photoUrl: String,
    val createdAt: String,
    val name: String,
    val description: String,
    val lat: Double,
    val lon: Double
) : Parcelable

