package com.example.sabbartask.ui.features.userDriver

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sabbartask.R
import com.example.sabbartask.data.remote.entities.FirebaseData
import com.example.sabbartask.utils.Constants
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.*
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState


val TAG: String = "UserDriverScreen"
private var firebaseDatabase: FirebaseDatabase? = null
var databaseReference: DatabaseReference? = null
private var isUser = false

@Composable
fun UserDriverScreen(state: UserDriverContract.State) {
    isUser = state.isUser?:false
    Surface(color = MaterialTheme.colors.background) {

        Column {
            AppPermission()
            val singapore = LatLng(1.35, 103.87)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(singapore, 10f)
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            )

        }
    }
}

@Composable
fun AppPermission() {
    val viewModel: UserDriverViewModel = hiltViewModel()
    val context = LocalContext.current
    val requestLocationPermission by viewModel.requestLocationPermission.collectAsState()

    if (!requestLocationPermission) {
        PermissionUI(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            stringResource(id = R.string.permission_location_rationale),
            rememberScaffoldState()
        ) { permissionAction ->
            {
                when (permissionAction) {
                    is PermissionAction.OnPermissionGranted -> {
                        Log.d(TAG, "Permission grant successful")
                            viewModel.setRequestLocationPermission(true)
                    }
                    is PermissionAction.OnPermissionDenied -> {
                        Log.d(TAG, "Permission grant denied")
                        viewModel.setRequestLocationPermission(false)


                    }
                }
            }
        }
    }
}

@Composable
fun GetData(context: Context) {
    val viewModel: UserDriverViewModel = hiltViewModel()
    firebaseDatabase = FirebaseDatabase.getInstance()
    databaseReference = firebaseDatabase?.getReference(Constants.DATA)

    databaseReference?.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val value = snapshot.getValue(FirebaseData::class.java)

//            if (isLocationAvailable(value?.pickup)) {
//                MapMarker(
//                    context = context,
//                    position = LatLng(value?.pickup?.lat ?: 0.0, value?.pickup?.lng ?: 0.0),
//                    type = 1
//                )
//            }
//            if (isLocationAvailable(value?.dropoff)) {
//                if (!isUser)
//                    startLocationService(context)
//
////                MapMarker(
////                    context = context,
////                    position = LatLng( value?.dropoff?.lat ?: 0.0, value?.dropoff?.lng ?: 0.0),
////                    type = 2
////                )
//            } else {
//                if (isUser && !isLocationAvailable(value?.pickup) && !isLocationAvailable(value?.driver)) {
//                 //   binding.btnAddPickupDropoff.showView()
//                }
//            }
//            if (isLocationAvailable(value?.driver)) {
////                MapMarker(
////                    context = context,
////                    position = LatLng( value?.driver?.lat ?: 0.0, value?.driver?.lng ?: 0.0),
////                    type = 0
//                //)
//                if (isUser) {
//                    if (value != null){
////                        adapter = TimelineAdapter(timeLineItems)
////                        binding.rvTimeline.adapter = adapter
////
////                        checkDriverLocationRegardingToDeliveryPoints(value)
//                    }
//                } else {
////                        viewModel.drawRoute(getDirectionURL(
////                            value?.driver!!,
////                            value.pickup!!,
////                            getString(R.string.google_api_key)
////                        ))
//
//                }
//            }

        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(context, "Fail to get data.", Toast.LENGTH_SHORT)
                .show()
        }
    })
}

@Composable
fun MapMarker(
    context: Context,
    position: LatLng,
    type: Int
) {
    val icon = bitmapDescriptor(
        context, R.drawable.ic_baseline_directions_car_24
    )
   Marker(
        position = position,
        icon = if (type==0) icon else null ,
    )
}

fun bitmapDescriptor(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}


@Composable
fun PermissionUI(
    context: Context,
    permission: String,
    permissionRationale: String,
    scaffoldState: ScaffoldState,
    permissionAction: (PermissionAction) -> () -> Unit
) {


    val permissionGranted =
        checkIfPermissionGranted(
            context,
            permission
        )

    if (permissionGranted) {
        Log.d(TAG, "Permission already granted, exiting..")
        GetData(context)
        permissionAction(PermissionAction.OnPermissionGranted)
        return
    }


    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(TAG, "Permission provided by user")
            permissionAction(PermissionAction.OnPermissionGranted)
        } else {
            Log.d(TAG, "Permission denied by user")
            // Permission Denied
            permissionAction(PermissionAction.OnPermissionDenied)
        }
    }


    val showPermissionRationale = shouldShowPermissionRationale(
        context,
        permission
    )


    if (showPermissionRationale) {
        Log.d(TAG, "Showing permission rationale for $permission")

        LaunchedEffect(showPermissionRationale) {

            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = permissionRationale,
                actionLabel = "Grant Access",
                duration = SnackbarDuration.Long

            )
            when (snackbarResult) {
                SnackbarResult.Dismissed -> {
                    Log.d(TAG, "User dismissed permission rationale for $permission")
                    //User denied the permission, do nothing
                    permissionAction(PermissionAction.OnPermissionDenied)
                }
                SnackbarResult.ActionPerformed -> {
                    Log.d(
                        TAG,
                        "User granted permission for $permission rationale. Launching permission request.."
                    )
                    launcher.launch(permission)
                }
            }
        }
    } else {
        //Request permissions again
        Log.d(TAG, "Requesting permission for $permission again")
        SideEffect {
            launcher.launch(permission)
        }

    }

}


fun shouldShowPermissionRationale(context: Context, permission: String): Boolean {

    val activity = context as Activity?
    return ActivityCompat.shouldShowRequestPermissionRationale(
        activity!!,
        permission
    )
}

fun checkIfPermissionGranted(context: Context, permission: String): Boolean {

    val activity = context as Activity?
    return ActivityCompat.checkSelfPermission(
        activity!!,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}


