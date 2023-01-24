package com.jef4tech.githubfinder.network

import com.jef4tech.githubfinder.models.UserData
import com.jef4tech.githubfinder.models.UserResponse
import okhttp3.ResponseBody
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
    suspend fun searchUser(@Query("q") query_parameter: String): Response<UserResponse>

    @GET("users/{userid}")
    suspend fun getUserData(@Path("userid") id: String): Response<UserData>
}