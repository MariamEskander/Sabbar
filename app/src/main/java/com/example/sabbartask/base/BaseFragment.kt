package com.example.sabbartask.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sabbartask.R
import com.example.sabbartask.utils.extensions.observe
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    private var pd: Dialog? = null

    abstract fun getViewModel(): BaseViewModel?

    abstract fun initClickListeners()

    abstract fun setObservers()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createCustomProgressDialog()
        onViewReady()
        initListeners()
        initClickListeners()
        setObservers()
    }

    private fun initListeners() {
        observe(getViewModel()?.showNetworkError) {
            Toast.makeText(requireContext(),getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show()
        }
    }

    abstract fun onViewReady()

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