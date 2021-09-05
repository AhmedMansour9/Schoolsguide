package com.eaapps.schoolsguide.features.details.subfeature

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentDialogJobBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.features.details.subfeature.adapters.JobsAdapter
import com.eaapps.schoolsguide.utils.createDialog
import com.eaapps.schoolsguide.utils.launchFragment
import com.eaapps.schoolsguide.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobDialogFragment : DialogFragment(R.layout.fragment_dialog_job) {

    private val binding: FragmentDialogJobBinding by viewBinding(FragmentDialogJobBinding::bind)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(R.style.AppTheme, Color.WHITE, true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bindArgs()
        binding.clicks()
    }

    private fun FragmentDialogJobBinding.bindArgs() {
        JobDialogFragmentArgs.fromBundle(requireArguments()).apply {
            dataSchool.let { it ->
                rcJobs.adapter = JobsAdapter(it.jobs, onShareJob = {
                    ShareCompat.IntentBuilder(requireContext())
                        .setType("text/plain")
                        .setChooserTitle(getString(R.string.share_job))
                        .setText("${resources.getString(R.string.title_job)} ${it.name} \n" +
                                "${resources.getString(R.string.title_job)} ${it.description}" +
                                " \n ${resources.getString(R.string.experience_job)} ${it.years_of_experience} years" +
                                "\n ${resources.getString(R.string.apply_job_)} https://saudischoolsguide.com/jobDetails/${it.id}"
                        )
                        .startChooser()

                }, onApplyJob = { jobData ->
                    launchFragment(
                        JobDialogFragmentDirections.actionJobDialogFragmentToJobViewDialogFragment(
                            jobData,
                            it.id
                        )
                    )
                })

                if (it.jobs.isEmpty())
                    noItem.run {
                        noItem.groupNo.visibleOrGone(true)
                        icon = ContextCompat.getDrawable(requireContext(), R.drawable.no_jobs)
                        titleNo = getString(R.string.job_no_msg)
                    }
            }
        }
    }

    private fun FragmentDialogJobBinding.clicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
    }
}
