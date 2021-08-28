package com.eaapps.schoolsguide.domain.model

import android.os.Parcel
import android.os.Parcelable

data class SearchType(var type: Int, var recommended: Boolean, var featured: Boolean,var search:Boolean) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type)
        parcel.writeByte(if (recommended) 1 else 0)
        parcel.writeByte(if (featured) 1 else 0)
        parcel.writeByte(if (search) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchType> {
        override fun createFromParcel(parcel: Parcel): SearchType {
            return SearchType(parcel)
        }

        override fun newArray(size: Int): Array<SearchType?> {
            return arrayOfNulls(size)
        }
    }

}

data class FilterModel(
    var search: String? = null,
    var type_id: Int? = null,
    var school_type: String? = null,
    var grade_id: Int? = null,
    var from_price: Int? = null,
    var to_price: Int? = null,
    var program_id: Int? = null,
    var city_id: Int? = null,
    var review: Int? = null,
    var filterPosition: FilterPositionModel = FilterPositionModel(),
) {
    fun isFilter(): Boolean =
        !school_type.isNullOrBlank() || grade_id != null || from_price != null ||
                to_price != null || program_id != null || city_id != null || review != null || type_id != null

    fun clearAll() {
        clearCategory()
        clearCity()
        clearEducationLevel()
        clearProgram()
        clearType()
        clearReview()
        clearFees()
    }

    fun clearReview() {
        review = null
        filterPosition.review = null
    }

    fun clearFees() {
        from_price = null
        to_price = null
        filterPosition.from_price = null
        filterPosition.to_price = null
    }

    fun clearCity() {
        city_id = null
        filterPosition.city_id = null
    }

    fun clearCategory() {
        type_id = null
        filterPosition.type_id = null
    }

    fun clearType() {
        school_type = null
        filterPosition.school_type = null
    }

    fun clearEducationLevel() {
        grade_id = null
        filterPosition.grade_id = null
    }

    fun clearProgram() {
        program_id = null
        filterPosition.program_id = null
    }

    data class FilterPositionModel(
        var type_id: Int? = null,
        var school_type: Int? = null,
        var grade_id: Int? = null,
        var from_price: Int? = null,
        var to_price: Int? = null,
        var program_id: Int? = null,
        var city_id: Int? = null,
        var review: Int? = null
    )
}