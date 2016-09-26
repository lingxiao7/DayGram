package com.example.lx.daygram;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lx on 2016/9/26.
 */
public class MenuYearFragment extends Fragment {
    public static final String EXTRA_DIARY_YEAR =
            "com.example.lx.daygram.diary_year";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_year, container, false);
        String diaryDate = (String)getArguments().getSerializable(EXTRA_DIARY_YEAR);

        return v;
    }


    public static MenuYearFragment newInstance(String year) {
        Bundle args = new Bundle();
        //args.putSerializable(EXTRA_DIARY_ID, diaryId);
        args.putSerializable(EXTRA_DIARY_YEAR, year);

        MenuYearFragment fragment = new MenuYearFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
