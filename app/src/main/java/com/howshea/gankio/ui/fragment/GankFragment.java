package com.howshea.gankio.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howshea.gankio.entity.Gank;
import com.howshea.gankio.R;
import com.howshea.gankio.ui.activity.WebActivity;
import com.howshea.gankio.utils.Utils;
import com.howshea.gankio.net.HttpRequest;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by haipo on 2016/10/22.
 */

public class GankFragment extends Fragment {

    private static final String ARG_TYPE = "type";

    private RecyclerView mGankRecyclerView;

    private LinearLayoutManager mLayoutManager;
    private String mType;
    private ArrayList<Gank> mGankList = new ArrayList<>();
    private Callback mCallback;
    private GankAdapter mAdapter;
    private int mPage;

    public interface Callback {
        void startRefresh();

        void stopRefresh();
    }


    public static GankFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TYPE, type);
        GankFragment fragment = new GankFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getString(ARG_TYPE);
        mCallback = (Callback) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frg_gank, container, false);
        mGankRecyclerView = (RecyclerView) view.findViewById(R.id.gank_recycler_view);
        initRecyclerView();

        return view;
    }


    private class GankViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {


        private final TextView mDescTextView;

        public GankViewHolder(View itemView) {
            super(itemView);
            mDescTextView = (TextView) itemView.findViewById(R.id.desc_text_view);
            itemView.setOnClickListener(this);
        }

        public void BindData(int position) {
            SpannableStringBuilder builder = new SpannableStringBuilder(mGankList.
                    get(position).getDesc()).
                    append(Utils.formatString(getActivity(), "  (@" +
                            mGankList.get(position).getWho() +
                            ")", R.style.AuthorTextAppearance));
            CharSequence gankText = builder.subSequence(0, builder.length());
            mDescTextView.setText(gankText);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Intent intent = WebActivity.newIntent(getActivity(),
                    mGankList.get(position).getUrl());
            startActivity(intent);
        }
    }

    private class GankAdapter extends RecyclerView.Adapter<GankViewHolder> {

        @Override
        public GankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.frg_gank_item,
                    parent, false);
            return new GankViewHolder(v);
        }

        @Override
        public void onBindViewHolder(GankViewHolder holder, int position) {
            holder.BindData(position);
        }

        @Override
        public int getItemCount() {
            return mGankList.size();
        }
    }

    //监听滑动到底
    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                int lastIndex = 0;
                lastIndex = mLayoutManager.findLastVisibleItemPosition();
                if (lastIndex + 1 == mGankList.size()) {
                    mCallback.startRefresh();
                    getGank(15, ++mPage);
                }

            }

        }
    };

    /**
     * 初始化界面和数据
     */
    private void initRecyclerView() {
        mPage = 1;
        HttpRequest.getInstance().getGank(new Subscriber<Gank>() {

            @Override
            public void onCompleted() {
                mLayoutManager = new LinearLayoutManager(getActivity());
                mGankRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new GankAdapter();
                mGankRecyclerView.setAdapter(mAdapter);
                mGankRecyclerView.addOnScrollListener(mScrollListener);
            }

            @Override
            public void onError(Throwable e) {
                unsubscribe();
                e.printStackTrace();
                handleCrash(e);
            }

            @Override
            public void onNext(Gank gank) {
                mGankList.add(gank);
            }
        }, mType, 15, mPage);
    }


    private void getGank(int count, int page) {
        Subscriber subscriber = new Subscriber<Gank>() {

            @Override
            public void onCompleted() {
                mAdapter.notifyDataSetChanged();
                mCallback.stopRefresh();
            }

            @Override
            public void onError(Throwable e) {
                unsubscribe();
                e.printStackTrace();
                handleCrash(e);
                mCallback.stopRefresh();
            }

            @Override
            public void onNext(Gank gank) {
                mGankList.add(gank);
            }
        };
        HttpRequest.getInstance().getGank(subscriber, mType, count, page);
    }

    /**
     * 暴露给Viewpager的更新方法
     */
    public void updateLatestData() {
        mGankList.clear();
        mPage = 1;
        getGank(15, 1);

    }


    private void handleCrash(Throwable e) {
        if (e instanceof UnknownHostException) {
            Snackbar.make(mGankRecyclerView, "网络连接错误", Snackbar.LENGTH_SHORT).show();
        }
        if (e instanceof SocketTimeoutException) {
            Snackbar.make(mGankRecyclerView, "请求超时", Snackbar.LENGTH_SHORT).show();
        }
    }



}