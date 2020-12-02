package com.gadgetsfolk.kidsgk.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.annotation.Keep

@Keep
class QuizType : Parcelable {
    var title: String? = null
    var sub_title: String? = null
    var img_url: String? = null
    var doc_id: String? = null

    constructor() {}
    private constructor(parcel: Parcel) {
        title = parcel.readString()
        sub_title = parcel.readString()
        img_url = parcel.readString()
        doc_id = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(title)
        parcel.writeString(sub_title)
        parcel.writeString(img_url)
        parcel.writeString(doc_id)
    }

    companion object CREATOR : Creator<QuizType> {
        override fun createFromParcel(parcel: Parcel): QuizType {
            return QuizType(parcel)
        }

        override fun newArray(size: Int): Array<QuizType?> {
            return arrayOfNulls(size)
        }
    }
}