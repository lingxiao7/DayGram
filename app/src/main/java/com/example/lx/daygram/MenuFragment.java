package com.example.lx.daygram;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by lx on 2016/9/25.
 * Main menu to change read view, add a new diary.
 * The choice of month is not completed.
 */
public class MenuFragment extends Fragment {
    private static boolean sIsShow = false;

    private TextView mMonthTextView;
    private TextView mYearTextView;
    private ImageButton mNewImageButton;
    private ImageButton mShowImageButton;

    private String mMonth;
    private String mYear;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onNewDiary(Diary diary);
        void onDiarySelectedMonth(String month);
        void onDiarySelectedYear(String year);
        void onShowDiaries(String year, String month, boolean fg);
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

        mMonthTextView = (TextView)v.findViewById(R.id.diary_list_menu_month);
        mMonthTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCallbacks.onDiarySelectedMonth(mMonth);
            }
        });

        mYearTextView = (TextView)v.findViewById(R.id.diary_list_menu_year);
        mYearTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCallbacks.onDiarySelectedYear(mYear);
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

                mCallbacks.onShowDiaries(mYear, mMonth, sIsShow);
                if (!sIsShow) sIsShow = true;
                else sIsShow = false;
            }
        });


        return v;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
