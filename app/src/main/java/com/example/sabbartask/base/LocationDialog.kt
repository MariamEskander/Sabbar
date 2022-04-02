package com.example.sabbartask.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.sabbartask.R
import com.example.sabbartask.databinding.DialogViewBinding


class LocationDialog(context: Context,val openGps: Boolean) : Dialog(context) {

    lateinit var binding : DialogViewBinding

    private lateinit var onLocationButtonDialog: OnLocationButtonClickListener

    fun setDialogListener(mListener: OnLocationButtonClickListener) {
        this.onLocationButtonDialog = mListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dialogTitle.text = context.getString(R.string.location_dialog_title)
        binding.dialogMsg.text = context.getString(R.string.location_dialog_msg)
        binding.confirm.text = context.getString(R.string.location_dialog_button)

        binding.confirm.setOnClickListener {
            if (::onLocationButtonDialog.isInitialized)
            onLocationButtonDialog.onLocationButtonDialog(openGps)
            dismiss()
        }
        binding.cancel.setOnClickListener {
            if (::onLocationButtonDialog.isInitialized)
            onLocationButtonDialog.onCancelLocationButtonDialog()
            dismiss()
        }
    }

    interface OnLocationButtonClickListener {
        fun onLocationButtonDialog(openGps: Boolean)
        fun onCancelLocationButtonDialog()
    }

}