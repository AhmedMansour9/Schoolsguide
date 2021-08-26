package com.eaapps.schoolsguide.features.details.subfeature

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentDialogJobViewBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.features.details.DetailsViewModel
import com.eaapps.schoolsguide.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import www.sanju.motiontoast.MotionToast

@InternalCoroutinesApi
@AndroidEntryPoint
class JobViewDialogFragment : DialogFragment(R.layout.fragment_dialog_job_view) {

    private val binding: FragmentDialogJobViewBinding by viewBinding(FragmentDialogJobViewBinding::bind)

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }

    private lateinit var dialogProcess: Dialog

    private lateinit var permission: ActivityResultLauncher<String>
    private lateinit var resultIntentPicActivity: ActivityResultLauncher<Intent>
    private lateinit var resultIntentPermissionActivity: ActivityResultLauncher<Intent>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupContentActivityResult()
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
        binding.executePendingBindings()
        binding.detailsViewModel = viewModel
        dialogProcess =
            requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))
        binding.bindArgs()
        binding.clicks()

        setupPermission()
        collectResultUploadCvResult()
    }

    private fun FragmentDialogJobViewBinding.bindArgs() {
        JobViewDialogFragmentArgs.fromBundle(requireArguments()).apply {
            jobData = dataJob
            viewModel.uploadCvModel.job_id = dataJob.id
            viewModel.uploadCvModel.school_id = schoolId
        }
    }

    private fun FragmentDialogJobViewBinding.clicks() {
        cancelBtn.setOnClickListener {
            dismiss()
        }
        chooseFile.setOnClickListener {
            takePermissions()
        }
        clearFile.setOnClickListener {
            binding.file.visibleOrInvisible(false)
            viewModel.uploadCvModel.attachment = null
        }
    }

    private fun collectResultUploadCvResult() {
        lifecycleScope.launchWhenStarted {
            viewModel.uploadCvFlow.stateFlow.collect(FlowEvent(onError = {
                dialogProcess.dismiss()
                MotionToast.createColorToast(
                    requireActivity(),
                    "Failed ☹",
                    it,
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    8000L,
                    ResourcesCompat.getFont(requireContext(), R.font.rpt_bold)

                )
            },
                onLoading = {
                    dialogProcess.show()
                },
                onSuccess = {
                    MotionToast.createColorToast(
                        requireActivity(),
                        getString(R.string.apply_j),
                        getString(R.string.job_applying),
                        MotionToast.TOAST_SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        8000L,
                        ResourcesCompat.getFont(requireContext(), R.font.rpt_bold)
                    )
                    viewModel.bookSchoolFlow.setValue(Resource.Nothing())
                    dialogProcess.dismiss()
                    dismiss()
                },
                onNothing = { dialogProcess.dismiss() }
            ))
        }
    }

    @SuppressLint("SetTextI18n", "Recycle")
    private fun setupContentActivityResult() {
        resultIntentPicActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val uri = it.data?.data
                    uri?.apply {
                        val file = FileHelper.writeTempFileFromUri(requireContext(), this)
                        viewModel.uploadCvModel.attachment = file
                        binding.file.visibleOrInvisible(true)
                        binding.nameFile.text = uri.lastPathSegment ?: "file"
                    }

                }
            }

        resultIntentPermissionActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager())
                            pickFileFromDevice()
                    }
                }
            }
    }

    private fun takePermissions() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION).apply {
                    addCategory("android,intent.category,DEFAULT")
                    data = Uri.parse(String.format("package%s", requireContext().packageName))
                }
                resultIntentPermissionActivity.launch(intent)
            } catch (e: Exception) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION).apply {
                    addCategory("android,intent.category,DEFAULT")
                    data = Uri.parse(String.format("package%s", requireContext().packageName))
                }
                resultIntentPermissionActivity.launch(intent)
            }
        } else {
            permission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun setupPermission() {
        permission = requestPermissionWithRationale(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Snackbar.make(binding.root, "Please allow read from storage", Snackbar.LENGTH_SHORT)
        ) {
            pickFileFromDevice()
        }
    }

    private fun pickFileFromDevice() {
        resultIntentPicActivity.launch(
            Intent(
                Intent.ACTION_GET_CONTENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        )

    }

    override fun onDismiss(dialog: DialogInterface) {
        findNavController().navigateUp()
    }

}
