package com.basic.gaugeprogress.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.GradientDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.basic.gaugeprogress.R;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

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
    private float mOriginX;
    private float mOriginY;
    private int mOlderProgress = 0;
    private boolean mIsRepeat = false;
    private int progressState = 0;
    private int mOriginProgress = 0;
    private int mCount=0;

    private static final int TOTAL_PROGRESS = 100;
    private static final int CIRCLE_ANGLE = 270;


    public GaugeProgressBar(Context context) {
        this(context, null);
    }

    public GaugeProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GaugeProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attrType = context.obtainStyledAttributes(attrs, R.styleable
                .custom_progress_attrs);
        mProgressWidth = attrType.getInt(1, 15);
        mProgressColor = attrType.getColor(2, Color.BLUE);
        mProgressTextColor = attrType.getColor(3, Color.WHITE);
        mProgressTextSize = attrType.getInt(4, 20);//这里应该是sp，将其转换为px
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
        float top = 0 + paddingTop + mProgressWidth;
        float bottom = top + temp;
        float left = 0 + paddingLeft + mProgressWidth;
        float right = left + temp - 2 * mProgressWidth;
        //求去圆心
        mOriginX = (left + right) / 2;
        mOriginY = (bottom + top) / 2;

        Paint paint = new Paint();
        paint.setColor(mProgressColor);
        paint.setAntiAlias(true);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(mProgressWidth);

        RectF oval = new RectF(left, top, right, bottom);
        canvas.drawArc(oval, 135, 270, false, paint); //顺时针方向开始画弧线，画环必须要加上 paint.setStyle(Style
        // .STROKE);
        //计算长度
        mOlderProgress = mProgress;
        float sweepAngle = ((float) mProgress / TOTAL_PROGRESS) * CIRCLE_ANGLE;
        int[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.RED};
        SweepGradient gradient = new SweepGradient(mOriginX, mOriginY, colors, null);
        paint.setShader(gradient);

        canvas.drawArc(oval, 135, sweepAngle, false, paint);
        if (mIsRepeat) {
            if (progressState == 1) {
                mProgress = mProgress + 1;
                if (mProgress <= mOriginProgress) {
                    invalidate();
                }  else {
                    mIsRepeat =false;
                }
            } else {
                mProgress = mProgress - 4;
                if (mProgress >= mOriginProgress) {
                    invalidate();
                }else {
                    mIsRepeat = false;
                }
            }

        }
    }


    private void initView() {
        mHeight = getHeight();
        mWidth = getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasure = measureSize(widthMeasureSpec);
        int heightMeasure = measureSize(heightMeasureSpec);
        setMeasuredDimension(widthMeasure, heightMeasure);
    }

    private int measureSize(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int result = getScreenWidth() / 2;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                if (result >= size) {
                    result = size;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
            default:
                break;
        }
        return result;
    }

    public synchronized void setProgress(int progress) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            throw new IllegalStateException("set progress must be called in UI thread");
        }
        if (progress < 0) {
            return;
        }
        if (progress > TOTAL_PROGRESS) {
            while (progress > TOTAL_PROGRESS) {
                progress = progress % TOTAL_PROGRESS;
            }
        }
        mOriginProgress = progress;
        int duration = Math.abs(progress - mOlderProgress);
        if (duration <= 2) {
            mProgress = progress;
            mIsRepeat = false;
        } else {
            mIsRepeat = true;
            if (progress > mOlderProgress) {//进度条加
                mProgress = mOlderProgress + 1;
                progressState = 1;
            } else {   //进度条减
                mProgress = mOlderProgress - 4;
                progressState = 2;
            }
        }
        invalidate();
    }

    private int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }


}
