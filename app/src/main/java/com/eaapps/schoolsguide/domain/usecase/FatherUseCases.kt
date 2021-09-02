package com.eaapps.schoolsguide.domain.usecase

import android.content.res.Resources
import androidx.paging.PagingData
import com.eaapps.schoolsguide.R
import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.domain.model.ReviewModel
import com.eaapps.schoolsguide.domain.repository.FatherRepository
import com.eaapps.schoolsguide.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(private val fatherRepository: FatherRepository) {

    suspend fun execute(schoolId: Int): Resource<ResponseEntity> =
        fatherRepository.toggleFavorite(schoolId)
}

class LoadFavoriteUseCase @Inject constructor(private val fatherRepository: FatherRepository) {
    suspend fun execute(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        fatherRepository.loadFavorite()
}

class LoadFollowUseCase @Inject constructor(private val fatherRepository: FatherRepository) {
    suspend fun execute(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        fatherRepository.loadFollow()
}

class ToggleFollowUseCase @Inject constructor(private val fatherRepository: FatherRepository) {
    suspend fun execute(schoolId: Int): Resource<ResponseEntity> =
        fatherRepository.toggleFollow(schoolId)
}

class ToggleRecommendedUseCase @Inject constructor(private val fatherRepository: FatherRepository) {
    suspend fun execute(schoolId: Int): Resource<ResponseEntity> =
        fatherRepository.toggleRecommendedIt(schoolId)
}

class PutReviewUseCase @Inject constructor(private val fatherRepository: FatherRepository) {
    suspend fun execute(reviewModel: ReviewModel): Resource<ResponseEntity> =
        fatherRepository.putReview(
            ReviewRequestEntity(
                reviewModel.school_id,
                reviewModel.comment,
                reviewModel.education,
                reviewModel.communication,
                reviewModel.facilities,
                reviewModel.safety,
                reviewModel.activities
            )
        )

    fun isValid(reviewModel: ReviewModel): Boolean =
        reviewModel.education > 0 &&
                reviewModel.communication > 0 &&
                reviewModel.facilities > 0 &&
                reviewModel.safety > 0 &&
                reviewModel.activities > 0 &&
                reviewModel.comment.isNotBlank()


    fun validMessage(resources: Resources,reviewModel: ReviewModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            reviewModel.education == 0 -> {
                return HashMap<String, String>().apply {
                    put("rate_education", resources.getString(R.string.education_rate_atleast))
                }
            }

            reviewModel.communication == 0 -> {
                return HashMap<String, String>().apply {
                    put("rate_communication", resources.getString(R.string.communicate_rate_least))
                }
            }

            reviewModel.facilities == 0 -> {
                return HashMap<String, String>().apply {
                    put("rate_facilities", resources.getString(R.string.facilities_rate_least))
                }
            }

            reviewModel.safety == 0 -> {
                return HashMap<String, String>().apply {
                    put("rate_safety", resources.getString(R.string.safety_rate_least))
                }
            }

            reviewModel.activities == 0 -> {
                return HashMap<String, String>().apply {
                    put("rate_activities", resources.getString(R.string.activities_rate_least))
                }
            }

            reviewModel.comment.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("comment", resources.getString(R.string.please_enter_comment))
                }
            }

            else -> return errorMap

        }

    }

}

class GetProfileFatherUseCase @Inject constructor(private val fatherRepository: FatherRepository) {
    suspend fun execute(): Resource<AuthResponse.AuthData> =
        fatherRepository.getProfileFather()
}

class MyOrderSchoolUseCase @Inject constructor(private val fatherRepository: FatherRepository) {
    suspend fun execute(): Resource<List<SchoolBookingRequestsResponse.RequestData>> =
        fatherRepository.myOrderSchools()
}