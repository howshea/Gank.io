package com.haipo.gankio.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haipo.gankio.R;
import com.haipo.gankio.UI.RatioImageView;
import com.haipo.gankio.net.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

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
    private List<String> pictureUrlList = new ArrayList<>();

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
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);


        //获取图片url列表
        requestMeizi();

        mGirlRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
        mGirlRecyclerView.setAdapter(new MeiziRecyclerViewAdapter());


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }


    private class MeiziHolder extends RecyclerView.ViewHolder {


        private final RatioImageView mGirlImageView;

        public MeiziHolder(View itemView) {
            super(itemView);
            mGirlImageView = (RatioImageView) itemView.findViewById(R.id.girl_image_view);
        }

        private void BindData() {
            mGirlImageView.setRadio(0.5f);
            mGirlImageView.setImageResource(R.mipmap.ic_launcher);
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
            holder.BindData();
        }

        @Override
        public int getItemCount() {
            return 10;
        }


    }

    /**
     * 发起请求返回数据
     */
    private void requestMeizi(){
        mSubscriber = new Subscriber<String>(){

            @Override
            public void onCompleted() {
                for (String s : pictureUrlList){
                    System.out.println(s);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Meizi","获取数据失败",e);
            }

            @Override
            public void onNext(String s) {
                pictureUrlList.add(s);
            }
        };
        HttpRequest.getInstance().getMeizi(mSubscriber,10,1);

    }

}
