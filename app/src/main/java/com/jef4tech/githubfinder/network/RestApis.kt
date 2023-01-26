package com.jef4tech.githubfinder.network

import com.jef4tech.githubfinder.models.RepositoryResponse
import com.jef4tech.githubfinder.models.UserData
import com.jef4tech.githubfinder.models.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * @author jeffin
 * @date 23/01/23
 */
interface RestApis {


    @GET("search/users")
    suspend fun searchUser(@Query("q") query_parameter: String,
                           @Query("per_page") per_page: Int,
                           @Query("page") page: Int
                           ): Response<UserResponse>

    @GET("users/{userid}")
    suspend fun getUserData(@Path("userid") id: String): Response<UserData>

    @GET("users/{userid}/repos")
    suspend fun getUserRepos(@Path("userid") id: String): Response<RepositoryResponse>


}