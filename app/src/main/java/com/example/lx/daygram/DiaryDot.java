package com.example.lx.daygram;

import android.content.Context;
import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by lx on 2016/9/25.
 * Simple class to stand for a dot with date. Provide setter
 * and getter.
 */
public class DiaryDot {
    private static final String JSON_DATE = "date";
    //private int mImgRrcId;
    //private Context mContext;
    private Date mDate;

    public DiaryDot(JSONObject json) throws JSONException {
        mDate = new Date(json.getString(JSON_DATE));
    }

    public  DiaryDot() {
        mDate = new Date();
    }
    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

}
