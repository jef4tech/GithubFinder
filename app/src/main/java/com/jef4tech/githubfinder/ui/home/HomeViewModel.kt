package com.jef4tech.githubfinder.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jef4tech.githubfinder.models.UserResponse
import com.jef4tech.githubfinder.network.RestApiImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val userList = MutableLiveData<UserResponse>()

    fun getUserList(searchWord: String) {
        viewModelScope.launch {
            val response = RestApiImpl.getUserList(searchWord)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    userList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error ${response.message()}")
                }
            }
        }
    }
    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }
}