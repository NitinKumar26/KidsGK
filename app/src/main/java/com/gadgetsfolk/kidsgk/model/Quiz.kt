package com.gadgetsfolk.kidsgk.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.annotation.Keep

@Keep
class Quiz : Parcelable {
    var question: String? = null
    var option_one: String? = null
    var option_two: String? = null
    var option_three: String? = null
    var option_four: String? = null
    var answer: String? = null
    var img_url: String? = null

    constructor() {}
    private constructor(parcel: Parcel) {
        question = parcel.readString()
        option_one = parcel.readString()
        option_two = parcel.readString()
        option_three = parcel.readString()
        option_four = parcel.readString()
        answer = parcel.readString()
        img_url = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(option_one)
        parcel.writeString(option_two)
        parcel.writeString(option_three)
        parcel.writeString(option_four)
        parcel.writeString(question)
        parcel.writeString(img_url)
    }

    companion object CREATOR : Creator<Quiz> {
        override fun createFromParcel(parcel: Parcel): Quiz {
            return Quiz(parcel)
        }

        override fun newArray(size: Int): Array<Quiz?> {
            return arrayOfNulls(size)
        }
    }
}