package com.haipo.gankio.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haipo.gankio.R;
import com.haipo.gankio.UI.RatioImageView;
import com.haipo.gankio.Utils.Utils;
import com.haipo.gankio.net.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;

/**
 * Created by haipo on 2016/10/11.
 *
 * 未解决问题：
 *
 * 没有网或者联网失败时没有相应的提示
 *
 * 图片闪烁
 *
 */

public class MeiziFragment extends Fragment {

    @BindView(R.id.meizi_recycler_view)
    RecyclerView mMeiziRecyclerView;
    @BindView(R.id.meizi_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    private Unbinder mUnbinder;
    private Subscriber<String> mSubscriber;
    private List<String> mPictureUrlList = new ArrayList<>();
    private MeiziRecyclerViewAdapter mAdapter;
    private int mPage;
    private StaggeredGridLayoutManager mLayoutManager;

    public static MeiziFragment newInstance() {
//        Bundle args = new Bundle();
        MeiziFragment fragment = new MeiziFragment();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frg_meizi, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initData();

        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPictureUrlList.clear();
                latestMeizi(10, 1);
                mPage = 1;
            }
        });


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }


    private class MeiziHolder extends RecyclerView.ViewHolder {


        private RatioImageView mGirlImageView;


        public MeiziHolder(View itemView) {
            super(itemView);
            mGirlImageView = (RatioImageView) itemView.findViewById(R.id.girl_image_view);
        }

        public void BindData(int position) {

            Utils.imageLoader(MeiziFragment.this, mPictureUrlList.get(position), mGirlImageView);
        }
    }


    private class MeiziRecyclerViewAdapter extends RecyclerView.Adapter<MeiziHolder> {


        @Override
        public MeiziHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.frg_meizi_item, parent, false);
            return new MeiziHolder(view);
        }

        @Override
        public void onBindViewHolder(MeiziHolder holder, int position) {
            holder.BindData(position);
        }

        @Override
        public int getItemCount() {
            return mPictureUrlList.size();
        }


    }

    /**
     * 发起请求返回数据
     */
    private void latestMeizi(int count, int page) {
        mSubscriber = new Subscriber<String>() {

            @Override
            public void onCompleted() {
                for (String s : mPictureUrlList) {
                    Log.i("meiziUrl", s);
                }
                mAdapter.notifyDataSetChanged();
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
//                mLayoutManager.scrollToPosition(mPictureUrlList.size() - 10);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("MeiziList", "获取数据失败", e);
            }

            @Override
            public void onNext(String s) {
                mPictureUrlList.add(s);
            }
        };

        HttpRequest.getInstance().getMeizi(mSubscriber, count, page);

    }


    /**
     * 初始化首页数据
     */
    private void initData() {
        mPage = 1;
        HttpRequest.getInstance().getMeizi(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                initRecyclerView();
                Snackbar.make(mRefreshLayout, "妹子加载完毕", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mPictureUrlList.add(s);
            }
        }, 10, mPage);


    }

    private void initRecyclerView() {
        mLayoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        mMeiziRecyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mAdapter = new MeiziRecyclerViewAdapter();
        mMeiziRecyclerView.setAdapter(mAdapter);

        mMeiziRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mLayoutManager.invalidateSpanAssignments();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastIndex = 0;
                    int[] ints = mLayoutManager.findLastVisibleItemPositions(null);
                    lastIndex = ints[0] > ints[1] ? ints[0] : ints[1];
                    if (lastIndex + 1 == mAdapter.getItemCount()) {
                        if (!mRefreshLayout.isRefreshing()) {
                            mRefreshLayout.setRefreshing(true);
                        }
                        latestMeizi(10, ++mPage);
                    }

                }

            }
        });
    }
}
