package com.example.lx.daygram;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
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
    private String FILENAME = "diary.json";
    private static final String FILEBASENAME = "diary";

    private ArrayList<Diary> mDiaries;
    private DayGramJSONSerializer mDayGramJSONSerializer;

    private static DiaryLab sDiaryLab;
    private static int mMonth, mYear;
    private static boolean isFileChanged = false;
    private Context mAppContext;

    private DiaryLab(Context appContext) {
        mAppContext = appContext;
        isFileChanged = false;
        Calendar c = Calendar.getInstance();
        if (mMonth == 0) mMonth = c.get(Calendar.MONTH)+1;
        if (mYear == 0) mYear = c.get(Calendar.YEAR);

        upDateFilename();
        mDayGramJSONSerializer = new DayGramJSONSerializer(mAppContext, FILENAME);
        mDiaries = new ArrayList<Diary>();

        try {
            mDiaries = mDayGramJSONSerializer.loadDiaries();
        } catch (Exception e) {
            mDiaries = new ArrayList<Diary>();
        } finally {
            if (0 == mDiaries.size()) mDiaries = new ArrayList<Diary>();
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
        if (sDiaryLab == null || isFileChanged) sDiaryLab = new DiaryLab(c.getApplicationContext());
        return sDiaryLab;
    }

    public static int getMonth() {
        return mMonth;
    }

    public static void setMonth(int mMonth) {
        DiaryLab.mMonth = mMonth;
    }

    public static int getYear() {
        return mYear;
    }

    public static void setYear(int mYear) {
        DiaryLab.mYear = mYear;
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

    public void upDateDiaryLab(int year, int month) {
        if (year == mYear && month == mMonth) return;
        mYear = year;
        mMonth = month;
        upDateFilename();
        isFileChanged = true;
    }

    private void upDateFilename() {
        FILENAME = FILEBASENAME + '_' + String.valueOf(mYear)  + '_' +  String.valueOf(mMonth) + ".json";
    }
}
