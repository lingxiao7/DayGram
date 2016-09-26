package com.example.lx.daygram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by lx on 2016/9/23.
 * The diary list activity, the DiaryListFragment's container. Also has a menu,
 * and override the implements of the menu to deal with some interaction.
 */
public class DiaryListActivity extends SingleFragmentActivity implements MenuFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        return new DiaryListFragment();
//        Date date = new Date();
//        String year = DateFormat.format("yyyy", date).toString();
//        String month = DateFormat.format("MM", date).toString();
//        return DiaryListFragment.newInstance(year, month);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            boolean isSave = data.getBooleanExtra(DiaryEditFragment.EXTRA_DIARY_SAVE, false);
            if (isSave) {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        }
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
    public void onShowDiaries(String year, String month, boolean fg) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        //Fragment oldFragment = fm.findFragmentById(R.id.fragmentListContainer);
        if (!fg) {
            Fragment newFragment = new DiaryDetailsListFragment();

            ft.replace(R.id.fragmentListContainer, newFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        else {
            Fragment newFragment = new DiaryListFragment();

            ft.replace(R.id.fragmentListContainer, newFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
