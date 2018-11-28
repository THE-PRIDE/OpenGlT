package com.mengyu.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class LoadingView extends View {
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingView(Context context) {
        super(context);
    }

    private void initPaint() {
        mArcOffset = (getHeight() - getWidth()) / 2;//圆心偏移量

        mDrawCirclePaint.setColor(Color.parseColor("#AAAAAA"));
        mDrawCirclePaint.setStyle(Paint.Style.STROKE);//画笔属性是空心圆
        mDrawCirclePaint.setAntiAlias(true);//设置是否使用抗锯齿功能，抗锯齿功能会消耗较大资源，绘制图形的速度会减慢；
        mDrawCirclePaint.setStrokeWidth(3);//设置画笔粗细

        mDrawArcPaint.setColor(Color.parseColor("#AAAAAA"));
        mDrawArcPaint.setStyle(Paint.Style.STROKE);//画笔属性是空心圆
        mDrawArcPaint.setAntiAlias(true);//设置是否使用抗锯齿功能，抗锯齿功能会消耗较大资源，绘制图形的速度会减慢；
        mDrawArcPaint.setStrokeWidth(9);//设置画笔粗细

        mDrawArcText.setColor(Color.parseColor("#AAAAAA"));
        mDrawArcText.setStyle(Paint.Style.FILL);//画笔属性是实心圆
        mDrawArcText.setTextSize(40);//设置字体大小
        mDrawArcText.setStrokeWidth(3);//设置画笔粗细

    }

    private Paint mDrawCirclePaint = new Paint();
    private Paint mDrawArcPaint = new Paint();
    private Paint mDrawArcText = new Paint();

    private int mArcOffset = 0;//扇形圆心偏移量

    private int sweepAngle;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 4 + 5, mDrawCirclePaint);
        refreshData(canvas, sweepAngle);
    }

    private void refreshData(Canvas canvas, int sweepAngle) {
        RectF rect = new RectF();
        float last = (sweepAngle * 100 / 360);
        canvas.drawText(last + "%", getWidth() / 2 - 40, getHeight() / 2, mDrawArcText);
        rect.set(getWidth() / 4, getWidth() / 4 + mArcOffset, getWidth() * 3 / 4, getWidth() * 3 / 4 + mArcOffset);
        canvas.drawArc(rect, 270, sweepAngle, false, mDrawArcPaint);

    }

    public void dataNotify(int sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }
}
