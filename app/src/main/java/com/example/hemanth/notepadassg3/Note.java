package com.example.hemanth.notepadassg3;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sandeepcmsm on 19/07/17.
 */

public class Note implements Parcelable {
    private String title;
    private String content;
    private long lu_time;

    public Note(String s, String s1, String s2) {
        this.title = s;
        this.content = s1;
        this.lu_time = 1234;
    }

    protected Note(Parcel in) {
        title = in.readString();
        content = in.readString();
        lu_time = in.readLong();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public long getLu_time() {
        return lu_time;
    }

    public void setLu_time(long lu_time) {
        this.lu_time = lu_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeLong(lu_time);
    }
}
