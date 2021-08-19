package com.eaapps.schoolsguide.features.details

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.AddInquiryBottomSheetBinding
import com.eaapps.schoolsguide.databinding.FragmentDetailsDialogBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.domain.model.NavigationPropertiesModel
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.createDialog
import com.eaapps.schoolsguide.utils.launchFragment
import com.eaapps.schoolsguide.utils.showBottomSheet
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlin.math.abs

typealias navigationItem = NavigationPropertiesModel

@InternalCoroutinesApi
@AndroidEntryPoint
class DetailsFragment : DialogFragment(R.layout.fragment_details_dialog) {

    private val binding: FragmentDetailsDialogBinding by viewBinding(FragmentDetailsDialogBinding::bind)

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(R.style.AppTheme, Color.TRANSPARENT, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.executePendingBindings()
        toggleFollowResultData()
        toggleRecommendedResultData()
        binding.bindActionBar()
        binding.bindArgs()
        binding.bindPropertiesList()
        binding.bindClicks()
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
        viewModel.loadSchoolDetails(dataSchool?.id!!)
        schoolDetailsResultData()
    }

    private fun FragmentDetailsDialogBinding.bindPropertiesList() {
        val grid = GridLayoutManager(requireContext(), 3)
        rcListProperties.layoutManager = grid
        rcListProperties.adapter = PropertiesAdapter(
            arrayListOf(
                navigationItem(
                    getString(R.string._tuition_fees),
                    R.drawable.attach_money_black_24dp
                ),
                navigationItem(getString(R.string.contact_times), R.drawable.date_range_black_24dp),
                navigationItem(
                    getString(R.string.awards_school),
                    R.drawable.military_tech_black_24dp
                ),
                navigationItem(getString(R.string.service_statistics), R.drawable.training),
                navigationItem(getString(R.string.news_school), R.drawable.news),
                navigationItem(getString(R.string.event_school), R.drawable.party),
                navigationItem(getString(R.string.achievements), R.drawable.podium_),
                navigationItem(getString(R.string.parent_comment), R.drawable.comment),
                navigationItem(getString(R.string.job_available), R.drawable.folder_job)
            )
        ) {

        }
    }

    private fun FragmentDetailsDialogBinding.bindClicks() {

        toolbar.setNavigationOnClickListener { dismiss() }

        follow.group.setOnClickListener {
            viewModel.toggleFollow(dataSchool?.id!!)
        }

        recommended.group.setOnClickListener {
            viewModel.toggleRecommended(dataSchool?.id!!)
        }

        joinDiscount.group.setOnClickListener {
        }

        valuation.group.setOnClickListener {
            inquiryBottomSheet()
        }

        bookNow.setOnClickListener {
            launchFragment(
                DetailsFragmentDirections.actionDetailsFragmentToBookNowBottomFragment(
                    dataSchool?.id!!
                )
            )
        }

    }

    private fun inquiryBottomSheet() {
        val binding = AddInquiryBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        val bottom = binding.showBottomSheet(requireActivity().windowManager, fullScreen = true)
        binding.bindListsDown()
        binding.bindClicks(bottom)
    }

    private fun AddInquiryBottomSheetBinding.bindListsDown() {
        val adapterType = ArrayAdapter(
            requireContext(),
            R.layout.city_list_item,
            resources.getStringArray(R.array.type_message)
        )

        val adapterMethod = ArrayAdapter(
            requireContext(),
            R.layout.city_list_item,
            resources.getStringArray(R.array.replay_method)
        )

        val adapterReplayTime = ArrayAdapter(
            requireContext(),
            R.layout.city_list_item,
            resources.getStringArray(R.array.replay_time)
        )

        (typeMessageEdit as? AutoCompleteTextView)?.setAdapter(adapterType)
        (replayMessageEdit as? AutoCompleteTextView)?.setAdapter(adapterMethod)
        (replayTimeEdit as? AutoCompleteTextView)?.setAdapter(adapterReplayTime)

    }

    private fun AddInquiryBottomSheetBinding.bindClicks(bottomSheetDialog: BottomSheetDialog) {
        cancelBtn.setOnClickListener {
            bottomSheetDialog.cancel()
        }

        sendMessage.setOnClickListener {

        }

        lifecycleScope.launchWhenStarted {
            viewModel.sendInquiryFlow.stateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {

            }))
        }
    }

    private fun schoolDetailsResultData() {
        lifecycleScope.launchWhenCreated {
            viewModel.schoolDetailsFlow.stateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {
                binding.dataSchool = it
            }))
        }
    }

    private fun toggleRecommendedResultData() {
        lifecycleScope.launchWhenCreated {
            viewModel.toggleRecommendedFlow.stateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {
                binding.dataSchool?.isRecommended?.apply {
                    binding.dataSchool?.isRecommended = !this
                    binding.dataSchool = binding.dataSchool
                }
            }))
        }
    }

    private fun toggleFollowResultData() {
        lifecycleScope.launchWhenCreated {
            viewModel.toggleFollowFlow.stateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {
                binding.dataSchool?.isFollowed?.apply {
                    binding.dataSchool?.isFollowed = !this
                    binding.dataSchool = binding.dataSchool
                }
            }))
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        findNavController().navigateUp()
    }

}