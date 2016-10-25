package com.howshea.gankio.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.howshea.gankio.R;
import com.howshea.gankio.UI.fragment.HistoryFragment;

import java.util.ArrayList;

public class HistoryActivity extends SingleFragmentActivity {

    private static final String EXTRA_HISTORY_DATE = "com.howshea.gankio.history_date";
    private ActionBar mActionBar;

    public static Intent newIntent(Context packageContext, ArrayList<String> datelist) {
        Intent intent = new Intent(packageContext, HistoryActivity.class);
        intent.putStringArrayListExtra(EXTRA_HISTORY_DATE, datelist);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return HistoryFragment.newInstance(getIntent().
                getStringArrayListExtra(EXTRA_HISTORY_DATE));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(getString(R.string.history_title));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
