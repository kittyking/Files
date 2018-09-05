package com.school.schoolapp.entity.store;

import android.widget.ImageButton;
import android.widget.TextView;

public class StoreCartGoods {

	private String goods_id;
	
	private int num;
	
	private ImageButton cut;
	
	private TextView number;

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public ImageButton getCut() {
		return cut;
	}

	public void setCut(ImageButton cut) {
		this.cut = cut;
	}

	public TextView getNumber() {
		return number;
	}

	public void setNumber(TextView number) {
		this.number = number;
	}
	
	
}
