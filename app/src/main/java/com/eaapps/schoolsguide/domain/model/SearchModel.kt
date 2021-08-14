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