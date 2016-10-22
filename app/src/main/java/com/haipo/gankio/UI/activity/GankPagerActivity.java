package com.haipo.gankio.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.haipo.gankio.R;
import com.haipo.gankio.UI.fragment.GankFragment;
import com.haipo.gankio.UI.widget.ReformSwipRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GankPagerActivity extends AppCompatActivity {

    @BindView(R.id.gank_toolbar)
    Toolbar mGankToolbar;
    @BindView(R.id.gank_tab_layout)
    TabLayout mGankTabLayout;
    @BindView(R.id.gank_view_pager)
    ViewPager mGankViewPager;
    @BindView(R.id.gank_refresh_layout)
    ReformSwipRefreshLayout mGankRefreshLayout;

    ArrayList<String> mTypeList;
    int[] mTypeIds = {
            R.string.android,
            R.string.ios,
            R.string.front_end,
            R.string.app,
            R.string.expanding_resources,
            R.string.recommend
    };
    private static final String TYPE="type";

    public static Intent newIntent(Context packageContext, String type) {
        Intent intent = new Intent(packageContext, GankPagerActivity.class);
        intent.putExtra(TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gank);
        ButterKnife.bind(this);
        //初始化Toolbar
        setSupportActionBar(mGankToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initViewPagerAndTab();
        String type = getIntent().getStringExtra(TYPE);
        for (int i = 0; i < mTypeList.size(); i++) {
            if (!TextUtils.isEmpty(type)&&mTypeList.get(i).equals(type)) {
                mGankViewPager.setCurrentItem(i);
                break;
            }
        }


        //设置下拉刷新控件
        mGankRefreshLayout.setColorSchemeResources(R.color.AKABENI);
        mGankRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

    }


    private void initViewPagerAndTab(){
        mTypeList=new ArrayList<>();
        for (int i=0;i<mTypeIds.length;i++){
            mTypeList.add(getString(mTypeIds[i]));
        }
        mGankViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mGankTabLayout));
        mGankViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mGankTabLayout.setupWithViewPager(mGankViewPager);
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter{

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTypeList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return GankFragment.newInstance(mTypeList.get(position));
        }

        @Override
        public int getCount() {
            return mTypeList.size();
        }

        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
