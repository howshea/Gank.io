package com.howshea.gankio.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.howshea.gankio.entity.Meizi;
import com.howshea.gankio.R;
import com.howshea.gankio.ui.activity.ShowPictureActivity;
import com.howshea.gankio.ui.widget.RatioImageView;
import com.howshea.gankio.utils.Utils;
import com.howshea.gankio.net.HttpRequest;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;

/**
 * Created by haipo on 2016/10/11.
 * <p>
 * 未解决问题：
 * <p>
 * 没有网或者联网失败时没有相应的提示
 * <p>
 * 图片闪烁
 */

public class MeiziFragment extends Fragment {

    @BindView(R.id.meizi_recycler_view)
    RecyclerView mMeiziRecyclerView;
    @BindView(R.id.meizi_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    private Unbinder mUnbinder;
    private Subscriber<String> mSubscriber;
    private List<Meizi> mMeiziList = new ArrayList<>();
    private MeiziRecyclerViewAdapter mAdapter;
    private int mPage;
    private StaggeredGridLayoutManager mLayoutManager;
    private Callback mCallback;
    private boolean isLoading = false;

    public interface Callback {
        void hidefab();

        void showfab();
    }


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
        mCallback = (Callback) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frg_meizi, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initData();

        mRefreshLayout.setColorSchemeResources(R.color.AKABENI);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMeiziList.clear();
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


    private class MeiziHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private RatioImageView mGirlImageView;

        public MeiziHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mGirlImageView = (RatioImageView) itemView.findViewById(R.id.girl_image_view);
        }

        public void BindData(int position) {
            mGirlImageView.setRadio(mMeiziList.get(position).getRadio());
            Utils.imageLoader(MeiziFragment.this, mMeiziList.get(position).getUrl(),
                    mGirlImageView);

        }

        @Override
        public void onClick(View v) {
            Intent i = ShowPictureActivity.newIntent(getActivity(),
                    mMeiziList.get(getLayoutPosition()).getUrl(),
                    mMeiziList.get(getLayoutPosition()).getPublishedAt());
            startActivity(i);
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
            return mMeiziList.size();

        }


    }

    /**
     * 发起请求返回数据
     */
    private void latestMeizi(int count, int page) {

        Subscriber subscriber = new Subscriber<Meizi>() {

            @Override
            public void onCompleted() {
                mAdapter.notifyItemChanged(mMeiziList.size() - 10);
                mRefreshLayout.setRefreshing(false);
                isLoading = false;
            }

            @Override
            public void onError(Throwable e) {
                unsubscribe();
                e.printStackTrace();
                handleCrash(e);
                mRefreshLayout.setRefreshing(false);
                isLoading = false;
            }

            @Override
            public void onNext(Meizi meizi) {
                mMeiziList.add(meizi);
            }
        };
        HttpRequest.getInstance().getMeizi(subscriber, count, page, getActivity());
    }


    /**
     * 初始化首页数据
     */
    private void initData() {
        mPage = 1;
        HttpRequest.getInstance().getMeizi(new Subscriber<Meizi>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
                initRecyclerView();
                Snackbar.make(mRefreshLayout, "妹子加载完毕", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                unsubscribe();
                e.printStackTrace();
                handleCrash(e);
            }

            @Override
            public void onNext(Meizi m) {
                mMeiziList.add(m);
            }
        }, 10, mPage, getActivity());


    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            mLayoutManager.invalidateSpanAssignments();
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                mCallback.showfab();
                int lastIndex;
                int[] ints = mLayoutManager.findLastVisibleItemPositions(null);
                lastIndex = ints[0] > ints[1] ? ints[0] : ints[1];
                if (isLoading) {
                    mRefreshLayout.setRefreshing(true);
                }
                if (lastIndex >= mAdapter.getItemCount() - 10 && !isLoading) {
                    mRefreshLayout.setRefreshing(true);
                    latestMeizi(10, ++mPage);
                    isLoading = true;
                }

            } else if (newState == RecyclerView.SCROLL_STATE_SETTLING || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                //在滑动时隐藏FloatActionButton
                mCallback.hidefab();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastIndex;
            int[] ints = mLayoutManager.findLastVisibleItemPositions(null);
            lastIndex = ints[0] > ints[1] ? ints[0] : ints[1];
            if (dy > 0 && lastIndex >= mAdapter.getItemCount() - 10 && !isLoading) {
                latestMeizi(10, ++mPage);
                isLoading = true;
            }
        }
    };

    private void initRecyclerView() {
        mLayoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        mMeiziRecyclerView.setLayoutManager(mLayoutManager);
//        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mAdapter = new MeiziRecyclerViewAdapter();
        mMeiziRecyclerView.setAdapter(mAdapter);

        mMeiziRecyclerView.addOnScrollListener(mScrollListener);
    }


    private void handleCrash(Throwable e) {
        if (e instanceof UnknownHostException) {
            Snackbar.make(mRefreshLayout, "好像没有网呀", Snackbar.LENGTH_SHORT).show();
            mRefreshLayout.setRefreshing(false);

        }
        if (e instanceof SocketTimeoutException) {
            Snackbar.make(mRefreshLayout, "啊咧>_<,网路好像不太畅通呢", Snackbar.LENGTH_SHORT).show();
            mRefreshLayout.setRefreshing(false);
        }
    }
}
