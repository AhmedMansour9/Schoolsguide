package com.eaapps.schoolsguide.domain.usecase

import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.domain.model.*
import com.eaapps.schoolsguide.domain.repository.CityRepository
import com.eaapps.schoolsguide.domain.repository.GeneralRepository
import com.eaapps.schoolsguide.utils.Resource
import javax.inject.Inject


class GetCitiesUseCase @Inject constructor(private val cityRepository: CityRepository) {
    suspend fun execute(): Resource<List<String>> = cityRepository.getCities()
}

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

    fun validMessage(addInquiryModel: InquiryModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            addInquiryModel.message_type.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("typeMessage", "Please Select Type Message")
                }
            }

            addInquiryModel.reply_type.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("replay_message", "Please Select Replay Method")
                }
            }


            addInquiryModel.reply_time.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("replay_time", "Please Select Best Time")
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

    fun validMessage(joinDiscountModel: JoinDiscountModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            joinDiscountModel.full_name.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("full_name", "Please Enter Full Name")
                }
            }

            joinDiscountModel.email.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("email", "Please Enter Email")
                }
            }


            joinDiscountModel.phone.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("phone", "Please Enter Phone")
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

    fun validMessage(bookSchoolModel: BookSchoolModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            bookSchoolModel.full_name.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("full_name", "Please Enter Full Name")
                }
            }

            bookSchoolModel.email.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("email", "Please Enter Email")
                }
            }

            bookSchoolModel.number_of_students.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("student_number", "Please Select Student Number")
                }
            }


            bookSchoolModel.phone.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("phone", "Please Enter Phone")
                }
            }

            bookSchoolModel.phone.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("address", "Please Enter Address")
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

