package com.example.sabbartask.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.sabbartask.R
import com.example.sabbartask.utils.extensions.observe
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var pd: Dialog? = null

    abstract fun getViewModel(): BaseViewModel?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createCustomProgressDialog()
        onViewReady()
        initListeners()
        setObservers()
    }

    private fun initListeners() {
        observe(getViewModel()?.showNetworkError) {
            Toast.makeText(requireContext(),getString(R.string.check_internet_connection),Toast.LENGTH_LONG).show()
        }
    }

    abstract fun onViewReady()

    abstract fun setObservers()

    private fun createCustomProgressDialog() {
        context?.let {
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


}