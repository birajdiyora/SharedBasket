package com.example.sharedbasket.utils

import android.os.Parcel
import android.os.Parcelable

data class ReceivedRequestState(
    val notificationId : String = "",
    val marketerId : String = "",
    val itemsRequesterId : String = "",
    val itemsRequesterName : String = "",
    val status : String = "",
    val marketName : String = "",
    val items : List<Item> = listOf(),
    val timeStamp : Long = 1
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Item)!!,
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(notificationId)
        parcel.writeString(marketerId)
        parcel.writeString(itemsRequesterId)
        parcel.writeString(itemsRequesterName)
        parcel.writeString(status)
        parcel.writeString(marketName)
        parcel.writeTypedList(items)
        parcel.writeLong(timeStamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReceivedRequestState> {
        override fun createFromParcel(parcel: Parcel): ReceivedRequestState {
            return ReceivedRequestState(parcel)
        }

        override fun newArray(size: Int): Array<ReceivedRequestState?> {
            return arrayOfNulls(size)
        }
    }
}
//
//    constructor(parcel: Parcel) : this(
//        parcel.readString()!!,
//        parcel.readString()!!,
//        parcel.readString()!!,
//        parcel.readString()!!,
//        parcel.readString()!!,
//        parcel.readString()!!,
//        parcel.readList(List<Item>),
//        parcel.readLong()!!
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(notificationId)
//        parcel.writeString(marketerId)
//        parcel.writeString(marketerName)
//        parcel.writeString(marketName)
//        parcel.writeLong(timeStamp)
//        parcel.writeString(status)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<Notification> {
//        override fun createFromParcel(parcel: Parcel): Notification {
//            return Notification(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Notification?> {
//            return arrayOfNulls(size)
//        }
//    }
//}

