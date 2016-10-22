package com.haipo.gankio.UI.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.haipo.gankio.R;
import com.haipo.gankio.UI.fragment.MeiziFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity implements MeiziFragment.Callback {

    @BindView(R.id.base_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.base_drawer_layout)
    DrawerLayout mBaseDrawerLayout;
    @BindView(R.id.base_fab)
    FloatingActionButton mBaseFab;
    @BindView(R.id.base_navigation_view)
    NavigationView mBaseNavigationView;

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
        initNavigationView();
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


    private void initNavigationView(){
        mBaseNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_item_android:
                        GotoActivity(getString(R.string.android));
                        break;
                    case R.id.menu_item_ios:
                        GotoActivity(getString(R.string.ios));
                        break;
                    case R.id.menu_item_front_end:
                        GotoActivity(getString(R.string.front_end));
                        break;
                    case R.id.menu_item_app:
                        GotoActivity(getString(R.string.app));
                        break;
                    case R.id.menu_item_expanding_resources:
                        GotoActivity(getString(R.string.expanding_resources));
                        break;
                    case R.id.menu_item_recommend:
                        GotoActivity(getString(R.string.recommend));
                        break;
                    case R.id.menu_item_about:
                        //TODO
                        break;
                    case R.id.menu_item_feedback:
                        //TODO
                        break;
                }
                return false;
            }
        });
    }

    private void GotoActivity(String type){
        Intent intent = GankPagerActivity.newIntent(this,type);
        startActivity(intent);

    }

    @Override
    protected void onStop() {
        mBaseDrawerLayout.closeDrawers();
        super.onStop();
    }

    @Override
    public void hidefab() {
        mBaseFab.hide();
    }

    @Override
    public void showfab() {
        mBaseFab.show();
    }
}
