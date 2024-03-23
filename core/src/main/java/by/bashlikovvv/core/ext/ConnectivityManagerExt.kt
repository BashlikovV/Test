package by.bashlikovvv.core.ext

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun ConnectivityManager?.isConnected(): Boolean {
    var isConnected = false
    this?.run {
        this.getNetworkCapabilities(this.activeNetwork)?.run {
            isConnected = when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
    }

    return isConnected
}