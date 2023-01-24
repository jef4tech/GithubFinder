package com.jef4tech.githubfinder.network

/**
 * @author jeffin
 * @date 24/01/23
 */
object RestApiImpl {
    suspend fun getUserList(searchWord: String) = RetrofitClientFactory.restApis.searchUser(searchWord)
    suspend fun getUserData(userName: String) = RetrofitClientFactory.restApis.getUserData(userName)
}