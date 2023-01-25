package com.jef4tech.githubfinder.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jef4tech.githubfinder.models.RepositoryResponse
import com.jef4tech.githubfinder.models.UserData
import com.jef4tech.githubfinder.network.RestApiImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val userData = MutableLiveData<UserData>()
    val repoData = MutableLiveData<RepositoryResponse>()
    var sortedRepoData = MutableLiveData<List<RepositoryResponse.RepositoryResponseItem>>()

    fun getUserData(userName: String) {
        viewModelScope.launch {
            val response = RestApiImpl.getUserData(userName)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    userData.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error ${response.message()}")
                }
            }
        }
    }

    fun getUserRepo(userName: String) {
        viewModelScope.launch {
            val response = RestApiImpl.getUserRepos(userName)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    repoData.postValue(response.body())
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

    //search with in live data
    fun search(query: String) {
        repoData.value?.let { it ->
            val filteredList = it.filter { it.name.startsWith(query, true) }
            sortedRepoData.value=filteredList
        }
    }
}