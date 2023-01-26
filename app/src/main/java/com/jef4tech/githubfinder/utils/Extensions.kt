package com.jef4tech.githubfinder.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author jeffin
 * @date 23/01/23
 */
object Extensions {
    fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
    fun loadImagefromUrl(context: Context, imgView: ImageView, url:String){
        Glide.with(context).load(url).apply( RequestOptions().override(100, 100)).fitCenter().into(imgView)
    }
    fun showToast(message: String,context: Context) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"

    fun getDayWithMonthName(input: String): String {
        val localDateTime = LocalDateTime.parse(input, DateTimeFormat.forPattern(DATE_FORMAT))
        return SimpleDateFormat("dd MMM''yy").format(localDateTime.toDate().time)
    }

}