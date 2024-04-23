package com.example.sharedbasket.utils

import android.os.Parcel
import android.os.Parcelable
import java.util.UUID

data class Notification(
    val notificationId : String = UUID.randomUUID().toString(),
    val senderUID: String,
    val senderName: String,
    val marketName: String,
    val timeStamp: Long = System.currentTimeMillis(),
    val status : String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(notificationId)
        parcel.writeString(senderUID)
        parcel.writeString(senderName)
        parcel.writeString(marketName)
        parcel.writeLong(timeStamp)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Notification> {
        override fun createFromParcel(parcel: Parcel): Notification {
            return Notification(parcel)
        }

        override fun newArray(size: Int): Array<Notification?> {
            return arrayOfNulls(size)
        }
    }
}

