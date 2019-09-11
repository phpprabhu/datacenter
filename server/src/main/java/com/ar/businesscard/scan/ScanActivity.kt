package com.ar.bankar.scan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.Button
import com.ar.bankar.R


class ScanActivity : FragmentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        findViewById<Button>(R.id.button).setOnClickListener(View.OnClickListener {
            finishScanActivity()
        })
    }

    private fun finishScanActivity() {
        val returnIntent = Intent()
        returnIntent.putExtra("card", "67030416075070533")
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }


}
