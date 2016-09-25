package com.example.lx.daygram;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by lx on 2016/9/22.
 */
public class DiaryLab {
    private static final String TAG = "DiaryLab";
    private static final String FIELNAME = "diary.json";
    private static final String FLELTESTNAME = "diarytest5.json";

    private ArrayList<Diary> mDiaries;
    private DayGramJSONSerializer mDayGramJSONSerializer;

    private static DiaryLab sDiaryLab;
    private Context mAppContext;

    private DiaryLab(Context appContext) {
        mAppContext = appContext;
        mDayGramJSONSerializer = new DayGramJSONSerializer(mAppContext, FLELTESTNAME);
        mDiaries = new ArrayList<Diary>();

        try {
            mDiaries = mDayGramJSONSerializer.loadDiaries();
        } catch (Exception e) {
            mDiaries = new ArrayList<Diary>();
            Date todayDate = new Date();
            for (Date i = new Date(110, 0, 0); i.compareTo(todayDate) < 0; i = new Date(i.getTime() + 24 * 60 * 60 * 1000)){
                Diary d = new Diary();
                d.setDate(i);
                mDiaries.add(d);
            }
        } finally {
            if (0 == mDiaries.size()) {
                //mDiaries = new ArrayList<Diary>();
                Date todayDate = new Date();
                for (Date i = new Date(110, 1, 1); i.compareTo(todayDate) < 0; i = new Date(i.getTime() + 24 * 60 * 60 * 1000)){
                    Diary d = new Diary();
                    d.setDate(i);
                    mDiaries.add(d);
                }
            }
        }
    }

    public static DiaryLab get(Context c) {
        if (sDiaryLab == null) sDiaryLab = new DiaryLab(c.getApplicationContext());
        return sDiaryLab;
    }

    public ArrayList<Diary> getDiaries() {
        return mDiaries;
    }

    public void deleteCrime(Diary d) {
        mDiaries.remove(d);
    }

    public Diary getDiary(Date date) {
        for (Diary d : mDiaries)
            if (d.getDate().equals(date))
                return d;
        return null;
    }

    public DiaryLab getDiaryMonth(String year, String month) {
        DiaryLab diaryMouth = null;
        boolean fBom = false;
        for (Diary d : mDiaries) {
            if (fBom && !(d.getYear() == year && d.getMonth() == month))
                return diaryMouth;
            if (d.getYear() == year && d.getMonth() == month) {
                fBom = true;
                diaryMouth.mDiaries.add(d);
            }
        }
        return diaryMouth;
    }

    public boolean saveDiaries() {
        try {
            mDayGramJSONSerializer.saveDiaries(mDiaries);
            Log.d(TAG, "diaries saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving diaries: ", e);
            return false;
        }
    }
}
