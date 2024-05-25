package com.example.reviewmanagement.Item;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionItem implements Parcelable {
    public int id;
    public int courseId;
    public int questionTypeId;
    public String question;
    public String answer;

    public QuestionItem(int id, int courseId, int questionTypeId, String question, String answer) {
        this.id = id;
        this.courseId = courseId;
        this.questionTypeId = questionTypeId;
        this.question = question;
        this.answer = answer;
    }

    protected QuestionItem(Parcel in) {
        id = in.readInt();
        courseId = in.readInt();
        questionTypeId = in.readInt();
        question = in.readString();
        answer = in.readString();
    }

    public static final Creator<QuestionItem> CREATOR = new Creator<QuestionItem>() {
        @Override
        public QuestionItem createFromParcel(Parcel in) {
            return new QuestionItem(in);
        }

        @Override
        public QuestionItem[] newArray(int size) {
            return new QuestionItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(courseId);
        dest.writeInt(questionTypeId);
        dest.writeString(question);
        dest.writeString(answer);
    }
}
