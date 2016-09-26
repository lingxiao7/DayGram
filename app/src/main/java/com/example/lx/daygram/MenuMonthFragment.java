package com.example.lx.daygram;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by lx on 2016/9/26.
 */
public class MenuMonthFragment extends Fragment{
    public static final String EXTRA_DIARY_MONTH =
            "com.example.lx.daygram.diary_month";
    private ImageButton[] month = new ImageButton[13];
    private String[] monthString = {
            "menu_mouth_1","menu_mouth_2","menu_mouth_3",
            "menu_mouth_4","menu_mouth_5","menu_mouth_6",
            "menu_mouth_7","menu_mouth_8","menu_mouth_9",
            "menu_mouth_10","menu_mouth_11","menu_mouth_12"
    };

    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_month, container, false);
        String diaryDate = (String)getArguments().getSerializable(EXTRA_DIARY_MONTH);

        /**
        for (int i = 0; i < 12; i++){
            int imgButId = getResources().getIdentifier(monthString[i], "id", "com.example.lx.daygram");
            //month[i] = (ImageButton)v.findViewById(getResId(monthString[i], R.id.class));
            month[i] = (ImageButton)v.findViewById(imgButId);
            month[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    fm.popBackStack();
                }
            });
        }*/
        return v;
    }

    public static MenuMonthFragment newInstance(String month) {
        Bundle args = new Bundle();
        //args.putSerializable(EXTRA_DIARY_ID, diaryId);
        args.putSerializable(EXTRA_DIARY_MONTH, month);

        MenuMonthFragment fragment = new MenuMonthFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
