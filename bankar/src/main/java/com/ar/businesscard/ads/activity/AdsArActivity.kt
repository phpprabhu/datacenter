package com.ar.bankar.ads.activity

import com.ar.bankar.R
import com.ar.businesscard.activity.BaseActivity
import com.ar.businesscard.ads.fragment.AdsARFragment
import com.google.ar.sceneform.ux.ArFragment

class AdsArActivity() : BaseActivity() {

    private lateinit var arFragment: ArFragment

    override val viewId: Int
        get() = R.layout.activity_ads

    override fun startAr() {
        arFragment = AdsARFragment()
        supportFragmentManager.beginTransaction().replace(R.id.add_ar_fragment, arFragment).commit()
    }
}