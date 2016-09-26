package com.example.lx.daygram;

import android.content.Context;
import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by lx on 2016/9/25.
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

    /**
    public int getImgRrcId() {
        return mImgRrcId;
    }

    public void setImgRrcId(int imgRrcId) {
        mImgRrcId = imgRrcId;
    }

     public Context getContext() {
     return mContext;
     }

     public void setContext(Context context) {
     mContext = context;
     }*/

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

}
