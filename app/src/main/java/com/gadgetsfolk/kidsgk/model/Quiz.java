package com.gadgetsfolk.kidsgk.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

@Keep
public class Quiz implements Parcelable {
    private String question, option_one, option_two, option_three, option_four, answer, img_url;

    public Quiz(){}

    private Quiz(Parcel parcel) {
        question = parcel.readString();
        option_one = parcel.readString();
        option_two = parcel.readString();
        option_three = parcel.readString();
        option_four = parcel.readString();
        answer = parcel.readString();
        img_url = parcel.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(option_one);
        parcel.writeString(option_two);
        parcel.writeString(option_three);
        parcel.writeString(option_four);
        parcel.writeString(question);
        parcel.writeString(img_url);
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel parcel) {
            return new Quiz(parcel);
        }

        @Override
        public Quiz[] newArray(int i) {
            return new Quiz[i];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption_one() {
        return option_one;
    }

    public void setOption_one(String option_one) {
        this.option_one = option_one;
    }

    public String getOption_two() {
        return option_two;
    }

    public void setOption_two(String option_two) {
        this.option_two = option_two;
    }

    public String getOption_three() {
        return option_three;
    }

    public void setOption_three(String option_three) {
        this.option_three = option_three;
    }

    public String getOption_four() {
        return option_four;
    }

    public void setOption_four(String option_four) {
        this.option_four = option_four;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
