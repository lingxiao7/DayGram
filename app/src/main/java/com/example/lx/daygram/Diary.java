package com.example.lx.daygram;

import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by lx on 2016/9/22.
 * The basic diary class. As you known, a diary has date, text and id.
 * You can set and get them, and you can save on a DIY Structure. The
 * casting between diary and json are also important.
 */
public class Diary {
    private static final String JSON_ID = "id";
    private static final String JSON_TEXT = "text";
    private static final String JSON_DATE = "date";

    private UUID mId;
    private String mText;
    private Date mDate;

    public Diary() {
        // produce unique identifier
        mId = UUID.randomUUID();

        mDate = new Date();
    }

    public Diary(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));

        if (json.has(JSON_TEXT))
            mText = json.getString(JSON_TEXT);
        mDate = new Date(json.getString(JSON_DATE));
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId);
        json.put(JSON_TEXT, mText);
        json.put(JSON_DATE, mDate);
        return json;
    }

    public UUID getId() {
        return mId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getMonth() {
        return DateFormat.format("M", mDate).toString();
    }

    public String getYear() {
        return DateFormat.format("YYYY", mDate).toString();
    }

    public String getTitle() {
        String title = null;
        if (mText == null) return null;
        if (mText.length() > 35) {
            title = mText.subSequence(0, 29).toString();
            title += "...";
        } else {
            title = mText;
        }
        return title;
    }
    public String getText() {
        return mText;
    }

    public void setText(String text) {
        if (text == null) return ;
        mText = text;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "mTitle='" + mText + '\'' +
                '}';
    }
}
