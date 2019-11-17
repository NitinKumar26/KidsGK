package com.gadgetsfolk.kidsgk.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

@Keep
public class QuizType implements Parcelable {
    private String title, sub_title;
    private String img_url;
    private String doc_id;

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public QuizType(){}

    private QuizType(Parcel parcel) {
        title = parcel.readString();
        sub_title = parcel.readString();
        img_url = parcel.readString();
        doc_id = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(sub_title);
        parcel.writeString(img_url);
        parcel.writeString(doc_id);
    }

    public static final Creator<QuizType> CREATOR = new Creator<QuizType>() {
        @Override
        public QuizType createFromParcel(Parcel parcel) {
            return new QuizType(parcel);
        }

        @Override
        public QuizType[] newArray(int i) {
            return new QuizType[i];
        }
    };

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

}
