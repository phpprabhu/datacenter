package com.ar.businesscard.utils


import android.util.Log

object Logger {
    private const val TAG = "Business card - AR"

    fun d(message: String, throwable: Throwable? = null) {
        Log.d(TAG, message, throwable)
    }

    fun e(message: String, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }
}