package com.eaapps.schoolsguide.domain.usecase

import android.util.Patterns
import com.eaapps.schoolsguide.data.entity.AddSchoolEntity
import com.eaapps.schoolsguide.data.entity.ChangeFatherProfileEntity
import com.eaapps.schoolsguide.data.entity.ChangePasswordEntity
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.domain.model.AddSchoolModel
import com.eaapps.schoolsguide.domain.model.UpdatePasswordModel
import com.eaapps.schoolsguide.domain.model.UpdateProfileModel
import com.eaapps.schoolsguide.domain.repository.ProfileRepository
import com.eaapps.schoolsguide.utils.Resource
import javax.inject.Inject

class AddSchoolUseCase @Inject constructor(private val profileRepository: ProfileRepository) {

    suspend fun execute(addSchoolModel: AddSchoolModel): Resource<ResponseEntity> =
        profileRepository.addSchool(
            AddSchoolEntity(
                addSchoolModel.school_name,
                addSchoolModel.phone,
                addSchoolModel.email,
                addSchoolModel.notes
            )
        )

    fun isValid(addSchoolModel: AddSchoolModel): Boolean =
        (addSchoolModel.school_name.isNotBlank() &&
                addSchoolModel.email.isNotBlank() &&
                Patterns.EMAIL_ADDRESS.matcher(addSchoolModel.email).matches()) &&
                (addSchoolModel.phone.isNotBlank())

    fun validMessage(addSchoolModel: AddSchoolModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            addSchoolModel.email.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("email", "Please Enter Email Address")
                }
            }
            !Patterns.EMAIL_ADDRESS.matcher(addSchoolModel.email).matches() -> {
                return errorMap.apply {
                    put("email", "Please Enter Validation Email")
                }
            }

            addSchoolModel.school_name.isBlank() -> {
                return errorMap.apply {
                    put("name", "Please Enter School Name")
                }
            }
            addSchoolModel.phone.isBlank() -> {
                return errorMap.apply {
                    put("phone", "Please Enter Phone Number")
                }
            }

            else -> return errorMap

        }

    }

}

class UpdatePasswordUseCase @Inject constructor(private val profileRepository: ProfileRepository) {

    suspend fun execute(updatePasswordModel: UpdatePasswordModel): Resource<ResponseEntity> =
        profileRepository.updatePassword(
            ChangePasswordEntity(
                updatePasswordModel.oldPassword,
                updatePasswordModel.password,
                updatePasswordModel.password
            )
        )

    fun isValid(updatePasswordModel: UpdatePasswordModel): Boolean =
        updatePasswordModel.oldPassword.isNotBlank()
                && (updatePasswordModel.password.isNotBlank()) && updatePasswordModel.confirmPassword.isNotBlank()
                && updatePasswordModel.password.length > 8
                && updatePasswordModel.password == updatePasswordModel.confirmPassword

    fun validMessage(updatePasswordModel: UpdatePasswordModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {

            updatePasswordModel.oldPassword.isBlank() -> {
                return errorMap.apply {
                    put("oldPassword", "For your security,please enter old password")
                }
            }

            updatePasswordModel.password.isBlank() -> {
                return errorMap.apply {
                    put("newPassword", "Please Enter New Password")
                }
            }

            updatePasswordModel.password.length < 8 -> {
                return errorMap.apply {
                    put("newPassword", "Password must be at least 8 characters")
                }
            }

            updatePasswordModel.password != updatePasswordModel.confirmPassword -> {
                return errorMap.apply {
                    put("confirmPassword", "confirm password must match the password")
                }
            }

            else -> return errorMap

        }

    }

}

class UpdateFatherProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {

    suspend fun execute(
        updateProfileModel: UpdateProfileModel
    ): Resource<ResponseEntity> =
        profileRepository.updateProfileFather(
            ChangeFatherProfileEntity(
                updateProfileModel.full_name,
                updateProfileModel.email,
                updateProfileModel.phone,
                updateProfileModel.city_id,
                updateProfileModel.gender,
                updateProfileModel.image
            )
        )

    fun isValid(updateProfileModel: UpdateProfileModel): Boolean =
        updateProfileModel.full_name.isNotBlank()
                && updateProfileModel.email.isNotBlank()
                && Patterns.EMAIL_ADDRESS.matcher(updateProfileModel.email).matches()
                && updateProfileModel.phone.isNotBlank()
                && updateProfileModel.city_id > -1

    fun validMessage(updateProfileModel: UpdateProfileModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            updateProfileModel.full_name.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("name", "Please Enter Full Name")
                }
            }

            updateProfileModel.email.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("email", "Please Enter Email Address")
                }
            }

            !Patterns.EMAIL_ADDRESS.matcher(updateProfileModel.email).matches() -> {
                return errorMap.apply {
                    put("email", "Please Enter Validation Email")
                }
            }

            updateProfileModel.phone.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("phone", "Please Enter Phone Number")
                }
            }

            updateProfileModel.city_id < 0 -> {
                return errorMap.apply {
                    put("city", "Please select an item in the list")
                }
            }

            else -> return errorMap

        }

    }


}