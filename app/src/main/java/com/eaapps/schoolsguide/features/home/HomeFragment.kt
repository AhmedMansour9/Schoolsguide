package com.eaapps.schoolsguide.features.home

import android.os.Bundle
import android.view.View
import androidx.core.app.ShareCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentHomeBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.domain.model.SearchType
import com.eaapps.schoolsguide.utils.*
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.JustifyContent
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

@InternalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)

    private val schoolTypeAdapter = SchoolTypeAdapter {
        launchFragment(
            HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                SearchType(
                    it.id,
                    recommended = false,
                    featured = false,
                    search = false
                )
            )
        )
    }

    private val sliderAdapter = SliderAdapter()

    private var jobTime: Job? = null
    private val featureSchoolHomeAdapter =
        SchoolHomeAdapter({ toggleFavorite(it) }, onShareSchool = { it ->
            shortLink(it) {
                ShareCompat.IntentBuilder(requireContext())
                    .setType("text/plain")
                    .setChooserTitle("Share School")
                    .setText(it)
                    .startChooser()
            }
        }) {
            launchFragment(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it))
        }

    private val recommendedSchoolHomeAdapter =
        SchoolHomeAdapter({ toggleFavorite(it) }, onShareSchool = { it ->
            shortLink(it) {
                ShareCompat.IntentBuilder(requireContext())
                    .setType("text/plain")
                    .setChooserTitle("Share School")
                    .setText(it)
                    .startChooser()
            }
        }) {
            launchFragment(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it))
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.executePendingBindings()
        checkInternet()
        binding.bindAdapters()
        binding.bindClicks()
        binding.bindRefresh()
        binding.bindViewPagerSlider()
        binding.bindSchoolTypeCollectData()
        binding.bindRecommendedCollectData()
        binding.bindFeatureCollectData()
        binding.bindSliderCollectData()
    }

    private fun checkInternet() {
        lifecycleScope.launchWhenStarted {
            NetworkChecker.isOnline().collect(
                FlowEvent(
                    onError = {
                        requireActivity().noInternetDialog {
                            homeViewModel.init()
                        }
                    },
                    onSuccess = {
                        homeViewModel.init()
                    })
            )
        }
    }

    private fun toggleFavorite(schoolId: Int) = homeViewModel.toggleFavorite(schoolId)

    private fun FragmentHomeBinding.bindAdapters() {
        val flexBoxLayoutManager = com.google.android.flexbox.FlexboxLayoutManager(requireContext())
        flexBoxLayoutManager.apply {
            alignItems = AlignItems.CENTER
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.NOWRAP
            justifyContent = JustifyContent.SPACE_EVENLY
        }
        rcSchoolType.layoutManager = flexBoxLayoutManager
        rcSchoolType.adapter = schoolTypeAdapter
        rcRecommended.adapter = recommendedSchoolHomeAdapter
        rcFeature.adapter = featureSchoolHomeAdapter
    }

    private fun FragmentHomeBinding.bindClicks() {
        cardSearch.setOnClickListener {
            launchFragment(
                HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                    SearchType(
                        -1,
                        recommended = false,
                        featured = false,
                        search = true
                    )
                )
            )
        }

        viewAllFeature.setOnClickListener {
            launchFragment(
                HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                    SearchType(
                        -1,
                        recommended = false,
                        featured = true,
                        search = false
                    )
                )
            )

        }

        viewAllRecommended.setOnClickListener {
            launchFragment(
                HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                    SearchType(
                        -1,
                        recommended = true,
                        featured = false,
                        search = false
                    )
                )
            )
        }
    }

    private fun FragmentHomeBinding.bindRefresh() {
        refresh.setOnRefreshListener {
            homeViewModel.init()
            refresh.isRefreshing = false
        }
    }

    private fun FragmentHomeBinding.bindIndicator(size: Int) {
        TabLayoutMediator(
            indicator, viewSlider
        ) { _, _ -> }.attach()
        jobTime = lifecycleScope.launchWhenStarted {
            var counter: Long = 0
            flow {
                val timeInMillis: Long = 4
                val delayTime = timeInMillis * 1000
                while (true) {
                    delay(timeMillis = delayTime)
                    emit(counter++)
                }
            }.onStart {
                emit(-1)
            }.onEach {
                if (viewSlider.currentItem < size - 1)
                    viewSlider.currentItem = viewSlider.currentItem + 1
                else
                    viewSlider.currentItem = 0
            }.launchIn(this)
        }

    }

    private fun FragmentHomeBinding.bindSchoolTypeCollectData() {
        lifecycleScope.launchWhenCreated {
            homeViewModel.schoolTypeStateFlow.collect(FlowEvent(
                onErrors = { it ->
                    shimmerSchoolType.visibleOrGone(false)
                    shimmerSchoolType.stopShimmer()
                    handleApiError(it, retry = {
                        homeViewModel.loadSchoolType()
                    })
                },
                onSuccess = {
                    shimmerSchoolType.visibleOrGone(false)
                    shimmerSchoolType.stopShimmer()
                    rcSchoolType.visibleOrGone(true)
                    schoolTypeAdapter.setData(it)
                },
                onLoading = {
                    rcSchoolType.visibleOrGone(false)
                    shimmerSchoolType.visibleOrGone(true)
                    shimmerSchoolType.startShimmer()
                }
            ))
        }
    }

    private fun FragmentHomeBinding.bindSliderCollectData() {
        lifecycleScope.launchWhenCreated {
            homeViewModel.sliderStateFlow.collect(FlowEvent(
                onErrors = { it ->
                    shimmerSlider.visibleOrGone(false)
                    shimmerSlider.stopShimmer()
                    handleApiError(it, retry = {
                        homeViewModel.loadSlider()
                    })

                }, onLoading = {
                    jobTime?.cancel()
                    viewSlider.visibleOrGone(false)
                    shimmerSlider.visibleOrGone(true)
                    shimmerSlider.startShimmer()
                }, onSuccess = {
                    shimmerSlider.visibleOrGone(false)
                    viewSlider.visibleOrGone(true)
                    shimmerSlider.stopShimmer()
                    sliderAdapter.setData(it.reversed())
                    bindIndicator(it.size)
                })
            )
        }
    }

    private fun FragmentHomeBinding.bindRecommendedCollectData() {
        lifecycleScope.launchWhenCreated {
            homeViewModel.recommendedStateFlow.collect(FlowEvent(
                onErrors = { it ->
                    shimmerRecommended.visibleOrGone(false)
                    shimmerRecommended.stopShimmer()
                    handleApiError(it, retry = {
                        homeViewModel.loadRecommendedSchool()
                    })
                }, onLoading = {
                    shimmerRecommended.visibleOrGone(true)
                    rcRecommended.visibleOrGone(false)
                    shimmerRecommended.startShimmer()
                }, onSuccess = {
                    shimmerRecommended.visibleOrGone(false)
                    shimmerRecommended.stopShimmer()
                    rcRecommended.visibleOrGone(true)
                    recommendedSchoolHomeAdapter.setData(it)
                })
            )

        }
    }

    private fun FragmentHomeBinding.bindFeatureCollectData() {
        lifecycleScope.launchWhenCreated {
            homeViewModel.featureStateFlow.collect(
                FlowEvent(
                    onErrors = { it ->
                        shimmerFeature.visibleOrGone(false)
                        shimmerFeature.stopShimmer()
                        handleApiError(it, retry = {
                            homeViewModel.loadFeatureSchool()
                        })
                    },
                    onLoading = {
                        shimmerFeature.visibleOrGone(true)
                        rcFeature.visibleOrGone(false)
                        shimmerFeature.startShimmer()
                    },
                    onSuccess = {
                        shimmerFeature.visibleOrGone(false)
                        shimmerFeature.stopShimmer()
                        rcFeature.visibleOrGone(true)
                        featureSchoolHomeAdapter.setData(it)
                    })
            )

        }
    }

    private fun FragmentHomeBinding.bindViewPagerSlider() {
        viewSlider.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewSlider.adapter = sliderAdapter
        val pagerOffset = resources.getDimensionPixelOffset(R.dimen.pagerOffset)
        val pagerMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        viewSlider.offscreenPageLimit = 2
        viewSlider.setPageTransformer { page, position ->
            val myOffset = position * -(2 * pagerOffset + pagerMarginPx)
            if (viewSlider.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(viewSlider) == ViewCompat.LAYOUT_DIRECTION_RTL)
                    page.translationX = -myOffset
                else
                    page.translationX = myOffset
            } else
                page.translationY = myOffset
        }
    }

}

