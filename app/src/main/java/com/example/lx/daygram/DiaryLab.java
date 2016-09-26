package com.example.lx.daygram;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by lx on 2016/9/22.
 * The lab of all diary. Only one static mDiaries is exampled, app can
 * get, operate, save it. Provide a sort comparator is easy to add any
 * diary by the order of date.
 */
public class DiaryLab {
    private static final String TAG = "DiaryLab";
    private static final String FIELNAME = "diary.json";
    private static final String FLELTESTNAME = "diarytest11.json";

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
            for (Date i = new Date(110, 1, 0); i.compareTo(todayDate) < 0; i = new Date(i.getTime() + 24 * 60 * 60 * 1000)){
                Diary d = new Diary();
                d.setDate(i);
                mDiaries.add(d);
            }
        } finally {
            if (0 == mDiaries.size()) mDiaries = new ArrayList<Diary>();
            /**
            if (0 == mDiaries.size()) {
                //mDiaries = new ArrayList<Diary>();
                Date todayDate = new Date();
                for (Date i = new Date(110, 1, 1); i.compareTo(todayDate) < 0; i = new Date(i.getTime() + 24 * 60 * 60 * 1000)){
                    Diary d = new Diary();
                    d.setDate(i);
                    mDiaries.add(d);
                }
            }*/
        }
    }

    public void sortDiary() {
        Collections.sort(mDiaries, new SortByDate());
    }

    public class SortByDate implements Comparator {
        public int compare(Object arg0, Object arg1) {
            Diary a0 = (Diary) arg0;
            Diary a1 = (Diary) arg1;
            return a0.getDate().compareTo(a1.getDate());
        }
    }

    public static DiaryLab get(Context c) {
        if (sDiaryLab == null) sDiaryLab = new DiaryLab(c.getApplicationContext());
        return sDiaryLab;
    }

    public ArrayList<Diary> getDiaries() {
        return mDiaries;
    }

    public void deleteDiary(Diary d) {
        mDiaries.remove(d);
    }

    public Diary getDiary(Date date) {
        for (Diary d : mDiaries)
            if (d.getDate().equals(date))
                return d;
        return null;
    }

    public DiaryLab getDiaryMonth(String year, String month) {
        DiaryLab diaryMouth = new DiaryLab(mAppContext);
        boolean fBom = false;
        for (Diary d : mDiaries) {
            if (fBom && !(d.getYear().equals(year)  && d.getMonth().equals(month) ))
                return diaryMouth;
            if (d.getYear() == year && d.getMonth() == month) {
                fBom = true;
                diaryMouth.mDiaries.add(d);
            }
        }
        if (diaryMouth == null) {
            diaryMouth = new DiaryLab(mAppContext);
            return diaryMouth;
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
