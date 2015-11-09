package com.basic.gaugeprogress.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.basic.gaugeprogress.R;

import java.util.ArrayList;

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

    private static final int TOTAL_PROGRESS = 100;
    private static final int CIRCLE_ANGLE = 360;


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
        canvas.drawArc(oval, 135, 270, false, paint); //顺时针方向开始画，以3点钟方向作为起点.
        //计算长度

        float sweepAngle = ((float)mProgress / TOTAL_PROGRESS) * CIRCLE_ANGLE;

        if(sweepAngle < 90 && sweepAngle > 0){
              paint.setColor(Color.GREEN);
        } else {
            ArrayList<Integer> list = new ArrayList<>();
            if(sweepAngle <= 180){
                list.add(Color.GREEN);
                list.add(Color.YELLOW);
            }  else {
                list.add(Color.GREEN);
                list.add(Color.YELLOW);
                list.add(Color.RED);
            }
            int[] colors = new int[list.size()];
            for (int i = 0; i< list.size();i++){
                 colors[i] = list.get(i);
            }
            SweepGradient gradient = new SweepGradient(0, 0, colors, null);
            paint.setShader(gradient);
        }

        canvas.drawArc(oval, 135, sweepAngle, false, paint);
    }

    private void initView() {
        mHeight = getHeight();
        mWidth = getWidth();
    }

    public void setProgress(int progress) {
        if (progress < 0) {
            return;
        }
        if (progress > TOTAL_PROGRESS) {
            while (progress > TOTAL_PROGRESS) {
                progress = progress % TOTAL_PROGRESS;
            }
        }

        mProgress = progress;
    }

}
