package com.school.schoolapp;

import java.util.ArrayList;
import java.util.List;

import com.school.schoolapp.activity.home.HomeMainActivity;
import com.school.schoolapp.activity.login.UserFastLoginActivity;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class GuideScreenActivity extends Activity {
	private ViewPager guideVP;
	private List<ImageView> imageViews = new ArrayList<>();
	private int[] images = {R.drawable.background_guide01,R.drawable.background_guide02,R.drawable.background_guide03};
	private Button enterBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_screen);

		enterBtn =(Button)findViewById(R.id.enterBtn);
		guideVP = (ViewPager)findViewById(R.id.guide);
		for(int i=0;i<images.length;i++){
			ImageView image = new ImageView(this);
			image.setImageDrawable(getResources().getDrawable(images[i]));
			image.setScaleType(ScaleType.FIT_XY);
			imageViews.add(image);
		}
		
		guideVP.setAdapter(new GuidePagerAdapter());
		guideVP.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if(arg0==images.length-1)
					enterBtn.setAlpha(1);
				else {
					enterBtn.setAlpha(0);
				}
				switch (arg0) {
				case 0:
					((RadioButton)findViewById(R.id.dot1)).setChecked(true);
					break;
				case 1:
					((RadioButton)findViewById(R.id.dot2)).setChecked(true);
					break;
				case 2:
					((RadioButton)findViewById(R.id.dot3)).setChecked(true);
					break;

				default:
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});

	}
	
	public void enterPressed(View v){
		LocalSharedPreferenceSingleton.getInstance().setShowedGuide(this);
		
		Intent intent = new Intent(this,UserFastLoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	private class GuidePagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return images.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager)container).removeView(imageViews.get(position));
		}
		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			((ViewPager)container).addView(imageViews.get(position));
			return imageViews.get(position);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guide_screen, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
