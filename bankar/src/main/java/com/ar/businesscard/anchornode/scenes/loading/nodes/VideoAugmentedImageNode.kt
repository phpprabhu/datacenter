package com.ar.businesscard.anchornode.scenes.loading.nodes

import android.content.Context
import android.view.MotionEvent
import android.widget.Toast
import com.ar.businesscard.anchornode.scenes.loading.nodes.LoaderImageNode.Companion.VIDEO_START
import com.ar.businesscard.utils.ar.ArResources
import com.ar.businesscard.utils.ar.AugmentedImageNode
import com.ar.businesscard.utils.ottobus.Events
import com.ar.businesscard.utils.ottobus.GlobalBus
import com.google.ar.sceneform.HitTestResult
import com.squareup.otto.Subscribe


class VideoAugmentedImageNode(val context: Context) : AugmentedImageNode(ArResources.videoRenderable),
    com.google.ar.sceneform.Node.OnTapListener {

    companion object{
        val VIDEO_PAUSED = "video paused"
    }

    @Subscribe
    fun getMessage(message: Events.Message) {
        if(message.message.equals(VIDEO_START)) {
            Toast.makeText(context, message.message ,Toast.LENGTH_SHORT).show()
            play()
        }
    }

    override fun onTap(p0: HitTestResult?, p1: MotionEvent?) {
        val messageToPost = Events.Message(VIDEO_PAUSED)
        GlobalBus.getBus().post(messageToPost)
        pause()
    }

    private fun pause() {
        if (ArResources.videoPlayer.isPlaying) {
            ArResources.videoPlayer.pause()
            setOnTapListener(null)
        }
    }

    private fun play() {
        if (!ArResources.videoPlayer.isPlaying) {
            ArResources.videoPlayer.start()
            setOnTapListener(this)
        }
    }


    override fun initLayout() {
        super.initLayout()

        // the renderable is rectangle, so it have to scale to r
        val videoRatio = ArResources.videoPlayer.videoWidth.toFloat() / ArResources.videoPlayer.videoHeight

        offsetZ = (anchorNode.arHeight / 2.0f)

        // make video a little bigger to cover the while image
        scaledWidth *= 1.0f
        scaledHeight = scaledHeight * 1.2f / videoRatio
        scaledDeep = 1f
        localRotation = ArResources.viewRenderableRotation

       setOnTapListener(this)
    }

    override fun onActivate() {
        super.onActivate()

        if (!ArResources.videoPlayer.isPlaying) {
        ArResources.videoPlayer.start()
    }
    }

    override fun onDeactivate() {
        super.onDeactivate()

        if (ArResources.videoPlayer.isPlaying) {
            ArResources.videoPlayer.pause()
        }
    }
}
