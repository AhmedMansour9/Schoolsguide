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
import com.eaapps.schoolsguide.utils.FlowEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()
    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val sliderAdapter = SliderAdapter()
    private val featureSchoolHomeAdapter = SchoolHomeAdapter()
    private val recommendedSchoolHomeAdapter = SchoolHomeAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.executePendingBindings()
        setupSlider()
        sliderCollectData()
        binding.rcRecommended.adapter = recommendedSchoolHomeAdapter
        binding.rcFeature.adapter = featureSchoolHomeAdapter
        recommendedCollectData()
        featureCollectData()
    }

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

    private fun sliderCollectData() {
        lifecycleScope.launchWhenCreated {
            homeViewModel.sliderStateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {
                sliderAdapter.setData(it)
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

