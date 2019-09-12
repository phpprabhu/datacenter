package com.ar.businesscard.ads.anchornode.scene

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.Toast
import com.ar.businesscard.anchornode.scenes.loading.nodes.*
import com.ar.businesscard.utils.ar.AugmentedImageNodeGroup
import com.google.ar.sceneform.ux.ArFragment

class AdsCarousalScene(val context: Context, val view: View?, val  arFragment: ArFragment, val activity: FragmentActivity) : AugmentedImageNodeGroup(),
    LoaderImageNode.LoaderImageNodeCallback, ManNode.ManSpeakCallback {
    override fun talkCompleted() {
        AccountHistoryAugmentedImageNode(context).init(anchorNode, this)
        ChartAugmentedImageNode(context).init(anchorNode, this)
        WelcomeAugmentedImageNode(context).init(anchorNode, this)
    }

    override fun loadingCompleted() {
        ManNode(context, arFragment, this, activity).init(anchorNode, this)
        Toast.makeText(context, "AR Initiated", Toast.LENGTH_LONG).show()
    }

    override fun onInit() {
        LoaderImageNode(context, this).init(anchorNode, this)
    }
}

