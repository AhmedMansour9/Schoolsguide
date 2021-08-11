package com.eaapps.schoolsguide.data.entity

import com.google.gson.annotations.SerializedName

data class ResponseEntity(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String
)

data class CityResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<City>,
    @SerializedName("message") val message: String
) {
    data class City(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String
    )
}

data class AuthResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val dataResponse: AuthData? = null
) {
    data class AuthData(
        @SerializedName("full_name") val full_name: String,
        @SerializedName("email") val email: String,
        @SerializedName("phone") val phone: String,
        @SerializedName("city") val city: CityResponse.City,
        @SerializedName("district") val district: String,
        @SerializedName("created_at") val created_at: String,
        @SerializedName("access_token") val access_token: String,
        @SerializedName("token_type") val token_type: String,
        @SerializedName("expires_at") val expires_at: String
    )
}

data class SliderResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: List<SliderData>,
    @SerializedName("message")
    val message: String
) {
    data class SliderData(
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String
    )
}

data class SchoolResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: SchoolData,
    @SerializedName("message") val message: String
) {
    data class SchoolData(
        @SerializedName("data") val data: List<DataSchool>,
        @SerializedName("meta") val meta: Meta
    ) {
        data class DataSchool(
            val about: Any,
            val achievements: List<Any>,
            val address: Any,
            val approvals: List<Approval>,
            val average_number_of_students_per_class: String,
            val awards: List<Any>,
            val blogs: List<Any>,
            val city: CityResponse.City,
            val communicates: List<Any>,
            val data_completion: String,
            val dates_for_communicating_with_supervisors: Any,
            val discount: String,
            val email: String,
            val events: List<Any>,
            val facebook_link: Any,
            val featured: Boolean,
            val fess_greater_similar: Boolean,
            val gallary: List<Gallary>,
            val grades: List<Grade>,
            val id: Int,
            val image: String,
            val instgram_link: String,
            val isFavoired: Boolean,
            val isFollowed: Boolean,
            val isRecommended: Boolean,
            val is_new: Boolean,
            val jobs: List<Any>,
            val lat: Double,
            val lng: Double,
            val mobil1: Any,
            val mobil2: Any,
            val name: String,
            val notifications: List<Any>,
            val number_of_basketball_courts: Any,
            val number_of_employees: Any,
            val number_of_football_fields: Any,
            val number_of_labs: String,
            val number_of_libraries: String,
            val number_of_volleyball_courts: Any,
            val number_views: Int,
            val phone1: String,
            val phone2: Any,
            val program: Program,
            val recomends: Int,
            val schoolReviews: List<Any>,
            val school_expenses: String,
            val school_type: String,
            val services: List<Service>,
            val short_description: Any,
            val time_of_work: Any,
            val total_number_review: Int,
            val total_review: Int,
            val tuition_fees: List<Any>,
            val twitter_link: String,
            val type: Type,
            val website_link: Any,
            val youtube_link: Any
        ) {
            data class Gallary(
                val image: String
            )

            data class Program(
                val id: Int,
                val name: String
            )

            data class Service(
                val id: Int,
                val image: String,
                val name: String
            )

            data class Type(
                val id: Int,
                val image: String,
                val name: String
            )

            data class Approval(
                val id: Int,
                val image: String,
                val name: String
            )

            data class Grade(
                val id: Int,
                val name: String
            )
        }

        data class Meta(
            val current_page: Int,
            val hasMorePages: Boolean,
            val last_page: Int,
            val per_page: Int,
            val total: Int
        )

    }
}

data class TypeResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<TypeData>,
    @SerializedName("message") val message: String
) {

    data class TypeData(val id: Int, val name: String, val image: String)
}