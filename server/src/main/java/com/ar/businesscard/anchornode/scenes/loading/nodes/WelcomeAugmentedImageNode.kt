package com.ar.businesscard.anchornode.scenes.loading.nodes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.ar.bankar.R
import com.ar.businesscard.activity.data.ARData
import com.ar.businesscard.utils.ar.ArResources
import com.ar.businesscard.utils.ar.AugmentedImageNode
import com.google.ar.sceneform.HitTestResult


class WelcomeAugmentedImageNode(private val context: Context) : AugmentedImageNode(ArResources.contactusRenderable), View.OnClickListener {

    override fun initLayout() {
        super.initLayout()

        // make it left
        offsetZ = -(anchorNode.arHeight / 2)
        offsetX = -(anchorNode.arWidth - anchorNode.arWidth * 0.15f )
    }

    override fun modifyLayout() {
        super.modifyLayout()

        localRotation = ArResources.viewRenderableRotation

        val contactUs = getButton(R.id.contact)
        val welcom = getTexview(R.id.welcome)

        welcom.text = "Welcome, ${ARData.getFName()}"
        contactUs.setOnClickListener(this)

    }

    private fun getButton(btnId: Int) = ArResources.contactusRenderable.get().view.findViewById<Button>(btnId)
    private fun getTexview(btnId: Int) = ArResources.contactusRenderable.get().view.findViewById<TextView>(btnId)

    override fun onTouchEvent(p0: HitTestResult?, p1: MotionEvent?): Boolean {
        isEnabled = false
        return false
    }

    override fun onClick(v: View) {
        if(v.id == R.id.contact){
            callFromDailer(context, "+32465325167")
        }
    }

    fun callFromDailer(mContext: Context, number: String) {
        try {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:$number")
            mContext.startActivity(callIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

