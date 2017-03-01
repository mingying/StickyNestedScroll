package com.chl.stickynav.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;

import com.chl.stickynav.MainPagerAdapter;
import com.chl.stickynav.R;
import com.chl.stickynav.Util;

/**
 * Created by caihanlin on 17/2/24.
 */

public class StickyNavLayout extends LinearLayout {

    private View mTopView;

    private View mNavView;

    private ViewPager mPager;

    private ViewGroup mInnerScrollView;

    private View mComTitleLayout;

    private int mTopViewHeight;

    private int mTouchSlop;

    private int mMaxVelocity, mMinVelocity;

    private float mLastY;

    private OverScroller mOverScroller;

    private VelocityTracker mVelocityTracker;

    private boolean isTopHidden;

    private boolean isInControl = false;

    private int mComTitleHeight;

    public StickyNavLayout(Context context) {
        super(context);
    }

    public StickyNavLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOrientation(VERTICAL);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();

        mOverScroller = new OverScroller(context);
        mVelocityTracker = VelocityTracker.obtain();

        mComTitleHeight = (int)Util.dp2px(context, 50f);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopView = findViewById(R.id.app_detail_topview);
        mNavView = findViewById(R.id.app_detail_nav);
        mPager = (ViewPager) findViewById(R.id.app_detail_pager);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams lp = mPager.getLayoutParams();
        lp.height = getMeasuredHeight() - mNavView.getMeasuredHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTopView.getMeasuredHeight();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action  = ev.getAction();
        float y = ev.getY();

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                getCurScrollView();
                if (mInnerScrollView instanceof ScrollView || mInnerScrollView instanceof NestedScrollView) {
                    if (!isInControl && isTopHidden && mInnerScrollView.getScrollY() == 0 && dy > 0) {
                        isInControl = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        MotionEvent ev2 = MotionEvent.obtain(ev);
                        dispatchTouchEvent(ev);
                        ev2.setAction(MotionEvent.ACTION_DOWN);
                        return dispatchTouchEvent(ev2);
                    }
                } else if (mInnerScrollView instanceof LinearLayout){
                    RecyclerView recyclerView = (RecyclerView) mInnerScrollView.findViewById(R.id.main_recycle_view);
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int firstPOs = layoutManager.findFirstVisibleItemPosition();
                    View firstView = null;
                    if (firstPOs == 0) {
                        firstView = layoutManager.findViewByPosition(0);
                    }
                    if (!isInControl && firstView != null && firstView.getTop() == 0 && isTopHidden && dy > 0) {
                        isInControl = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        MotionEvent ev2 = MotionEvent.obtain(ev);
                        dispatchTouchEvent(ev);
                        ev2.setAction(MotionEvent.ACTION_DOWN);
                        return dispatchTouchEvent(ev2);
                    }
                }

                break;

        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action  = ev.getAction();
        float y = ev.getY();

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                getCurScrollView();
                float dy = y - mLastY;
                if (Math.abs(dy) > mTouchSlop) {
                    if (mInnerScrollView instanceof ScrollView || mInnerScrollView instanceof NestedScrollView) {
                        if (!isTopHidden || (mInnerScrollView.getScrollY() == 0 && isTopHidden && dy > 0)) {

                            return true;
                        }
                    } else if (mInnerScrollView instanceof LinearLayout){
                        if (!isTopHidden) {
                            return true;
                        } else {
                            RecyclerView recyclerView = (RecyclerView) mInnerScrollView.findViewById(R.id.main_recycle_view);
                            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                            int firstPOs = layoutManager.findFirstVisibleItemPosition();
                            View firstView = null;
                            if (firstPOs == 0) {
                                firstView = layoutManager.findViewByPosition(0);
                            }
                            if (firstView != null && firstView.getTop() == 0 && isTopHidden && dy > 0) {
                                return true;
                            }
                        }

                    }
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                break;

            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();

                break;

        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        int action = event.getAction();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                mVelocityTracker.clear();
                mVelocityTracker.addMovement(event);
                mLastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                if (Math.abs(dy) > mTouchSlop) {
                    scrollBy(0, (int) (-dy));
                    mLastY = y;
                    // 如果topView隐藏，且上滑动时，则改变当前事件为ACTION_DOWN
                    if (isTopHidden && dy < 0) {
                        isInControl = false;
                        event.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(event);

                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMinVelocity);
                int velocityY = (int)mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinVelocity) {
                    fling(velocityY);
                }
                mVelocityTracker.clear();
                recycleVelocityTracker();
                break;

            case MotionEvent.ACTION_CANCEL:
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                recycleVelocityTracker();
                break;
        }

        return super.onTouchEvent(event);
    }

    private void fling(int velocatyY) {
        mOverScroller.fling(0, getScrollY(), 0, velocatyY, 0, 0, 0, mTopViewHeight- mComTitleHeight);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mOverScroller.computeScrollOffset()) {
            scrollTo(0, getScrollY());
            invalidate();
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }

        if (y > mTopViewHeight- mComTitleHeight) {
            y = mTopViewHeight- mComTitleHeight;
        }

        if (getScaleY() != y) {
            super.scrollTo(x, y);
        }
        updateTitleBackGround();
        isTopHidden = getScrollY() == mTopViewHeight - mComTitleHeight;
    }

    private void getCurScrollView() {
        int curIndex = mPager.getCurrentItem();
        MainPagerAdapter adapter = (MainPagerAdapter) mPager.getAdapter();
        View view = adapter.getItem(curIndex);
        if (view != null) {
            mInnerScrollView = (ViewGroup) view;
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
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
