package com.eaapps.schoolsguide.features.profile.subfeature.orderSchool

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentOrdersBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class OrderSchoolFragment : DialogFragment(R.layout.fragment_orders) {

    private val binding: FragmentOrdersBinding by viewBinding(FragmentOrdersBinding::bind)

    private val viewModel: OrderViewModel by viewModels()

    private val orderAdapter = OrderAdapter {
        launchFragment(OrderSchoolFragmentDirections.actionOrderSchoolFragmentToDetailsFragment(it))
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(
            R.style.AppTheme,
            Color.WHITE,
            true,
            shouldInterceptBackPress = true
        ) { dismiss() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindList()
        binding.bindCollectOrderList()
        binding.bindClicks()
    }

    private fun FragmentOrdersBinding.bindList() {
        rcOrders.adapter = orderAdapter
    }

    private fun FragmentOrdersBinding.bindClicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun FragmentOrdersBinding.bindCollectOrderList() {
        lifecycleScope.launchWhenStarted {
            viewModel.ordersFlow.stateFlow.collect(FlowEvent(
                onLoading = {
                    progressBar.visibleOrGone(true)
                },
                onErrors = {
                    handleApiError(it) {
                        viewModel.loadOrder()
                    }
                },
                onSuccess = {
                    progressBar.visibleOrGone(false)
                    if (it.isEmpty())
                        noItem.apply {
                            noItem.groupNo.visibleOrGone(true)
                            icon = ContextCompat.getDrawable(requireContext(), R.drawable.no_list)
                            titleNo = getString(R.string.orders_no_msg)
                        }
                    else
                        orderAdapter.setData(it)
                }
            ))
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        findNavController().navigateUp()
    }

}