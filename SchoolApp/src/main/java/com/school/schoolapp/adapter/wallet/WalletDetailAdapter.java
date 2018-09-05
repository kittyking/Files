package com.school.schoolapp.adapter.wallet;

import java.util.ArrayList;
import java.util.List;

import com.school.schoolapp.R;
import com.school.schoolapp.classes.wallet.WalletDetailVO;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WalletDetailAdapter extends BaseAdapter {
	
	private List<WalletDetailVO> details;
	private Context context;
	
	public WalletDetailAdapter(Context context){
		this.context = context;
		details  = new ArrayList<>();
	}
	public void addDetail(List<WalletDetailVO> details){
		for(WalletDetailVO detail : details){
			this.details.add(detail);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return details.size();
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
		convertView=LayoutInflater.from(context).inflate(R.layout.adapter_wallet_detail, null);
		((TextView)convertView.findViewById(R.id.log)).setText(details.get(position).getLog());
		((TextView)convertView.findViewById(R.id.time)).setText(details.get(position).getTime());
		TextView money = (TextView)convertView.findViewById(R.id.money);
		if(details.get(position).getFlag() == 1){//负数
			money.setText(details.get(position).getMoney());
			money.setTextColor(Color.GREEN);
		}else{
			money.setText(details.get(position).getMoney());
			money.setTextColor(Color.RED);
		}
		return convertView;
	}

}
