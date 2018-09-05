package com.school.schoolapp.adapter.technical;

import java.util.List;

import com.school.schoolapp.R;
import com.school.schoolapp.entity.technical.SkillContractEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SkillContractAdapter extends BaseAdapter {
	
	private List<SkillContractEntity> contracts;
	private Context mContext;
	
	public SkillContractAdapter(Context context){
		mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contracts.size();
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
		convertView = LayoutInflater.from(mContext).inflate(R.layout.cell_skill_contract, null);
		return convertView;
	}

}
