package com.ar.businesscard.ads.anchornode

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.View
import com.ar.bankar.activity.ARActivity
import com.ar.businesscard.ads.anchornode.scene.AdsCarousalScene
import com.ar.businesscard.utils.Logger
import com.ar.businesscard.utils.ar.AugmentedImageAnchorNode
import com.ar.businesscard.utils.ar.AugmentedImageNodeGroup
import com.ar.businesscard.utils.ottobus.Events
import com.ar.businesscard.utils.ottobus.GlobalBus
import com.google.ar.sceneform.ux.ArFragment

class AdsAnchorNode(val context: Context, val view: View?, val arFragment: ArFragment, val activity: FragmentActivity) : AugmentedImageAnchorNode() {

    override val imageWidth: Float = 1F // 100 cm
    override val imageHeight: Float = 0.6667f // 66.7 cm

    private val sceneList = mutableListOf<AugmentedImageNodeGroup>()
    private var currentSceneIndex = 0

    override fun onInit() {
        GlobalBus.getBus().post(Events.Message(ARActivity.AR_DETECTED))

        sceneList.add(AdsCarousalScene(context, view, arFragment, activity).init(this))
    }

    override fun onActivate() {
        super.onActivate()

        changeScene(0)
    }

    private fun changeScene(index: Int) {
        currentSceneIndex = index

        Logger.d("changeScene($currentSceneIndex)")

        sceneList.forEachIndexed { i, scene ->
            scene.isEnabled = i == currentSceneIndex
        }
    }
}
