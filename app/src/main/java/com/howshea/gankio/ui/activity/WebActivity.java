package com.howshea.gankio.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.howshea.gankio.ui.fragment.WebFragment;

/**
 * Created by haipo on 2016/10/23.
 */

public class WebActivity extends SingleFragmentActivity {

    private static final String EXTRA_URL = "com.howshea.gankio.url";
    private WebFragment mWebFragment;

    public static Intent newIntent(Context packageContext, String url) {
        Intent intent = new Intent(packageContext, WebActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        mWebFragment = WebFragment.newInstance(getIntent().getStringExtra(EXTRA_URL));
        return mWebFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mWebFragment.webCanGoBack()){
            mWebFragment.webGoBack();
        }else {
            super.onBackPressed();
        }
    }


}
