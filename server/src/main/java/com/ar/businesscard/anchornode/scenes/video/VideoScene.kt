package com.ar.businesscard.anchornode.scenes.video

import android.content.Context
import com.ar.businesscard.anchornode.scenes.loading.nodes.VideoAugmentedImageNode
import com.ar.businesscard.utils.ar.AugmentedImageNodeGroup

class VideoScene(val context: Context) : AugmentedImageNodeGroup() {
    override fun onInit() {
        VideoAugmentedImageNode(context).init(anchorNode, this)
    }
}

/*
class VideoAugmentedImageNode : AugmentedImageNode(ArResources.videoRenderable) {
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
}*/
