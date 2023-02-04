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

    val loader = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val userList = MutableLiveData<UserResponse>()
    var userListPage = 1
    var limit = 10
    var isSearched = false
    var userListResponse:UserResponse?=null

    fun getUserList(searchWord: String) {
        loader.value = true
        viewModelScope.launch {
            val response = RestApiImpl.getUserList(searchWord,userListPage)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    userList.postValue(response.body())
                    userList.postValue(handleUserListResponse(response.body()))
                    loader.value = false
                } else {
                    onError("Error ${response.message()}")
                }
            }
        }
    }

    private fun handleUserListResponse(body: UserResponse?): UserResponse? {
        body?.let {
            userResponse ->
            userListPage++
            if(userListResponse == null || !isSearched) {
                userListResponse = userResponse
            } else {
                val oldlist = userListResponse?.items
                val newlist = userResponse.items
                oldlist?.addAll(newlist)
            }
        }
        return userListResponse
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loader.value = false
    }
}