package com.eaapps.schoolsguide.features.search.subfeature

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.FragmentDialogMapBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.features.search.Filter
import com.eaapps.schoolsguide.features.search.MapSchoolAdapter
import com.eaapps.schoolsguide.features.search.ShareViewModel
import com.eaapps.schoolsguide.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.ktx.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

private const val TAG = "SearchMapDialogFragment"

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchMapDialogFragment : DialogFragment(R.layout.fragment_dialog_map) {

    private val binding: FragmentDialogMapBinding by viewBinding(FragmentDialogMapBinding::bind)
    private val shareViewModel: ShareViewModel by activityViewModels()

    private lateinit var mapFragment: SupportMapFragment
    private var googleMap: GoogleMap? = null
    private lateinit var permission: ActivityResultLauncher<String>
    private lateinit var bitmapIconRide: Bitmap

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupPermission()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = createDialog(
        R.style.AppTheme,
        Color.WHITE,
        true,
        shouldInterceptBackPress = true
    ) { dismiss() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bitmapIconRide = AppCompatResources.getDrawable(requireContext(), R.drawable.rider_icon)
            ?.toBitmap() as Bitmap
        binding.clicks()
        binding.bindMap()
        binding.bindCollectFilterFire()
        binding.collectMarkSchoolMap()
    }

    private fun setupPermission() {
        permission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it)
                location()
            else
                snackbar("Please Allow Permission Location")
        }
    }

    private fun takePermission() {
        if (requireContext().hasPermission(Manifest.permission.ACCESS_FINE_LOCATION))
            permission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        else
            location()
    }

    @SuppressLint("MissingPermission")
    private fun FragmentDialogMapBinding.bindMap() {
        mapFragment = childFragmentManager.findFragmentById(map.id) as SupportMapFragment
        lifecycleScope.launchWhenCreated {
            googleMap = mapFragment.awaitMap()
            googleMap?.apply {
                mapType = GoogleMap.MAP_TYPE_NORMAL
                setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
                isMyLocationEnabled = true
                uiSettings.isZoomControlsEnabled = false
                uiSettings.isCompassEnabled = false
                uiSettings.isZoomGesturesEnabled = true
                uiSettings.isIndoorLevelPickerEnabled = true
                uiSettings.isScrollGesturesEnabled = true
                setMaxZoomPreference(30F)
                setMinZoomPreference(9F)
                takePermission()
            }
        }


        lifecycle.coroutineScope.launchWhenStarted {
            googleMap?.cameraEvents()?.collect {
                when (it) {
                    CameraIdleEvent -> Unit
                    CameraMoveCanceledEvent -> Unit
                    CameraMoveEvent -> Unit
                    is CameraMoveStartedEvent -> {
                    }
                }
            }
        }
    }

    private fun FragmentDialogMapBinding.clicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }

        filterBtn.setOnClickListener {
            launchFragment(SearchMapDialogFragmentDirections.actionSearchMapDialogFragmentToFilterBottomFragment())
        }
    }

    private fun FragmentDialogMapBinding.bindCollectFilterFire() {
        lifecycleScope.launchWhenCreated {
            shareViewModel.filterFire.collect(FlowEvent(onSuccess = {
                when (it) {
                    Filter.APPLY_FILTER -> {
                        shareViewModel.loadSchoolMap()
                        if (shareViewModel.filterModel.isFilter()) {
                            filterBtn.setImageResource(R.drawable.baseline_filter_alt_black_48)
                        } else {
                            filterBtn.setImageResource(R.drawable.outline_filter_alt_black_48)
                        }
                    }
                    Filter.CLEAR_FILTER -> {
                        if (shareViewModel.filterModel.isFilter()) {
                            filterBtn.setImageResource(R.drawable.baseline_filter_alt_black_48)
                        } else {
                            filterBtn.setImageResource(R.drawable.outline_filter_alt_black_48)
                        }
                    }
                }
            }, onNothing = {
                googleMap?.clear()
            }))
        }
    }

    private fun location() {
        requireContext().currentLocation({
            googleMap?.apply {
                newLocationWithZoom(it.latitude, it.longitude, animation = false)
            }
        }) {
            takePermission()
        }
    }

    override fun onResume() {
        mapFragment.onResume()
        shareViewModel.mapSearch = true
        super.onResume()
    }

    override fun onDestroy() {
        mapFragment.onDestroy()
        super.onDestroy()
    }

    override fun onStop() {
        mapFragment.onStop()
        super.onStop()
    }

    override fun onDismiss(dialog: DialogInterface) {
        lifecycleScope.launchWhenStarted {
            shareViewModel.filterFire.emit(Resource.Nothing())
        }
        findNavController().navigateUp()
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun FragmentDialogMapBinding.collectMarkSchoolMap() {
        lifecycleScope.launchWhenCreated {
            shareViewModel.mapStateFlow.stateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {
                val list: ArrayList<LatLng> = ArrayList()
                val latLngBounds = LatLngBounds.Builder()
                it.forEach { schoolData ->
                    list.add(LatLng(schoolData.lat, schoolData.lng))
                    latLngBounds.include(LatLng(schoolData.lat, schoolData.lng))
                    googleMap?.apply {
                        addMarker {
                            position(LatLng(schoolData.lat, schoolData.lng))
                            anchor(0.5f, 0.5f)
                            icon(BitmapDescriptorFactory.fromBitmap(bitmapIconRide))
                        }.tag = schoolData
                    }
                }


                val bounds = latLngBounds.build()
                val width = resources.displayMetrics.widthPixels
                val height = resources.displayMetrics.heightPixels
                googleMap?.apply {
                    animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            width,
                            height,
                            (width * 0.20).toInt()
                        )
                    )
                }
                googleMap?.setOnMarkerClickListener { marker ->
                    groupRc.visibleOrGone(true)
                    val dataSchool = marker.tag as SchoolResponse.SchoolData.DataSchool
                    rcSchool.smoothScrollToPosition(it.indexOf(dataSchool))
                    false
                }
                rcSchool.adapter = MapSchoolAdapter(it)

            }))
        }
    }

}
