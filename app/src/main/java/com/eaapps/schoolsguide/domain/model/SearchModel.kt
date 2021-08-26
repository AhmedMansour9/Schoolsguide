package com.eaapps.schoolsguide.domain.model

import android.os.Parcel
import android.os.Parcelable

data class SearchType(var type: Int, var recommended: Boolean, var featured: Boolean) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type)
        parcel.writeByte(if (recommended) 1 else 0)
        parcel.writeByte(if (featured) 1 else 0)
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
    var filterPosition: FilterPositionModel = FilterPositionModel()
) {
    fun isFilter(): Boolean =
        !school_type.isNullOrBlank() || grade_id != null || from_price != null ||
                to_price != null || program_id != null || city_id != null || review != null

    fun clear() {
        type_id = null
        school_type = null
        grade_id = null
        from_price = null
        to_price = null
        program_id = null
        city_id = null
        review = null
        filterPosition = FilterPositionModel()
    }

    data class FilterPositionModel(
        var school_type: Int? = null,
        var grade_id: Int? = null,
        var from_price: Int? = null,
        var to_price: Int? = null,
        var program_id: Int? = null,
        var city_id: Int? = null,
        var review: Int? = null
    )
}