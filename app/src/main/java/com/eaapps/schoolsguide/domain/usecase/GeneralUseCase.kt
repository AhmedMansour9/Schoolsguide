package com.eaapps.schoolsguide.domain.usecase

import android.content.res.Resources
import android.util.Patterns
import androidx.paging.PagingData
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.domain.model.*
import com.eaapps.schoolsguide.domain.repository.GeneralRepository
import com.eaapps.schoolsguide.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ProgramSchoolUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(): Resource<List<ProgramsResponse.Programs>> =
        generalRepository.getSchoolPrograms()
}

class GradesSchoolUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(): Resource<List<GradesResponse.Grades>> =
        generalRepository.getSchoolGrades()
}

class CitiesUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(): Resource<List<CityResponse.City>> =
        generalRepository.getCities()
}

class SchoolDetailsUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(schoolId: Int): Resource<SchoolResponse.SchoolData.DataSchool> =
        generalRepository.getSchoolDetails(schoolId)
}

class AddInquiryUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(addInquiryModel: InquiryModel): Resource<ResponseEntity> =
        generalRepository.addInquiry(
            InquiryRequestEntity(
                addInquiryModel.message_type,
                addInquiryModel.reply_type,
                addInquiryModel.reply_time,
                addInquiryModel.message,
                addInquiryModel.school_id!!
            )
        )


    fun isValid(addInquiryModel: InquiryModel): Boolean =
        addInquiryModel.message_type.isNotBlank()
                && addInquiryModel.reply_type.isNotBlank()
                && addInquiryModel.reply_time.isNotBlank()
                && addInquiryModel.message.isNotBlank()

    fun validMessage(resources: Resources, addInquiryModel: InquiryModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            addInquiryModel.message_type.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("typeMessage", resources.getString(R.string.please_select_type_message))
                }
            }

            addInquiryModel.reply_type.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("replay_message", resources.getString(R.string.please_select_replay_method))
                }
            }


            addInquiryModel.reply_time.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("replay_time", resources.getString(R.string.please_select_best))
                }
            }

            else -> return errorMap

        }

    }
}

class JoinDiscountSchoolUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(joinDiscountModel: JoinDiscountModel): Resource<ResponseEntity> =
        generalRepository.joinDiscount(
            DiscountRequestEntity(
                joinDiscountModel.full_name,
                joinDiscountModel.email,
                joinDiscountModel.phone,
                joinDiscountModel.number_of_students,
                joinDiscountModel.school_id!!
            )
        )


    fun isValid(joinDiscountModel: JoinDiscountModel): Boolean =
        joinDiscountModel.full_name.isNotBlank()
                && joinDiscountModel.email.isNotBlank()
                && joinDiscountModel.phone.isNotBlank()
                && joinDiscountModel.number_of_students!! > -1

    fun validMessage(
        resources: Resources,
        joinDiscountModel: JoinDiscountModel
    ): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            joinDiscountModel.full_name.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("full_name", resources.getString(R.string.please_enter_full_name))
                }
            }

            joinDiscountModel.email.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("email", resources.getString(R.string.please_enter_email))
                }
            }


            joinDiscountModel.phone.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("phone", resources.getString(R.string.please_enter_phone))
                }
            }

            else -> return errorMap

        }

    }
}

class BookSchoolUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(bookSchoolModel: BookSchoolModel): Resource<ResponseEntity> =
        generalRepository.bookNow(
            HashMap<String, Any>().apply {
                put("full_name", bookSchoolModel.full_name)
                put("phone", bookSchoolModel.phone)
                put("email", bookSchoolModel.email)
                put("number_of_students", bookSchoolModel.number_of_students)
                put("school_id", bookSchoolModel.school_id!!)
                put("grades", bookSchoolModel.grades!!)
            }
        )


    fun isValid(bookSchoolModel: BookSchoolModel): Boolean =
        bookSchoolModel.full_name.isNotBlank()
                && bookSchoolModel.email.isNotBlank()
                && bookSchoolModel.phone.isNotBlank()
                && bookSchoolModel.address.isNotBlank()
                && bookSchoolModel.number_of_students.isNotBlank()
                && bookSchoolModel.grades?.size!! > 0

    fun validMessage(
        resources: Resources,
        bookSchoolModel: BookSchoolModel
    ): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            bookSchoolModel.full_name.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("full_name", resources.getString(R.string.please_enter_full_name))
                }
            }

            bookSchoolModel.email.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("email", resources.getString(R.string.please_enter_email))
                }
            }

            bookSchoolModel.number_of_students.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("student_number", resources.getString(R.string.please_select_student_n))
                }
            }


            bookSchoolModel.phone.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("phone", resources.getString(R.string.please_enter_phone))
                }
            }

            bookSchoolModel.phone.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("address", resources.getString(R.string.please_enter_address))
                }
            }


            else -> return errorMap

        }

    }
}

class UploadCvUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(uploadCvModel: UploadCvModel): Resource<ResponseEntity> =
        generalRepository.uploadCv(
            uploadCvModel.job_id!!,
            uploadCvModel.school_id!!,
            uploadCvModel.attachment!!
        )
}

class FilterMapUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(filterModel: FilterModel): Resource<List<SchoolResponse.SchoolData.DataSchool>> =
        generalRepository.filterSchools(
            FilterRequestEntity(
                filterModel.search,
                filterModel.school_type,
                filterModel.type_id,
                filterModel.grade_id,
                filterModel.from_price,
                filterModel.to_price,
                filterModel.program_id,
                filterModel.city_id,
                filterModel.review
            )
        )
}

class AddSchoolUseCase @Inject constructor(private val generalRepository: GeneralRepository) {

    suspend fun execute(addSchoolModel: AddSchoolModel): Resource<ResponseEntity> =
        generalRepository.addSchool(
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

    fun validMessage(
        resources: Resources,
        addSchoolModel: AddSchoolModel
    ): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            addSchoolModel.email.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("email", resources.getString(R.string.please_enter_email))
                }
            }
            !Patterns.EMAIL_ADDRESS.matcher(addSchoolModel.email).matches() -> {
                return errorMap.apply {
                    put("email", resources.getString(R.string.please_enter_v_email))
                }
            }

            addSchoolModel.school_name.isBlank() -> {
                return errorMap.apply {
                    put("name", resources.getString(R.string.please_enter_school_name))
                }
            }
            addSchoolModel.phone.isBlank() -> {
                return errorMap.apply {
                    put("phone", resources.getString(R.string.please_enter_phone))
                }
            }

            else -> return errorMap

        }

    }

}

class LoadAllRecommendedUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        generalRepository.loadAllRecommended()
}

class LoadAllFeaturedUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        generalRepository.loadAllFeatured()
}

class LoadTypedSchoolUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(typeId: Int): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        generalRepository.loadAllTypedSchool(typeId)
}

class LoadFilterSchoolsUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(filterModel: FilterModel): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        generalRepository.loadAllSchoolsByFilter(
            FilterRequestEntity(
                filterModel.search,
                filterModel.school_type,
                filterModel.type_id,
                filterModel.grade_id,
                filterModel.from_price,
                filterModel.to_price,
                filterModel.program_id,
                filterModel.city_id,
                filterModel.review
            )
        )
}

class LoadSchoolTypeUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(): Resource<List<TypeResponse.TypeData>> = generalRepository.getTypeSchool()
}

class LoadSliderUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(): Resource<List<SliderResponse.SliderData>> = generalRepository.getSlider()
}

class LoadRecommendedUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(): Resource<List<SchoolResponse.SchoolData.DataSchool>> =
        generalRepository.getRecommended()

}

class LoadFeatureUseCase @Inject constructor(private val generalRepository: GeneralRepository) {
    suspend fun execute(): Resource<List<SchoolResponse.SchoolData.DataSchool>> =
        generalRepository.getFeature()
}