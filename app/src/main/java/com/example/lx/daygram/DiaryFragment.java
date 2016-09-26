package com.example.lx.daygram;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;
import java.util.UUID;
import android.view.View.OnClickListener;

/**
 * Created by lx on 2016/9/23.
 * Now it's abandoned. Substituted by DiaryEditFragment.
 */
public class DiaryFragment extends Fragment implements OnClickListener {
    public static final String EXTRA_DIARY_DATE =
            "com.example.lx.daygram.diary_date";
    public static final String EXTRA_DIARY_ID =
            "com.example.lx.daygram.diary_id";

    private Diary mDiary;
    private TextView mTextField;
    private TextView mDateTextView;
    private ImageButton mHomeImageButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mDiary = new Diary();
        //Date diaryDate = (Date)getActivity().getIntent()
        //        .getSerializableExtra(EXTRA_DIARY_DATE);
        Date diaryDate = (Date)getArguments().getSerializable(EXTRA_DIARY_DATE);

        if (diaryDate != null) {
            mDiary = DiaryLab.get(getActivity()).getDiary(diaryDate);
            return;
        }
        /**
        UUID diaryId = (UUID)getArguments().getSerializable(EXTRA_DIARY_ID);


        if (diaryId != null) {
            mDiary = DiaryLab.get(getActivity()).getDiary(diaryId);
            return;
        }**/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_diary, container, false);

        mTextField = (TextView)v.findViewById(R.id.diary_details_text);
        mTextField.setText(mDiary.getText());
        mTextField.setOnClickListener(this);
        /**mTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mDiary.getTitle().length() <= 25)
                    mDiary.setTitle(s.toString());
                mDiary.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });**/

        mDateTextView = (TextView)v.findViewById(R.id.diary_details_date);
        String date = (String) DateFormat.format("EEEE/MM dd/yyyy", mDiary.getDate());
        mDateTextView.setText(date);

        // It isn't completed
        mHomeImageButton = (ImageButton)v.findViewById(R.id.diary_details_home_button);
        mHomeImageButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        return v;
        //return super.onCreateView(inflater, container, savedInstanceState);

    }

    public static DiaryFragment newInstance(Date diaryDate) {
        Bundle args = new Bundle();
        //args.putSerializable(EXTRA_DIARY_ID, diaryId);
        args.putSerializable(EXTRA_DIARY_DATE, diaryDate);

        DiaryFragment fragment = new DiaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        Fragment diaryEditFragment = DiaryEditFragment.newInstance(mDiary.getDate());
        ft.replace(R.id.viewPager, diaryEditFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
