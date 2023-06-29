package com.example.intermediateiqbal.viewmodel

import androidx.lifecycle.*
import com.example.intermediateiqbal.User
import com.example.intermediateiqbal.UserPreferences
import com.example.intermediateiqbal.retrofit.APIConfig
import com.example.intermediateiqbal.retrofit.response.StoryItem
import com.example.intermediateiqbal.retrofit.response.StoryResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _listUserStory = MutableLiveData<List<StoryItem>>()
    val listUserStory : LiveData<List<StoryItem>> = _listUserStory

    fun userStory(token: String)  {
        val client = APIConfig.getAPIService().getStories("Bearer $token")
        client.enqueue(object : Callback<StoryResponse> {

            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null)
                {
                    _listUserStory.value = responseBody.listStory
                }
                else
                {
                    _listUserStory.value = emptyList()
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {}

        })
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.logout()
        }
    }

    fun getUser(): LiveData<User> {
        return userPreferences.getUser().asLiveData()
    }

}