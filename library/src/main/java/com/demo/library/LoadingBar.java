package com.demo.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * @author wangzy
 * @desciption
 * @date 2015/10/22. 9:31
 */
public class LoadingBar extends View {


    private final Context context;
    /**
     * 进度条画板
     */
    private Paint mPaintProgress;
    /**
     * 画完一次后的
     */
    private Paint mPaintLoaded;
    /**
     * 进度条
     */
    private RectF mRectF;
    /**
     * 加载结果画板
     */
    private Paint mPaintResult;
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
     * 加载结果监听器
     */
    private ResultListener mListener;
    /**
     * 进度条颜色
     */
    private int mLoadingBarColor = 0xff0097A7;
    /**
     * 当前进度
     */
    private int mCurrentProgress = 0;
    /**
     * 进度最大值
     */
    private int maxProgress = 10;
    /**
     * 是否正在加载
     */
    private boolean isLoading;
    /**
     * 第一次旋转
     */
    private boolean isFirst;
    /**
     * 旋转睡眠时间
     */
    private static int PROGRESS_DELAY = 100;
    /**
     * 进度条半径
     */
    private float mRadius;

    public LoadingBar(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public LoadingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    public LoadingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.loadingbar);
        mLoadingBarColor = a.getColor(R.styleable.loadingbar_progressColor, mLoadingBarColor);
        a.recycle();

        mHandlerLoading = new Handler();
        mRunnableLoading = new Runnable() {
            @Override
            public void run() {
                if (isLoading) {

                    if (mCurrentProgress > maxProgress) {
                        mCurrentProgress = 0;
                        isFirst = !isFirst;
                    }
                    mCurrentProgress++;
                    setProgress(mCurrentProgress);
                    mHandlerLoading.postDelayed(mRunnableLoading, PROGRESS_DELAY);
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
        mPaintResult = new Paint();
        mPaintResult.setAntiAlias(true);
        mPaintResult.setColor(Color.WHITE);

        mRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int minSide = (width + height) / 2;
        this.setMeasuredDimension(width, height);
        mCenterX = minSide / 2;
        mCenterY = minSide / 2;

        mRectF.set(20.0f, 20.0f, minSide - 20.0f, minSide - 20.f);
        mRadius = mCenterX * 0.8f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirst) {
            canvas.drawArc(mRectF, -90, 360, false, mPaintLoaded);
            canvas.drawArc(mRectF, -90, calculateLoadedProgress(), false, mPaintProgress);

        } else {
            canvas.drawArc(mRectF, -90, 360, false, mPaintProgress);
            canvas.drawArc(mRectF, -90, calculateLoadedProgress(), false, mPaintLoaded);
        }

    }

    private int calculateLoadedProgress() {
        return (360 * mCurrentProgress) / maxProgress;
    }

    public void setProgress(int progress) {
        this.mCurrentProgress = progress;
        postInvalidate();
    }

    public void loading() {
        isLoading = true;
        isFirst = true;
        mHandlerLoading.removeCallbacksAndMessages(null);
        mHandlerLoading.postDelayed(mRunnableLoading, PROGRESS_DELAY);
    }
}
