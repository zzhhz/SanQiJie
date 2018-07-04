package com.chehubang.duolejie.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Beidouht on 2017/10/20.
 */

public class IconGridView extends GridView {

    public IconGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IconGridView(Context context) {
        super(context);
    }

    public IconGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
