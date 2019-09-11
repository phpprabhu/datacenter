package com.ar.bankar.atm.view

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.*
import com.ar.bankar.R
import com.corebuild.arlocation.demo.api.FoursquareAPI
import com.corebuild.arlocation.demo.model.Geolocation
import com.corebuild.arlocation.demo.model.Venue
import com.corebuild.arlocation.demo.model.VenueWrapper
import com.corebuild.arlocation.demo.model.converter.VenueTypeConverter
import com.corebuild.arlocation.demo.utils.AugmentedRealityLocationUtils
import com.corebuild.arlocation.demo.utils.AugmentedRealityLocationUtils.INITIAL_MARKER_SCALE_MODIFIER
import com.corebuild.arlocation.demo.utils.AugmentedRealityLocationUtils.INVALID_MARKER_SCALE_MODIFIER
import com.corebuild.arlocation.demo.utils.PermissionUtils
import com.google.ar.core.TrackingState
import com.google.ar.core.exceptions.CameraNotAvailableException
import com.google.ar.core.exceptions.UnavailableException
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_augmented_reality_location.*
import kotlinx.android.synthetic.main.location_layout_renderable.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.appoly.arcorelocation.LocationMarker
import uk.co.appoly.arcorelocation.LocationScene
import java.lang.ref.WeakReference
import java.util.concurrent.CompletableFuture
import android.view.animation.AccelerateInterpolator as AccelerateInterpolator1

class AugmentedRealityLocationActivity : AppCompatActivity(), Callback<VenueWrapper>, View.OnClickListener,
    SeekBar.OnSeekBarChangeListener , CompoundButton.OnCheckedChangeListener {

    companion object{
        const val CATEGORY_ATM: String = "52f2ab2ebcbc57f1066b8b56"
        const val CATEGORY_BANK: String = "4bf58dd8d48988d10a951735"
    }

    override fun onCheckedChanged(view: CompoundButton, isChecked: Boolean) {
        if (isChecked){
            if(view.id == R.id.chkAtm){
                selectedCategory = CATEGORY_ATM
            } else{
                selectedCategory = CATEGORY_BANK
            }
            LocationAsyncTask(WeakReference(this@AugmentedRealityLocationActivity), seekBar!!.progress).execute(locationScene!!)

        } else{
            venuesSet.clear()
            if(chkBank.isChecked){
                selectedCategory = CATEGORY_BANK
            } else{
                selectedCategory = CATEGORY_ATM
            }
            LocationAsyncTask(WeakReference(this@AugmentedRealityLocationActivity), seekBar!!.progress).execute(locationScene!!)
        }
    }

    override fun onClick(v: View) {
        if(v.id == R.id.close_bank){
            outToLeftAnimation(containerLeftBank)
        } else if(v.id == R.id.close_atm){
            outToLeftAnimation(containerLeftAtm)
        } else if(v.id == R.id.navigate){
            navigateMap(selectedLat +","+ selectedLong)
        } else if(v.id == R.id.navigate_atm){
            navigateMap(selectedLat +","+ selectedLong)
        } else if(v.id == R.id.makeAppoint){
            makeAppointment("https://www.bnpparibasfortis.be/en/Customer-flow/Make-an-appointment?axes4=priv")
        }
    }

    private lateinit var selectedLat: String
    private lateinit var selectedLong: String
    private lateinit var selectedCategory: String
    private lateinit var selectedName: String
    private lateinit var selectedAddress1: String
    private lateinit var selectedAddress2: String
    private lateinit var selectedDistance: String

    private var arCoreInstallRequested = false

    // Our ARCore-Location scene
    private var locationScene: LocationScene? = null

    private var arHandler = Handler(Looper.getMainLooper())

    lateinit var loadingDialog: AlertDialog

    private val resumeArElementsTask = Runnable {
        locationScene?.resume()
        arSceneView.resume()
    }

    lateinit var foursquareAPI: FoursquareAPI
    private var apiQueryParams = mutableMapOf<String, String>()

    private var userGeolocation = Geolocation.EMPTY_GEOLOCATION

    private var venuesSet: MutableSet<Venue> = mutableSetOf()
    private var areAllMarkersLoaded = false

    lateinit var containerLeftBank: RelativeLayout
    lateinit var containerLeftAtm: RelativeLayout
    lateinit var closeAtm: TextView
    lateinit var closeBank: TextView
    lateinit var navigateATM: Button
    lateinit var navigateBank: Button
    lateinit var makeAppointment: Button
    lateinit var seekBar: SeekBar
    lateinit var chkAtm: CheckBox
    lateinit var chkBank: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_augmented_reality_location)

        containerLeftBank = findViewById(R.id.container_left)
        containerLeftAtm = findViewById(R.id.container_left_atm)
        closeAtm = findViewById(R.id.close_atm)
        closeBank = findViewById(R.id.close_bank)
        navigateATM = findViewById(R.id.navigate)
        navigateBank = findViewById(R.id.navigate_atm)
        makeAppointment = findViewById(R.id.makeAppoint)
        seekBar = findViewById(R.id.seekbar)
        chkAtm = findViewById(R.id.chkAtm)
        chkBank = findViewById(R.id.chkBank)

        seekBar.progress = 1000
        seekBar.max = 5000

        distance.text = toKm(seekBar.progress)

        closeAtm.setOnClickListener(this)
        closeBank.setOnClickListener(this)
        navigateATM.setOnClickListener(this)
        navigateBank.setOnClickListener(this)
        makeAppointment.setOnClickListener(this)
        seekBar.setOnSeekBarChangeListener(this)
        chkBank.setOnCheckedChangeListener(this)
        chkAtm.setOnCheckedChangeListener(this)

        selectedCategory = CATEGORY_ATM

        setupRetrofit()
        setupLoadingDialog()
    }

    private fun toKm(i: Int) = (i / 1000).toString()+"km"

    override fun onResume() {
        super.onResume()
        checkAndRequestPermissions()
    }

    override fun onPause() {
        super.onPause()
        arSceneView.session?.let {
            locationScene?.pause()
            arSceneView?.pause()
        }
    }

    private fun setupRetrofit() {
        apiQueryParams["client_id"] = "TVPR0CS4VRC0LDVP3YB3SRFE0ODMJCN4UNC5LTQ1TFBGMYZC"//YOUR CLIENT ID
        apiQueryParams["client_secret"] = "ZC2K3ZKS21YFIKPILKSICQSON2GT0DZGBMBNVTFERIUZHLK4"// YOUR CLIENT SECRET
        apiQueryParams["v"] = "20190716"
        apiQueryParams["limit"] = "100"
        apiQueryParams["categoryId"] = selectedCategory
        apiQueryParams["radius"] = "1000"

        val gson = GsonBuilder()
            .registerTypeAdapter(
                VenueWrapper::class.java,
                VenueTypeConverter()
            )
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(FoursquareAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        foursquareAPI = retrofit.create(FoursquareAPI::class.java)
    }

    private fun setupLoadingDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val dialogHintMainView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null) as LinearLayout
        alertDialogBuilder.setView(dialogHintMainView)
        loadingDialog = alertDialogBuilder.create()
        loadingDialog.setCanceledOnTouchOutside(false)
        loadingDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    private fun setupSession() {
        if (arSceneView == null) {
            return
        }

        if (arSceneView.session == null) {
            try {
                val session = AugmentedRealityLocationUtils.setupSession(this, arCoreInstallRequested)
                if (session == null) {
                    arCoreInstallRequested = true
                    return
                } else {
                    arSceneView.setupSession(session)
                }
            } catch (e: UnavailableException) {
                AugmentedRealityLocationUtils.handleSessionException(this, e)
            }
        }

        if (locationScene == null) {
            locationScene = LocationScene(this, arSceneView)
            locationScene!!.setMinimalRefreshing(true)
            locationScene!!.setOffsetOverlapping(true)
//            locationScene!!.setRemoveOverlapping(true)
            locationScene!!.anchorRefreshInterval = 2000
        }

        try {
            resumeArElementsTask.run()
        } catch (e: CameraNotAvailableException) {
            Toast.makeText(this, "Unable to get camera", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if (userGeolocation == Geolocation.EMPTY_GEOLOCATION) {
            LocationAsyncTask(WeakReference(this@AugmentedRealityLocationActivity)).execute(locationScene!!)
        }
    }

    private fun fetchVenues(deviceLatitude: Double, deviceLongitude: Double, progress: String, categoryId: String) {
        loadingDialog.show()
        userGeolocation = Geolocation(deviceLatitude.toString(), deviceLongitude.toString())
        apiQueryParams["ll"] = "$deviceLatitude,$deviceLongitude"
        apiQueryParams["radius"] = progress
        apiQueryParams["categoryId"] = categoryId
        foursquareAPI.searchVenues(apiQueryParams).enqueue(this)
    }

    override fun onResponse(call: Call<VenueWrapper>, response: Response<VenueWrapper>) {
        locationScene!!.clearMarkers()
        locationScene!!.refreshAnchors()
        loadingDialog.dismiss()
        val venueWrapper = response.body() ?: VenueWrapper(listOf())
        //venuesSet.clear()

        val distinctList = venueWrapper.venueList.distinct()
        var filteredList: List<Venue> = distinctList.filter { s -> s.name.contains("bnp", ignoreCase = true) }

        //venuesSet.addAll(venueWrapper.venueList)
        venuesSet.addAll(filteredList)
        areAllMarkersLoaded = false

        renderVenues()

        Log.d("","+++onResponse+++")
    }

    override fun onFailure(call: Call<VenueWrapper>, t: Throwable) {
        //handle api ic_call failure
    }

    private fun renderVenues() {
        setupAndRenderVenuesMarkers()
        updateVenuesMarkers()
    }

    private fun setupAndRenderVenuesMarkers() {
        venuesSet.forEach { venue ->
            val completableFutureViewRenderable = ViewRenderable.builder()
                .setView(this, R.layout.location_layout_renderable)
                .build()
            CompletableFuture.anyOf(completableFutureViewRenderable)
                .handle<Any> { _, throwable ->
                    //here we know the renderable was built or not
                    if (throwable != null) {
                        // handle renderable load fail
                        return@handle null
                    }
                    try {

                        val venueMarker = LocationMarker(
                            venue.long.toDouble(),
                            venue.lat.toDouble(),
                            setVenueNode(venue, completableFutureViewRenderable)
                        )
                        arHandler.postDelayed({
                            attachMarkerToScene(
                                venueMarker,
                                completableFutureViewRenderable.get().view
                            )
                            if (venuesSet.indexOf(venue) == venuesSet.size - 1) {
                                areAllMarkersLoaded = true
                            }
                        }, 200)

                    } catch (ex: Exception) {
                        //                        showToast(getString(R.string.generic_error_msg))
                    }
                    null
                }
        }
    }

    private fun updateVenuesMarkers() {
        arSceneView.scene.addOnUpdateListener()
        {
            if (!areAllMarkersLoaded) {
                return@addOnUpdateListener
            }

            locationScene?.mLocationMarkers?.forEach { locationMarker ->
                locationMarker.height =
                    AugmentedRealityLocationUtils.generateRandomHeightBasedOnDistance(
                        locationMarker?.anchorNode?.distance ?: 0
                    )
            }


            val frame = arSceneView!!.arFrame ?: return@addOnUpdateListener
            if (frame.camera.trackingState != TrackingState.TRACKING) {
                return@addOnUpdateListener
            }
            locationScene!!.processFrame(frame)
        }
    }


    private fun attachMarkerToScene(
        locationMarker: LocationMarker,
        layoutRendarable: View
    ) {
        resumeArElementsTask.run {
            locationMarker.scalingMode = LocationMarker.ScalingMode.FIXED_SIZE_ON_SCREEN
            locationMarker.scaleModifier = INITIAL_MARKER_SCALE_MODIFIER

            locationScene?.mLocationMarkers?.add(locationMarker)
            locationMarker.anchorNode?.isEnabled = true

            arHandler.post {
                locationScene?.refreshAnchors()
                layoutRendarable.pinContainer.visibility = View.VISIBLE
            }
        }
        locationMarker.setRenderEvent { locationNode ->
            layoutRendarable.distance.text = AugmentedRealityLocationUtils.showDistance(locationNode.distance)
            resumeArElementsTask.run {
                computeNewScaleModifierBasedOnDistance(locationMarker, locationNode.distance)
            }
        }
    }

    private fun computeNewScaleModifierBasedOnDistance(locationMarker: LocationMarker, distance: Int) {
        val scaleModifier = AugmentedRealityLocationUtils.getScaleModifierBasedOnRealDistance(distance)
        return if (scaleModifier == INVALID_MARKER_SCALE_MODIFIER) {
            detachMarker(locationMarker)
        } else {
            locationMarker.scaleModifier = scaleModifier
        }
    }

    private fun detachMarker(locationMarker: LocationMarker) {
        locationMarker.anchorNode?.anchor?.detach()
        locationMarker.anchorNode?.isEnabled = false
        locationMarker.anchorNode = null
    }


    private fun setVenueNode(venue: Venue, completableFuture: CompletableFuture<ViewRenderable>): Node {
        val node = Node()
        node.renderable = completableFuture.get()

        val nodeLayout = completableFuture.get().view
        val venueName = nodeLayout.name
        val icon = nodeLayout.categoryIcon
        val markerLayoutContainer = nodeLayout.pinContainer
        venueName.text = venue.name
        if(venue.category.equals("Banks",ignoreCase = true) || venue.category.equals("Offices",ignoreCase = true)){
            icon.setBackgroundResource(R.drawable.ic_bank_building);
        } else{
            icon.setBackgroundResource(R.drawable.ic_atm);
        }
        markerLayoutContainer.visibility = View.GONE
        nodeLayout.setOnTouchListener { _, _ ->
            //Toast.makeText(this, venue.address, Toast.LENGTH_SHORT).show()
            selectedLat = venue.lat
            selectedLong = venue.long
            selectedName = venue.name
            selectedAddress1 = venue.address1
            selectedAddress2 = venue.address2
            selectedDistance = venue.distance
            inFromLeftAnimation(containerLeftBank)
            updateDetail()
            false
        }

        //Glide.with(this)
        //    .load(venue.iconURL)
        //    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        //    .into(nodeLayout.categoryIcon)

        return node
    }

    private fun updateDetail() {
        name.text = selectedName
        address1.text = selectedAddress1
        address2.text = selectedAddress2
        navigate.text = "Navigate(${selectedDistance}m)"
    }

    private fun  inFromLeftAnimation(containerLeft: RelativeLayout) {
        val param = containerLeft.layoutParams as RelativeLayout.LayoutParams
        param.setMargins(0,40,0,40)
        containerLeft.layoutParams = param
        containerLeft.visibility = View.VISIBLE
        val inFromLeft =  TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f);
            inFromLeft.duration = 500;
            inFromLeft.interpolator = android.view.animation.AccelerateInterpolator()
        containerLeft.startAnimation(inFromLeft)
    }

    private fun outToLeftAnimation(containerLeft: RelativeLayout) {
    val outToLeft =  TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, -1.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f);
        outToLeft.duration = 500;
        outToLeft.interpolator = android.view.animation.AccelerateInterpolator()
        containerLeft.startAnimation(outToLeft)
        containerLeft.visibility = View.GONE
    }


    private fun checkAndRequestPermissions() {
        if (!PermissionUtils.hasLocationAndCameraPermissions(this)) {
            PermissionUtils.requestCameraAndLocationPermissions(this)
        } else {
            setupSession()
        }
    }

    private fun navigateMap(to: String){
        val lat = locationScene?.deviceLocation?.currentBestLocation?.latitude
        val long = locationScene?.deviceLocation?.currentBestLocation?.longitude
        var from = lat.toString()+","+long.toString()

        var intent = Intent(
            Intent.ACTION_VIEW,
        Uri.parse("http://maps.google.com/maps?saddr=$from&daddr=$to"));
        startActivity(intent)
    }

    private fun makeAppointment(url: String){
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, results: IntArray) {
        if (!PermissionUtils.hasLocationAndCameraPermissions(this)) {
            Toast.makeText(
                this, R.string.camera_and_location_permission_request, Toast.LENGTH_LONG
            )
                .show()
            if (!PermissionUtils.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                PermissionUtils.launchPermissionSettings(this)
            }
            finish()
        }
    }

    inner class LocationAsyncTask(private val activityWeakReference: WeakReference<AugmentedRealityLocationActivity>) :
        AsyncTask<LocationScene, Void, List<Double>>() {

        var progress: String = "1000"

        constructor(activityWeakReference: WeakReference<AugmentedRealityLocationActivity>, newProgress: Int): this(activityWeakReference){
            progress = newProgress.toString()
        }

        override fun onPreExecute() {
            super.onPreExecute()
            activityWeakReference.get()!!.loadingDialog.show()
        }

        override fun doInBackground(vararg p0: LocationScene): List<Double> {
            var deviceLatitude: Double?
            var deviceLongitude: Double?
            do {
                deviceLatitude = p0[0].deviceLocation?.currentBestLocation?.latitude
                deviceLongitude = p0[0].deviceLocation?.currentBestLocation?.longitude
            } while (deviceLatitude == null || deviceLongitude == null)
            return listOf(deviceLatitude, deviceLongitude)
        }

        override fun onPostExecute(geolocation: List<Double>) {
            activityWeakReference.get()!!.fetchVenues(
                deviceLatitude = geolocation[0],
                deviceLongitude = geolocation[1],
                progress = progress,
                categoryId = selectedCategory
            )
            super.onPostExecute(geolocation)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        distance.text = toKm(seekBar!!.progress)
        LocationAsyncTask(WeakReference(this@AugmentedRealityLocationActivity), seekBar!!.progress).execute(locationScene!!)
    }
}