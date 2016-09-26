package com.example.lx.daygram;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;
import java.util.UUID;

/**
 * Created by lx on 2016/9/23.
 */
public class DiaryEditFragment extends Fragment {
    public static final String EXTRA_DIARY_DATE =
            "com.example.lx.daygram.diary_date";

    private String mTextString;
    private Diary mDiary;
    private EditText mTextField;
    private TextView mDateTextView;
    private ImageButton mHomeImageButton;
    private ImageButton mTimeImageButton;
    private ImageButton mSaveImageButton;

    private int mCursorIndex;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Date diaryDate = (Date)getArguments().getSerializable(EXTRA_DIARY_DATE);
        if (diaryDate != null) {
            mDiary = DiaryLab.get(getActivity()).getDiary(diaryDate);
            if (mDiary == null) {
                mDiary = new Diary();
                mDiary.setDate(diaryDate);
            }
            return;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_diary_edit, container, false);

        mTextField = (EditText)v.findViewById(R.id.diary_details_text);
        mTextString = mDiary.getText();
        mTextField.setFocusable(false);
        mTextField.setFocusableInTouchMode(false);
        mTextField.setText(mTextString);
        mTextField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mTextField.setFocusable(true);
                mTextField.setFocusableInTouchMode(true);
                mCursorIndex = mTextField.getSelectionStart();
            }
        });
        mTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextString = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateTextView = (TextView)v.findViewById(R.id.diary_details_date);
        String date = (String) DateFormat.format("EEEE/MMMM  dd/yyyy", mDiary.getDate());
        mDateTextView.setText(date);

        // It isn't completed
        mHomeImageButton = (ImageButton)v.findViewById(R.id.diary_details_home_button);
        mHomeImageButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        // Insert Time
        mTimeImageButton = (ImageButton)v.findViewById(R.id.diary_details_time);
        mTimeImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String time = (String)DateFormat.format("hh:mm aa", new Date());
                mCursorIndex = mTextField.getSelectionStart();
                Editable editable = mTextField.getText();
                editable.insert(mCursorIndex, time);
                mTextString = mTextField.getText().toString();
            }
        });

        // Save Diary
        mSaveImageButton = (ImageButton)v.findViewById(R.id.diary_details_save);
        mSaveImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mTextField.setFocusable(false);
                mTextField.setFocusableInTouchMode(false);
                if (mTextString != null) mDiary.setText(mTextString);
                DiaryLab.get(getActivity()).sortDiary();
                DiaryLab.get(getActivity()).saveDiaries();
                getActivity().onBackPressed();
            }
        });
        return v;
    }

    public static DiaryEditFragment newInstance(Date diaryDate) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DIARY_DATE, diaryDate);

        DiaryEditFragment fragment = new DiaryEditFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
