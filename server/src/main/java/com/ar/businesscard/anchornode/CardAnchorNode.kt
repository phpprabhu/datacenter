package com.ar.businesscard.anchornode

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.View
import com.ar.bankar.activity.ARActivity
import com.ar.businesscard.anchornode.scenes.loading.LoadingScene
import com.ar.businesscard.utils.Logger
import com.ar.businesscard.utils.ar.AugmentedImageAnchorNode
import com.ar.businesscard.utils.ar.AugmentedImageNodeGroup
import com.ar.businesscard.utils.ottobus.Events
import com.ar.businesscard.utils.ottobus.GlobalBus
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.coroutines.delay


class CardAnchorNode(val context: Context, val view: View?, val arFragment: ArFragment, val activity: FragmentActivity) : AugmentedImageAnchorNode() {

    override val imageWidth: Float = 1F // 100 cm
    override val imageHeight: Float = 0.6667f // 66.7 cm

    private val sceneList = mutableListOf<AugmentedImageNodeGroup>()
    private var currentSceneIndex = 0

    override fun onInit() {
        GlobalBus.getBus().post(Events.Message(ARActivity.AR_DETECTED))

        sceneList.add(LoadingScene(context, view, arFragment, activity).init(this))
        //sceneList.add(VideoScene(context).init(this))
        //sceneList.add(SwarmScene2().init(this))
        //sceneList.add(SwarmScene3().init(this))

       // GlobalScope.async {
       //     showPlayIcon()
       // }
    }

    override fun onActivate() {
        super.onActivate()

        changeScene(0)
    }

    suspend fun loadVideoAfterFewSec() {
        delay(1000L) // pretend we are doing something useful here
        changeScene(1)
    }

    fun forwardScene() {
        changeScene((currentSceneIndex + 1) % sceneList.size)
    }

    fun backwardScene() {
        changeScene((currentSceneIndex - 1 + sceneList.size) % sceneList.size)
    }

    private fun changeScene(index: Int) {
        currentSceneIndex = index

        Logger.d("changeScene($currentSceneIndex)")

        sceneList.forEachIndexed { i, scene ->
            scene.isEnabled = i == currentSceneIndex
        }
    }
}
