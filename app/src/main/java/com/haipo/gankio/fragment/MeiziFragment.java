package com.haipo.gankio.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.haipo.gankio.R;
import com.haipo.gankio.UI.RatioImageView;
import com.haipo.gankio.net.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;

/**
 * Created by haipo on 2016/10/11.
 */

public class MeiziFragment extends Fragment {

    @BindView(R.id.girl_recycler_view)
    RecyclerView mGirlRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private Unbinder mUnbinder;
    private Subscriber<String> mSubscriber;
    private List<String> mPictureUrlList = new ArrayList<>();
    private AppCompatActivity mContext;
    private MeiziRecyclerViewAdapter mAdapter;
    ;

    public static MeiziFragment newInstance() {
        Bundle args = new Bundle();
        MeiziFragment fragment = new MeiziFragment();
        fragment.setArguments(args);
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
        //初始化toolbar
        mContext = (AppCompatActivity) getActivity();

        mContext.setSupportActionBar(mToolbar);


        initData();


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
            Glide.with(mContext)
                    .load(mPictureUrlList.get(position))
                    .asBitmap()
                    .into(mGirlImageView);
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
    private void requestMeizi() {
        mSubscriber = new Subscriber<String>() {

            @Override
            public void onCompleted() {
                for (String s : mPictureUrlList) {
                    Log.i("meiziUrl", s);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Meizi", "获取数据失败", e);
            }

            @Override
            public void onNext(String s) {
                mPictureUrlList.add(s);
            }
        };

        HttpRequest.getInstance().getMeizi(mSubscriber, 10, 1);

    }


    private void initData(){


        HttpRequest.getInstance().getMeizi(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                initRecyclerView();
                Snackbar.make(mToolbar,"妹子加载完毕",Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mPictureUrlList.add(s);
            }
        }, 10, 1);


    }

    private void initRecyclerView(){
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        mGirlRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mAdapter = new MeiziRecyclerViewAdapter();
        mGirlRecyclerView.setAdapter(mAdapter);

        mGirlRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager.invalidateSpanAssignments();
            }
        });
    }
}
