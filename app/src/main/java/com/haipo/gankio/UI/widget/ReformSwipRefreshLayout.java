package com.haipo.gankio.UI.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by haipo on 2016/10/22.
 * 解决viewPager的滑动被拦截
 */

public class ReformSwipRefreshLayout extends SwipeRefreshLayout {
    private float startY;
    private float startX;
    // 记录viewPager是否拖拽的标记
    private boolean mIsDraggingFlag;
    private final int mTouchSlop;

    public ReformSwipRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                startX = ev.getX();
                // 初始化标记
                mIsDraggingFlag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果viewpager正在拖拽中，那么不拦截
                if(mIsDraggingFlag) {
                    return false;
                }
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = endY - startY;
                // 如果X轴位移大于Y轴位移或者Y轴位移为负数时，事件交给子View处理
                if(distanceX > mTouchSlop && distanceX > distanceY) {
                    mIsDraggingFlag = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
                mIsDraggingFlag = false;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

}
