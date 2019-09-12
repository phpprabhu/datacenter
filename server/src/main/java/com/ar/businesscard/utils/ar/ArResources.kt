package com.ar.businesscard.utils.ar


import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.ar.bankar.R
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ExternalTexture
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import java.util.concurrent.CompletableFuture

object ArResources {

    lateinit var loaderRenderable: CompletableFuture<ViewRenderable>
    lateinit var linksRenderable: CompletableFuture<ViewRenderable>
    lateinit var contactusRenderable: CompletableFuture<ViewRenderable>
    lateinit var chartRenderable: CompletableFuture<ViewRenderable>

    val viewRenderableRotation = Quaternion(Vector3(1f, 0f, 0f), -90f)

    fun init(context: Context): CompletableFuture<Void> {

        loaderRenderable = getLoader(context)

        linksRenderable = getLinks(context)

        contactusRenderable = getContactUs(context)

        chartRenderable = getCharts(context)
       
        return CompletableFuture.allOf(
            loaderRenderable
        )
    }


    private fun getLinks(context: Context): CompletableFuture<ViewRenderable> {
        return ViewRenderable.builder().setView(context, R.layout.view_links)
            .setVerticalAlignment(ViewRenderable.VerticalAlignment.TOP)
            .build()
    }

    private fun getContactUs(context: Context): CompletableFuture<ViewRenderable> {
        return ViewRenderable.builder().setView(context, R.layout.view_contactus)
            .setVerticalAlignment(ViewRenderable.VerticalAlignment.TOP)
            .build()
    }

    private fun getCharts(context: Context): CompletableFuture<ViewRenderable> {
        return ViewRenderable.builder().setView(context, R.layout.view_charts)
            .setVerticalAlignment(ViewRenderable.VerticalAlignment.TOP)
            .build()
    }

    private fun getLoader(context: Context): CompletableFuture<ViewRenderable> {
        return ViewRenderable.builder().setView(context, R.layout.view_loading)
            .setVerticalAlignment(ViewRenderable.VerticalAlignment.CENTER)
            .build()
    }

}
