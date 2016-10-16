package com.haipo.gankio.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.haipo.gankio.R;
import com.haipo.gankio.fragment.MeiziFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {

    @BindView(R.id.base_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.base_drawer_layout)
    DrawerLayout mBaseDrawerLayout;

    protected Fragment createFragment() {
        return MeiziFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        addFragmentInto();
        initDrawerLayout();
    }

    private void addFragmentInto(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private void initDrawerLayout(){
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,
                mBaseDrawerLayout, mToolbar
                , R.string.open, R.string.close);
        drawerToggle.syncState();
        mBaseDrawerLayout.addDrawerListener(drawerToggle);
    }
}
