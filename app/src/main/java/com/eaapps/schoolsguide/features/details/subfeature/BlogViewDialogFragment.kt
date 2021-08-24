package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.fromHtml
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentBlogViewDialogBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.GlideImageGetter
import com.eaapps.schoolsguide.utils.createDialog
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlogViewDialogFragment : DialogFragment(R.layout.fragment_blog_view_dialog) {

    private val binding: FragmentBlogViewDialogBinding by viewBinding(FragmentBlogViewDialogBinding::bind)
    private var titleToolbar: String = ""
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(R.style.AppTheme, Color.TRANSPARENT, false,shouldInterceptBackPress = true){dismiss()}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindArgs()
        binding.bindActionBar()
        binding.bindToolbar()
    }

    private fun FragmentBlogViewDialogBinding.bindArgs() {

        BlogViewDialogFragmentArgs.fromBundle(requireArguments()).apply {
            blog = this.blogData.let {
                titleToolbar = if (type == "news") getString(R.string.news_details) else getString(R.string.events_details)
                shortDesc.text = fromHtml(it.description!!, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH, GlideImageGetter(textView = shortDesc), null)
                it
            }
        }
    }

    private fun FragmentBlogViewDialogBinding.bindToolbar() {
        toolbar.setNavigationOnClickListener {
            dismiss()
        }
    }

    private fun FragmentBlogViewDialogBinding.bindActionBar() {
        toolbar.title = ""
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsing.setContentScrimColor(ContextCompat.getColor(requireContext(), R.color.colorApp1))
        collapsing.setStatusBarScrimColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorApp1
            )
        )

        collapsing.isTitleEnabled = false
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (kotlin.math.abs(verticalOffset) > 200) {
                if (toolbar.title == "")
                    toolbar.title = titleToolbar
            } else {
                if (toolbar.title.isNotEmpty())
                    toolbar.title = ""
            }
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        findNavController().navigateUp()
    }
}