package com.eaapps.schoolsguide.data.network.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.eaapps.schoolsguide.data.entity.*
import com.eaapps.schoolsguide.data.network.apiServices.GeneralApis
import com.eaapps.schoolsguide.data.network.dataSources.FeaturedPagingSource
import com.eaapps.schoolsguide.data.network.dataSources.FilterSchoolPagingSource
import com.eaapps.schoolsguide.data.network.dataSources.RecommendedPagingDataSource
import com.eaapps.schoolsguide.data.network.dataSources.TypedSchoolPagingSource
import com.eaapps.schoolsguide.domain.repository.GeneralRepository
import com.eaapps.schoolsguide.utils.Resource
import com.eaapps.schoolsguide.utils.safeCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class GeneralRepositoryImpl @Inject constructor(private val generalApis: GeneralApis) :
    GeneralRepository {

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

    override suspend fun getSchoolPrograms(): Resource<List<ProgramsResponse.Programs>> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = generalApis.getSchoolProgramsAsync()
                Resource.Success(result.data)
            }, "")
        }

    override suspend fun getSchoolGrades(): Resource<List<GradesResponse.Grades>> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = generalApis.getSchoolGradesAsync()
                Resource.Success(result.data)
            }, "")
        }

    override suspend fun getCities(): Resource<List<CityResponse.City>> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = generalApis.getCitiesAsync()
                Resource.Success(result.data)
            }, "Exception occurred!")
        }

    override suspend fun getSchoolDetails(schoolId: Int): Resource<SchoolResponse.SchoolData.DataSchool> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = generalApis.schoolDetailsAsync(schoolId)
                Resource.Success(result.data)
            }, "Exception occurred!")
        }

    override suspend fun addInquiry(addInquiryRequestEntity: InquiryRequestEntity) =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = generalApis.addInquiryAsync(addInquiryRequestEntity)
                Resource.Success(result)
            }, "Exception occurred!")
        }

    override suspend fun joinDiscount(discountRequestEntity: DiscountRequestEntity): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = generalApis.joinDiscountAsync(discountRequestEntity)
                Resource.Success(result)
            }, "Exception occurred!")
        }

    override suspend fun bookNow(body: HashMap<String, Any>): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = generalApis.bookSchoolAsync(body)
                Resource.Success(result)
            }, "Exception occurred!")
        }

    override suspend fun uploadCv(jobId: Int, schoolId: Int, file: File): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = generalApis.uploadCvAsync(
                    jobId.toString()
                        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                    schoolId.toString()
                        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                    MultipartBody.Part.createFormData(
                        "attachment",
                        filename = file.name,
                        file.asRequestBody("multipart/form-data; charset=utf-8".toMediaTypeOrNull())
                    )
                )
                Resource.Success(result)
            }, "Exception occurred!")
        }

    override suspend fun filterSchools(filterRequestEntity: FilterRequestEntity): Resource<List<SchoolResponse.SchoolData.DataSchool>> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = generalApis.filterMapSchoolAsync(
                    search = filterRequestEntity.search,
                    city_id = filterRequestEntity.city_id,
                    program_id = filterRequestEntity.program_id,
                    school_type = filterRequestEntity.school_type,
                    from_price = filterRequestEntity.from_price,
                    to_price = filterRequestEntity.to_price,
                    type_id = filterRequestEntity.type_id
                )
                Resource.Success(result.data.data)
            }, "")
        }

    override suspend fun getTypeSchool(): Resource<List<TypeResponse.TypeData>> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = generalApis.getSchoolTypeAsync()
                Resource.Success(result.data)
            }, "")
        }

    override suspend fun getSlider(): Resource<List<SliderResponse.SliderData>> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = generalApis.getSliderAsync()
                Resource.Success(result.data)
            }, "")
        }

    override suspend fun getRecommended(): Resource<List<SchoolResponse.SchoolData.DataSchool>> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = generalApis.getRecommendedSchoolAsync()
                Resource.Success(result.data.data)
            }, "")
        }

    override suspend fun getFeature(): Resource<List<SchoolResponse.SchoolData.DataSchool>> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = generalApis.getFeaturedSchoolAsync()
                Resource.Success(result.data.data)
            }, "")
        }

    override suspend fun addSchool(addSchoolEntity: AddSchoolEntity): Resource<ResponseEntity> =
        withContext(Dispatchers.IO) {
            safeCall(call = {
                val result = generalApis.addSchoolAsync(addSchoolEntity)
                Resource.Success(result)
            }, "Exception occurred!")
        }

    override suspend fun loadAllRecommended(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ), pagingSourceFactory = { RecommendedPagingDataSource(generalApis) }).flow


    override suspend fun loadAllFeatured(): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ), pagingSourceFactory = { FeaturedPagingSource(generalApis) }).flow


    override suspend fun loadAllTypedSchool(typeId: Int): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ), pagingSourceFactory = { TypedSchoolPagingSource(generalApis, typeId) }).flow

    override suspend fun loadAllSchoolsByFilter(filterRequestEntity: FilterRequestEntity): Flow<PagingData<SchoolResponse.SchoolData.DataSchool>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                FilterSchoolPagingSource(
                    generalApis,
                    filterRequestEntity
                )
            }).flow

}