package com.ar.businesscard.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.ar.bankar.R


object CustomDialog {

    lateinit var context: Context
    val MAIL_ID = "p.santana@global.leaseweb.com"
    val PHONE = "+919008044500"

    fun showDialog(
        context: Context,
        linkSelected: Int
    ) {

        this.context = context;
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, null)
        val title = view.findViewById(R.id.title) as TextView
        val imageButton = view.findViewById(R.id.image) as ImageButton

        title.text = "Hello There!"

        imageButton.setImageResource(R.drawable.smile)

        builder.setPositiveButton("Yes"
        ) { _, i ->
           openLink(linkSelected)
        }

        builder.setNegativeButton("No"
        ) { _, i ->
            Toast.makeText(
                context,
                "Never Mind!",
                Toast.LENGTH_SHORT
            ).show()
        }

        builder.setView(view)

        builder.show()
    }

    private fun openLink(linkSelected: Int) {
        when(linkSelected){

        }
    }

    private fun navigateVideoCall() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}