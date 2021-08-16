package com.eaapps.schoolsguide.data.network.dataSources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eaapps.schoolsguide.data.entity.FilterRequestEntity
import com.eaapps.schoolsguide.data.entity.SchoolResponse
import com.eaapps.schoolsguide.data.network.ApiServices
import retrofit2.HttpException
import java.io.IOException

private const val TYPED_STATING_INDEX = 1

class FilterSchoolPagingSource(
    private val apiServices: ApiServices,
    private val filterRequestEntity: FilterRequestEntity
) :
    PagingSource<Int, SchoolResponse.SchoolData.DataSchool>() {

    companion object {
        const val LIMITED_LOAD = 30
    }

    override fun getRefreshKey(state: PagingState<Int, SchoolResponse.SchoolData.DataSchool>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(-1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SchoolResponse.SchoolData.DataSchool> {
        return try {
            val nextPageNumber = params.key ?: TYPED_STATING_INDEX
            val response = filterRequestEntity.let {
                apiServices.filterSchool(
                    search = it.search,
                    city_id = it.city_id,
                    program_id = it.program_id,
                    school_type = it.school_type,
                    from_price = it.from_price,
                    to_price = it.to_price,
                    type_id = it.type_id,
                    page = nextPageNumber,
                    perPage = LIMITED_LOAD
                )

            }
            val dataSchool = response.body()?.data?.data
            val meta = response.body()?.data?.meta
            val nextKey = if (meta?.last_page == nextPageNumber) {
                null
            } else {
                nextPageNumber + 1
            }
            LoadResult.Page(
                data = dataSchool!!,
                prevKey = if (nextPageNumber == TYPED_STATING_INDEX) null else nextPageNumber - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

}