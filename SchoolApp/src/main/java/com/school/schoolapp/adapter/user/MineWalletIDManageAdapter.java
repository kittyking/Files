package com.school.schoolapp.adapter.user;

import com.school.schoolapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MineWalletIDManageAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	
	public MineWalletIDManageAdapter(Context context){
		this.mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
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
		convertView = mInflater.inflate(R.layout.adapter_wallet_id_manage_cell, null);
		
		return convertView;
	}

}
