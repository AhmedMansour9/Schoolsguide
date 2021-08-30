package com.eaapps.schoolsguide.features.profile.updateProfile

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
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
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.databinding.FragmentDialogEditProfileBinding
import com.eaapps.schoolsguide.delegate.viewBinding
import com.eaapps.schoolsguide.domain.model.UpdateProfileModel
import com.eaapps.schoolsguide.features.MainViewModel
import com.eaapps.schoolsguide.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import www.sanju.motiontoast.MotionToast

@AndroidEntryPoint
@InternalCoroutinesApi
class UpdateProfileFragment : DialogFragment(R.layout.fragment_dialog_edit_profile) {

    private val binding: FragmentDialogEditProfileBinding by viewBinding(
        FragmentDialogEditProfileBinding::bind
    )

    private val viewModel: UpdateProfileViewModel by viewModels()

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var dialogProcess: Dialog

    private lateinit var permission: ActivityResultLauncher<String>
    private lateinit var resultIntentPicActivity: ActivityResultLauncher<Intent>
    private lateinit var resultIntentPermissionActivity: ActivityResultLauncher<Intent>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupContentActivityResult()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = createDialog(
        R.style.AppTheme,
        Color.WHITE,
        true,
        shouldInterceptBackPress = true
    ) { dismiss() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.updateProfileViewModel = viewModel
        dialogProcess =
            requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))
        setupPermission()
        binding.bindClicks()
        binding.bindProfileCollectCollectResult()
        binding.bindCitiesCollectResult()
        binding.bindGenderList()
        updateProfileCollectResult()
    }

    private fun FragmentDialogEditProfileBinding.bindGenderList() {
        val gender = resources.getStringArray(R.array.gender)
        val adapter = ArrayAdapter(requireContext(), R.layout.city_list_item, gender)

        genderEditProfile.setAdapter(adapter)

        genderEditProfile.setOnItemClickListener { _, _, position, _ ->
            viewModel.updateProfileModel.gender = gender[position]
        }

    }

    private fun FragmentDialogEditProfileBinding.bindClicks() {
        edit.setOnClickListener {
            takePermissions()
//            if (requireContext().hasPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
//               // storageHelper.openFilePicker()
//            }
//            // chooseImageFromGalleries()
//            else
//                permisson.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        backBtn.setOnClickListener {
            dismiss()
        }

        cityEditProfile.setOnItemClickListener { _, _, position, _ ->
            viewModel.updateProfileModel.city_id = position + 1
        }
    }

    @SuppressLint("SetTextI18n", "Recycle")
    private fun setupContentActivityResult() {
        resultIntentPicActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val uri = it.data?.data
                    uri?.apply {
                        val file = FileHelper.writeTempFileFromUri(requireContext(), this)
                        file.apply {
                            binding.profileImg.setImageURI(Uri.parse(path))
                            viewModel.updateProfileModel.image =
                                FileHelper.multiPartFile(file, "image")
                        }
                    }

                }
            }

        resultIntentPermissionActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager())
                            pickImageFromGallery()
                    }
                }
            }
    }

    private fun setupPermission() {
        permission = requestPermissionWithRationale(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Snackbar.make(binding.root, "Please allow read from storage", Snackbar.LENGTH_SHORT)
        ) {
            pickImageFromGallery()
        }
    }

    private fun updateProfileCollectResult() {
        lifecycleScope.launchWhenCreated {
            viewModel.updateProfileStateFlow.collect(FlowEvent(onError = {
                dialogProcess.dismiss()
                MotionToast.createColorToast(
                    requireActivity(),
                    "Failed ☹",
                    it,
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(requireContext(), R.font.rpt_bold)

                )
            },
                onLoading = {
                    requireContext().hiddenKeyboard(binding.done)
                    dialogProcess.show()
                },
                onSuccess = {
                    mainViewModel.loadProfile() // Load Profile Again
                    dialogProcess.dismiss()
                    dismiss()
                },
                onNothing = { dialogProcess.dismiss() }
            ))
        }

    }

    private fun FragmentDialogEditProfileBinding.bindCitiesCollectResult() {
        lifecycleScope.launchWhenCreated {
            viewModel.citiesStateFlow.collect(
                FlowEvent(onError = {},
                    onSuccess = { it ->
                        val listString = ArrayList<String>()
                        it.forEach {
                            listString.add(it.name)
                        }
                        val adapter =
                            ArrayAdapter(requireContext(), R.layout.city_list_item, listString)
                        cityEditProfile.setAdapter(adapter)
                    })
            )
        }
    }

    private fun FragmentDialogEditProfileBinding.bindProfileCollectCollectResult() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.profileStateFlow.stateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {
                viewModel.updateProfileModel =
                    UpdateProfileModel(
                        it.full_name,
                        it.email,
                        it.phone ?: "",
                        it.city?.id ?: -1,
                        it.gender ?: "",
                        null
                    )
                cityEditProfile.setText(it.city?.name, false)
                profileImg.loadImage(it.image ?: "")
                genderEditProfile.setText(it.gender, false)
            }))
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        findNavController().navigateUp()
    }

    override fun onDestroy() {
        dialogProcess.dismiss()
        super.onDestroy()
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

    private fun pickImageFromGallery() {
        resultIntentPicActivity.launch(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        )

    }
}
