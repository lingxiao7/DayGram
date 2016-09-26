package com.example.lx.daygram;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by lx on 2016/9/23.
 * The superclass of FragmentActivity.
 */


public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

    protected int getLayoutResId() {
        return R.layout.activity_fragment1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_fragment1);
        setContentView(getLayoutResId());
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragmentList = fm.findFragmentById(R.id.fragmentListContainer);
        if (fragmentList == null) {
            fragmentList = createFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.fragmentListContainer, fragmentList);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        Fragment fragmentMenu = fm.findFragmentById(R.id.fragmentMenuContainer);
        if (fragmentMenu == null) {
            fragmentMenu = new MenuFragment();

            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.fragmentMenuContainer, fragmentMenu);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}