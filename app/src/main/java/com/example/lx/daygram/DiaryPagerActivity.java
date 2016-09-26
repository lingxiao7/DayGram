package com.example.lx.daygram;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by lx on 2016/9/23.
 * ViewPager help the app to swipe to replace diary.
 */
public class DiaryPagerActivity extends FragmentActivity implements DiaryEditFragment.Callbacks{
    private ViewPager mViewPager;
    private ArrayList<Diary> mDiaries;

    @Override
    public void onSave(Intent data) {
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mDiaries = DiaryLab.get(this).getDiaries();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Diary diary = mDiaries.get(position);
                Date date = null;
                if (diary == null) {
                    date = (Date) getIntent()
                            .getSerializableExtra(DiaryEditFragment.EXTRA_DIARY_DATE);
                }
                else date = diary.getDate();
                return DiaryEditFragment.newInstance(date);
                //return DiaryFragment.newInstance(diary.getDate());
            }

            @Override
            public int getCount() {
                return mDiaries.size();
            }
        });

        Date diaryDate = (Date) getIntent()
                .getSerializableExtra(DiaryEditFragment.EXTRA_DIARY_DATE);

        for (int i = 0; i < mDiaries.size(); i++) {
            if (mDiaries.get(i).getDate().equals(diaryDate)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            public void onPageScrollStateChanged(int state){}

            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels){ }

            public void onPageSelected(int pos) {
                Diary diary = mDiaries.get(pos);
                if (diary.getText() != null) {
                    String text = diary.getText();
                    if (text.length() > 35) {
                        String title = text.subSequence(0, 29).toString();
                        title += "...";
                        setTitle(title);
                    } else {
                        setTitle(text);
                    }
                }
            }
        });
    }
}
