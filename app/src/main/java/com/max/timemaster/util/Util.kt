//package app.appworks.school.stylish.util
//
//import android.content.Context
//import android.net.ConnectivityManager
//import android.net.NetworkInfo
//import app.appworks.school.stylish.StylishApplication
//
///**
// * Updated by Wayne Chen in Mar. 2019.
// */
//object Util {
//
//    /**
//     * Determine and monitor the connectivity status
//     *
//     * https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
//     */
//    fun isInternetConnected(): Boolean {
//        val cm = StylishApplication.instance
//            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
//        return activeNetwork?.isConnectedOrConnecting == true
//    }
//
//    fun getString(resourceId: Int): String {
//        return StylishApplication.instance.getString(resourceId)
//    }
//
//    fun getColor(resourceId: Int): Int {
//        return StylishApplication.instance.getColor(resourceId)
//    }
//}
