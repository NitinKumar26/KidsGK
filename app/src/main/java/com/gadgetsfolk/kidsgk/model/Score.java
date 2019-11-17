package com.gadgetsfolk.kidsgk.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Score implements Parcelable {
    private String correct_questions, total_questions, percentage, remark;

    public Score(){}

    private Score(Parcel parcel){
        correct_questions = parcel.readString();
        total_questions = parcel.readString();
        percentage = parcel.readString();
        remark = parcel.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(correct_questions);
        parcel.writeString(total_questions);
        parcel.writeString(percentage);
        parcel.writeString(remark);
    }


    public static final Creator<Score> CREATOR = new Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel parcel) {
            return new Score(parcel);
        }

        @Override
        public Score[] newArray(int i) {
            return new Score[i];
        }
    };


    public String getCorrect_questions() {
        return correct_questions;
    }

    public void setCorrect_questions(String correct_questions) {
        this.correct_questions = correct_questions;
    }

    public String getTotal_questions() {
        return total_questions;
    }

    public void setTotal_questions(String total_questions) {
        this.total_questions = total_questions;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
