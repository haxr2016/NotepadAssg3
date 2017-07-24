package com.example.hemanth.notepadassg3.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;


public class Note implements Parcelable, Comparator<Note> {

    public static final String TITLE = "title";
    public static final String ID = "id";
    public static final String CONTENT = "content";
    public static final String LU_TIME = "lu_time";

    private String id;
    private String title;
    private String content;
    private String lu_time;

    public Note(String s, String s1, String s2, String path) {
        this.title = s;
        this.content = s1;
        this.lu_time = "";
        this.id="";
    }

    protected Note(Parcel in) {
        id = in.readString();
        title = in.readString();
        content = in.readString();
        lu_time = in.readString();
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

    public Note() {
        this.title = "";
        this.content = "";
        this.id="";

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLu_time() {
        return this.lu_time;
    }

    public void setLu_time(String lu_time) {
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
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(lu_time);
    }

    public static Note getModelFromJson(JSONObject jsonObject) {
        try {
            Note note = new Note();
            note.setId(jsonObject.getString(ID));
            note.setTitle(jsonObject.getString(TITLE));
            note.setContent(jsonObject.getString(CONTENT));
            note.setLu_time(jsonObject.getString(LU_TIME));
            return note;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getJsonFromModel(Note note) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(TITLE, note.getTitle());
            jsonObject.put(CONTENT, note.getContent());
            jsonObject.put(ID, note.getId());
            jsonObject.put(LU_TIME, note.getLu_time());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int compare(Note note, Note t1) {
        return Long.compare(Long.parseLong(note.getLu_time()), Long.parseLong(t1.getLu_time()));
    }

}
