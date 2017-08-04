package com.rachel.androidtvanim.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.rachel.androidtvanim.R;


/**
 * Created by zhengshaorui on 2017/6/11.
 */

public class FlyBroadLayout extends View {
    private static final String TAG = "zsr";
    private FlyBroadLayout mView;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private int mRectOffsertWidth = 0; //有些.9需要设置边距，根据实际填写
    private int mAnimationTime = 200;
    private int mAnimationTimeWH = 10;// 边沿的时间
    public FlyBroadLayout(Context context) {
        this(context,null);
    }

    public FlyBroadLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        mView = this;



		mRectOffsertWidth = context.getResources().getDimensionPixelSize(R.dimen.x15);

    }

    public FlyBroadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setFocusView(View currentView,View oldView,float scale){
        if (currentView != null) {
            //放大与否

            if (scale <=1){
                scale = 1;
            }

            currentView.animate().scaleX(scale).scaleY(scale).setDuration(mAnimationTime).start();


            if (oldView != null) {
                oldView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(mAnimationTime).start();

            }

            Rect oldrect = findLocation(this); //当前view的rect
            Rect torect = findLocation(currentView); //获取到焦点view 的坐标
            float translateLeft = torect.left  - oldrect.left  - mRectOffsertWidth
                    - (currentView.getWidth()*scale - currentView.getWidth())/2; //左焦点
            float translateTop = torect.top - oldrect.top  - mRectOffsertWidth
                    - (currentView.getHeight()*scale - currentView.getHeight())/2;  //右焦点



            //当两者之间有大小之分，才去改变，减少绘制
            if (currentView.getWidth() - this.getWidth() != 0) {
                //移动时改变边框大小，让效果看起来更加自然
                layoutSizeChangeWidthAnim(this.getWidth(), currentView.getWidth() + 2 * mRectOffsertWidth
                    + (currentView.getWidth()*scale - currentView.getWidth()));
                layoutSizeChangeHeightAnim(this.getHeight(), currentView.getHeight() + 2 * mRectOffsertWidth
                       + (currentView.getHeight()*scale - currentView.getHeight()));
            }
            //移动函数
            flyRectAnim(translateLeft, translateTop);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "----------------gogo-------------------");
        findLocation(this);
    }

    /**
     * 调整移动时，边框宽度的变化
     * @param oldwidht
     * @param curwidht
     */
    private void layoutSizeChangeWidthAnim(int oldwidht,float curwidht){
        ValueAnimator animator = ValueAnimator.ofFloat(oldwidht,curwidht);
       // Log.d("zsr", "oldwidht, curwidht: " + (curwidht - oldwidht));
       // int time = (int) (1.0f * Math.abs(curwidht - oldwidht)/1.5);
        animator.setDuration(mAnimationTime - mAnimationTimeWH);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        float width = (float) Float.parseFloat( animation.getAnimatedValue().toString());
                        ViewGroup.LayoutParams params = mView.getLayoutParams();
                        params.width = (int) width;
                        mView.setLayoutParams(params);
                    }
                });

            }
        });
        animator.start();
    }

    /**
     * 调整移动时，边框高度的变化
     * @param oldHeight
     * @param curHeight
     */
    private void layoutSizeChangeHeightAnim(int oldHeight,float curHeight){
        ValueAnimator animator = ValueAnimator.ofFloat(oldHeight,curHeight);
      //  Log.d("zsr", "oldHeight, curHeight: " + (curHeight - oldHeight));
      //  int time = (int) (1.0f * Math.abs(curHeight - oldHeight)/1.5);
        animator.setDuration(mAnimationTime - mAnimationTimeWH);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        float height = (float) Float.parseFloat( animation.getAnimatedValue().toString());
                        ViewGroup.LayoutParams params = mView.getLayoutParams();
                        params.height = (int) height;
                        mView.setLayoutParams(params);
                        mView.setVisibility(VISIBLE);
                    }
                });
            }
        });

        animator.start();
    }

    /**
     * 移动函数
     * @param left
     * @param top
     */
    private void flyRectAnim(float left,float top){
        //非常简单，我们只要提供left 和 top 即可
        animate().translationX(left).translationY(top).setDuration(mAnimationTime).
                setInterpolator(new LinearInterpolator()).start();
    }


    /**
     * 获取当前view的坐标
     * @param view
     * @return
     */
    private Rect findLocation(View view){
        ViewGroup viewGroup = (ViewGroup) this.getParent();
        if (viewGroup != null && view != null) {
            Rect rect = new Rect();
            viewGroup.offsetDescendantRectToMyCoords(view, rect);

            return rect;
        }
        return null;
    }
    
    
   
}
