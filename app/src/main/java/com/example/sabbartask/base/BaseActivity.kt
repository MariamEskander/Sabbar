package com.example.sabbartask.base

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sabbartask.R
import com.example.sabbartask.data.remote.entities.LocationData
import com.example.sabbartask.utils.extensions.observe
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() ,
    LocationDialog.OnLocationButtonClickListener {

    private var pd: Dialog? = null

    abstract fun getViewModel(): BaseViewModel?

    abstract fun initView()

    abstract fun initClickListeners()

    abstract fun setObservers()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onStart() {
        super.onStart()
        createCustomProgressDialog()
        initView()
        initListeners()
        initClickListeners()
        setObservers()
    }

    private fun initListeners() {
        observe(getViewModel()?.showNetworkError) {
            Toast.makeText(this,getString(R.string.check_internet_connection),Toast.LENGTH_LONG).show()
        }
    }

    private fun createCustomProgressDialog() {
        this.let {
            pd = Dialog(it, R.style.DialogCustomTheme)
            pd?.setContentView(R.layout.layout_dialog_progress)
            pd?.setCancelable(false)
        }
    }

    fun showDialogLoading() {
        pd?.let {
            if (!it.isShowing)
                it.show()
        }
    }

    fun hideDialogLoading() {
        pd?.let {
            if (it.isShowing)
                it.dismiss()
        }
    }


    override fun onLocationButtonDialog(openGps: Boolean) {
        if (openGps)
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        else {
            val gpsSetting = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            gpsSetting.data = uri
            startActivity(gpsSetting)
        }
    }

    fun getDirectionURL(origin: LocationData, dest: LocationData, secret: String) : String{
        return "directions/json?origin=${origin.lat},${origin.lat}" +
                "&destination=${dest.lat},${dest.lat}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }


    override fun onCancelLocationButtonDialog() {
        finish()
    }

}