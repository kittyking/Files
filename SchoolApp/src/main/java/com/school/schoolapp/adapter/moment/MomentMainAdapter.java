package com.school.schoolapp.adapter.moment;

import com.school.schoolapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MomentMainAdapter extends BaseAdapter {
	
	private Context context;
	
	public MomentMainAdapter(Context context){
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
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
		switch (position%2) {
		case 0:
			convertView = LayoutInflater.from(context).inflate(R.layout.cell_moment_main_service, null);
			break;
		case 1:
			convertView = LayoutInflater.from(context).inflate(R.layout.cell_moment_main_picture, null);
			break;

		default:
			break;
		}
		
		return convertView;
	}

}
