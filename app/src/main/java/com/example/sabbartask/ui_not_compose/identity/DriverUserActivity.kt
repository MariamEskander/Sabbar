package com.example.sabbartask.ui_not_compose.identity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.sabbartask.R
import com.example.sabbartask.base.BaseActivity
import com.example.sabbartask.base.BaseViewModel
import com.example.sabbartask.data.remote.entities.FirebaseData
import com.example.sabbartask.data.remote.entities.LocationData
import com.example.sabbartask.data.remote.entities.NOTIFCATION_TYPE
import com.example.sabbartask.databinding.ActivityDriverBinding
import com.example.sabbartask.services.LocationUtils
import com.example.sabbartask.ui_not_compose.identity.adapter.TimelineAdapter
import com.example.sabbartask.ui_not_compose.identity.sheets.LocationsSheet
import com.example.sabbartask.utils.Constants
import com.example.sabbartask.utils.Constants.DATA
import com.example.sabbartask.utils.Constants.IS_USER
import com.example.sabbartask.utils.extensions.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
open class DriverUserActivity : BaseActivity(), OnMapReadyCallback {

    companion object {
        fun start(activity: Activity?, isUser: Boolean, finish: Boolean = true) {
            if (finish)
                activity?.finish()

            activity?.launchActivity<DriverUserActivity> {
                val bundle = Bundle()
                bundle.putBoolean("isUser", isUser)
                this.putExtras(bundle)
            }
        }
    }

    private var adapter: TimelineAdapter?= null
    private var isUser: Boolean = false
    private var mCurrLocationMarker: Marker? = null
    private var pickupLocationMarker: Marker? = null
    private var dropOffLocationMarker: Marker? = null
    private var mMap: GoogleMap? = null
    private var timeLineItems = getTimeLinesItems()

    private var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null

    private lateinit var binding: ActivityDriverBinding
    private val viewModel: DriverUserViewModel by viewModels()
    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun initView() {
        isUser = intent.getBooleanExtra(IS_USER, false)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference(DATA)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_api_key))
        }

        binding.btnAddPickupDropoff.hideView()
        binding.clStatus.hideView()
        binding.clRate.hideView()

        checkLocation()
    }

    private fun getData() {
        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.btnAddPickupDropoff.hideView()
                binding.clStatus.hideView()
                binding.clRate.hideView()

                val value = snapshot.getValue(FirebaseData::class.java)
                if (isLocationAvailable(value?.pickup)) {
                    setMarker(value?.pickup?.lat ?: 0.0, value?.pickup?.lng ?: 0.0, 1)
                }
                if (isLocationAvailable(value?.dropoff)) {
                    if (!isUser)
                        startLocationService(this@DriverUserActivity)

                    setMarker(value?.dropoff?.lat ?: 0.0, value?.dropoff?.lng ?: 0.0, 2)
                } else {
                    if (isUser && !isLocationAvailable(value?.pickup) && !isLocationAvailable(value?.driver)) {
                        binding.btnAddPickupDropoff.showView()
                    }
                }
                if (isLocationAvailable(value?.driver)) {
                    setMarker(value?.driver?.lat ?: 0.0, value?.driver?.lng ?: 0.0, 0)

                    if (isUser) {
                        if (value != null){
                            adapter = TimelineAdapter(timeLineItems)
                            binding.rvTimeline.adapter = adapter

                            checkDriverLocationRegardingToDeliveryPoints(value)
                        }
                    } else {
//                        viewModel.drawRoute(getDirectionURL(
//                            value?.driver!!,
//                            value.pickup!!,
//                            getString(R.string.google_api_key)
//                        ))

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DriverUserActivity, "Fail to get data.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun checkDriverLocationRegardingToDeliveryPoints(value: FirebaseData) {
        val results = FloatArray(1)
        val data: Float

        when (value.notificationType) {
            NOTIFCATION_TYPE.NEAR_PICKUP.ordinal -> {
                data = getLocationDistance(
                    value.driver?.lat ?: 0.0, value.driver?.lng ?: 0.0,
                    value.pickup?.lat ?: 0.0, value.pickup?.lng ?: 0.0, results
                )
                if (data <= 5000) {
                    sendNotifications(this, "near pickup location")
                    addToFirebase(
                        databaseReference,
                        Constants.NOTIFICATION_TYPE,
                        NOTIFCATION_TYPE.PICKUP.ordinal
                    )
                }
                updateDeliveryStatusView(NOTIFCATION_TYPE.NEAR_PICKUP.ordinal)

            }
            NOTIFCATION_TYPE.PICKUP.ordinal -> {
                data = getLocationDistance(
                    value.driver?.lat ?: 0.0, value.driver?.lng ?: 0.0,
                    value.pickup?.lat ?: 0.0, value.pickup?.lng ?: 0.0, results
                )
                if (data <= 100) {
                    sendNotifications(this, "in pickup location")
                    addToFirebase(
                        databaseReference,
                        Constants.NOTIFICATION_TYPE,
                        NOTIFCATION_TYPE.NEAR_DROP_OFF.ordinal
                    )
                    updateDeliveryStatusView(NOTIFCATION_TYPE.PICKUP.ordinal)
                } else
                    updateDeliveryStatusView(NOTIFCATION_TYPE.NEAR_PICKUP.ordinal)


            }
            NOTIFCATION_TYPE.NEAR_DROP_OFF.ordinal -> {
                data = getLocationDistance(
                    value.driver?.lat ?: 0.0, value.driver?.lng ?: 0.0,
                    value.dropoff?.lat ?: 0.0, value.dropoff?.lng ?: 0.0,
                    results
                )
                if (data <= 5000) {
                    sendNotifications(this, "near drop-off location")
                    addToFirebase(
                        databaseReference,
                        Constants.NOTIFICATION_TYPE,
                        NOTIFCATION_TYPE.DROP_OFF.ordinal
                    )
                    updateDeliveryStatusView(NOTIFCATION_TYPE.NEAR_DROP_OFF.ordinal)
                } else
                    updateDeliveryStatusView(NOTIFCATION_TYPE.PICKUP.ordinal)
            }
            NOTIFCATION_TYPE.DROP_OFF.ordinal -> {
                data = getLocationDistance(
                    value.driver?.lat ?: 0.0, value.driver?.lng ?: 0.0,
                    value.dropoff?.lat ?: 0.0, value.dropoff?.lng ?: 0.0,
                    results
                )
                if (data <= 100) {
                    sendNotifications(this, "in drop-off location")
                    addToFirebase(
                        databaseReference,
                        Constants.NOTIFICATION_TYPE,
                        NOTIFCATION_TYPE.RATE.ordinal
                    )
                    addToFirebase(databaseReference, Constants.PICKUP, LocationData(0.0, 0.0))
                    addToFirebase(databaseReference, Constants.DROP_OFF, LocationData(0.0, 0.0))
                    updateDeliveryStatusView(NOTIFCATION_TYPE.RATE.ordinal)
                } else
                    updateDeliveryStatusView(NOTIFCATION_TYPE.NEAR_DROP_OFF.ordinal)
            }
            else -> {
                updateDeliveryStatusView(NOTIFCATION_TYPE.RATE.ordinal)

            }
        }
    }

    private fun updateDeliveryStatusView(ordinal: Int) {
        if (ordinal == NOTIFCATION_TYPE.RATE.ordinal || ordinal == NOTIFCATION_TYPE.DROP_OFF.ordinal){
            binding.clStatus.hideView()
            showRateView()
        }else{
            binding.clStatus.showView()
            updateList(ordinal)
        }
    }

    private fun updateList(ordinal: Int) {
        for (i in 1 until ordinal-1)
            timeLineItems[i].done = true
        for (i in ordinal until timeLineItems.size)
            timeLineItems[i].done = false

        timeLineItems[0].done = true
        adapter?.updateList(timeLineItems)
    }

    private fun showRateView() {
        binding.clRate.showView()
        binding.tvSubmit.setOnClickListener {
            removeMarkers()
            addToFirebase(
                databaseReference,
                Constants.NOTIFICATION_TYPE,
                NOTIFCATION_TYPE.NEAR_PICKUP.ordinal
            )
            addToFirebase(databaseReference, Constants.DRIVER, LocationData(0.0, 0.0))
        }
    }

    private fun removeMarkers() {
        mMap?.clear()
        mCurrLocationMarker?.remove()
        dropOffLocationMarker?.remove()
        pickupLocationMarker?.remove()
    }

    private fun checkLocation() {
        if (LocationUtils.checkLocationPermissions(this)) {
            getData()
        } else {
            LocationUtils.requestPermissions(
                this,
                Constants.MY_PERMISSION_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun initClickListeners() {
        binding.btnAddPickupDropoff.setOnClickListener {
            LocationsSheet.newInstance().show(supportFragmentManager, LocationsSheet.TAG)
        }
    }

    override fun setObservers() {}

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mMap?.isMyLocationEnabled = true
            } else
                finish()
        } else {
            mMap?.isMyLocationEnabled = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.MY_PERMISSION_ACCESS_FINE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getData()
            } else
                finish()
        }
    }

    private fun setMarker(latitude: Double, longitude: Double, type: Int) {
        if (type == 0)
            mCurrLocationMarker?.remove()
        //Place current location marker
        val latLng = LatLng(latitude, longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)

        when (type) {
            0 -> {
                markerOptions.icon(
                    bitmapDescriptorFromVector(
                        this,
                        R.drawable.ic_baseline_directions_car_24
                    )
                )
                mCurrLocationMarker = mMap?.addMarker(markerOptions)
            }
            1 -> pickupLocationMarker =  mMap?.addMarker(markerOptions)
            2 -> dropOffLocationMarker =  mMap?.addMarker(markerOptions)
        }

        //move map camera
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.zoomIn())
    }
}