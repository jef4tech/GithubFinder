package com.jef4tech.githubfinder.network

/**
 * @author jeffin
 * @date 24/01/23
 */
object RestApiImpl {
    suspend fun getUserList() = RetrofitClientFactory.restApis.searchUser("jeffin johny")
}