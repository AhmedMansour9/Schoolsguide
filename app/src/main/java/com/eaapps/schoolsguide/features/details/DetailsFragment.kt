package com.eaapps.schoolsguide.features.details

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.Observable
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.binding.ImageBinding
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.databinding.FragmentDetailsDialogBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.domain.model.NavigationPropertiesModel
import com.eaapps.schoolsguide.features.details.subfeature.adapters.PhotosAdapter
import com.eaapps.schoolsguide.utils.*
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

typealias navigationItem = NavigationPropertiesModel

@InternalCoroutinesApi
@AndroidEntryPoint
class DetailsFragment : DialogFragment(R.layout.fragment_details_dialog) {

    private val binding: FragmentDetailsDialogBinding by viewBinding(FragmentDetailsDialogBinding::bind)

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }

    private lateinit var dataSchool: SchoolResponse.SchoolData.DataSchool

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(
            R.style.AppTheme,
            Color.TRANSPARENT,
            false,
            shouldInterceptBackPress = true
        ) { dismiss() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.executePendingBindings()
        setupArgs()
        checkInternet()
        toggleFollowResultData()
        toggleRecommendedResultData()
        binding.bindActionBar()
        binding.bindSchoolDetailsResultData()
        binding.bindPropertiesList()
        binding.bindClicks()
    }

    private fun FragmentDetailsDialogBinding.bindActionBar() {
        toolbar.title = ""
        toolbar.inflateMenu(R.menu.details_menu)
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.support) {
                launchFragment(
                    DetailsFragmentDirections.actionDetailsFragmentToAddInquiryBottomFragment(
                        dataSchool?.id!!
                    )
                )
            } else {
                launchFragment(
                    DetailsFragmentDirections.actionDetailsFragmentToNotificationBottomFragment(
                        dataSchool!!
                    )
                )
            }
            false
        }
        collapsing.setContentScrimColor(ContextCompat.getColor(requireContext(), R.color.colorApp3))
        collapsing.setStatusBarScrimColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorApp3
            )
        )
        collapsing.isTitleEnabled = false
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (kotlin.math.abs(verticalOffset) > 200) {
                if (toolbar.title == "")
                    toolbar.title = getString(R.string.school_details)
            } else {
                if (toolbar.title.isNotEmpty())
                    toolbar.title = ""
            }
        })


    }

    private fun setupArgs() {
        dataSchool = DetailsFragmentArgs.fromBundle(requireArguments()).dataSchool
    }

    private fun checkInternet() {
        lifecycleScope.launchWhenStarted {
            NetworkChecker.isOnline().collect(
                FlowEvent(
                    onError = {
                        requireActivity().noInternetDialog {
                            viewModel.loadSchoolDetails(dataSchool.id)
                        }
                    },
                    onSuccess = {
                        viewModel.loadSchoolDetails(dataSchool.id)
                    })
            )
        }
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
                1 -> launchFragment(
                    DetailsFragmentDirections.actionDetailsFragmentToTuiitionFeesBottomFragment(
                        dataSchool!!
                    )
                )

                2 -> launchFragment(
                    DetailsFragmentDirections.actionDetailsFragmentToSupervisorContactBottomFragment(
                        dataSchool!!
                    )
                )
                3 -> launchFragment(
                    DetailsFragmentDirections.actionDetailsFragmentToAwardsBottomFragment(
                        dataSchool!!
                    )
                )
                4 -> launchFragment(
                    DetailsFragmentDirections.actionDetailsFragmentToServiceStatisticsBottomFragment(
                        dataSchool!!
                    )
                )

                5 -> launchFragment(
                    DetailsFragmentDirections.actionDetailsFragmentToBlogDialogFragment(
                        dataSchool!!, "news"
                    )
                )

                6 -> launchFragment(
                    DetailsFragmentDirections.actionDetailsFragmentToBlogDialogFragment(
                        dataSchool!!, "events"
                    )
                )

                7 -> launchFragment(
                    DetailsFragmentDirections.actionDetailsFragmentToAchievementDialogFragment(
                        dataSchool!!
                    )
                )

                8 -> launchFragment(
                    DetailsFragmentDirections.actionDetailsFragmentToReviewParentBottomFragment(
                        dataSchool!!
                    )
                )

                9 -> launchFragment(
                    DetailsFragmentDirections.actionDetailsFragmentToJobDialogFragment(
                        dataSchool!!
                    )
                )

            }
        }
    }

    private fun openWebPage(url: String) {
        var webpage = Uri.parse(url)
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://$url")
        }
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun FragmentDetailsDialogBinding.bindClicks() {

        toolbar.setNavigationOnClickListener { dismiss() }

        expandableBtn.setOnClickListener {
            about.toggle()
            if (about.isExpanded) {
                expandableBtn.icon = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.baseline_expand_more_black_20
                )
                expandableBtn.text = getString(R.string.expand_)
            } else {
                expandableBtn.icon = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.baseline_expand_less_black_20
                )
                expandableBtn.text = getString(R.string.collapse_)
            }
        }

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
                    dataSchool!!
                )
            )
        }

        faceBtn.setOnClickListener {
            dataSchool!!.facebook_link?.apply {
                if (this.isNotBlank())
                    openWebPage(this)
            }
        }

        instaBtn.setOnClickListener {
            dataSchool!!.instgram_link?.apply {
                if (this.isNotBlank())
                    openWebPage(this)
            }
        }

        twitterBtn.setOnClickListener {
            dataSchool!!.twitter_link?.apply {
                if (this.isNotBlank())
                    openWebPage(this)
            }
        }

        youtubeBtn.setOnClickListener {
            dataSchool!!.youtube_link?.apply {
                if (this.isNotBlank())
                    openWebPage(this)
            }
        }
    }

    private fun FragmentDetailsDialogBinding.bindSchoolDetailsResultData() {
        lifecycleScope.launchWhenStarted {
            viewModel.schoolDetailsFlow.stateFlow.collect(FlowEvent(onErrors = {
                bindStopShimmer()
                handleApiError(it) {
                    viewModel.loadSchoolDetails(dataSchool?.id!!)
                }
            }, onSuccess = {
                bindStopShimmer()
                dataSchool = it
                bindSchoolPhotosList()
            }, onLoading = {
                bindStartShimmer()
            }))
        }
    }

    private fun FragmentDetailsDialogBinding.bindSchoolPhotosList() {
        rcPhotos.adapter = PhotosAdapter(dataSchool!!.gallary) {
            ImageBinding.imageByUrl(picSchool, it.image)
        }
        rcPhotos.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }

        })
    }

    private fun FragmentDetailsDialogBinding.bindStartShimmer() {
        picShimmerLayout.startShimmer()
        picShimmerLayout.visibleOrGone(true)

        detailsShimmer.detailsShimmer.startShimmer()
        detailsShimmer.detailsShimmer.visibleOrGone(true)

        shimmerLike.startShimmer()
        shimmerLike.visibleOrGone(true)

        shimmerSee.startShimmer()
        shimmerSee.visibleOrGone(true)
    }

    private fun FragmentDetailsDialogBinding.bindStopShimmer() {

        picShimmerLayout.stopShimmer()
        picShimmerLayout.visibleOrGone(false)

        detailsShimmer.detailsShimmer.stopShimmer()
        detailsShimmer.detailsShimmer.visibleOrGone(false)

        shimmerLike.stopShimmer()
        shimmerLike.visibleOrGone(false)

        shimmerSee.stopShimmer()
        shimmerSee.visibleOrGone(false)

        groupSee.visibleOrGone(true)
        groupRate.visibleOrGone(true)
        detailsGroup.visibleOrGone(true)
        picSchool.visibleOrGone(true)

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
        viewModel.schoolDetailsFlow.setValue(Resource.Nothing())
        findNavController().navigateUp()
    }

}

private const val TAG = "DetailsFragment"