package com.haipo.gankio.activity;

import android.support.v4.app.Fragment;

import com.haipo.gankio.fragment.MeiziFragment;

public class BaseActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return MeiziFragment.newInstance();
    }
}
