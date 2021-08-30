package com.eaapps.schoolsguide.domain.usecase

import androidx.paging.PagingData
import com.eaapps.schoolsguide.data.entity.AuthResponse
import com.eaapps.schoolsguide.data.entity.ResponseEntity
import com.eaapps.schoolsguide.data.entity.ReviewRequestEntity
import com.eaapps.schoolsguide.data.entity.SchoolResponse
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


    fun validMessage(reviewModel: ReviewModel): HashMap<String, String> {
        val errorMap = HashMap<String, String>()
        errorMap.clear()
        when {
            reviewModel.education == 0 -> {
                return HashMap<String, String>().apply {
                    put("rate_education", "Education rate at least 1")
                }
            }

            reviewModel.communication == 0 -> {
                return HashMap<String, String>().apply {
                    put("rate_communication", "Communication rate at least 1")
                }
            }

            reviewModel.facilities == 0 -> {
                return HashMap<String, String>().apply {
                    put("rate_facilities", "Facilities rate at least 1")
                }
            }

            reviewModel.safety == 0 -> {
                return HashMap<String, String>().apply {
                    put("rate_safety", "Safety rate at least 1")
                }
            }

            reviewModel.activities == 0 -> {
                return HashMap<String, String>().apply {
                    put("rate_activities", "Activities rate at least 1")
                }
            }

            reviewModel.comment.isBlank() -> {
                return HashMap<String, String>().apply {
                    put("comment", "Please Enter Comment")
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