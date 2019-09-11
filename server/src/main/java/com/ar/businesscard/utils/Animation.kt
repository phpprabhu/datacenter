package com.ar.businesscard.utils

import android.content.Context
import android.view.View
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import com.ar.bankar.R

object Animation {

     fun hide(v: View, duration: Long) {
        v.animate().alpha(0f).setDuration(duration)
    }

    fun show(v: View, duration: Long) {
        v.visibility = VISIBLE
    }

    fun blink(view: View, duration: Long, context: Context) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.blink)
        view.startAnimation(animation)
    }

    fun rotate(view: View, duration: Long, context: Context) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.rotate)
        view.startAnimation(animation)
    }
}