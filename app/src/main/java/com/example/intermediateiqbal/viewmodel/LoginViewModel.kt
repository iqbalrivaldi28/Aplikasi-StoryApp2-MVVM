package com.example.intermediateiqbal.viewmodel

import androidx.lifecycle.*
import com.example.intermediateiqbal.User
import com.example.intermediateiqbal.UserPreferences
import com.example.intermediateiqbal.retrofit.APIConfig
import com.example.intermediateiqbal.retrofit.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreferences) : ViewModel() {

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin : LiveData<Boolean> = _isLogin

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun login(email: String, pass: String) {
        _isLogin.value = false
        val client = APIConfig.getAPIService().loginUser(email,
            pass)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val user = User(
                        responseBody.loginResult.name,
                        responseBody.loginResult.token,
                        true,
                    )
                    saveUser(user)
                    _isLogin.value = true
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

            }
        })
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }


}