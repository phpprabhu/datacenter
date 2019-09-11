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
    val MAIL_ID = "ks.ananth1987@gmail.com"
    val PHONE = "+32465325167"

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
            1 -> {
                navigateFB()
            }
            2 -> {
                navigateLinkedIn()
            }
            3 -> {
                navigateMail()
            }
            4 -> {
                navigateCall()
            }
            5 -> {
                navigateVideoCall()
            }
        }
    }

    private fun navigateVideoCall() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun navigateCall() {
        try {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:$PHONE")
            context.startActivity(callIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "No SIM Found", Toast.LENGTH_LONG).show()
        }    }

    private fun navigateMail() {
        val mailto = "mailto:$MAIL_ID"

        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse(mailto)

        try {
            startActivity(context, emailIntent, null)
        } catch (e: ActivityNotFoundException) {
            //TODO: Handle case where no email app is available
        }
    }

    private fun navigateLinkedIn() {
        Logger.d("linkedin opening");
        val profile_url = "https://www.linkedin.com/in/ananth-ks-21587a2b"
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profile_url))
            intent.setPackage("com.linkedin.android")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ContextCompat.startActivity(context, intent, null)
        } catch (e: Exception) {
            ContextCompat.startActivity(context, Intent(Intent.ACTION_VIEW, Uri.parse(profile_url)), null)
        }    }

    private fun navigateFB() {
        Logger.d("facebook opening");
        val facebookIntent = Intent(Intent.ACTION_VIEW)
        val facebookUrl = getFacebookPageURL(context,"https://www.facebook.com/ananthks2008","ananthks2008")
        facebookIntent.data = Uri.parse(facebookUrl)
        ContextCompat.startActivity(context, facebookIntent, null)
    }

    fun getFacebookPageURL(context: Context,FACEBOOK_URL: String, FACEBOOK_PAGE_ID: String ): String {
        val packageManager = context.getPackageManager()
        try {
            val versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
            return if (versionCode >= 3002850) { //newer versions of fb app
                "fb://facewebmodal/f?href=$FACEBOOK_URL"
            } else { //older versions of fb app
                "fb://page/$FACEBOOK_PAGE_ID"
            }
        } catch (e: PackageManager.NameNotFoundException) {
            return FACEBOOK_URL //normal web url
        }

    }
}