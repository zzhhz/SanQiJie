package common.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import beidouht.common.R;


/**
 * Created by Beidouht on 2017/4/6.
 */

public class RedTipTextView extends android.support.v7.widget.AppCompatTextView {
    public static final int RED_TIP_INVISIBLE = 0;
    public static final int RED_TIP_VISIBLE = 1;
    public static final int RED_TIP_GONE = 2;
    private int tipVisibility = 0;

    public RedTipTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(null);
    }

    public RedTipTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(attrs);
    }

    public RedTipTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        if(attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.RedTipTextView);
            tipVisibility = array.getInt(R.styleable.RedTipTextView_redTipsVisibility, 0);
            array.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if(tipVisibility == 1) {
            int width = getWidth();
            int paddingRight = getPaddingRight();
            Paint paint = new Paint();
           // paint.setColor(Color.RED);
            paint.setColor(getResources().getColor(R.color.red_dot));
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(width - 15, getHeight()/2-5, 10, paint);
        }
    }

    public void setVisibilityCount(int visibility) {
        tipVisibility = visibility;
        invalidate();
    }
}
