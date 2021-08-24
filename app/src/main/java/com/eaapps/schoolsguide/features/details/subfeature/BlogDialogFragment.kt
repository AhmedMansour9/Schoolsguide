package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentDialogBlogsBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.features.details.subfeature.adapters.BlogAdapter
import com.eaapps.schoolsguide.utils.createDialog
import com.eaapps.schoolsguide.utils.launchFragment
import com.eaapps.schoolsguide.utils.visibleOrGone
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
        BlogDialogFragmentArgs.fromBundle(requireArguments()).apply {
            dataSchool.let { it ->
                blogLabel.text =
                    if (type == "news") getString(R.string.news) else getString(R.string.events)
                rcBlogs.adapter = BlogAdapter(if (type == "news") it.blogs else it.events) {
                    launchFragment(
                        BlogDialogFragmentDirections.actionBlogDialogFragmentToBlogViewDialogFragment(
                            it,
                            type
                        )
                    )
                }

                if (rcBlogs.adapter?.itemCount == 0)
                    noItem.run {
                        noItem.groupNo.visibleOrGone(true)
                        if (type == "news") {
                            icon = ContextCompat.getDrawable(requireContext(), R.drawable.no_news)
                            titleNo = getString(R.string.events_no_msg)
                        } else {
                            icon = ContextCompat.getDrawable(requireContext(), R.drawable.no_event)
                            titleNo = getString(R.string.events_no_msg)
                        }
                    }
            }
        }
    }

    private fun FragmentDialogBlogsBinding.clicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }
}
