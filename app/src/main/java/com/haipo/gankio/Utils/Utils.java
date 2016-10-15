package com.haipo.gankio.Utils;

import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by haipo on 2016/10/14.
 */

public  class  Utils {
    public static void imageLoader(Fragment fragment, String url, ImageView imageView){
        Glide.with(fragment)
                .load(url)
                .asBitmap()
                .into(imageView);
    }
}
