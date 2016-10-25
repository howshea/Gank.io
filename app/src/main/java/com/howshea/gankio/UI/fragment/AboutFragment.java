package com.howshea.gankio.UI.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.howshea.gankio.R;

/**
 * Created by haipo on 2016/10/25.
 */

public class AboutFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_about, null);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("关闭",null)
                .create();
    }
}
