package com.school.schoolapp.adapter.store;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.school.schoolapp.R;
import com.school.schoolapp.actionview.GoodsDetailActionView;
import com.school.schoolapp.activity.store.StoreGoodsDetailActivity;
import com.school.schoolapp.callback.store.GoodDetailCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.entity.store.GoodDetailVO;
import com.school.schoolapp.entity.store.StoreCartEntity;
import com.school.schoolapp.entity.store.StoreGoodsVO;
import com.school.schoolapp.tool.animationtool.AnimationTool;
import com.school.schoolapp.tool.imageloader.ImageLoaderTool;

import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StoreGoodsBuyAdapter extends BaseAdapter {
	
    private LayoutInflater mInflater;
	
    private List<StoreGoodsVO> goods;
	
    private Handler handler;
    
    private Context mContext;
    
    private Handler animationHandler;
    private GoodsDetailActionView detail;
    private int curpage=0;
    
    private int displayway=0;//判断列表显示还是块显示 0=列表 1=块
    
	public StoreGoodsBuyAdapter(Context context,List<StoreGoodsVO> goods,int displayway){
    	this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.goods = goods;
		this.displayway = displayway;
		if(goods ==null)
			this.goods = new ArrayList<StoreGoodsVO>();
	}
	public void setHandler(Handler handler){
		this.handler = handler;
	}
	public void setAnimationHandler(Handler animationHandler){
		this.animationHandler = animationHandler;
	}
	
	public int getCurpage() {
		return curpage;
	}
	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}
	public void add(List<StoreGoodsVO> goodVOs) {
		// TODO Auto-generated method stub
		
		for(StoreGoodsVO good:goodVOs){
			goods.add(good);
		}

	}
	public void clearGoods(){
		this.goods = new ArrayList<StoreGoodsVO>();
	}
	public List<StoreGoodsVO> getGoods(){
		return goods;
	}

	public int getGoodSize(){
		return goods.size();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(goods.size()>0){
			//判断显示方式
			if(displayway==0){
				return goods.size();
			}else{
				if(goods.size()%3==0)
					return goods.size()/3;
				return goods.size()/3+1;
			}
		    
		}
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
		
		//判断显示方式
		if(displayway==0){
			convertView = mInflater.inflate(R.layout.adapter_goods_listviewcell, null);
			setListDis(position, convertView);
		}else{
			convertView = mInflater.inflate(R.layout.cell_goods_three, null);
			setGridDis(position, convertView);
		}
		
		return convertView;
	}
	
	
	private List<ImageButton> adds=new ArrayList<>(),cuts=new ArrayList<>();
	private List<TextView> numbers =new ArrayList<>();
	
	private void setListDis(final int position,View convertView){
		final int index = position;
		if(goods!=null && goods.size()>0){
			final ImageButton cutBtn = (ImageButton)convertView.findViewById(R.id.cutBtn);
			final TextView number = (TextView)convertView.findViewById(R.id.numberTV);
			TextView goodsName = (TextView)convertView.findViewById(R.id.goodsName);
			goodsName.setText(goods.get(position).getGoodsName());
			goodsName.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					getDetail(position,number,cutBtn);
				}
			});
			TextView goodsTitle = (TextView)convertView.findViewById(R.id.goodsTitle);
			goodsTitle.setText(goods.get(position).getGoodsTitle());
			
			TextView marketPrice = (TextView)convertView.findViewById(R.id.marketPrice);
			marketPrice.setText("￥"+goods.get(position).getMarketPrice());
			marketPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
			
			TextView shopPrice = (TextView)convertView.findViewById(R.id.shopPrice);
			shopPrice.setText("￥"+goods.get(position).getShopPrice());
			
			if(StoreCartEntity.getInstance().getCartGoods()!=null && StoreCartEntity.getInstance().getCartGoods().size()>0){
				for(int i=0;i<StoreCartEntity.getInstance().getCartGoods().size();i++){
					if(goods.get(position).getGoods_id().equals(StoreCartEntity.getInstance().getCartGoods().get(i).getGoods_id())){
						number.setText(StoreCartEntity.getInstance().getCartGoods().get(i).getNum()+"");
						cutBtn.setVisibility(View.VISIBLE);
						number.setAlpha(1);
					}
						
				}
			}
			//判断库存是否为0
			if(goods.get(position).getStock().equals("0")){
				((ImageView)convertView.findViewById(R.id.goodsout)).setVisibility(View.VISIBLE);
				return ;
			}
			
			
			cutBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int cur = Integer.parseInt(number.getText().toString());
					if(cur>0){
						
						int num =  cur - 1;
						number.setText(""+num);
						StoreCartEntity.getInstance().delGoods(goods.get(index).getGoods_id(),goods.get(index).getShopPrice(),goods.get(index).getMarketPrice());
						handler.sendMessage(new Message());
						
						if(num ==0){
							cutBtn.setVisibility(View.INVISIBLE);
							number.setAlpha(0);
						}
					}
				}
			});
			
			ImageButton addBtn = (ImageButton)convertView.findViewById(R.id.addBtn);
			addBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int stock = Integer.parseInt(goods.get(index).getStock());
					int cur =Integer.parseInt(number.getText().toString()) ;
					if(cur < stock){
						cur++;
						number.setText(""+cur);
						cutBtn.setAlpha(1);
						cutBtn.setVisibility(View.VISIBLE);
						number.setAlpha(1);
						StoreCartEntity.getInstance().addGoods(goods.get(index), goods.get(index).getGoods_id(),goods.get(index).getShopPrice(),goods.get(index).getMarketPrice(),cutBtn,number);
						handler.sendMessage(new Message());
						startAnimation(v);
					}else {
						ToastTool.showWithMessage("库存不够啦", mContext);
					}
				}
			});
			
			//显示商品图片
			if(!ImageLoader.getInstance().isInited())
		    	ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
			
			ImageView iv = (ImageView)convertView.findViewById(R.id.goodsImg);
	        ImageLoader.getInstance().displayImage(goods.get(position).getGoodsImg(), iv);
	        iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					getDetail(position,number,cutBtn);
				}
			});
	        
			//显示倒计时，接口返回值没给。
	        adds.add(addBtn);
	        cuts.add(cutBtn);
	        numbers.add(number);
	        
		}
	}
	
	private void startAnimation(View view){
		int[] location = new  int[2] ;
		view.getLocationOnScreen(location);
		AnimationTool.getInstance().setStartX(location[0]);
		AnimationTool.getInstance().setStartY(location[1]);
		animationHandler.sendEmptyMessage(0);
	}

	public List<ImageButton> getAddBtn(){
		return adds;
	}
	public List<ImageButton> getCutBtn(){
		return cuts;
	}
	public List<TextView> getNumberTextView(){
		return numbers;
	}
	private int[] nameids = {R.id.goodsName1,R.id.goodsName2,R.id.goodsName3};
	private int[] titles = {R.id.goodsTitle1,R.id.goodsTitle2,R.id.goodsTitle3};
	private int[] marketPriceids = {R.id.marketPrice1,R.id.marketPrice2,R.id.marketPrice3};
	private int[] shopPriceids={R.id.shopPrice1,R.id.shopPrice2,R.id.shopPrice3};
	private int[] goodsOuts={R.id.goodsout1,R.id.goodsout2,R.id.goodsout3};
	private int[] cutids={R.id.cutBtn1,R.id.cutBtn2,R.id.cutBtn3};
	private int[] addids={R.id.addBtn1,R.id.addBtn2,R.id.addBtn3};
	private int[] numberids={R.id.numberTV1,R.id.numberTV2,R.id.numberTV3};
	private int[] imgids={R.id.goodsImg1,R.id.goodsImg2,R.id.goodsImg3};
	private int[] relativeids={R.id.good1,R.id.good2,R.id.good3};
	
 	private void setGridDis(final int position,View convertView){
		if(goods!=null && goods.size()>0){
			
			
			for(int temp=0;temp<3;temp++){
				final int curItme = position*3+temp;
						
				if(curItme>=goods.size()){
					if(temp==1){
						((RelativeLayout)convertView.findViewById(R.id.good2)).setAlpha(0);
						((RelativeLayout)convertView.findViewById(R.id.good3)).setAlpha(0);
					}else{
						((RelativeLayout)convertView.findViewById(R.id.good3)).setAlpha(0);
					}
					return;
				}
				RelativeLayout rl = (RelativeLayout)convertView.findViewById(relativeids[temp]);

				final ImageButton cutBtn = (ImageButton)convertView.findViewById(cutids[temp]);
				final TextView number = (TextView)convertView.findViewById(numberids[temp]);
				
				TextView goodsName = (TextView)convertView.findViewById(nameids[temp]);
				goodsName.setText(goods.get(curItme).getGoodsName());
				goodsName.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getDetail(curItme,number,cutBtn);
					}
				});
				
				TextView goodsTitle = (TextView)convertView.findViewById(titles[temp]);
				goodsTitle.setText(goods.get(curItme).getGoodsTitle());
				
				TextView marketPrice = (TextView)convertView.findViewById(marketPriceids[temp]);
				marketPrice.setText("￥"+goods.get(curItme).getMarketPrice());
				marketPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
				
				TextView shopPrice = (TextView)convertView.findViewById(shopPriceids[temp]);
				shopPrice.setText("￥"+goods.get(curItme).getShopPrice());
				
				
				if(StoreCartEntity.getInstance().getCartGoods()!=null && StoreCartEntity.getInstance().getCartGoods().size()>0){
					for(int i=0;i<StoreCartEntity.getInstance().getCartGoods().size();i++){
						if(goods.get(curItme).getGoods_id().equals(StoreCartEntity.getInstance().getCartGoods().get(i).getGoods_id())){
							number.setText(StoreCartEntity.getInstance().getCartGoods().get(i).getNum()+"");
							cutBtn.setVisibility(View.VISIBLE);
							number.setAlpha(1);
						}
							
					}
				}
				
				
				if(goods.get(curItme).getStock().equals("0")){
					((ImageView)convertView.findViewById(goodsOuts[temp])).setVisibility(View.VISIBLE);
				}
				
				
				cutBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int cur = Integer.parseInt(number.getText().toString());
						if(cur>0){
							
							int num =  cur - 1;
							number.setText(""+num);
							StoreCartEntity.getInstance().delGoods(goods.get(curItme).getGoods_id(),goods.get(curItme).getShopPrice(),goods.get(curItme).getMarketPrice());
							handler.sendMessage(new Message());
							
							if(num ==0){
								cutBtn.setVisibility(View.INVISIBLE);
								number.setAlpha(0);
							}
						}
					}
				});
				
				final ImageButton addBtn = (ImageButton)convertView.findViewById(addids[temp]);
				//判断是否关闭
				
				addBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int stock = Integer.parseInt(goods.get(position).getStock());
						int cur =Integer.parseInt(number.getText().toString()) ;
						if(cur < stock){
							cur++;
							number.setText(""+cur);
							cutBtn.setAlpha(1);
							cutBtn.setVisibility(View.VISIBLE);
							number.setAlpha(1);
							StoreCartEntity.getInstance().addGoods(goods.get(curItme), goods.get(curItme).getGoods_id(),goods.get(curItme).getShopPrice(),goods.get(curItme).getMarketPrice(),cutBtn,number);
							handler.sendMessage(new Message());
							startAnimation(v);
						}else {
							ToastTool.showWithMessage("库存不够啦", mContext);
						}
						
					}
				});
				
				//显示商品图片
				ImageView iv = (ImageView)convertView.findViewById(imgids[temp]);
		        ImageLoaderTool.getInstance().displayImage(goods.get(curItme).getGoodsImg(), iv,mContext);
		        iv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getDetail(curItme,number,cutBtn);
					}
				});
		        
				//显示倒计时，接口返回值没给。
		        adds.add(addBtn);
		        cuts.add(cutBtn);
		        numbers.add(number);
		        
		        rl.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//getDetail(curItme,number,cutBtn);
					}
				});
			}
		}
	}
 	private void getDetail(final int curItme,final TextView number,final ImageButton cutBtn){
 		//首先获取商品详情
		String url =mContext.getString(R.string.base_url)+mContext.getString(R.string.store_get_good_detail);
		RequestParams params = new RequestParams();
		String s =  LocalSharedPreferenceSingleton.getInstance().getUserInfo(mContext).getTicket();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(mContext).getTicket());
		params.put("gid", goods.get(curItme).getGoods_id());
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				GoodDetailCallback detailcallback = new Gson().fromJson(new String(arg2), GoodDetailCallback.class);
				if(detailcallback.getResult().equals("1")){
					showDetail(curItme,detailcallback.getData(),number,cutBtn);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
 	}
 	
 	private void showDetail(final int position,GoodDetailVO detailVO,final TextView number,final ImageButton cutBtn){
 		//获取详情成功，打开详情页面
		if(detail!=null&&detail.isShowing()==1)
			return;
		
		detail = new GoodsDetailActionView(mContext,goods.get(position),detailVO,handler).builder();
		detail.setAddBtnListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 添加按钮按下效果
				int stock = Integer.parseInt(goods.get(position).getStock());
				int cur =Integer.parseInt(number.getText().toString()) ;
				if(cur < stock){
					cur++;
					number.setText(""+cur);
					cutBtn.setAlpha(1);
					cutBtn.setVisibility(View.VISIBLE);
					number.setAlpha(1);
					StoreCartEntity.getInstance().addGoods(goods.get(position), goods.get(position).getGoods_id(),goods.get(position).getShopPrice(),goods.get(position).getMarketPrice(),cutBtn,number);
					handler.sendMessage(new Message());
				}else {
					ToastTool.showWithMessage("库存不够啦", mContext);
				}
				detail.addBtnOpetion();
			}
			
		}).setCutBtnListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int cur = Integer.parseInt(number.getText().toString());
				if(cur>0){
					
					int num =  cur - 1;
					number.setText(""+num);
					StoreCartEntity.getInstance().delGoods(goods.get(position).getGoods_id(),goods.get(position).getShopPrice(),goods.get(position).getMarketPrice());
					handler.sendMessage(new Message());
					
					if(num ==0){
						cutBtn.setVisibility(View.INVISIBLE);
						number.setAlpha(0);
					}
				}
				detail.cutBtnOpetion();
				
			}
		});
		
		detail.show();
 	}
}
