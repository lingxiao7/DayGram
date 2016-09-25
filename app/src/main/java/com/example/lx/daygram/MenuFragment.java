package com.example.lx.daygram;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by lx on 2016/9/25.
 */
public class MenuFragment extends Fragment {
    private TextView mMonthTextView;
    private TextView mYearTextView;
    private ImageButton mNewImageButton;
    private ImageButton mShowImageButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        mMonthTextView = (TextView)v.findViewById(R.id.diary_list_menu_month);

        mYearTextView = (TextView)v.findViewById(R.id.diary_list_menu_year);

        mNewImageButton = (ImageButton)v.findViewById(R.id.diary_list_menu_new);

        mShowImageButton = (ImageButton)v.findViewById(R.id.diary_list_menu_show);

        return v;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
