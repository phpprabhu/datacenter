package com.ar.businesscard.ads.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ar.bankar.activity.ARActivity
import com.ar.businesscard.ads.anchornode.AdsAnchorNode
import com.ar.businesscard.utils.Logger
import com.ar.businesscard.utils.ar.ArResources
import com.ar.businesscard.utils.ar.AugmentedImageAnchorNode
import com.ar.businesscard.utils.ottobus.Events
import com.ar.businesscard.utils.ottobus.GlobalBus
import com.google.ar.core.*
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment


class AdsARFragment: ArFragment(){

    private val trackableMap = mutableMapOf<String, AugmentedImageAnchorNode>()

    var setOnStarted: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view!!.visibility = View.GONE

        // Turn off the plane discovery since we're only looking for ArImages
        planeDiscoveryController.hide()
        planeDiscoveryController.setInstructionView(null)
        arSceneView.planeRenderer.isEnabled = false


        GlobalBus.getBus().register(this)

        arSceneView.scene.addOnUpdateListener(::onUpdateFrame)


        ArResources.init(this.context!!).handle { _, _ ->
            setOnStarted?.invoke()

            view.visibility = View.VISIBLE
        }

        createMyNode()
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

        config.augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, context!!.resources.assets.open("ar.imgdb"))

        return config
    }

    private fun createArNode(image: AugmentedImage) {
        Logger.d("create : ${image.name}(${image.index}), pose: ${image.centerPose}, ex: ${image.extentX}, ez: ${image.extentZ}")

        when (image.name) {
            "card.jpg","bnp_card.png" -> {

                val node = AdsAnchorNode(context!!, view, this, activity!!).init(image)
                trackableMap[image.name] = node
                arSceneView.scene.addChild(node)

                Toast.makeText(context, "${image.name} added", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onUpdate(frameTime: FrameTime?) {
        if (arSceneView.arFrame!!.camera.trackingState != TrackingState.TRACKING) {
            Log.d("", "onUpdate: Tracking not started yet")
            return
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
                    GlobalBus.getBus().post(Events.Message(ARActivity.AR_TRACKING))
                } else {
                    createArNode(image)
                }
                TrackingState.STOPPED -> {
                    Logger.d("remove node: ${image.name}(${image.index})")
                    Toast.makeText(context, "${image.name} removed", Toast.LENGTH_LONG).show()

                    trackableMap.remove(image.name).let {
                        arSceneView.scene.removeChild(it)
                    }
                    GlobalBus.getBus().post(Events.Message(ARActivity.AR_STOPED))
                }
                else -> {
                }
            }
        }
    }


    private fun createMyNode() {

        ViewRenderable.builder()
            .setView(context, com.ar.bankar.R.layout.view_loading)
            .build()
            .thenAccept { viewRenderable ->


                val forward = arSceneView.scene.camera.forward
                val cameraPosition = arSceneView.scene.camera.worldPosition
                val position = cameraPosition + forward

                val direction = cameraPosition - position
                direction.y = position.y

                Node().apply {
                    worldPosition = position
                    renderable = viewRenderable
                    setParent(arSceneView.scene.camera)
                    setLookDirection(direction)
                }
            }
    }

    operator fun Vector3.plus(other: Vector3): Vector3 {
        return Vector3.add(this, other)
    }

    operator fun Vector3.minus(other: Vector3): Vector3 {
        return Vector3.subtract(this, other)
    }
}