package com.codingwithrufat.abbapplication.utils

import android.os.Parcel
import android.os.Parcelable

data class MorphyParcelableItem(
    val name: String? = null,
    val status: String? = null,
    val gender: String? = null,
    val species: String? = null,
    val location: String? = null,
    val character: String? = null,
    val image: String? = null
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(status)
        dest?.writeString(gender)
        dest?.writeString(species)
        dest?.writeString(location)
        dest?.writeString(character)
        dest?.writeString(image)
    }

    companion object CREATOR : Parcelable.Creator<MorphyParcelableItem> {
        override fun createFromParcel(parcel: Parcel): MorphyParcelableItem {
            return MorphyParcelableItem(parcel)
        }

        override fun newArray(size: Int): Array<MorphyParcelableItem?> {
            return arrayOfNulls(size)
        }
    }
}