package com.chl.stickynav;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.chl.stickynav.widget.SimpleViewPagerIndicator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private SimpleViewPagerIndicator mPagerIndicator;

    private RecyclerView mRecyclerView;

    private MainPagerAdapter mMainPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.app_detail_pager);
        mPagerIndicator = (SimpleViewPagerIndicator) findViewById(R.id.app_detail_nav);

        View scrollView  = LayoutInflater.from(this).inflate(R.layout.main_scroll_layout, null);
        View recycleView = LayoutInflater.from(this).inflate(R.layout.main_listview_layout, null);
        mRecyclerView = (RecyclerView) recycleView.findViewById(R.id.main_recycle_view);

        ArrayList<String> list = new ArrayList<>();
        list.add("JAVA");
        list.add("C");
        list.add("C++");
        list.add("C#");
        list.add("Python");
        list.add("PHP");
        list.add("GO");
        list.add("Linux");
        list.add("JAVA");
        list.add("C");
        list.add("C++");
        list.add("C#");
        list.add("Python");
        list.add("PHP");
        list.add("GO");
        list.add("Linux");
        list.add("JAVA");
        list.add("C");
        list.add("C++");
        list.add("C#");
        list.add("Python");
        list.add("PHP");
        list.add("GO");
        list.add("Linux");
        RecycleAdapter recycleAdapter = new RecycleAdapter(list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(recycleAdapter);


        ArrayList<String> titles = new ArrayList<>();
        titles.add("介绍");
        titles.add("评论");
        ArrayList<View> views = new ArrayList<>();
        views.add(scrollView);
        views.add(recycleView);

        mPagerIndicator.setTitles(titles);
        mMainPagerAdapter = new MainPagerAdapter(titles, views);
        mViewPager.setAdapter(mMainPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("chl", "position = " + position + " ; positionOffset = " + positionOffset);
                mPagerIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

}
