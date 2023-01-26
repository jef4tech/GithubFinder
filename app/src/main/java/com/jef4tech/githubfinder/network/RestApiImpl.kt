package com.jef4tech.githubfinder.network

/**
 * @author jeffin
 * @date 24/01/23
 */
object RestApiImpl {
    suspend fun getUserList(searchWord: String, page: Int) = RetrofitClientFactory.restApis.searchUser(searchWord,10,page)
    suspend fun getUserData(userName: String) = RetrofitClientFactory.restApis.getUserData(userName)
    suspend fun getUserRepos(userName: String) = RetrofitClientFactory.restApis.getUserRepos(userName)
}