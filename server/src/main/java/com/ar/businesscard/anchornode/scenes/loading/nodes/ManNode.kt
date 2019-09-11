package com.ar.businesscard.anchornode.scenes.loading.nodes

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.support.v4.app.FragmentActivity
import com.ar.businesscard.activity.data.ARData
import com.ar.businesscard.utils.Logger
import com.ar.businesscard.utils.ar.ArResources
import com.ar.businesscard.utils.ar.AugmentedImageNode
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import java.util.*


class ManNode(val context: Context, val arFragment: ArFragment, val callback: ManSpeakCallback, val activity: FragmentActivity) : AugmentedImageNode(null), TextToSpeech.OnInitListener,
    TextToSpeech.OnUtteranceCompletedListener {

    private lateinit var tts: TextToSpeech
    private lateinit var node: TransformableNode
    private val clientName = ARData.getFName()


    override fun modifyLayout() {

        tts = TextToSpeech(context, this, "com.google.android.tts")

        super.modifyLayout()


        localRotation = Quaternion.axisAngle(Vector3(-0f, -360f, 0f), 270f)

        node = TransformableNode(arFragment.transformationSystem)
        node.localRotation = Quaternion.axisAngle(Vector3(-0f, -360f, 0f), 270f)
        node.renderable = ArResources.manRenderable.getNow(null)

        // Set the min and max scales of the ScaleController.
        // Default min is 0.75, default max is 1.75.
        node.scaleController.minScale = 0.0299f
        node.scaleController.maxScale = 0.2000f

        // Set the local scale of the node BEFORE setting its parent
        node.localScale = Vector3(-0f, -100f, -100f)

        node.setParent(anchorNode)

        startTalking(ArResources.manRenderable.getNow(null))
    }


    private fun startTalking(andyRenderable: ModelRenderable) {
        val data = andyRenderable.getAnimationData("mixamo.com")
        val animator = ModelAnimator(data, andyRenderable)
        animator.repeatCount = 6
        animator.start()
    }

    private fun speak(text: String){
        tts.setOnUtteranceProgressListener(utteranceProgressListener)
        tts.language = Locale.US
        val params = HashMap<String, String>()
        params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "stringId"
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params)
        tts.setOnUtteranceCompletedListener(this)

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            Logger.d("TextToSpeech success")
            speak("Hello, $clientName... Welcome to BNP Paribas fortis app. You can check your Account balance and transaction now ")
        } else{
            Logger.d("TextToSpeech failed")
        }
    }

    override fun onUtteranceCompleted(utteranceId: String?) {
        activity.runOnUiThread(Runnable {
            callback.talkCompleted()
            removeAnchorNode(node)
        })
    }

    interface ManSpeakCallback{
        fun talkCompleted()
    }

    var utteranceProgressListener: UtteranceProgressListener = object : UtteranceProgressListener() {

        override fun onStart(utteranceId: String) {
            Logger.d( "onStart ( utteranceId :$utteranceId ) ")
        }

        override fun onError(utteranceId: String) {
            Logger.d( "onError ( utteranceId :$utteranceId ) ")
        }

        override fun onDone(utteranceId: String) {
            Logger.d( "onDone ( utteranceId :$utteranceId ) ")
        }
    }


    private fun removeAnchorNode(nodeToremove: TransformableNode?) {
        //Remove an anchor node
        if (nodeToremove != null) {
            arFragment.arSceneView.scene.removeChild(nodeToremove)
            nodeToremove.setParent(null)
        }
    }

}