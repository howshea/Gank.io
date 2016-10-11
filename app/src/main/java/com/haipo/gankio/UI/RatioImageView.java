package com.haipo.gankio.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by haipo on 2016/10/11.
 */

public class RatioImageView extends ImageView {
    private float radio;

    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRadio(float radio) {
        this.radio = radio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width / radio);
        setMeasuredDimension(width,height);
    }
}
