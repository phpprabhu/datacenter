package com.ar.businesscard.anchornode.scenes.loading

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.Toast
import com.ar.businesscard.anchornode.scenes.loading.nodes.*
import com.ar.businesscard.utils.ar.AugmentedImageNodeGroup
import com.google.ar.sceneform.ux.ArFragment

class LoadingScene(
    val context: Context,
    val view: View?,
    val arFragment: ArFragment,
    val activity: FragmentActivity,
    val serverID: Int
) : AugmentedImageNodeGroup(),
    LoaderImageNode.LoaderImageNodeCallback, ManNode.ManSpeakCallback {
    override fun talkCompleted() {

    }

    override fun loadingCompleted() {
        ManNode(context, arFragment, this, activity).init(anchorNode, this)
        Toast.makeText(context, "AR Initiated", Toast.LENGTH_LONG).show()
    }

    override fun onInit() {
        //AccountHistoryAugmentedImageNode(context).init(anchorNode, this)
        ChartAugmentedImageNode(context).init(anchorNode, this)
        WelcomeAugmentedImageNode(context, serverID).init(anchorNode, this)
    }
}







