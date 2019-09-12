package com.ar.businesscard.anchornode.scenes.loading.nodes

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.ar.bankar.R
import com.ar.businesscard.data.Server
import com.ar.businesscard.data.ServerList
import com.ar.businesscard.utils.ar.ArResources
import com.ar.businesscard.utils.ar.AugmentedImageNode
import com.google.ar.sceneform.HitTestResult
import com.google.gson.Gson
import java.io.IOException


class WelcomeAugmentedImageNode(
    private val context: Context,
    val serverID: Int
) : AugmentedImageNode(ArResources.contactusRenderable), View.OnClickListener {

    override fun initLayout() {
        super.initLayout()

        // make it left
        offsetZ = -(anchorNode.arHeight / 2)
        offsetX = -(anchorNode.arWidth - anchorNode.arWidth * 0.15f )
    }

    override fun modifyLayout() {
        super.modifyLayout()

        localRotation = ArResources.viewRenderableRotation

       val serverList = parseFromAssset()
        val server = getSelectedServer(serverID, serverList.servers)

        val pc = getButton(R.id.pc)
        val scan = getButton(R.id.scan)

        val brand = getTexview(R.id.brand)
        val model = getTexview(R.id.model)
        val serial = getTexview(R.id.serial)
        val processor = getTexview(R.id.processor)
        val ram = getTexview(R.id.ram)
        val disks = getTexview(R.id.disks)
        val ip = getTexview(R.id.ip)
        val private_network = getTexview(R.id.private_network)
        val rm = getTexview(R.id.rm)
        val raid = getTexview(R.id.raid)

        brand.text = "Brand : ${server.brand}"
        model.text = "Model : ${server.model}"
        serial.text = "Serial : ${server.serial}"
        processor.text = "Processor : ${server.processor}"
        ram.text = "RAM : ${server.ram}"
        disks.text = "Disks : ${server.disks}"
        ip.text = "Public IP : ${server.publicIP}"
        private_network.text = "PrivateN etwork : ${server.privateNetwork}"
        rm.text = "Remote Management : ${server.remoteManagement}"
        raid.text = "Hardware RAID : ${server.hardwareRAID}"


        pc.setOnClickListener(this)
        scan.setOnClickListener(this)

    }

    private fun getSelectedServer(serverId: Int, servers: List<Server>): Server {
        val server: Server? = servers.find { it.id == serverId }
        return server!!
    }

    private fun getButton(btnId: Int) = ArResources.contactusRenderable.get().view.findViewById<Button>(btnId)
    private fun getTexview(btnId: Int) = ArResources.contactusRenderable.get().view.findViewById<TextView>(btnId)

    override fun onTouchEvent(p0: HitTestResult?, p1: MotionEvent?): Boolean {
        isEnabled = false
        return false
    }

    override fun onClick(v: View) {
        if(v.id == com.ar.bankar.R.id.pc){
            showDialog("Remote Management")
        }else if(v.id == com.ar.bankar.R.id.scan){
            showDialog("Scan")
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

    fun  parseFromAssset(): ServerList {
        val gson = Gson()
        val servers = gson.fromJson(loadData("servers.json"), ServerList::class.java)
        return servers
    }

    fun loadData(inFile: String): String {
        var tContents = ""

        try {
            val stream = context.getAssets().open(inFile)

            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            tContents = String(buffer)
        } catch (e: IOException) {
            // Handle exceptions here
        }

        return tContents

    }

    fun showDialog(msg: String){
        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                }

                DialogInterface.BUTTON_NEGATIVE -> {
                }
            }//Yes button clicked
            //No button clicked
        }

        val builder = AlertDialog.Builder(context)
        builder.setMessage(msg).setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show()
    }
}

