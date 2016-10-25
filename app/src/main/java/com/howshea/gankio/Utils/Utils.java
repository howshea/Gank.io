package com.howshea.gankio.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.howshea.gankio.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by haipo on 2016/10/14.
 */

public class Utils {

    public static void imageLoader(Fragment fragment, String url, ImageView imageView) {
        Glide.with(fragment)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(imageView);
    }


    public static synchronized float imageLoader(Context context, String url) {
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            return bitmap.getWidth() / (float) bitmap.getHeight();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return 0.5f;
    }

    public static SpannableString formatString(Context context, String text, int style) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new TextAppearanceSpan(context, style), 0, text.length(), 0);
        return spannableString;
    }

    public static String formatDate(String publishAt) {
        String[] strings = publishAt.split("T");
        return strings[0];
    }

    public static Uri saveFile(Bitmap bitmap, String title) {
        File filesDir = new File(Environment.getExternalStorageDirectory(), "Gankio/pictures");
        if (!filesDir.exists()) {
            filesDir.mkdirs();
        }
        File file = new File(filesDir, Utils.formatDate(title) + ".jpg");
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            assert bitmap != null;
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {

        }
        return Uri.fromFile(file);
    }


    public static void sendEmail(Context context, int emailId) {
        Uri uri = Uri.parse("mailto:" + context.getString(emailId));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
         intent.putExtra(Intent.EXTRA_SUBJECT, "Gank.io意见反馈"); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_text)); // 正文
        context.startActivity(intent);
    }
}
