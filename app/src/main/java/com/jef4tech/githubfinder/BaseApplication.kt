package com.jef4tech.githubfinder

import android.app.Application

/**
 * @author jeffin
 * @date 23/01/23
 */
class BaseApplication: Application() {
    companion object {
        lateinit var instance:BaseApplication
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}