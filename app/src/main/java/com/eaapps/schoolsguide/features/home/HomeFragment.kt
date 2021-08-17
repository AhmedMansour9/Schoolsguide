package com.eaapps.schoolsguide.features.home

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentHomeBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.domain.model.SearchType
import com.eaapps.schoolsguide.utils.FlowEvent
import com.eaapps.schoolsguide.utils.launchFragment
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.JustifyContent
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
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
                    featured = false
                )
            )
        )
    }

    private val sliderAdapter = SliderAdapter()

    private val featureSchoolHomeAdapter = SchoolHomeAdapter({ toggleFavorite(it) }) {
        launchFragment(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it))
    }

    private val recommendedSchoolHomeAdapter =SchoolHomeAdapter({ toggleFavorite(it) }) {
        launchFragment(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.executePendingBindings()
        //requireActivity().colorStatusBar(R.color.transparent, R.color.black, false)
        // requireActivity().transparentStatusBar()
        setupSlider()
        val flexBoxLayoutManager = com.google.android.flexbox.FlexboxLayoutManager(requireContext())
        flexBoxLayoutManager.apply {
            alignItems = AlignItems.CENTER
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.NOWRAP
            justifyContent = JustifyContent.SPACE_EVENLY
        }
        binding.rcSchoolType.layoutManager = flexBoxLayoutManager
        binding.rcSchoolType.adapter = schoolTypeAdapter

        binding.rcRecommended.adapter = recommendedSchoolHomeAdapter

        binding.rcFeature.adapter = featureSchoolHomeAdapter

        binding.cardSearch.setOnClickListener {
            launchFragment(
                HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                    SearchType(
                        -1,
                        recommended = false,
                        featured = false
                    )
                )
            )
        }

        schoolTypeCollectData()
        sliderCollectData()
        recommendedCollectData()
        featureCollectData()

        binding.viewAllFeature.setOnClickListener {
            launchFragment(
                HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                    SearchType(
                        -1,
                        recommended = false,
                        featured = true
                    )
                )
            )

        }

        binding.viewAllRecommended.setOnClickListener {
            launchFragment(
                HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                    SearchType(
                        -1,
                        recommended = true,
                        featured = false
                    )
                )
            )
        }
    }

    private fun toggleFavorite(schoolId: Int) = homeViewModel.toggleFavorite(schoolId)

    private fun setupSlider() {
        binding.viewSlider.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewSlider.adapter = sliderAdapter
        val pagerOffset = resources.getDimensionPixelOffset(R.dimen.pagerOffset)
        val pagerMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        binding.viewSlider.offscreenPageLimit = 2
        binding.viewSlider.setPageTransformer { page, position ->
            val myOffset = position * -(2 * pagerOffset + pagerMarginPx)
            if (binding.viewSlider.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(binding.viewSlider) == ViewCompat.LAYOUT_DIRECTION_RTL)
                    page.translationX = -myOffset
                else
                    page.translationX = myOffset
            } else
                page.translationY = myOffset
        }
    }

    private fun setupIndicator(size: Int) {
        TabLayoutMediator(
            binding.indicator, binding.viewSlider
        ) { _, _ -> }.attach()

        lifecycleScope.launchWhenStarted {
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
                if (binding.viewSlider.currentItem < size - 1)
                    binding.viewSlider.currentItem = binding.viewSlider.currentItem + 1
                else
                    binding.viewSlider.currentItem = 0

            }.launchIn(this)
        }
    }

    private fun schoolTypeCollectData() {
        lifecycleScope.launchWhenCreated {
            homeViewModel.schoolTypeStateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {
                schoolTypeAdapter.setData(it)
            }))
        }
    }

    private fun sliderCollectData() {
        lifecycleScope.launchWhenCreated {
            homeViewModel.sliderStateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {
                sliderAdapter.setData(it.reversed())
                setupIndicator(it.size)
            }))
        }
    }

    private fun recommendedCollectData() {
        lifecycleScope.launchWhenCreated {
            homeViewModel.recommendedStateFlow.collect(FlowEvent(onError = {}, onSuccess = {
                recommendedSchoolHomeAdapter.setData(it)
            }))

        }
    }

    private fun featureCollectData() {
        lifecycleScope.launchWhenCreated {
            homeViewModel.featureStateFlow.collect(FlowEvent(onError = {}, onSuccess = {
                featureSchoolHomeAdapter.setData(it)
            }))

        }
    }

}

