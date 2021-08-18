package com.eaapps.schoolsguide.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
            parcel.writeString(name)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<City> {
            override fun createFromParcel(parcel: Parcel): City {
                return City(parcel)
            }

            override fun newArray(size: Int): Array<City?> {
                return arrayOfNulls(size)
            }
        }
    }
}


data class ProgramsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<Programs>,
    @SerializedName("message") val message: String
) {
    data class Programs(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String
    )
}


data class GradesResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<Grades>,
    @SerializedName("message") val message: String
) {
    data class Grades(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String?
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
            parcel.writeString(name)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Grades> {
            override fun createFromParcel(parcel: Parcel): Grades {
                return Grades(parcel)
            }

            override fun newArray(size: Int): Array<Grades?> {
                return arrayOfNulls(size)
            }
        }
    }
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
        @SerializedName("expires_at") val expires_at: String,
        @SerializedName("image") val image: String,
        @SerializedName("gender") val gender: String
    )
}

data class AuthResetResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val dataResponse: String? = null
)



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

data class SchoolDetailsResponse(@SerializedName("success") val success: Boolean,
                                 @SerializedName("data") val data: SchoolResponse.SchoolData.DataSchool,
                                 @SerializedName("message") val message: String)

data class SchoolResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: SchoolData,
    @SerializedName("message") val message: String
) {
    data class SchoolData(
        @SerializedName("data") val data: List<DataSchool>,
        @SerializedName("meta") val meta: Meta
    ) {
        @Parcelize
        data class DataSchool(
            val about: String,
            val achievements: List<ShareData>,
            val address: String,
            val approvals: List<Approval>,
            val average_number_of_students_per_class: String,
            val awards: List<ShareData>,
            val blogs: List<ShareData>,
            val city: CityResponse.City,
            val communicates: List<CommunicateData>,
            val data_completion: String,
            val dates_for_communicating_with_supervisors: String,
            val discount: String,
            val email: String,
            val events: List<ShareData>,
            val facebook_link: String,
            val featured: Boolean,
            val fess_greater_similar: Boolean,
            val gallary: List<Gallary>,
            val grades: List<Grade>,
            val id: Int,
            val image: String,
            val instgram_link: String,
            var isFavoired: Boolean,
            var isFollowed: Boolean,
            var isRecommended: Boolean,
            val is_new: Boolean,
            val jobs: List<JobData>,
            val lat: Double,
            val lng: Double,
            val mobil1: String,
            val mobil2: String,
            val name: String,
            val notifications: List<ShareData>,
            val number_of_basketball_courts: String,
            val number_of_employees: String,
            val number_of_football_fields: String,
            val number_of_labs: String,
            val number_of_libraries: String,
            val number_of_volleyball_courts: String,
            val number_views: Int,
            val phone1: String,
            val phone2: String,
            val program: Program,
            val recomends: Int,
            val schoolReviews: List<SchoolReview>,
            val school_expenses: String,
            val school_type: String,
            val services: List<Service>,
            val short_description: String,
            val time_of_work: String,
            val total_number_review: Int,
            val total_review: Int,
            val tuition_fees: List<TuitionFee>,
            val twitter_link: String,
            val type: Type,
            val website_link: String,
            val youtube_link: String
        ) : Parcelable {
            data class Gallary(
                val image: String
            ) : Parcelable {
                constructor(parcel: Parcel) : this(parcel.readString()!!) {
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeString(image)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<Gallary> {
                    override fun createFromParcel(parcel: Parcel): Gallary {
                        return Gallary(parcel)
                    }

                    override fun newArray(size: Int): Array<Gallary?> {
                        return arrayOfNulls(size)
                    }
                }
            }

            data class TuitionFee(
                val created_at: String?,
                val grade: GradesResponse.Grades?,
                val id: Int,
                val total: Int
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                    parcel.readString(),
                    parcel.readParcelable(GradesResponse.Grades::class.java.classLoader),
                    parcel.readInt(),
                    parcel.readInt()
                ) {
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeString(created_at)
                    parcel.writeParcelable(grade, flags)
                    parcel.writeInt(id)
                    parcel.writeInt(total)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<TuitionFee> {
                    override fun createFromParcel(parcel: Parcel): TuitionFee {
                        return TuitionFee(parcel)
                    }

                    override fun newArray(size: Int): Array<TuitionFee?> {
                        return arrayOfNulls(size)
                    }
                }
            }

            data class Program(
                val id: Int,
                val name: String
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                    parcel.readInt(),
                    parcel.readString()!!
                ) {
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeInt(id)
                    parcel.writeString(name)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<Program> {
                    override fun createFromParcel(parcel: Parcel): Program {
                        return Program(parcel)
                    }

                    override fun newArray(size: Int): Array<Program?> {
                        return arrayOfNulls(size)
                    }
                }
            }

            data class Service(
                val id: Int,
                val image: String,
                val name: String
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                    parcel.readInt(),
                    parcel.readString()!!,
                    parcel.readString()!!
                ) {
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeInt(id)
                    parcel.writeString(image)
                    parcel.writeString(name)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<Service> {
                    override fun createFromParcel(parcel: Parcel): Service {
                        return Service(parcel)
                    }

                    override fun newArray(size: Int): Array<Service?> {
                        return arrayOfNulls(size)
                    }
                }
            }

            data class Type(
                val id: Int,
                val image: String,
                val name: String
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                    parcel.readInt(),
                    parcel.readString()!!,
                    parcel.readString()!!
                ) {
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeInt(id)
                    parcel.writeString(image)
                    parcel.writeString(name)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<Type> {
                    override fun createFromParcel(parcel: Parcel): Type {
                        return Type(parcel)
                    }

                    override fun newArray(size: Int): Array<Type?> {
                        return arrayOfNulls(size)
                    }
                }
            }

            data class Approval(
                val id: Int,
                val image: String,
                val name: String
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                    parcel.readInt(),
                    parcel.readString()!!,
                    parcel.readString()!!
                ) {
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeInt(id)
                    parcel.writeString(image)
                    parcel.writeString(name)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<Approval> {
                    override fun createFromParcel(parcel: Parcel): Approval {
                        return Approval(parcel)
                    }

                    override fun newArray(size: Int): Array<Approval?> {
                        return arrayOfNulls(size)
                    }
                }
            }

            data class Grade(
                val id: Int,
                val name: String
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                    parcel.readInt(),
                    parcel.readString()!!
                ) {
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeInt(id)
                    parcel.writeString(name)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<Grade> {
                    override fun createFromParcel(parcel: Parcel): Grade {
                        return Grade(parcel)
                    }

                    override fun newArray(size: Int): Array<Grade?> {
                        return arrayOfNulls(size)
                    }
                }
            }

            data class CommunicateData(
                val id: Int,
                val suprevisor_name: String,
                val suprevisor_email: String,
                val suprevisor_phone: String,
                val grades: String,
                val shunt: String
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                    parcel.readInt(),
                    parcel.readString()!!,
                    parcel.readString()!!,
                    parcel.readString()!!,
                    parcel.readString()!!,
                    parcel.readString()!!
                ) {
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeInt(id)
                    parcel.writeString(suprevisor_name)
                    parcel.writeString(suprevisor_email)
                    parcel.writeString(suprevisor_phone)
                    parcel.writeString(grades)
                    parcel.writeString(shunt)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<CommunicateData> {
                    override fun createFromParcel(parcel: Parcel): CommunicateData {
                        return CommunicateData(parcel)
                    }

                    override fun newArray(size: Int): Array<CommunicateData?> {
                        return arrayOfNulls(size)
                    }
                }
            }

            data class JobData(
                val id: Int,
                val vacant_places: Int,
                val years_of_experience: Int,
                val name: String,
                val description: String,
                val nationality: String
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                    parcel.readInt(),
                    parcel.readInt(),
                    parcel.readInt(),
                    parcel.readString()!!,
                    parcel.readString()!!,
                    parcel.readString()!!
                ) {
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeInt(id)
                    parcel.writeInt(vacant_places)
                    parcel.writeInt(years_of_experience)
                    parcel.writeString(name)
                    parcel.writeString(description)
                    parcel.writeString(nationality)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<JobData> {
                    override fun createFromParcel(parcel: Parcel): JobData {
                        return JobData(parcel)
                    }

                    override fun newArray(size: Int): Array<JobData?> {
                        return arrayOfNulls(size)
                    }
                }
            }

            data class SchoolReview(
                val comment: String,
                val created_at: String,
                val father: Father?,
                val review: Int
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                    parcel.readString()!!,
                    parcel.readString()!!,
                    parcel.readParcelable(Father::class.java.classLoader),
                    parcel.readInt()
                ) {
                }

                data class Father(
                    val city: CityResponse.City?,
                    val created_at: String,
                    val district: String,
                    val email: String,
                    val full_name: String,
                    val gender: String,
                    val image: String,
                    val phone: String
                ) : Parcelable {
                    constructor(parcel: Parcel) : this(
                        parcel.readParcelable(CityResponse.City::class.java.classLoader),
                        parcel.readString()!!,
                        parcel.readString()!!,
                        parcel.readString()!!,
                        parcel.readString()!!,
                        parcel.readString()!!,
                        parcel.readString()!!,
                        parcel.readString()!!
                    ) {
                    }

                    override fun writeToParcel(parcel: Parcel, flags: Int) {
                        parcel.writeParcelable(city, flags)
                        parcel.writeString(created_at)
                        parcel.writeString(district)
                        parcel.writeString(email)
                        parcel.writeString(full_name)
                        parcel.writeString(gender)
                        parcel.writeString(image)
                        parcel.writeString(phone)
                    }

                    override fun describeContents(): Int {
                        return 0
                    }

                    companion object CREATOR : Parcelable.Creator<Father> {
                        override fun createFromParcel(parcel: Parcel): Father {
                            return Father(parcel)
                        }

                        override fun newArray(size: Int): Array<Father?> {
                            return arrayOfNulls(size)
                        }
                    }
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeString(comment)
                    parcel.writeString(created_at)
                    parcel.writeParcelable(father, flags)
                    parcel.writeInt(review)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<SchoolReview> {
                    override fun createFromParcel(parcel: Parcel): SchoolReview {
                        return SchoolReview(parcel)
                    }

                    override fun newArray(size: Int): Array<SchoolReview?> {
                        return arrayOfNulls(size)
                    }
                }
            }

            data class ShareData(
                val created_at: String?,
                val description: String?,
                val image: String?,
                val short_description: String?,
                val title: String?
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                    parcel.readString(),
                    parcel.readString(),
                    parcel.readString(),
                    parcel.readString(),
                    parcel.readString()
                ) {
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeString(created_at)
                    parcel.writeString(description)
                    parcel.writeString(image)
                    parcel.writeString(short_description)
                    parcel.writeString(title)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<ShareData> {
                    override fun createFromParcel(parcel: Parcel): ShareData {
                        return ShareData(parcel)
                    }

                    override fun newArray(size: Int): Array<ShareData?> {
                        return arrayOfNulls(size)
                    }
                }
            }
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

    public data class TypeData(val id: Int, val name: String, val image: String)
}