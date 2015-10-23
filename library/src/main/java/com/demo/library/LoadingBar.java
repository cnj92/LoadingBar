package com.demo.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;


/**
 * @author wangzy
 * @desciption
 * @date 2015/10/22. 9:31
 */
public class LoadingBar extends View {

    /**
     * 进度条画笔
     */
    private Paint mPaintProgress;
    /**
     * 进度条背景
     */
    private Paint mPaintLoaded;
    /**
     * 进度条所在矩形
     */
    private RectF mRectF;
    //中心点坐标
    private int mCenterX, mCenterY;
    /**
     * 加载handler
     */
    private Handler mHandlerLoading;
    /**
     * 加载线程
     */
    private Runnable mRunnableLoading;
    /**
     * 进度条颜色
     */
    private int mLoadingBarColor = 0xFF00BCD4;
    /**
     * 尾部进度
     */
    private int mProgressFoot = 0;
    /**
     * 头部进度
     */
    private int mProgressHead = 0;
    /**
     * 进度最大值
     */
    private int maxProgress = 100;
    /**
     * 是否正在加载
     */
    private boolean isLoading;
    /**
     * 头部速度大于尾部
     */
    private boolean isChanse;
    /**
     * 旋转睡眠时间
     */
    private static int PROGRESS_DELAY = 5;


    /**
     * 加载完成动画路径1
     */
    private Path mPath1;
    /**
     * 加载完成动画路径2
     */
    private Path mPath2;
    /**
     * 路径坐标
     */
    private float pathX, pathY;
    /**
     * 第二条路径坐标
     */
    private float pathX2, pathY2;

    /**
     * 控件宽高
     */
    private int minSide;
    /**
     * 内边框
     */
    private static float padding = 20.0f;

    private Handler mHandlerFailed;

    private Runnable mRunnableFailed;

    private Handler mHandlerSuccess;

    private Runnable mRunnableSuccess;


    public LoadingBar(Context context) {
        super(context);
        init(context, null);
    }

    public LoadingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoadingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mHandlerLoading = new Handler();
        mRunnableLoading = new Runnable() {
            @Override
            public void run() {
                if (isLoading) {

                    if (mProgressHead > mProgressFoot + 95) {
                        isChanse = true;
                    }

                    if (mProgressHead < mProgressFoot + 5) {
                        isChanse = false;
                    }
                    if (isChanse) {
                        mProgressFoot += 2;
                        mProgressHead += 1;
                    } else {
                        mProgressFoot += 1;
                        mProgressHead += 2;
                    }
                    if (mProgressHead >= maxProgress) {
                        mProgressHead = 0;
                        mProgressFoot = mProgressFoot - maxProgress;
                    }
                    setProgressFoot(mProgressFoot);
                    setProgressHead(mProgressHead);
                    mHandlerLoading.postDelayed(mRunnableLoading, PROGRESS_DELAY);
                }
            }
        };
        mHandlerFailed = new Handler();

        mRunnableFailed = new Runnable() {
            @Override
            public void run() {
                if (pathX < minSide - mCenterX / 3 - padding) {
                    pathX += 5.0f;
                    pathY += 5.0f;
                    setPaint1LineTo(pathX, pathY);
                    mHandlerFailed.postDelayed(mRunnableFailed, PROGRESS_DELAY);
                } else if (pathX2 > mCenterX / 3 + padding) {
                    pathX2 -= 5.0f;
                    pathY2 += 5.0f;
                    setPaint2LineTo(pathX2, pathY2);
                    mHandlerFailed.postDelayed(mRunnableFailed, PROGRESS_DELAY);
                }
            }
        };
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

        mPaintProgress = new Paint();
        mPaintProgress.setAntiAlias(true);
        mPaintProgress.setColor(mLoadingBarColor);
        mPaintProgress.setStyle(Paint.Style.STROKE);
        mPaintProgress.setStrokeWidth(12.0f);
        mPaintLoaded = new Paint();
        mPaintLoaded.setAntiAlias(true);
        mPaintLoaded.setColor(Color.WHITE);
        mPaintLoaded.setStyle(Paint.Style.STROKE);
        mPaintLoaded.setStrokeWidth(13.0f);

        mRectF = new RectF();

        mPath1 = new Path();
        mPath2 = new Path();

        loading();
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isLoading) {
            canvas.drawArc(mRectF, 0, 360, false, mPaintLoaded);
            canvas.drawArc(mRectF, calculateProgressFoot(), calculateProgressHead(), false, mPaintProgress);
        } else {
            canvas.drawArc(mRectF, 0, 360, false, mPaintProgress);
        }

        canvas.drawPath(mPath1, mPaintProgress);
        canvas.drawPath(mPath2, mPaintProgress);
    }

    private int calculateProgressHead() {
        return (360 * (mProgressHead - mProgressFoot)) / maxProgress;
    }

    private int calculateProgressFoot() {
        return (360 * mProgressFoot) / maxProgress;
    }

    private void setProgressHead(int progress) {
        this.mProgressHead = progress;
        postInvalidate();
    }

    private void setProgressFoot(int progress) {
        this.mProgressFoot = progress;
        postInvalidate();
    }

    private void setPaint1LineTo(float x, float y) {
        this.mPath1.lineTo(x, y);
        postInvalidate();
    }

    private void setPaint2LineTo(float x, float y) {
        this.mPath2.lineTo(x, y);
        postInvalidate();
    }

    public void setProgressColor(int color) {
        this.mPaintProgress.setColor(color);
    }

    public void loading() {
        isLoading = true;
        isChanse = false;
        mHandlerLoading.removeCallbacksAndMessages(null);
        mHandlerLoading.postDelayed(mRunnableLoading, PROGRESS_DELAY);
    }

    /**
     * @param result
     */
    public void loadingComplete(boolean result) {
        isLoading = false;
        if (result) {
            success();
        } else {
            failed();
        }
    }

    /**
     * 失败动画
     */
    private void failed() {
        pathX = padding + mCenterX / 3;
        pathY = padding + mCenterX / 3;
        mPath1.moveTo(pathX, pathY);
        pathX2 = minSide - mCenterX / 3 - padding;
        pathY2 = padding + mCenterX / 3;
        mPath2.moveTo(pathX2, pathY2);
        mHandlerFailed.removeCallbacksAndMessages(null);
        mHandlerFailed.postDelayed(mRunnableFailed, PROGRESS_DELAY);
    }

    /**
     * 成功动画
     */
    private void success() {
        pathX = padding + mCenterX / 4;
        pathY = mCenterY;
        mPath1.moveTo(pathX, pathY);
        mHandlerSuccess.removeCallbacksAndMessages(null);
        mHandlerSuccess.postDelayed(mRunnableSuccess, PROGRESS_DELAY);
    }
}
