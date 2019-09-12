package com.ar.businesscard.anchornode.scenes.loading.nodes

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import com.airbnb.lottie.LottieAnimationView
import com.ar.bankar.R
import com.ar.businesscard.utils.Animation
import com.ar.businesscard.utils.ar.ArResources
import com.ar.businesscard.utils.ar.AugmentedImageNode
import com.ar.businesscard.utils.ottobus.Events
import com.ar.businesscard.utils.ottobus.GlobalBus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoaderImageNode(val context: Context,val callback: LoaderImageNodeCallback) : AugmentedImageNode(ArResources.loaderRenderable) {

    companion object{
        val VIDEO_START = "play video "
    }

    lateinit var loader: LottieAnimationView
    lateinit var play: LottieAnimationView

    override fun modifyLayout() {
        super.modifyLayout()

        localRotation = ArResources.viewRenderableRotation
        loader = ArResources.loaderRenderable.get().view.findViewById(R.id.loader)
        play = ArResources.loaderRenderable.get().view.findViewById(R.id.play)
        val container: RelativeLayout = ArResources.loaderRenderable.get().view.findViewById(R.id.container)

        GlobalScope.launch(Dispatchers.Main) {
            showPlayIcon(context, loader, play, container)
        }

        play.setOnClickListener {
            val messageToPost = Events.Message(VIDEO_START)
            GlobalBus.getBus().post(messageToPost)
            Animation.hide(play, 2000)
        }
    }

    suspend fun showPlayIcon(context: Context, loader: View, play: View, container: RelativeLayout) {
        delay(2000)
        loader.visibility = View.INVISIBLE
        //Animation.blink(container,1000, context)
       // delay(1000)
        container.background = context.getDrawable(R.color.transparent)

        play.visibility = View.GONE

        callback.loadingCompleted()
    }

    override fun initLayout() {
        super.initLayout()
    }

    interface LoaderImageNodeCallback{
        fun loadingCompleted()
    }
}