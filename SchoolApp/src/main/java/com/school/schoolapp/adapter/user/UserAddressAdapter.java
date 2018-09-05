package com.school.schoolapp.adapter.user;

import java.util.List;

import com.school.schoolapp.R;
import com.school.schoolapp.activity.billing.AddressAddActivity;
import com.school.schoolapp.activity.billing.AddressEditActivity;
import com.school.schoolapp.entity.user.UserAdressVO;
import com.school.schoolapp.entity.user.UserFloorVO;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class UserAddressAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
		
    private List<UserAdressVO> address;
    
    private Context mContext;
	
	public UserAddressAdapter(Context context,List<UserAdressVO> address){
		this.mInflater = LayoutInflater.from(context);
		this.address = address;
		this.mContext = context;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return address.size();
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
		convertView = mInflater.inflate(R.layout.adapter_address_cell, null);
		
		TextView nameTV= (TextView)convertView.findViewById(R.id.nameTV);
		nameTV.setText(address.get(position).getName());
		
		TextView phoneTV= (TextView)convertView.findViewById(R.id.phoneTV);
		phoneTV.setText(address.get(position).getMobile());
		
		TextView addressTV= (TextView)convertView.findViewById(R.id.addressTV);
		addressTV.setText(address.get(position).getSchoolName()+address.get(position).getFloorName()+address.get(position).getAddress());
		
		final int index = position;
		ImageButton editBtn = (ImageButton)convertView.findViewById(R.id.editBtn);
		editBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,AddressAddActivity.class);
				intent.putExtra("AddressID", address.get(index).getAddress_id());
				intent.putExtra("Flag", "1");//1代表修改
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

}
