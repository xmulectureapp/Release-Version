//用户向导
package com.lecture.lectureapp;

import java.util.ArrayList;

//import cn.buaa.myweixin.Whatsnew;
//import cn.buaa.myweixin.WhatsnewDoor;

import com.lecture.lectureapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Guide extends Activity implements OnPageChangeListener{

	private ArrayList<View> mPageViews;
    private LayoutInflater mInflater;
    private LinearLayout mGroups;
    private FrameLayout mMain;
    private ViewPager mViewPager;
    private ImageView[] mImageViews;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mInflater = LayoutInflater.from(this);
        mPageViews = new ArrayList<View>();
        mPageViews.add(mInflater.inflate(R.layout.guide_item0, null));
        mPageViews.add(mInflater.inflate(R.layout.guide_item1, null));
        mPageViews.add(mInflater.inflate(R.layout.guide_item2, null));
        mPageViews.add(mInflater.inflate(R.layout.guide_item3, null));
        mPageViews.add(mInflater.inflate(R.layout.guide_item4, null));
//        mPageViews.add(mInflater.inflate(R.layout.guide_item3, null));
        
        mMain = (FrameLayout) mInflater.inflate(R.layout.guide, null);
        mGroups = (LinearLayout) mMain.findViewById(R.id.viewGroup);
        mViewPager = (ViewPager) mMain.findViewById(R.id.guidePages);
        mImageViews = new ImageView[mPageViews.size()];
        
        for(int i=0; i<mPageViews.size(); i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new LayoutParams(20, 20));
            mImageViews[i] = iv;
            if(i == 0) {
                mImageViews[i].setBackgroundResource(R.drawable.guide_page_indicator_focused);
            } else {
                mImageViews[i].setBackgroundResource(R.drawable.guide_page_indicator);
            }
            mGroups.addView(mImageViews[i]);
        }
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOnPageChangeListener(this);
        setContentView(mMain);
    }
    
    private PagerAdapter mPageAdapter = new PagerAdapter() {
        
        @Override
        public void startUpdate(View arg0) {
            
        }
        
        @Override
        public Parcelable saveState() {
            return null;
        }
        
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            
        }
        
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager)arg0).addView(mPageViews.get(arg1));
            return mPageViews.get(arg1);
        }
        
        @Override
        public int getCount() {
            return mPageViews.size();
        }
        
        @Override
        public void finishUpdate(View arg0) {
            
        }
        
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager)arg0).removeView(mPageViews.get(arg1));
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset,
            int positionOffsetPixels) {
        
    }

    @Override
    public void onPageSelected(int position) {
        for(int i=0; i<mPageViews.size(); i++) {
            mImageViews[position].setBackgroundResource(R.drawable.guide_page_indicator_focused);
            if(position != i) {
                mImageViews[i].setBackgroundResource(R.drawable.guide_page_indicator);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        
    }
    public void startbutton(View v) {  
      	Intent intent = new Intent();
		intent.setClass(Guide.this,MainView.class);
		startActivity(intent);
		this.finish();
      }  
}
