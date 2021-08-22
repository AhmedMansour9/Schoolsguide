package com.eaapps.schoolsguide.features.details

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentDetailsDialogBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.domain.model.NavigationPropertiesModel
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.createDialog
import com.eaapps.schoolsguide.utils.launchFragment
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToast.Companion.LONG_DURATION
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
                    1,
                    getString(R.string._tuition_fees),
                    R.drawable.attach_money_black_24dp
                ),
                navigationItem(
                    2,
                    getString(R.string.contact_times),
                    R.drawable.date_range_black_24dp
                ),
                navigationItem(
                    3,
                    getString(R.string.awards_school),
                    R.drawable.military_tech_black_24dp
                ),
                navigationItem(4, getString(R.string.service_statistics), R.drawable.training),
                navigationItem(5, getString(R.string.news_school), R.drawable.news),
                navigationItem(6, getString(R.string.event_school), R.drawable.party),
                navigationItem(7, getString(R.string.achievements), R.drawable.podium_),
                navigationItem(8, getString(R.string.parent_comment), R.drawable.comment),
                navigationItem(9, getString(R.string.job_available), R.drawable.folder_job)
            )
        ) {
            when (it.id) {
                1 -> {
                    if (dataSchool?.tuition_fees?.size!! > 0)
                        launchFragment(
                            DetailsFragmentDirections.actionDetailsFragmentToTuiitionFeesBottomFragment(
                                dataSchool!!
                            )
                        )
                    else
                        MotionToast.createColorToast(
                            requireActivity(),
                            getString(R.string.info),
                            getString(R.string._info_fees),
                            MotionToast.TOAST_INFO,
                            MotionToast.GRAVITY_BOTTOM,
                            LONG_DURATION,
                            ResourcesCompat.getFont(requireContext(), R.font.rpt_bold)
                        )

                }

                2 -> launchFragment(
                    DetailsFragmentDirections.actionDetailsFragmentToSupervisorContactBottomFragment(
                        dataSchool!!
                    )
                )

                4 -> launchFragment(
                    DetailsFragmentDirections.actionDetailsFragmentToServiceStatisticsBottomFragment(
                        dataSchool!!
                    )
                )

                8 -> launchFragment(
                    DetailsFragmentDirections.actionDetailsFragmentToReviewParentBottomFragment(
                        dataSchool!!
                    )
                )
            }
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
            launchFragment(
                DetailsFragmentDirections.actionDetailsFragmentToJoinDiscountBottomFragment(
                    dataSchool?.id!!
                )
            )
        }

        valuation.group.setOnClickListener {
            launchFragment(
                DetailsFragmentDirections.actionDetailsFragmentToSchoolValueBottomFragment(
                    dataSchool?.id!!
                )
            )
        }

        bookNow.setOnClickListener {
            launchFragment(
                DetailsFragmentDirections.actionDetailsFragmentToBookNowBottomFragment(
                    dataSchool?.id!!
                )
            )
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