package com.chl.stickynav.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.chl.stickynav.R;
import com.chl.stickynav.Util;

/**
 * Created by caihanlin on 17/2/27.
 */

public class StickyNavLayout2 extends LinearLayout implements NestedScrollingParent {

    private static final String TAG = "StickyNavLayout";

    private View mTop;
    private View mNav;
    private ViewPager mViewPager;

    private int mTopViewHeight;

    private OverScroller mScroller;
    private int mComTitleHeight;
    private View mComTitleLayout;

    public StickyNavLayout2(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        mScroller = new OverScroller(context);
        mComTitleHeight = (int) Util.dp2px(context, 50f);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mTop = findViewById(R.id.app_detail_topview);
        mNav = findViewById(R.id.app_detail_nav);
        mViewPager = (ViewPager) findViewById(R.id.app_detail_pager);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        //不限制顶部的高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.height = getMeasuredHeight() - mNav.getMeasuredHeight();
        //setMeasuredDimension(getMeasuredWidth(), mTop.getMeasuredHeight() + mNav.getMeasuredHeight() + mViewPager.getMeasuredHeight());

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTop.getMeasuredHeight();
    }


    public void fling(int velocityY)
    {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y)
    {
        if (y < 0)
        {
            y = 0;
        }
        if (y > mTopViewHeight - mComTitleHeight)
        {
            y = mTopViewHeight - mComTitleHeight;
        }
        if (y != getScrollY())
        {
            super.scrollTo(x, y);
        }
        updateTitleBackGround();
    }

    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset())
        {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    //onStartNestedScroll 返回true ，NestedScrollingChild 和 NestedScrollingParent 的关系才能有意义。
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes)
    {
        Log.e(TAG, "onStartNestedScroll");
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes)
    {
        Log.e(TAG, "onNestedScrollAccepted");
    }

    @Override
    public void onStopNestedScroll(View target)
    {
        Log.e(TAG, "onStopNestedScroll");
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed)
    {
        Log.e(TAG, "onNestedScroll");
    }

    ////子布局中分发过来的事件就可以在这里面进行处理。将自己消耗掉的位移加给consumed[0],consumed[1]就可以了。
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed)
    {
        boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight - mComTitleHeight;
        boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);
        Log.e(TAG, "===onNestedPreScroll hiddenTop " + hiddenTop + dy);
        Log.e(TAG, "===onNestedPreScroll showTop " + showTop);
        if (hiddenTop || showTop)
        {
            scrollBy(0, dy);
            consumed[1] = dy;//父布局中消耗的y方向上的位移
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed)
    {
        Log.e(TAG, "onNestedFling");
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY)
    {
        Log.e(TAG, "onNestedPreFling");
        //down - //up+
        if (getScrollY() >= mTopViewHeight - mComTitleHeight) return false;
        fling((int) velocityY);
        return true;
    }

    @Override
    public int getNestedScrollAxes()
    {
        Log.e(TAG, "getNestedScrollAxes");
        return 0;
    }

    public void setComTitleLayout(View comTitleLayout) {
        this.mComTitleLayout = comTitleLayout;
    }

    private void updateTitleBackGround() {
        if (mComTitleLayout != null) {
            float fraction = (float) (getScrollY() * 1.0 / (mTopViewHeight - mComTitleHeight));
            mComTitleLayout.setBackgroundColor(Color.argb((int) (fraction * 255), 239, 186, 206));
        }

    }

}
