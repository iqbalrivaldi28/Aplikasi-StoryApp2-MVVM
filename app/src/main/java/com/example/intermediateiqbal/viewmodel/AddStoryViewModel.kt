package com.example.intermediateiqbal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.intermediateiqbal.retrofit.APIConfig
import com.example.intermediateiqbal.retrofit.response.AddStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel : ViewModel() {

    private val _isStoryUpload = MutableLiveData<Boolean>()
    val storyUpload : LiveData<Boolean> = _isStoryUpload

    fun storyUserUpload(
        file : MultipartBody.Part,
        description: RequestBody,
        token: String) {

        _isStoryUpload.value = false
        val client = APIConfig.getAPIService().uploadStory(file, description, "Bearer $token")
        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _isStoryUpload.value = true
                }
            }
            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
            }
        })
    }

}