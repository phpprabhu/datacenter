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

    lateinit var videoPlayer: MediaPlayer
    lateinit var videoRenderable: CompletableFuture<ModelRenderable>
    lateinit var manRenderable: CompletableFuture<ModelRenderable>
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

        videoRenderable = getVideo(context)

        manRenderable = getMan(context)

       
        return CompletableFuture.allOf(
            loaderRenderable
        )
    }

    private fun getVideo(context: Context): CompletableFuture<ModelRenderable> {
        val texture = ExternalTexture()
        videoPlayer = MediaPlayer.create(context, R.raw.video_b)
        videoPlayer.setSurface(texture.surface)
        videoPlayer.isLooping = true
        return ModelRenderable.builder()
            .setSource(context, com.google.ar.sceneform.rendering.R.raw.sceneform_view_renderable).build().also {
                it.thenAccept { renderable ->
                    renderable.material.setExternalTexture("viewTexture", texture)
                }
            }
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

    private fun getMan(context: Context): CompletableFuture<ModelRenderable> {
        return ModelRenderable.builder()
            .setSource(context, Uri.parse("man_coat_wave.sfb"))
            .build()
            .thenApply<ModelRenderable> { renderable -> this.setRenderable(renderable) }
            .exceptionally { throwable -> this.onException() }
    }

    private fun onException(): ModelRenderable? {
        return null
    }

    private fun setRenderable(modelRenderable: ModelRenderable): ModelRenderable {
        return modelRenderable
    }

}
