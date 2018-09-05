package com.school.schoolapp.adapter.moment;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MomentPagerAdapter extends PagerAdapter {
	
	private List<ImageView> listviews;
	public MomentPagerAdapter(List<ImageView> listviews){
		this.listviews=listviews;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listviews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager) container).removeView(listviews.get(position));
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		((ViewPager) container).addView(listviews.get(position));
		return listviews.get(position);
	}
	

}
