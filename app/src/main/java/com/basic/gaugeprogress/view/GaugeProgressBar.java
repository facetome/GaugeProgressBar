package com.basic.gaugeprogress.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basic.gaugeprogress.R;

/**
 * 仪表盘进度条.
 */
public class GaugeProgressBar extends View {
    private int mWidth;
    private int mHeight;
    private boolean mIsInit = false;
    private int mProgressWidth;
    private int mProgressColor;
    private int mProgressTextColor;
    private int mProgressTextSize;
    private int mProgress;


    public GaugeProgressBar(Context context) {
        super(context);
    }

    public GaugeProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GaugeProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attrType = context.obtainStyledAttributes(attrs, R.styleable
                .custom_progress_attrs);
        mProgressWidth = attrType.getInt(R.attr.progress_width, 15);
        mProgressColor = attrType.getColor(R.attr.progress_color, Color.BLUE);
        mProgressTextColor = attrType.getColor(R.attr.progress_text_color, Color.WHITE);
        mProgressTextSize = attrType.getInt(R.attr.pregress_text_size, 20);//这里应该是sp，将其转换为px
        attrType.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mIsInit) {
            initView();
            mIsInit = true;
        }

        int temp = Math.min(mWidth, mHeight);
        int paddingTop = Math.abs((mHeight - temp) / 2);
        int paddingLeft = Math.abs((mWidth - temp) / 2);
        float top = 0 + paddingTop;
        float buttom = top + temp;
        float left = 0 + paddingLeft;
        float right = left + paddingLeft;

        Paint paint = new Paint();
        paint.setColor(mProgressColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mProgressWidth);

        RectF oval = new RectF(left, top, right, buttom);
        canvas.drawArc(oval, 135, 45, false, paint); //顺时针方向开始画，以3点钟方向作为起点.
    }

    private void initView() {
        mHeight = getHeight();
        mWidth = getWidth();
    }

}
