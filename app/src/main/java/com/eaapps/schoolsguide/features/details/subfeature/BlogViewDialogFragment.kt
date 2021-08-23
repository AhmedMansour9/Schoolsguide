package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.fromHtml
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentBlogViewDialogBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.utils.createDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlogViewDialogFragment : DialogFragment(R.layout.fragment_blog_view_dialog),
    Html.ImageGetter {

    private val binding: FragmentBlogViewDialogBinding by viewBinding(FragmentBlogViewDialogBinding::bind)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(R.style.AppTheme, Color.TRANSPARENT, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindArgs()
        binding.bindToolbar()
    }

    private fun FragmentBlogViewDialogBinding.bindArgs() {
        BlogViewDialogFragmentArgs.fromBundle(requireArguments()).blogData.apply {
            blogData = this
            shortDesc.text = fromHtml(
                description!!,
                HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH
            )
        }
    }

    private fun FragmentBlogViewDialogBinding.bindToolbar() {
        toolbar.setNavigationOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        findNavController().navigateUp()
    }

    override fun getDrawable(source: String?): Drawable {
        TODO("Not yet implemented")
    }

}