package com.haipo.gankio.UI.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haipo.gankio.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by haipo on 2016/10/22.
 */

public class GankFragment extends Fragment {

    @BindView(R.id.gank_recycler_view)
    RecyclerView mGankRecyclerView;
    private Unbinder mUnbinder;

    public static GankFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        GankFragment fragment = new GankFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frg_gank, container, false);

        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }
}
