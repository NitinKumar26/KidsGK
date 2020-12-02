package com.gadgetsfolk.kidsgk.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

class Score : Parcelable {
    var correct_questions: String? = null
    var total_questions: String? = null
    var percentage: String? = null
    var remark: String? = null

    constructor() {}
    private constructor(parcel: Parcel) {
        correct_questions = parcel.readString()
        total_questions = parcel.readString()
        percentage = parcel.readString()
        remark = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(correct_questions)
        parcel.writeString(total_questions)
        parcel.writeString(percentage)
        parcel.writeString(remark)
    }

    companion object CREATOR : Creator<Score> {
        override fun createFromParcel(parcel: Parcel): Score {
            return Score(parcel)
        }

        override fun newArray(size: Int): Array<Score?> {
            return arrayOfNulls(size)
        }
    }
}