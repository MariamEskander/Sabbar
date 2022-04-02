package com.example.sabbartask.ui_not_compose.identity.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.sabbartask.R
import com.example.sabbartask.base.BaseBottomSheetDialogFragment
import com.example.sabbartask.base.BaseViewModel
import com.example.sabbartask.data.remote.entities.LocationData
import com.example.sabbartask.data.remote.entities.NOTIFCATION_TYPE
import com.example.sabbartask.databinding.SheetLocationBinding
import com.example.sabbartask.ui_not_compose.identity.DriverUserViewModel
import com.example.sabbartask.utils.Constants.DATA
import com.example.sabbartask.utils.Constants.DRIVER
import com.example.sabbartask.utils.Constants.DROP_OFF
import com.example.sabbartask.utils.Constants.NOTIFICATION_TYPE
import com.example.sabbartask.utils.Constants.PICKUP
import com.example.sabbartask.utils.extensions.addToFirebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationsSheet : BaseBottomSheetDialogFragment() {

    companion object {
        const val TAG = "LocationsSheet"

        fun newInstance(): LocationsSheet {
            val args = Bundle()
            val fragment = LocationsSheet()
            fragment.arguments = args
            return fragment
        }

    }

    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null

    private val viewModel: DriverUserViewModel by viewModels()

    override fun getViewModel(): BaseViewModel = viewModel
    private var _binding: SheetLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SheetLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewReady() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase?.getReference(DATA)

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnAddPickupDropoff.setOnClickListener {
            if (binding.etPickupLat.text?.trim().isNullOrEmpty() ||
                binding.etPickupLng.text?.trim().isNullOrEmpty() ||
                binding.etDropoffLat.text?.trim().isNullOrEmpty() ||
                binding.etDropoffLng.text?.trim().isNullOrEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.data_required),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                sendDataToFirebase()
                dismiss()
            }
        }
    }

    private fun sendDataToFirebase() {
        addToFirebase(
            databaseReference, PICKUP, LocationData(
                binding.etPickupLat.text?.trim().toString().toDouble(),
                binding.etPickupLng.text?.trim().toString().toDouble()
            )
        )
        addToFirebase(
            databaseReference, DROP_OFF, LocationData(
                binding.etDropoffLat.text?.trim().toString().toDouble(),
                binding.etDropoffLng.text?.trim().toString().toDouble()
            )
        )
        addToFirebase(
            databaseReference, DRIVER, LocationData(0.0, 0.0)
        )
        addToFirebase(
            databaseReference, NOTIFICATION_TYPE,  NOTIFCATION_TYPE.NEAR_PICKUP.ordinal
        )

    }


    override fun setObservers() {}

}