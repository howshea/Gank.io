package com.howshea.gankio.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.howshea.gankio.R;
import com.howshea.gankio.ui.activity.WebActivity;

import java.util.ArrayList;

/**
 * Created by haipo on 2016/10/25.
 */

public class HistoryFragment extends Fragment {

    private static final String ARG_HISTORY_DATE ="history_date";

    private ArrayList<String> mDateList;
    private RecyclerView mRecyclerView;

    public static HistoryFragment newInstance(ArrayList<String> dateList) {

        Bundle args = new Bundle();
         args.putStringArrayList(ARG_HISTORY_DATE,dateList);
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mDateList = getArguments().getStringArrayList(ARG_HISTORY_DATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_history, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.history_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new DateAdapter());
        return view;
    }

    private class DateAdapter extends RecyclerView.Adapter<DateHolder>{

        @Override
        public DateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity())
                    .inflate(R.layout.frg_gank_item, parent, false);
            return new DateHolder(v);
        }

        @Override
        public void onBindViewHolder(DateHolder holder, int position) {
            holder.BindData(position);
        }

        @Override
        public int getItemCount() {
            return mDateList.size();
        }
    }

    private class DateHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView mTextview;

        public DateHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextview = (TextView) itemView.findViewById(R.id.desc_text_view);
            mTextview.setTextSize(18);
        }

        public void BindData(int position){
            mTextview.setText(mDateList.get(position));
        }

        @Override
        public void onClick(View v) {
            String date = mDateList.get(getLayoutPosition()).replace("-", "/");
            String url  = "http://gank.io/" + date;
            Intent i = WebActivity.newIntent(getActivity(), url);
            startActivity(i);
        }
    }
}
