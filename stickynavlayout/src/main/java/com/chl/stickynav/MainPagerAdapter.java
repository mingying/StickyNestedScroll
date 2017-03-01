package com.chl.stickynav;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by caihanlin on 17/2/24.
 */

public class MainPagerAdapter extends PagerAdapter {

    private ArrayList<String> mTitles;

    private ArrayList<View> mViews;

    public MainPagerAdapter(ArrayList<String> titles, ArrayList<View> views) {
        mTitles = titles;
        mViews = views;
    }

    @Override
    public int getCount() {
        return mTitles == null ? 0 : mTitles.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    public View getItem(int pos){
        if (mViews!=null && pos < getCount()){
            return mViews.get(pos);
        }
        return null;
    }
}
