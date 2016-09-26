package com.example.lx.daygram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by lx on 2016/9/23.
 */
public class DiaryListActivity extends SingleFragmentActivity implements MenuFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        return new DiaryListFragment();
    }

    @Override
    public void onNewDiary(Diary diary) {
        Intent i = new Intent(this, DiaryPagerActivity.class);
        i.putExtra(DiaryFragment.EXTRA_DIARY_DATE, diary.getDate());
        startActivity(i);
    }

    @Override
    public void onDiarySelectedMonth(String month) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment oldMenu = fm.findFragmentById(R.id.fragmentMenuContainer);
        Fragment newMenu = MenuMonthFragment.newInstance(month);

        ft.replace(R.id.fragmentMenuContainer, newMenu);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onDiarySelectedYear(String year) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment oldMenu = fm.findFragmentById(R.id.fragmentMenuContainer);
        Fragment newMenu = MenuYearFragment.newInstance(year);

        ft.replace(R.id.fragmentMenuContainer, newMenu);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onShowDiaries(String year, String month) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment oldFragment = fm.findFragmentById(R.id.fragmentListContainer);
        Fragment newFragment = new DiaryDetailsListFragment();

        ft.replace(R.id.fragmentListContainer, newFragment);
        ft.addToBackStack(null);
        ft.commit();

    }
}
