package com.school.schoolapp.adapter.user;

import java.util.List;

import com.school.schoolapp.R;
import com.school.schoolapp.entity.store.StoreGoodsVO;
import com.school.schoolapp.entity.user.UserFloorVO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserFloorChooseAdapter extends BaseAdapter {
	
    private LayoutInflater mInflater;
	
    private List<UserFloorVO> floors;
	
	public UserFloorChooseAdapter(Context context,List<UserFloorVO> floors){
		this.mInflater = LayoutInflater.from(context);
		this.floors = floors;
	}

	public void setAdatperList(List<UserFloorVO> floors){
		this.floors = floors;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(floors != null)
		    return floors.size();
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
		convertView = mInflater.inflate(R.layout.adapter_user_floor_choose_cell, null);
		if(floors != null){
			TextView floor = (TextView)convertView.findViewById(R.id.floorNum);
			floor.setText(floors.get(position).getFloorName());
			
			TextView status = (TextView)convertView.findViewById(R.id.floorStatus);
			if(floors.get(position).getOpened().equals("1"))
				status.setText("已开通");
			else
				status.setText("未开通");
		}
		return convertView;
	}

}
