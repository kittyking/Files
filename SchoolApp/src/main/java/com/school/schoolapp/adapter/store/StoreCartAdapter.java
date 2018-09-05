package com.school.schoolapp.adapter.store;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.school.schoolapp.R;
import com.school.schoolapp.entity.store.StoreCartEntity;
import com.school.schoolapp.entity.store.StoreCartGoods;
import com.school.schoolapp.entity.store.StoreGoodsVO;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StoreCartAdapter extends BaseAdapter {

	 private LayoutInflater mInflater;
		
     //private List<E>
	 private Context mContext;
	 
	 private Handler handler;
	 private Handler cartHandler;
	 public StoreCartAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
			this.mContext = context;
	}	
		
	 
	public Handler getHandler() {
		return handler;
	}


	public void setHandler(Handler handler) {
		this.handler = handler;
	}


	public Handler getCartHandler() {
		return cartHandler;
	}


	public void setCartHandler(Handler cartHandler) {
		this.cartHandler = cartHandler;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(StoreCartEntity.getInstance().getCartGoods()!=null)
			return StoreCartEntity.getInstance().getCartGoods().size();
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
		convertView = mInflater.inflate(R.layout.adapter_cart_listviewcell, null);
		final int index = position;
		
		final StoreCartGoods good = StoreCartEntity.getInstance().getCartGoods().get(position);
		
		TextView goodsName = (TextView)convertView.findViewById(R.id.goodsName);
		goodsName.setText(StoreCartEntity.getInstance().getGoodsVO().get(position).getGoodsName());

		TextView goodsTitle = (TextView)convertView.findViewById(R.id.goodsTitle);
		goodsTitle.setText(StoreCartEntity.getInstance().getGoodsVO().get(position).getGoodsTitle());
		
		TextView marketPrice = (TextView)convertView.findViewById(R.id.marketPrice);
		marketPrice.setText(StoreCartEntity.getInstance().getGoodsVO().get(position).getMarketPrice());
		marketPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
		
		TextView shopPrice = (TextView)convertView.findViewById(R.id.shopPrice);
		shopPrice.setText(StoreCartEntity.getInstance().getGoodsVO().get(position).getShopPrice());
		
		final TextView number = (TextView)convertView.findViewById(R.id.number);
		number.setText(good.getNum()+"");
		
		final ImageButton cutBtn = (ImageButton)convertView.findViewById(R.id.cut);
		cutBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int cur = Integer.parseInt(number.getText().toString());
				if(cur>0){
					ImageButton cutGood = good.getCut();
					TextView numberGood = good.getNumber();
					
					int num =  cur - 1;
					number.setText(""+num);
					numberGood.setText(""+num);
					StoreCartEntity.getInstance().delGoods(good.getGoods_id(),StoreCartEntity.getInstance().getGoodsVO().get(index).getShopPrice(),StoreCartEntity.getInstance().getGoodsVO().get(index).getMarketPrice());
					handler.sendMessage(new Message());
					cartHandler.sendMessage(new Message());
					if(num ==0){
						cutBtn.setVisibility(View.INVISIBLE);
						cutGood.setVisibility(View.INVISIBLE);
						number.setAlpha(0);
						numberGood.setAlpha(0);
					}
				}
			}
		});
		
		ImageButton addBtn = (ImageButton)convertView.findViewById(R.id.add);
		addBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int stock = Integer.parseInt(StoreCartEntity.getInstance().getGoodsVO().get(index).getStock());
				int cur =Integer.parseInt(number.getText().toString()) ;
				
				ImageButton cutGood = good.getCut();
				TextView numberGood = good.getNumber();
				
				if(cur < stock){
					cur++;
					number.setText(""+cur);
					numberGood.setText(""+cur);
					cutGood.setVisibility(View.VISIBLE);
					cutBtn.setVisibility(View.VISIBLE);
					number.setAlpha(1);
					numberGood.setAlpha(1);
					StoreCartEntity.getInstance().addGoods(StoreCartEntity.getInstance().getGoodsVO().get(index), StoreCartEntity.getInstance().getGoodsVO().get(index).getGoods_id(),StoreCartEntity.getInstance().getGoodsVO().get(index).getShopPrice(),StoreCartEntity.getInstance().getGoodsVO().get(index).getMarketPrice(),cutGood,numberGood);
					handler.sendMessage(new Message());
					cartHandler.sendMessage(new Message());
				}else {
					Toast.makeText(mContext, "库存不够啦", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		

		//显示商品图片
		if(!ImageLoader.getInstance().isInited())
    	ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
		
		ImageView iv = (ImageView)convertView.findViewById(R.id.goodsImg);
        ImageLoader.getInstance().displayImage(StoreCartEntity.getInstance().getGoodsVO().get(position).getGoodsImg(), iv);
        
        
        if(position == StoreCartEntity.getInstance().getCartGoods().size()-1)
        	((TextView)convertView.findViewById(R.id.divider)).setAlpha(0);
		
		return convertView;
	}

}
