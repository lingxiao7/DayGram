package com.example.lx.daygram;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by lx on 2016/9/23.
 */
public class DiaryListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new DiaryListFragment();
    }
}
