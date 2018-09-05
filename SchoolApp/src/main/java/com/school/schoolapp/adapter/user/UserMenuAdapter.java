package com.school.schoolapp.adapter.user;

import java.util.List;

import com.school.schoolapp.R;
import com.school.schoolapp.entity.user.UserMenuEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserMenuAdapter extends BaseAdapter {
	
	LayoutInflater mInflater;
	
	List<UserMenuEntity> menus; 

	public UserMenuAdapter(Context context,List<UserMenuEntity> menus){
		mInflater = LayoutInflater.from(context);
		this.menus = menus;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return menus.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = mInflater.inflate(R.layout.adapter_menu_user, null);
		TextView tv = (TextView)convertView.findViewById(R.id.titleTextView);
		tv.setText(menus.get(position).getTitleStr());
		ImageView iv = (ImageView)convertView.findViewById(R.id.iconImageView);
		iv.setImageResource(menus.get(position).getImageSrc());
		return convertView;
	}

}
