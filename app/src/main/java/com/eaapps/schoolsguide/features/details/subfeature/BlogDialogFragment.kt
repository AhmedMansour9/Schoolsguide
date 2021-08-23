package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentDialogBlogsBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.features.details.subfeature.adapters.BlogAdapter
import com.eaapps.schoolsguide.utils.createDialog
import com.eaapps.schoolsguide.utils.launchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlogDialogFragment : DialogFragment(R.layout.fragment_dialog_blogs) {

    private val binding: FragmentDialogBlogsBinding by viewBinding(FragmentDialogBlogsBinding::bind)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(R.style.AppTheme, Color.WHITE, true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindArgs()
        binding.clicks()
    }

    private fun FragmentDialogBlogsBinding.bindArgs() {
        BlogDialogFragmentArgs.fromBundle(requireArguments()).dataSchool.apply {
            rcNews.adapter = BlogAdapter(this.blogs) {
                launchFragment(
                    BlogDialogFragmentDirections.actionBlogDialogFragmentToBlogViewDialogFragment(
                        it
                    )
                )
            }
        }
    }

    private fun FragmentDialogBlogsBinding.clicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }
}
