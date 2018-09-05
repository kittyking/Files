package com.school.schoolapp.adapter.user;

import java.util.List;

import com.school.schoolapp.R;
import com.school.schoolapp.entity.user.UserCouponVO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserCouponAdapter extends BaseAdapter {

	private Context mContext;
	private List<UserCouponVO> coupons;
	
	public UserCouponAdapter(Context context,List<UserCouponVO> coupons){
		this.mContext = context;
		this.coupons = coupons;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return coupons.size();
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
		convertView = LayoutInflater.from(mContext).inflate(R.layout.cell_coupon_mine, null);
		UserCouponVO coupon = coupons.get(position);
		((TextView)convertView.findViewById(R.id.title)).setText(coupon.getTitle());
		((TextView)convertView.findViewById(R.id.summary)).setText(coupon.getSummary());
		((TextView)convertView.findViewById(R.id.price)).setText(coupon.getPrice());
		((TextView)convertView.findViewById(R.id.endtime)).setText("有效期至 "+coupon.getEndTime());
		return convertView;
	}

}
