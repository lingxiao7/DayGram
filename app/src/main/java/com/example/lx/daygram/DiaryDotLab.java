package com.example.lx.daygram;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lx on 2016/9/26.
 * The lab of all dots. Like DiaryLab
 */
public class DiaryDotLab {
    private ArrayList<DiaryDot> mDots;
    private DayGramJSONSerializer mDayGramJSONSerializer;

    private static DiaryDotLab sDiaryDotLab;
    private Context mAppContext;
    private static int mYear, mMonth;

    private DiaryDotLab(Context appContext) {
        mAppContext = appContext;
        mDots = new ArrayList<DiaryDot>();
        ArrayList<Diary> mDiaries = DiaryLab.get(appContext).getDiaries();

        int j = 0;
        Date beginDate = new Date(mYear - 1900, mMonth - 1, 1);
        Date endDate = new Date(mYear - 1900, mMonth , 1);

        for (Date i = beginDate; i.compareTo(endDate) < 0; i = new Date(i.getTime() + 24 * 60 * 60 * 1000)){
            DiaryDot dot = new DiaryDot();
            if (j >= mDiaries.size() || mDiaries.get(j).getDate().compareTo(i) != 0) {
                dot.setDate(i);
                mDots.add(dot);
            }
            else j++;
        }
    }

    public static DiaryDotLab get(Context c, int year, int month) {
        if (sDiaryDotLab == null || !(mYear == year && mMonth == month)) {
            if (year != 0) mYear = year;
            if (month != 0) mMonth = month;
            sDiaryDotLab = new DiaryDotLab(c.getApplicationContext());
        }
        return sDiaryDotLab;
    }

    public ArrayList<DiaryDot> getDots() {
        return mDots;
    }

    public DiaryDot getDot(Date date) {
        for (DiaryDot dot : mDots)
            if (dot.getDate().equals(date))
                return dot;
        return null;
    }

    public void deleteDiaryDot(DiaryDot dot) {
        mDots.remove(dot);
    }
}
