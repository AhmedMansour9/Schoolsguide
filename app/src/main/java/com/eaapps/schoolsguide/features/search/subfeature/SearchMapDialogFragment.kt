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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.FragmentDialogMapBinding
import com.eaapps.schoolsguide.delegate.viewBinding
 import com.eaapps.schoolsguide.features.search.adapter.MapSchoolAdapter
import com.eaapps.schoolsguide.features.search.viewmodels.Filter
import com.eaapps.schoolsguide.features.search.viewmodels.ShareViewModel
import com.eaapps.schoolsguide.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchMapDialogFragment : DialogFragment(R.layout.fragment_dialog_map) {

    private val binding: FragmentDialogMapBinding by viewBinding(FragmentDialogMapBinding::bind)

    private val shareViewModel: ShareViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ShareViewModel::class.java)
    }

    private val mapSchoolAdapter = MapSchoolAdapter()

    private lateinit var permission: ActivityResultLauncher<String>
    private lateinit var bitmapIconRide: Bitmap
    private lateinit var mapFragment: SupportMapFragment
    private var googleMap: GoogleMap? = null
    private val latLngBounds = LatLngBounds.Builder()
    private val markers = ArrayList<Marker>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupPermission()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(R.style.AppTheme, Color.WHITE, lightBar = true, true)
        { dismiss() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.executePendingBindings()
        bitmapIconRide = AppCompatResources.getDrawable(requireContext(), R.drawable.rider_icon)
            ?.toBitmap() as Bitmap
        binding.clicks()
        binding.bindMap()
        binding.bindList()
        binding.bindCollectFilterFire()
        binding.collectMarkSchoolMap()
    }

    private fun setupPermission() {
        permission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) location() else snackbar("Please Allow Permission Location")
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
                isMyLocationEnabled = false
                uiSettings.isZoomControlsEnabled = false
                uiSettings.isCompassEnabled = false
                uiSettings.isMapToolbarEnabled = false
                uiSettings.isZoomGesturesEnabled = true
                uiSettings.isIndoorLevelPickerEnabled = true
                uiSettings.isScrollGesturesEnabled = true
                setMaxZoomPreference(30F)
                setMinZoomPreference(5F)
                takePermission()
            }
        }
    }

    private fun FragmentDialogMapBinding.clicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }

        filterBtn.setOnClickListener {
            launchFragment(
                SearchMapDialogFragmentDirections.actionSearchMapDialogFragmentToFilterBottomFragment(
                    1
                )
            )
        }
    }

    private fun FragmentDialogMapBinding.bindStartLoading() {
        noResult.visibleOrGone(false)
        groupRc.visibleOrGone(false)
        progressBar.visibleOrGone(true)
    }

    private fun FragmentDialogMapBinding.bindStopLoading() {
        groupRc.visibleOrGone(false)
        progressBar.visibleOrGone(true)
    }

    private fun FragmentDialogMapBinding.bindCollectFilterFire() {
        lifecycleScope.launchWhenCreated {
            shareViewModel.filterMapFire.observe(viewLifecycleOwner, ObserveEvent(onSuccess = {
                when (it) {
                    Filter.APPLY_FILTER -> {
                        if (shareViewModel.filterModel.isFilter()) {
                            shareViewModel.filterSchoolByMap()
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

    private fun FragmentDialogMapBinding.bindList() {
        rcSchool.adapter = mapSchoolAdapter
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun FragmentDialogMapBinding.bindSchoolMarkers() {
        markers.forEach {
            latLngBounds.include(it.position)
        }
        val bounds = latLngBounds.build()
        googleMap?.apply {
            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
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
            val index = mapSchoolAdapter.indexSchool(dataSchool)
            if (index > -1)
                rcSchool.smoothScrollToPosition(index)
            googleMap?.setPadding(0, 0, 0, resources.getDimensionPixelOffset(R.dimen._190sdp))
            false
        }
    }

    @SuppressLint("PotentialBehaviorOverride", "SetTextI18n")
    private fun FragmentDialogMapBinding.collectMarkSchoolMap() {
        lifecycleScope.launchWhenCreated {
            shareViewModel.mapStateFlow.stateFlow.collect(
                FlowEvent(
                    onLoading = {
                        mapSchoolAdapter.setData(ArrayList())
                        googleMap?.clear()
                        bindStartLoading()
                    },
                    onError = {
                        bindStopLoading()
                        requireActivity().toastingError(it)
                    },
                    onSuccess = {
                        if (it.isNotEmpty()) {
                            mapSchoolAdapter.setData(it)
                            filterNumbers.text = "${it.size}"
                            schoolFilter.text = getString(R.string.schools_found)
                            val listSchoolLocations: ArrayList<LatLng> = ArrayList()
                            it.forEach { schoolData ->
                                listSchoolLocations.add(LatLng(schoolData.lat, schoolData.lng))
                                googleMap?.apply {
                                    val marker = addMarker {
                                        position(LatLng(schoolData.lat, schoolData.lng))
                                        anchor(0.5f, 0.5f)
                                        icon(BitmapDescriptorFactory.fromBitmap(bitmapIconRide))
                                    }
                                    marker.tag = schoolData
                                    markers.add(marker)
                                }
                            }
                            bindSchoolMarkers()
                        } else {
                            filterNumbers.text = ""
                            schoolFilter.text = getString(R.string.no_result)
                            groupRc.visibleOrGone(true)
                            noResult.visibleOrGone(true)
                        }
                        progressBar.visibleOrGone(false)
                    })
            )
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

    override fun onDismiss(dialog: DialogInterface) {
        shareViewModel.filterDefault()
        findNavController().navigateUp()
    }

    override fun onResume() {
        mapFragment.onResume()
        super.onResume()
    }

    override fun onStop() {
        mapFragment.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapFragment.onDestroy()
        super.onDestroy()
    }

}
