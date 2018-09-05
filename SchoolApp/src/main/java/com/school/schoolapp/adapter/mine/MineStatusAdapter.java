package com.school.schoolapp.adapter.mine;

import com.school.schoolapp.R;
import com.school.schoolapp.activity.mine.seller.MineServiceSoldActivity;

import android.content.Context;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MineStatusAdapter extends BaseAdapter {
	private Context context;
	private String[] status;
	
	public MineStatusAdapter(Context context,String[] status){
		this.context=context;
		this.status=status;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return status.length;
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
		convertView=LayoutInflater.from(context).inflate(R.layout.cell_service_status, null);
		TextView tx =(TextView)convertView.findViewById(R.id.title);
		tx.setText(status[position]);
		return convertView;
	}

}
