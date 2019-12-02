package com.ervin.mypokedex.utils

import android.annotation.TargetApi
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import androidx.core.app.NotificationCompat
import com.ervin.mypokedex.R
import java.net.InetAddress
import java.net.UnknownHostException


class Utils{
    companion object{
        fun calculateNoOfColumn(context: Context, widthColumnInFloat: Float): Int{
            val display : DisplayMetrics = context.resources.displayMetrics
            val screen = display.widthPixels / display.density
            return (screen / widthColumnInFloat + 0.5).toInt()
        }
    }
}

fun View.setGone(){
    visibility = View.GONE
}

fun View.setShow(){
    visibility = View.VISIBLE
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

fun isInternetAvailable(): Boolean {
    try {
        val address = InetAddress.getByName("www.google.com")
        return address.hostName != ""
    } catch (e: UnknownHostException) {
        // Log error
    }
    return false

}

fun Context.cancelNotification(notifyId:Int){
    val notify = NOTIFICATION_SERVICE
    val notifyManager = this.getSystemService(notify) as NotificationManager
    notifyManager.cancel(notifyId)
}

fun Context.getNotificationBuilder(channelId: String, importance: Int): NotificationCompat.Builder {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.prepareChannel(channelId, importance)
        NotificationCompat.Builder(this, channelId)
    } else {
        NotificationCompat.Builder(this, channelId)
    }
}

@TargetApi(26)
private fun Context.prepareChannel(id: String, importance: Int) {
    val appName = getString(R.string.app_name)
    val description = "Description"
    val nm = getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager

    val nChannel =  NotificationChannel(id, appName, importance)
    nChannel.description = description
    nm.createNotificationChannel(nChannel)
}

const val ON_LOADING_POKEMON = 1234
const val ON_FINISH_NOTIFICATION = 1233
const val ON_FAILED_NOTIFICATION = 1232
const val ONGOING_CHANNEL_ID = "1232"
