package com.jef4tech.githubfinder.network

import com.jef4tech.githubfinder.BaseApplication
import com.jef4tech.githubfinder.BuildConfig
import com.jef4tech.githubfinder.utils.Extensions.hasNetwork
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author jeffin
 * @date 23/01/23
 */
object RetrofitClientFactory {

    val BASE_URL = "https://api.github.com/"
    val API_KEY = ""



    val retrofitClient: Retrofit.Builder by lazy {
        val cacheSize = 50 * 1024
        val myCache = Cache(File(BaseApplication.instance.cacheDir, "responses"), cacheSize.toLong())
        val levelType: Level
        if (BuildConfig.BUILD_TYPE.contentEquals("debug"))
            levelType = Level.BODY else levelType = Level.NONE

        val logging = HttpLoggingInterceptor()
        logging.setLevel(levelType)

        val okhttpClient = OkHttpClient.Builder().cache(myCache) .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
        okhttpClient.addInterceptor { chain ->

            var request = chain.request()

            request = if (BaseApplication.instance.let { hasNetwork(it) }!!)
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5)  .addHeader("Authorization", API_KEY).build()
            else
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                    .build()

            chain.proceed(request)
        }


        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okhttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val restApis: RestApis by lazy {
        retrofitClient
            .build()
            .create(RestApis::class.java)
    }

}