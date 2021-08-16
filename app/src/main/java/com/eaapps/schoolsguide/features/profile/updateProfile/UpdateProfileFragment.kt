package com.eaapps.schoolsguide.features.profile.updateProfile

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import www.sanju.motiontoast.MotionToast
import java.io.File

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

    private lateinit var resultIntentActivity: ActivityResultLauncher<Intent>

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

        binding.genderEditProfile.setOnItemClickListener { _, _, position, _ ->
            viewModel.updateProfileModel.gender = gender[position]
        }

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

        resultIntentActivity.launch(Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).apply {

        })
//        resultIntentActivity.launch("image/*")
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
        resultIntentActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val uri = it.data?.data
                    val es = uri?.let {
                        var result = ""
                        var isok = false
                        var cursor: Cursor? = null
                        try {
                            val proj = arrayOf(MediaStore.Images.Media.DATA)
                            cursor = requireContext().getContentResolver().query(
                                it,
                                proj,
                                null,
                                null,
                                null
                            )!!
                            val column_index =
                                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                            cursor.moveToFirst()
                            result = cursor.getString(column_index)
                            isok = true
                        } finally {
                            cursor?.close()
                        }
                        result
                    }

                    val sa = uri?.let {
                        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        val cursor: Cursor?
                        val columnIndexID: Int
                        val listOfAllImages: MutableList<Uri> = mutableListOf()
                        val projection = arrayOf(MediaStore.Images.Media._ID)
                        var imageId: Long
                        cursor = requireContext().contentResolver.query(
                            uriExternal,
                            projection,
                            null,
                            null,
                            null
                        )
                        cursor?.apply {
                            columnIndexID =
                                cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                            while (cursor.moveToNext()) {
                                imageId = cursor.getLong(columnIndexID)
                                val uriImage = Uri.withAppendedPath(uriExternal, "" + imageId)
                                listOfAllImages.add(uriImage)
                            }
                        }
                        cursor?.close()
                        listOfAllImages

                    }


                    binding.profileImg.loadImage(es!!)
                    viewModel.updateProfileModel.image = getPartBody(es)
                }
            }
//        resultIntentActivity = registerForActivityResult(ActivityResultContracts.GetContent()) {
//            if (it != null) {
//
//                 //viewModel.updateProfileModel.image =  it.
//                binding.profileImg.loadImage(it)
//            }
//        }
    }

    private fun getPartBody(path: String): MultipartBody.Part = File(path).let {
        val requestFile = it.asRequestBody("multipart/form-data; charset=utf-8".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("image", filename = it.name, requestFile)
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
                    UpdateProfileModel(
                        it.full_name,
                        it.email,
                        it.phone,
                        it.city.id,
                        it.gender,
                        getPartBody(it.image)
                    )
                binding.cityEditProfile.setText(it.city.name)
                binding.profileImg.loadImage(it.image)
                val gender = resources.getStringArray(R.array.gender)
                binding.genderEditProfile.setSelection(0)
//                binding.genderEditProfile.setSelection(gender.indexOfFirst { per -> per == it.gender })

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