package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentDialogAchievementBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.features.details.subfeature.adapters.AchievementAdapter
import com.eaapps.schoolsguide.utils.createDialog
import com.eaapps.schoolsguide.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AchievementDialogFragment : DialogFragment(R.layout.fragment_dialog_achievement) {

    private val binding: FragmentDialogAchievementBinding by viewBinding(
        FragmentDialogAchievementBinding::bind
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(R.style.AppTheme, Color.WHITE, true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindArgs()
        binding.clicks()
    }

    private fun FragmentDialogAchievementBinding.bindArgs() {
        AchievementDialogFragmentArgs.fromBundle(requireArguments()).dataSchool.apply {
            rcAchievement.adapter = AchievementAdapter(this.achievements)
            if (rcAchievement.adapter?.itemCount == 0)
                noItem.run {
                    noItem.groupNo.visibleOrGone(true)
                    icon = ContextCompat.getDrawable(requireContext(), R.drawable.no_list)
                    titleNo = getString(R.string.achievement_no_msg)
                }

        }
    }

    private fun FragmentDialogAchievementBinding.clicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }
}
