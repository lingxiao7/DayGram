package com.example.lx.daygram;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by lx on 2016/9/25.
 * Main menu to change read view, add a new diary.
 * The choice of month is not completed.
 */
public class MenuFragment extends Fragment {
    private static boolean sIsShow = false;

    private ImageButton mNewImageButton;
    private ImageButton mShowImageButton;


    private int MonthNow;
    private int YearNow;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onNewDiary(Diary diary);
        void onShowDiaries(boolean fg);
        void upDateActivity();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        Calendar c = Calendar.getInstance();

        MonthNow = DiaryLab.getMonth();
        YearNow = DiaryLab.getYear();
        //spinner监听
        final Spinner spinnerM = (Spinner) v.findViewById(R.id.spinner_month);
        spinnerM.setSelection(MonthNow-1,true);
        spinnerM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                MonthNow=pos+1;
                DiaryLab.get(getActivity()).upDateDiaryLab(YearNow, MonthNow);
                mCallbacks.upDateActivity();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        final Spinner spinnerY = (Spinner) v.findViewById(R.id.spinner_year);
        spinnerY.setSelection(YearNow-2012,true);
        spinnerY.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                YearNow=pos+2012;
                DiaryLab.get(getActivity()).upDateDiaryLab(YearNow, MonthNow);
                mCallbacks.upDateActivity();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        mNewImageButton = (ImageButton)v.findViewById(R.id.diary_list_menu_new);
        mNewImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Diary diary = new Diary();
                diary.setDate(new Date());
                DiaryLab.get(getActivity()).getDiaries().add(diary);
                mCallbacks.onNewDiary(diary);
            }
        });

        mShowImageButton = (ImageButton)v.findViewById(R.id.diary_list_menu_show);
        mShowImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                mCallbacks.onShowDiaries(sIsShow);
                if (!sIsShow) sIsShow = true;
                else sIsShow = false;
            }
        });


        return v;
    }
}
