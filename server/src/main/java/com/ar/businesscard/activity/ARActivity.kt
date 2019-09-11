package com.ar.bankar.activity


import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.ar.bankar.R
import com.ar.businesscard.activity.BaseActivity
import com.ar.businesscard.activity.fragment.SceneARFragment
import com.ar.businesscard.utils.ottobus.Events
import com.google.ar.sceneform.ux.ArFragment
import com.squareup.otto.Subscribe


class ARActivity : BaseActivity() {

    companion object{
        val AR_DETECTED = "AR.Detected"
        val AR_TRACKING = "AR.Tracking"
        val AR_STOPED = "AR.Stoped"
    }

    private lateinit var arFragment: ArFragment
    lateinit var layoutProgressBar: FrameLayout

    override val viewId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutProgressBar = findViewById<FrameLayout>(R.id.layoutProgressBar)
    }

    override fun startAr() {

        //parserHistory(applicationContext)

        arFragment = SceneARFragment()
        supportFragmentManager.beginTransaction().replace(R.id.ar_fragment, arFragment).commit()
    }

    @Subscribe
    fun getMessage(message: Events.Message) {
        if(message.message.equals(AR_DETECTED)) {
            layoutProgressBar.visibility = View.GONE
        } else if (message.message.equals(AR_STOPED) || message.message.equals(AR_TRACKING)){
            //layoutProgressBar.visibility = View.VISIBLE
        }
    }
}
