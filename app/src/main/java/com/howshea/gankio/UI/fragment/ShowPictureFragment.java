package com.howshea.gankio.UI.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.howshea.gankio.R;
import com.howshea.gankio.Utils.Utils;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class ShowPictureFragment extends Fragment {

    private static final String ARG_PICTURE_URL = "pictureUrl";
    private static final String ARG_PICTURE_DATE = "pictureDate";
    private static final int REQUEST_CODE_ASK_PERMISSIONS=0x123;

    private String pictureUrl;
    private ImageView mImageView;
    private Bitmap mBitmap;
    private String mPictureDate;
    private AppCompatActivity mContext;

    public static ShowPictureFragment newInstance(String pictureUrl, String pictureDate) {
        ShowPictureFragment fragment = new ShowPictureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PICTURE_URL, pictureUrl);
        args.putString(ARG_PICTURE_DATE, pictureDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (AppCompatActivity) getActivity();
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            pictureUrl = getArguments().getString(ARG_PICTURE_URL);
            mPictureDate = getArguments().getString(ARG_PICTURE_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frg_show_picture, container, false);
        mImageView = (ImageView) view.findViewById(R.id.big_image_view);
        loadInto();
        return view;
    }

    private void loadInto() {
        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                mBitmap = resource;
                mImageView.setImageBitmap(resource);
            }
        };
        Glide.with(mContext)
                .load(pictureUrl)
                .asBitmap()
                .into(target);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void saveBitmapToFileWrapper() {
        int hasPermission = mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasPermission!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
            ,REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        saveBitmapToFile();
    }


    private void saveBitmapToFile() {
        Observable
                .create(new Observable.OnSubscribe<Uri>() {
                    @Override
                    public void call(Subscriber<? super Uri> subscriber) {
                        Uri uri = Utils.saveFile(mBitmap, mPictureDate);
                        subscriber.onNext(uri);
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Uri>() {
                    @Override
                    public void call(Uri uri) {
                        //通知图库更新数据
                        if (uri!=null){
                            Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                    uri);
                            mContext.sendBroadcast(i);
                            Snackbar.make(mImageView,
                                    R.string.sava_success,
                                    Snackbar.LENGTH_LONG).show();
                        }else {
                            Snackbar.make(mImageView,
                                    R.string.sava_failed,
                                    Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_picture, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mContext.onBackPressed();
                break;
            case R.id.save_file:
                if (mImageView != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        saveBitmapToFileWrapper();
                    } else {
                        saveBitmapToFile();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    saveBitmapToFile();
                }else {
                    Toast.makeText(mContext, "你拒绝了权限申请", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
