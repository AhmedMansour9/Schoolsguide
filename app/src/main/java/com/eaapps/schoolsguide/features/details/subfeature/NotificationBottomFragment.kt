package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.NotificationsBottomSheetBinding
import com.eaapps.schoolsguide.features.details.subfeature.adapters.NotificationsAdapter
import com.eaapps.schoolsguide.utils.dialogShow
import com.eaapps.schoolsguide.utils.visibleOrGone
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class NotificationBottomFragment : BottomSheetDialogFragment() {
    private lateinit var binding: NotificationsBottomSheetBinding

    override fun getTheme(): Int = R.style.CustomBottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NotificationsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheet.dialogShow(resources, 0.55f, draggable = true)
        return bottomSheet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buildArgs()
        binding.bindClicks()

    }

    private fun NotificationsBottomSheetBinding.buildArgs() {
        NotificationBottomFragmentArgs.fromBundle(requireArguments()).dataSchool.apply {
            rcNotifications.adapter = NotificationsAdapter(this.notifications)
            noItem.run {
                noItem.groupNo.visibleOrGone(notifications.isEmpty())
                icon = ContextCompat.getDrawable(requireContext(), R.drawable.no_notifications)
                titleNo = getString(R.string.notification_no_msg)
            }
        }
    }

    private fun NotificationsBottomSheetBinding.bindClicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

}