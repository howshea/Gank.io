package com.howshea.gankio.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import com.howshea.gankio.ui.fragment.ShowPictureFragment;
import com.howshea.gankio.utils.Utils;

public class ShowPictureActivity extends SingleFragmentActivity {

    private static final String EXTRA_PICTURE_URL = "com.howshea.gankio.picture_url";
    private static final String EXTRA_PICTURE_DATE = "com.howshea.gankio.picture_date";

    public static Intent newIntent(Context packageContext, String url,String date) {
        Intent intent = new Intent(packageContext, ShowPictureActivity.class);
        intent.putExtra(EXTRA_PICTURE_URL, url);
        intent.putExtra(EXTRA_PICTURE_DATE,date);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        return ShowPictureFragment.newInstance(getIntent().getStringExtra(EXTRA_PICTURE_URL),
                getIntent().getStringExtra(EXTRA_PICTURE_DATE));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        String date = Utils.formatDate(getIntent().getStringExtra(EXTRA_PICTURE_DATE));
        actionBar.setTitle(date);
    }
}
