package com.howshea.gankio.UI.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.howshea.gankio.R;

/**
 * Created by haipo on 2016/10/23.
 */

public class WebFragment extends Fragment {
    private static final String ARG_URL = "url";


    private NumberProgressBar mWebProgressBar;
    private WebView mWebView;

    private String mUrl;
    private ActionBar mActionBar;


    public static WebFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString(ARG_URL);
        setHasOptionsMenu(true);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        mActionBar = activity.getSupportActionBar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frg_web, container, false);
        mWebProgressBar = (NumberProgressBar) v.findViewById(R.id.web_progress_bar);
        mWebView = (WebView) v.findViewById(R.id.web_view);
        initWebView();
        return v;
    }


    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        mWebView.loadUrl(mUrl);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mWebProgressBar.setVisibility(View.GONE);
                } else {
                    mWebProgressBar.setVisibility(View.VISIBLE);
                    mWebProgressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {

                //不能在此处拿actionbar，而是应该先初始化好，否则在快速进入退出界面时，
                //容易报NullPointerException
                mActionBar.setTitle(title);
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_gank,menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_share:
                shareTo();
                break;
            case R.id.menu_open_by_browser:
                openByBrowser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareTo(){
        ShareCompat.IntentBuilder
                .from(getActivity())
                .setType("text/plain")
                .setText(getShareText())
                .setChooserTitle(R.string.share_to)
                .startChooser();
    }

    private void openByBrowser(){
        Uri uri = Uri.parse(mUrl);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
    }

    private String getShareText(){
        return mActionBar.getTitle()+","+mUrl;
    }

    public boolean webCanGoBack() {
        return mWebView.canGoBack();
    }

    public void webGoBack() {
        mWebView.goBack();
    }

}
