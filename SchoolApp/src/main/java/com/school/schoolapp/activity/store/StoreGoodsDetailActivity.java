package com.school.schoolapp.activity.store;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.school.schoolapp.BaseActivity;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.R.menu;
import com.school.schoolapp.entity.store.StoreCartEntity;
import com.school.schoolapp.entity.store.StoreGoodsVO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StoreGoodsDetailActivity extends BaseActivity {
	
	private String img;

	private StoreGoodsVO good;
	private ViewPager imgPager;
	private int currentPage= 0;
	private Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			currentPage++;
			imgPager.setCurrentItem(currentPage);
			handler.sendEmptyMessageDelayed(0, 3000);
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_goods_detail);
		this.titleTextView.setText("商品详情");
		
		Intent intent = getIntent();
		good = (StoreGoodsVO)intent.getSerializableExtra("Good");
		
		TextView goodsName = (TextView)findViewById(R.id.goodsName);
		goodsName.setText(good.getGoodsName());
		
		TextView goodsTitle= (TextView)findViewById(R.id.goodsTitle);
		goodsTitle.setText(good.getGoodsTitle());
		
		TextView shopPrice= (TextView)findViewById(R.id.shopPrice);
		shopPrice.setText(good.getShopPrice());
		
		TextView marketPrice= (TextView)findViewById(R.id.marketPrice);
		marketPrice.setText(good.getMarketPrice());
		marketPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
		
		WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		
		img = good.getGoodsImg();
		imgs =img.split(",");
		
		imgPager = (ViewPager)findViewById(R.id.imgPager);
		ViewGroup.LayoutParams params = imgPager.getLayoutParams();
		params.height = width;
		params.width = width;
		imgPager.setLayoutParams(params);
		imgPager.setAdapter(new ImgPagerAdapter());
		handler.sendEmptyMessageDelayed(0, 3000);
		
		Button add = (Button)findViewById(R.id.addCart);
//		add.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(StoreCartEntity.getInstance().addGoods(good, good.getGoods_id(), good.getShopPrice(), good.getMarketPrice())){
//					Toast.makeText(StoreGoodsDetailActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
//				}
//					
//			}
//		});
		
		setupAddCut();
		
	}
	
	String[] imgs;
	ImageButton cutBtn,addBtn;
	TextView number;
	private void setupAddCut(){
		
		addBtn = (ImageButton)findViewById(R.id.addBtn);
		cutBtn = (ImageButton)findViewById(R.id.cutBtn);
		number = (TextView)findViewById(R.id.numberTV);
		if(StoreCartEntity.getInstance().getCartGoods()!=null && StoreCartEntity.getInstance().getCartGoods().size()>0){
			for(int i=0;i<StoreCartEntity.getInstance().getCartGoods().size();i++){
				if(good.getGoods_id().equals(StoreCartEntity.getInstance().getCartGoods().get(i).getGoods_id())){
					number.setText(StoreCartEntity.getInstance().getCartGoods().get(i).getNum()+"");
					cutBtn.setVisibility(View.VISIBLE);
					number.setAlpha(1);
				}
					
			}
		}
		
		
		cutBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int cur = Integer.parseInt(number.getText().toString());
				if(cur>0){
					
					int num =  cur - 1;
					number.setText(""+num);
					StoreCartEntity.getInstance().delGoods(good.getGoods_id(),good.getShopPrice(),good.getMarketPrice());
					handler.sendMessage(new Message());
					
					if(num ==0){
						cutBtn.setVisibility(View.INVISIBLE);
						number.setAlpha(0);
					}
				}
			}
		});
		addBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int stock = Integer.parseInt(good.getStock());
				int cur =Integer.parseInt(number.getText().toString()) ;
				if(cur < stock){
					cur++;
					number.setText(""+cur);
					cutBtn.setAlpha(1);
					cutBtn.setVisibility(View.VISIBLE);
					number.setAlpha(1);
					//StoreCartEntity.getInstance().addGoods(good, good.getGoods_id(),good.getShopPrice(),good.getMarketPrice());
					handler.sendMessage(new Message());
				}else {
					Toast.makeText(StoreGoodsDetailActivity.this, "库存不够啦", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}
	
	public class ImgPagerAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1000;
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			//super.destroyItem(container, position, object);
		}
		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			//显示商品图片
			ImageView iv =(ImageView) ((ViewPager)container).getChildAt(position);
			if(iv!=null)
				return iv;
			else{
				iv = new ImageView(StoreGoodsDetailActivity.this);
		        ((ViewPager)container).addView(iv);
		        ImageLoader.getInstance().displayImage(imgs[position%imgs.length], iv);
			}
			
			return iv;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store_goods_detail, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
