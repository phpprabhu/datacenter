package com.ar.bankar.activity

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.ar.bankar.R
import com.ar.bankar.scan.ScanActivity
import com.ar.businesscard.activity.data.Caller
import com.ar.businesscard.nfc.Listener
import com.ar.businesscard.nfc.NFCReadFragment
import com.ar.businesscard.utils.ottobus.GlobalBus
import com.pro100svitlo.creditCardNfcReader.CardNfcAsyncTask
import com.pro100svitlo.creditCardNfcReader.utils.CardNfcUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AuthenticationActivity: FragmentActivity(), Listener, View.OnClickListener, CardNfcAsyncTask.CardNfcInterface {

    private var mNfcReadFragment: NFCReadFragment = NFCReadFragment.newInstance()
    private var nfcAdapter: NfcAdapter? = null
    private lateinit var cardNfcUtils: CardNfcUtils
    private var intentFromOnCreate: Boolean = false
    private var cardNfcAsyncTask: CardNfcAsyncTask? = null
    private var text: TextView? = null
    private lateinit var nfc: Button
    private lateinit var scan: Button

    companion object{
        val CARD = "67030416075070533"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authenticate)
        GlobalBus.getBus().register(this)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        initializeNFC()


        text = findViewById<TextView>(R.id.text)
        nfc = findViewById<Button>(R.id.nfc)
        scan = findViewById<Button>(R.id.scan_card)

        nfc.setOnClickListener(this)
        scan.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()

        intentFromOnCreate = false

        if (nfcAdapter?.isEnabled == false) {
            Log.i("MainActivity", "NFC is disabled, please enable it in settings")
        } else if (nfcAdapter != null){
            cardNfcUtils.enableDispatch()
        }

    }

    override fun onPause() {
        super.onPause()

        if (nfcAdapter != null){
            cardNfcUtils.disableDispatch()
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (nfcAdapter?.isEnabled == true) {
            cardNfcAsyncTask = CardNfcAsyncTask.Builder(this, intent, intentFromOnCreate==true)
                .build()
        }

    }

    override fun startNfcReadCard() {
        //notify user that scannig start
        Log.i("MainActivity", "startNfcReadCard")
    }

    override fun cardIsReadyToRead() {
        val card = cardNfcAsyncTask?.cardNumber
        val expiredDate = cardNfcAsyncTask?.cardExpireDate
        val cardType = cardNfcAsyncTask?.cardType

        Log.i("MainActivity", card)
        Log.i("MainActivity", expiredDate)
        Log.i("MainActivity", cardType)

        if(mNfcReadFragment != null && mNfcReadFragment.isVisible)
            mNfcReadFragment.dismiss()

        callService(card)
    }

    private fun showLoadingIcon() {
        findViewById<LottieAnimationView>(R.id.fetching).visibility = View.VISIBLE
        findViewById<LottieAnimationView>(R.id.card).visibility = View.GONE
        nfc.isEnabled = false
        scan.isEnabled = false
        text?.text = "Fetching data..."
    }

    private fun callService(card: String?){
        // call Account, history and userinfo service
        showLoadingIcon()

        GlobalScope.launch(Dispatchers.Main) {
            val caller = Caller()
            caller.parserHistory(applicationContext)
        }

        validateCardNumber(card)
    }

    private fun validateCardNumber(card: String?) {
        if(card.equals(CARD)){
            startActivity(Intent(this, ARActivity::class.java))
        }
    }

    private fun showReadFragment() {
        mNfcReadFragment.show(supportFragmentManager, "fragment_edit_name")
    }

    override fun onDialogDisplayed() {
    }

    override fun onDialogDismissed() {
    }


    override fun onClick(v: View) {
        if (v.id == R.id.nfc)   {
            showReadFragment()
        }else if(v.id == R.id.scan_card){
            val i = Intent(this, ScanActivity::class.java)
            startActivityForResult(i, 1)
        }
    }

    private fun initializeNFC() {
        if (nfcAdapter == null){
            //do something if there are no nfc module on device
            Log.i("MainActivity", "No NFC Module on Device")
        } else {
            //do something if there are nfc module on device

            cardNfcUtils = CardNfcUtils(this);
            //next few lines here needed in case you will scan credit card when app is closed
            intentFromOnCreate = true
            onNewIntent(intent)
        }
    }


    override fun cardWithLockedNfc() {
    }

    override fun doNotMoveCardSoFast() {
    }

    override fun unknownEmvCard() {
    }

    override fun finishNfcReadCard() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === 1) {
            if (resultCode === Activity.RESULT_OK) {
                val card = data?.getStringExtra("card")
                callService(card)
            }
            if (resultCode === Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
