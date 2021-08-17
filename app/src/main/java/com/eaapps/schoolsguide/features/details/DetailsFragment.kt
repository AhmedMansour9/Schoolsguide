package com.eaapps.schoolsguide.features.details

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentDetailsDialogBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.createDialog
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs


@AndroidEntryPoint
class DetailsFragment : DialogFragment(R.layout.fragment_details_dialog) {

    private val binding: FragmentDetailsDialogBinding by viewBinding(FragmentDetailsDialogBinding::bind)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(R.style.AppTheme, Color.TRANSPARENT, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.executePendingBindings()
        binding.bindActionBar()
        binding.bindArgs()
    }

    private fun FragmentDetailsDialogBinding.bindActionBar() {
        toolbar.title = ""
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsing.setContentScrimColor(ContextCompat.getColor(requireContext(), R.color.colorApp3))
        collapsing.setStatusBarScrimColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorApp3
            )
        )

        collapsing.isTitleEnabled = false
        appbar.addOnOffsetChangedListener(OnOffsetChangedListener { _, verticalOffset ->
            if (abs(verticalOffset) > 200) toolbar.title = "School Details" else toolbar.title = ""
        })
    }

    private fun FragmentDetailsDialogBinding.bindArgs() {
        dataSchool = DetailsFragmentArgs.fromBundle(requireArguments()).dataSchool
    }

}