package com.school.schoolapp.adapter.user;

import java.util.List;

import com.school.schoolapp.entity.user.UserSchoolVO;

import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserSchoolAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	
	private List<UserSchoolVO> schools;
	
	public UserSchoolAdapter(Context context,List<UserSchoolVO> schools){
		this.mInflater = LayoutInflater.from(context);
		this.schools = schools;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return schools.size();
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
		convertView = mInflater.inflate(R.layout.simple_list_item_1, null);
		TextView tv = (TextView)convertView.findViewById(R.id.text1);
		tv.setText(schools.get(position).getSchoolName());
		
		return convertView;
	}

}
