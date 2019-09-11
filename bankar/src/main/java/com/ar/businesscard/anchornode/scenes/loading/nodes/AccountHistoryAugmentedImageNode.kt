package com.ar.businesscard.anchornode.scenes.loading.nodes

import android.content.Context
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import com.ar.bankar.R
import com.ar.businesscard.activity.data.ARData
import com.ar.businesscard.models.history.Movement
import com.ar.businesscard.utils.ar.ArResources
import com.ar.businesscard.utils.ar.AugmentedImageNode
import com.google.ar.sceneform.HitTestResult


class AccountHistoryAugmentedImageNode(private val context: Context) : AugmentedImageNode(ArResources.linksRenderable) {
    override fun initLayout() {
        super.initLayout()

        // make it under
        offsetZ = anchorNode.arHeight / 2 + anchorNode.arHeight * 0.05f
    }

    override fun modifyLayout() {
        super.modifyLayout()

        localRotation = ArResources.viewRenderableRotation

        val recyclerView = getButton(R.id.recycler_view)

        val movementList = prepareList()
        val mAdapter = HistoryListAdapter(movementList)
        val mLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = mAdapter

        val controller =AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);
        recyclerView.layoutAnimation = controller;
        recyclerView.scheduleLayoutAnimation();


        mAdapter.notifyDataSetChanged();
    }

    private fun prepareList(): List<Movement> {

        return ARData.getTransactionList()
    }

    private fun getButton(btnId: Int) = ArResources.linksRenderable.get().view.findViewById<RecyclerView>(btnId)

    override fun onTouchEvent(p0: HitTestResult?, p1: MotionEvent?): Boolean {
        isEnabled = false
        return false
    }
}
