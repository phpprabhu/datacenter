package com.ar.businesscard.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ar.bankar.activity.ARActivity.Companion.AR_STOPED
import com.ar.bankar.activity.ARActivity.Companion.AR_TRACKING
import com.ar.businesscard.anchornode.CardAnchorNode
import com.ar.businesscard.utils.Logger
import com.ar.businesscard.utils.ar.ArResources
import com.ar.businesscard.utils.ar.AugmentedImageAnchorNode
import com.ar.businesscard.utils.ottobus.Events
import com.ar.businesscard.utils.ottobus.GlobalBus
import com.google.ar.core.*
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.ux.ArFragment


class SceneARFragment: ArFragment(){
    private val trackableMap = mutableMapOf<String, AugmentedImageAnchorNode>()
    var setOnStarted: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view!!.visibility = View.GONE

        // Turn off the plane discovery since we're only looking for ArImages
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        arSceneView.planeRenderer.isEnabled = false
       /* arSceneView.scene.setOnTouchListener { _, motionEvent ->
            swipeAnGestureDetector.onTouchEvent(motionEvent)
        }*/

        GlobalBus.getBus().register(this)

        arSceneView.scene.addOnUpdateListener(::onUpdateFrame)


        ArResources.init(this.context!!).handle { _, _ ->
            setOnStarted?.invoke()

            view.visibility = View.VISIBLE
        }

        return view
    }

    override fun onPause() {
        super.onPause()
        trackableMap.forEach {
            arSceneView.scene.removeChild(it.value)
        }

        trackableMap.clear()
    }

    override fun getSessionConfiguration(session: Session): Config {
        val config = super.getSessionConfiguration(session)
        config.focusMode = Config.FocusMode.AUTO

        config.augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, context!!.resources.assets.open("output.imgdb"))

        return config
    }

    private fun createArNode(image: AugmentedImage) {
        Logger.d("create : ${image.name}(${image.index}), pose: ${image.centerPose}, ex: ${image.extentX}, ez: ${image.extentZ}")

        when (image.name) {
            "qr.png" -> {
                val node = CardAnchorNode(context!!, view, this, activity!!).init(image)
                trackableMap[image.name] = node
                arSceneView.scene.addChild(node)

                Toast.makeText(context, "${image.name} added", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onUpdateFrame(@Suppress("UNUSED_PARAMETER") frameTime: FrameTime?) {
        val frame = arSceneView.arFrame

        // If there is no frame or ARCore is not tracking yet, just return.
        if (frame == null || frame.camera.trackingState != TrackingState.TRACKING) {
            return
        }

        frame.getUpdatedTrackables(AugmentedImage::class.java).forEach { image ->
            when (image.trackingState) {
                TrackingState.TRACKING -> if (trackableMap.contains(image.name)) {
                    if (trackableMap[image.name]?.update(image) == true) {
                        Logger.d("update node: ${image.name}(${image.index}), pose: ${image.centerPose}, ex: ${image.extentX}, ez: ${image.extentZ}")
                    }
                    GlobalBus.getBus().post(Events.Message(AR_TRACKING))
                } else {
                    createArNode(image)
                }
                TrackingState.STOPPED -> {
                    Logger.d("remove node: ${image.name}(${image.index})")
                    Toast.makeText(context, "${image.name} removed", Toast.LENGTH_LONG).show()

                    trackableMap.remove(image.name).let {
                        arSceneView.scene.removeChild(it)
                    }
                    GlobalBus.getBus().post(Events.Message(AR_STOPED))
                }
                else -> {
                }
            }
        }
    }

}