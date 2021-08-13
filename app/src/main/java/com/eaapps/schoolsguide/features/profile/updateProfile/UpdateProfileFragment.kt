package com.eaapps.schoolsguide.features.profile.updateProfile

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toFile
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

    private lateinit var permisson: ActivityResultLauncher<String>

    private lateinit var resultIntentActivity: ActivityResultLauncher<String>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        createDialog(R.style.AppTheme, Color.WHITE, true)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.updateProfileViewModel = viewModel

        dialogProcess =
            requireContext().progressSmallDialog(requireContext().getColorResource(R.color.colorApp1Dark))

        setupPermissionStorage()

        setupContentActivityResult()

        setupGenderList()

        setupClicks()

        profileCollectCollectResult()

        citiesCollectResult()

        updateProfileCollectResult()

    }

    private fun setupGenderList() {
        val gender = resources.getStringArray(R.array.gender)
        val adapter = ArrayAdapter(requireContext(), R.layout.city_list_item, gender)
        binding.genderEditProfile.setAdapter(adapter)
    }

    private fun setupClicks() {
        binding.edit.setOnClickListener {
            if (requireContext().hasPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE))
                chooseImageFromGalleries()
            else
                permisson.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        binding.backBtn.setOnClickListener {
            dismiss()
        }

        binding.cityEditProfile.setOnItemClickListener { _, _, position, _ ->
            viewModel.updateProfileModel.city_id = position + 1
        }
    }

    private fun chooseImageFromGalleries() {
        resultIntentActivity.launch("image/*")
    }

    private fun setupPermissionStorage() {
        permisson = requestPermissionWithRationale(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Snackbar.make(
                binding.root,
                "Please allow read from storage",
                Snackbar.LENGTH_SHORT
            )
        ) { chooseImageFromGalleries() }
    }

    @SuppressLint("SetTextI18n")
    private fun setupContentActivityResult() {
        resultIntentActivity = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                viewModel.updateProfileModel.image = it.toFile()
                binding.profileImg.loadImage(it)
            }
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

    private fun citiesCollectResult() {
        lifecycleScope.launchWhenCreated {
            viewModel.citiesStateFlow.collect(
                FlowEvent(onError = {},
                    onSuccess = {
                        val adapter = ArrayAdapter(requireContext(), R.layout.city_list_item, it)
                        binding.cityEditProfile.setAdapter(adapter)
                    })
            )
        }
    }

    private fun profileCollectCollectResult() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.profileStateFlow.collect(FlowEvent(onError = {
            }, onSuccess = {
                viewModel.updateProfileModel =
                    UpdateProfileModel(it.full_name, it.email, it.phone, it.city.id)
                binding.cityEditProfile.setText(it.city.name)
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
}