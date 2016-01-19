package com.basic.gaugeprogress.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.GradientDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.basic.gaugeprogress.R;

import java.util.ArrayList;
import java.util.List;
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
    private float mProgress;
    private float mOriginX;
    private float mOriginY;
    private float mOlderProgress = 0;
    private boolean mIsRepeat = false;
    private float mOriginProgress = 0;
    private float mDuration = 0;

    private static final int TOTAL_PROGRESS = 100;
    private static final int CIRCLE_ANGLE = 270;
    private List<Integer> mProgressList;


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
        drawCanvas(canvas);
        invalidateView();
    }

    private void drawCanvas(Canvas canvas) {
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

        RectF oval = new RectF(left, top, right, bottom);
        Paint paint = new Paint();
        paint.setColor(mProgressColor);
        paint.setAntiAlias(true);
        paint.setStyle(Style.STROKE);
        paint.setStrokeCap(Cap.ROUND);
        paint.setStrokeWidth(mProgressWidth);
        canvas.drawArc(oval, 135, 270, false, paint); //顺时针方向开始画弧线，画环必须要加上 paint.setStyle(Style
        // .STROKE);
        //计算长度
        float sweepAngle = mProgress / TOTAL_PROGRESS * CIRCLE_ANGLE;
        int[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.RED};
        SweepGradient gradient = new SweepGradient(mOriginX, mOriginY, colors, null);
        paint.setShader(gradient);
        canvas.drawArc(oval, 135, sweepAngle, false, paint);
    }

    private void invalidateView() {
        if (mProgressList != null && mProgressList.size() > 0) {
            mOriginProgress = mProgressList.get(0);
            //比较上一次setProgress的值和此次设置的值的大小
            float duration = Math.abs(mOriginProgress - mOlderProgress);
            if (duration > 0) {
                if (duration <= 2) {//此时情况下进行平稳的加减
                    mProgress = mProgressList.get(0);
                    mOlderProgress = mProgress;
                    mIsRepeat = false;
                    mProgressList.remove(0);
                } else {
                    mIsRepeat = true;
                    if (mOriginProgress >= mOlderProgress) { //此时表示为大范围内的进度增加
                        mDuration  = 5  ;
                    } else {  //此时表示为大范围内的进度减小
                        mDuration = -5 ;
                    }
                }
            }

            if (mIsRepeat) {
                mProgress = mProgress + mDuration;
                Log.d("wangliangsen", "mProgress >>>>>>>>>>>>" + mProgress);
                if (mDuration > 0) {
                    if (mProgress >= mOriginProgress) {
                        mOlderProgress = mProgress;
                        mProgressList.remove(0);
                        mIsRepeat = false;
                    }
                } else {
                    if (mProgress <= mOriginProgress) {
                        mOlderProgress = mProgress;
                        mProgressList.remove(0);
                        mIsRepeat = false;
                    }
                }
                invalidate();
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

    /**
     * 进度条设置进度参数
     * @progress must not lg 100， if the progress lg the 100,the view will  draw the
     * progress repatly from 0 to 100.
     * @param progress progress
     */
    public synchronized void setProgress(int progress) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            throw new IllegalStateException("set progress must be called in UI thread");
        }
        if (progress < 0) {
            return;
        }
        if (progress > TOTAL_PROGRESS) {
        while (progress > TOTAL_PROGRESS ) {
            if(progress % TOTAL_PROGRESS == 0){
                progress = 100;
            }else {
                progress = progress % TOTAL_PROGRESS;
            }

        }
    }
    if (mProgressList == null) {
        mProgressList = new ArrayList<>();
    }
    mProgressList.add(new Integer(progress));
    invalidate(); //调用刷新ondraw并不是立即执行，而是要过一段时间后，所以这将是一个异步刷新的过程
    }

    private int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }


}
