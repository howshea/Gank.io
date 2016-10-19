package com.haipo.gankio.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by haipo on 2016/10/14.
 */

public class Utils {
    public static void imageLoader(Fragment fragment, String url, ImageView imageView) {
        Glide.with(fragment)
                .load(url)
                .asBitmap()
                .fitCenter()
                .into(imageView);
    }

    public static Bitmap imageLoader(Context context, String url) {
        Bitmap bitmap=null;
        try {
            bitmap = Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            return bitmap;
        }
    }

    public static float getImageInfo(String resurl){
        float radio=0.5f;
        try {
            URL url = new URL(resurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5*1000);
            Bitmap bitmap=null;
            if (conn.getResponseCode()==200){
                InputStream inStream = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(inStream);
                radio = bitmap.getWidth()/(float)bitmap.getHeight();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return radio;
    }
}
