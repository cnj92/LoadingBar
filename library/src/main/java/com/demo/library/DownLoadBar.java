package com.demo.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author wangzy
 * @desciption
 * @date 2015/10/23. 9:50
 */
public class DownLoadBar extends View {
    /**
     * 进度条画笔
     */
    private Paint mPaintProgress;
    /**
     * 进度条背景
     */
    private Paint mPaintProgressBg;
    /**
     * 进度条所在矩形
     */
    private RectF mRectF;
    //中心点坐标
    private int mCenterX, mCenterY;

    /**
     * 进度条颜色
     */
    private int mLoadingBarColor = 0xFF00BCD4;
    /**
     * 当前进度
     */
    private int mCurrentProgress = 0;
    /**
     * 进度最大值
     */
    private int maxProgress = 100;
    /**
     * 是否正在加载
     */
    private boolean isLoading = true;
    /**
     * 加载完成动画路径1
     */
    private Path mPath1;
    /**
     * 路径坐标
     */
    private float pathX, pathY;
    /**
     * 控件宽高
     */
    private int minSide;
    /**
     * 内边框
     */
    private static float padding = 20.0f;
    /**
     * 文字进度画笔
     */
    private Paint mPaintText;
    /**
     * 字体大小
     */
    private static final int FONT_SIZE = 40;
    /**
     * 动画延迟
     */
    private static int PROGRESS_DELAY = 5;
    /**
     * 点击监听器
     */
    private ClickListener listener;
    /**
     * 是否加载完成
     */
    private boolean isComplete = false;
    /**
     * 文字y坐标
     */
    private int textY;

    private Handler mHandlerSuccess;

    private Runnable mRunnableSuccess;

    public DownLoadBar(Context context) {
        super(context);
        init();
    }
    public DownLoadBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public DownLoadBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaintProgress = new Paint();
        mPaintProgress.setAntiAlias(true);
        mPaintProgress.setColor(mLoadingBarColor);
        mPaintProgress.setStyle(Paint.Style.STROKE);
        mPaintProgress.setStrokeWidth(12.0f);

        mPaintProgressBg = new Paint();
        mPaintProgressBg.setAntiAlias(true);
        mPaintProgressBg.setColor(Color.WHITE);
        mPaintProgressBg.setStyle(Paint.Style.STROKE);
        mPaintProgressBg.setStrokeWidth(13.0f);

        mPaintText = new Paint();
        mPaintText.setTextSize(FONT_SIZE);
        mPaintText.setColor(mLoadingBarColor);
        mPaintText.setTextAlign(Paint.Align.CENTER);

        mRectF = new RectF();

        mPath1 = new Path();

        mHandlerSuccess = new Handler();
        mRunnableSuccess = new Runnable() {
            @Override
            public void run() {
                if (pathX < mCenterX - 10) {
                    pathX += 5.5f;
                    pathY += 5.0f;
                    setPaint1LineTo(pathX, pathY);
                    mHandlerSuccess.postDelayed(mRunnableSuccess, PROGRESS_DELAY);
                } else if (pathX < minSide - mCenterX / 4 - padding - 5) {
                    pathX += 5.0f;
                    pathY -= 5.5f;
                    setPaint1LineTo(pathX, pathY);
                    mHandlerSuccess.postDelayed(mRunnableSuccess, PROGRESS_DELAY);
                }
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        minSide = width < height ? width : height;
        this.setMeasuredDimension(width, height);
        mCenterX = minSide / 2;
        mCenterY = minSide / 2;
        mRectF.set(padding, padding, minSide - padding, minSide - padding);
        Paint.FontMetricsInt fontMetrics = mPaintText.getFontMetricsInt();
        textY = (minSide - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRectF, 0, 360, false, mPaintProgressBg);
        if (isLoading) {
            if (mCurrentProgress < 100) {
                canvas.drawText(mCurrentProgress + "%", mCenterX, textY, mPaintText);
                canvas.drawArc(mRectF, 0, calculateProgress(), false, mPaintProgress);
            } else {
                canvas.drawArc(mRectF, 0, 360, false, mPaintProgress);
                canvas.drawPath(mPath1, mPaintProgress);
                success();
            }
        } else {
            canvas.drawArc(mRectF, 0, calculateProgress(), false, mPaintProgress);
            canvas.drawText("| |", mCenterX, textY, mPaintText);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                if (isComplete) {
                    return true;
                }
                if (listener != null) {
                    if (isLoading) {
                        listener.pause();
                    } else {
                        listener.restart();
                    }
                    isLoading = !isLoading;
                }
                postInvalidate();
                break;

        }
        return super.onTouchEvent(event);
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public void setmCurrentProgress(int mCurrentProgress) {
        this.mCurrentProgress = mCurrentProgress;
        postInvalidate();
    }

    private float calculateProgress() {
        return (360 * mCurrentProgress) / maxProgress;
    }

    private void setPaint1LineTo(float x, float y) {
        this.mPath1.lineTo(x, y);
        postInvalidate();
    }


    private void success() {
        mPath1.reset();
        isComplete = true;
        pathX = padding + mCenterX / 4;
        pathY = mCenterY;
        mPath1.moveTo(pathX, pathY);
        mHandlerSuccess.postDelayed(mRunnableSuccess, PROGRESS_DELAY);
    }
}
