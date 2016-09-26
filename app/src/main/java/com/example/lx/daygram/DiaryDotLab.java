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

    private DiaryDotLab(Context appContext) {
        mAppContext = appContext;
        mDots = new ArrayList<DiaryDot>();
        ArrayList<Diary> mDiaries = DiaryLab.get(appContext).getDiaries();

        Date todayDate = new Date();
        int j = 0;

        for (Date i = new Date(110, 1, 1); i.compareTo(todayDate) < 0; i = new Date(i.getTime() + 24 * 60 * 60 * 1000)){
            DiaryDot dot = new DiaryDot();
            if (j >= mDiaries.size() || mDiaries.get(j).getDate().compareTo(i) != 0) {
                dot.setDate(i);
                mDots.add(dot);
            }
            else j++;
        }
    }

    public static DiaryDotLab get(Context c) {
        if (sDiaryDotLab == null) sDiaryDotLab = new DiaryDotLab(c.getApplicationContext());
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

    public DiaryDotLab getDiaryDotMonth(String year, String month) {
        DiaryDotLab dotMouth = new DiaryDotLab(mAppContext);
        boolean fBom = false;
        for (DiaryDot d : mDots) {
            if (fBom && !(d.getYear().equals(year)  && d.getMonth().equals(month) ))
                return dotMouth;
            if (d.getYear() .equals(year)  && d.getMonth().equals(month) ) {
                fBom = true;
                dotMouth.mDots.add(d);
            }
        }
        if (dotMouth == null) {
            return new DiaryDotLab(mAppContext);
        }
        return dotMouth;
    }


    public void deleteDiaryDot(DiaryDot dot) {
        mDots.remove(dot);
    }
}
