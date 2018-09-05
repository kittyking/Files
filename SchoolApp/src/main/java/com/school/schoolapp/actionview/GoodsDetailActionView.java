package com.school.schoolapp.actionview;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.school.schoolapp.R;
import com.school.schoolapp.activity.store.StoreGoodsDetailActivity;
import com.school.schoolapp.activity.store.StoreGoodsDetailActivity.ImgPagerAdapter;
import com.school.schoolapp.entity.store.GoodDetailVO;
import com.school.schoolapp.entity.store.StoreCartEntity;
import com.school.schoolapp.entity.store.StoreGoodsVO;
import com.school.schoolapp.tool.imageloader.ImageLoaderTool;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsDetailActionView {

	private Context context;
	private Display display;
	private Dialog dialog;
	private StoreGoodsVO good;
	private String img;
	private ViewPager imgPager;
	private int currentPage= 0;
	private List<String> imgs;
	private int isShow =0;
	private Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			currentPage++;
			imgPager.setCurrentItem(currentPage);
			handler.sendEmptyMessageDelayed(0, 3000);
		};
	};
	private Handler cartaddHandler;
	private GoodDetailVO detail;
	public GoodsDetailActionView(Context context,StoreGoodsVO good,GoodDetailVO detail,Handler cartaddHandler){
		this.context = context;
		this.good = good;
		this.detail = detail;
		this.cartaddHandler = cartaddHandler;
		// 取得window对象
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		//得到窗口的尺寸对象
		display = windowManager.getDefaultDisplay();
	}
	
	public GoodsDetailActionView builder(){
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_goods_detail_actionsheet, null);
		// 设置Dialog最小宽度为屏幕宽度
		view.setMinimumWidth(display.getWidth());
		
		setupView(view);
		
		dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width =  display.getWidth();
		lp.x = 0;
		lp.y = 0;
		dialogWindow.setAttributes(lp);
		return this;
	}
	private TextView cartmid;
	private ImageView leftImage;
	private RelativeLayout addcartrl,addcutrl;
	private void setupView(View view){
		TextView goodsName = (TextView)view.findViewById(R.id.goodsName);
		goodsName.setText(good.getGoodsName());
		
		TextView goodsTitle= (TextView)view.findViewById(R.id.goodsTitle);
		goodsTitle.setText(good.getGoodsTitle());
		
		TextView shopPrice= (TextView)view.findViewById(R.id.shopPrice);
		shopPrice.setText("￥"+good.getShopPrice());
		
		TextView marketPrice= (TextView)view.findViewById(R.id.marketPrice);
		marketPrice.setText("￥"+good.getMarketPrice());
		marketPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
		
		cartmid = (TextView)view.findViewById(R.id.cartmid);
		addcutrl=(RelativeLayout)view.findViewById(R.id.addRelative);
		addcartrl=(RelativeLayout)view.findViewById(R.id.cartRelative);
		(((RelativeLayout)view.findViewById(R.id.cartRelative))).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addToCartAnimation();
			}
		}); 
		
		ImageButton cancelBtn = (ImageButton)view.findViewById(R.id.close);
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		
		int width = display.getWidth();
		img = good.getGoodsImg();
		imgs =detail.getImg();
		
		imgPager = (ViewPager)view.findViewById(R.id.imgPager);
		ViewGroup.LayoutParams params = imgPager.getLayoutParams();
		params.height = width;
		params.width = width;
		imgPager.setLayoutParams(params);
		imgPager.setAdapter(new ImgPagerAdapter());
		handler.sendEmptyMessageDelayed(0, 3000);
		
		setupAddCut(view);
	}
	
	
	public void show() {
		if(isShow==0){
			isShow=1;
			dialog.show();
			
		}
	}
	public void dismiss(){
		if(isShow==1){
			isShow=0;
			dialog.dismiss();
		}
	}
	
	public int isShowing(){
		return isShow;
	}
	
	private class ImgPagerAdapter extends PagerAdapter{
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
				iv = new ImageView(context);
		        ((ViewPager)container).addView(iv);
		        ImageLoaderTool.getInstance().displayImage(imgs.get(position%imgs.size()), iv, context);
			}
			
			return iv;
		}
	}
	
	private ImageButton cutBtn,addBtn;
	private TextView number;
	private void setupAddCut(View view){
		
		addBtn = (ImageButton)view.findViewById(R.id.addBtnDetail);
		cutBtn = (ImageButton)view.findViewById(R.id.cutBtnDetail);
		number = (TextView)view.findViewById(R.id.numberTVDetail);
		if(StoreCartEntity.getInstance().getCartGoods()!=null && StoreCartEntity.getInstance().getCartGoods().size()>0){
			for(int i=0;i<StoreCartEntity.getInstance().getCartGoods().size();i++){
				if(good.getGoods_id().equals(StoreCartEntity.getInstance().getCartGoods().get(i).getGoods_id())){
					number.setText(StoreCartEntity.getInstance().getCartGoods().get(i).getNum()+"");
					cutBtn.setVisibility(View.VISIBLE);
					number.setAlpha(1);
					cartmid.setVisibility(View.GONE);
					addcutrl.setAlpha(1);
				}
					
			}
		}
		
	}
	public void addToCartAnimation(){
		cartmid.setText("");
		ScaleAnimation animation = new ScaleAnimation(1f,0.1f,1f,1f,Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF,1f);
		animation.setDuration(500);
		animation.setFillAfter(true);
		cartmid.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
//				handler.sendMessage(new Message());
//				addBtnOpetion();
				addBtn.performClick();
				
				cartmid.setVisibility(View.GONE);
				
				ScaleAnimation sAnimation = new ScaleAnimation(0f,1f,1f,1f,Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF,1f);
				sAnimation.setDuration(500);
				sAnimation.setFillAfter(true);
				addcutrl.setAlpha(1);
				addcutrl.startAnimation(sAnimation);
			}
		});
	}
	
	public GoodsDetailActionView setAddBtnListener(OnClickListener listener){
		addBtn.setOnClickListener(listener);
		return this;
	}
	public GoodsDetailActionView setCutBtnListener(OnClickListener listener){
		cutBtn.setOnClickListener(listener);
		return this;
	}
	public void addBtnOpetion(){
		int stock = Integer.parseInt(good.getStock());
		int cur =Integer.parseInt(number.getText().toString()) ;
		if(cur < stock){
			cur++;
			number.setText(""+cur);
			cutBtn.setAlpha(1);
			cutBtn.setVisibility(View.VISIBLE);
			number.setAlpha(1);
		}else {
			Toast.makeText(context, "库存不够啦", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void cutBtnOpetion(){
		int cur = Integer.parseInt(number.getText().toString());
		if(cur>0){
			int num =  cur - 1;
			number.setText(""+num);
			
			if(num ==0){
//				cutBtn.setVisibility(View.INVISIBLE);
				number.setAlpha(0);
				cutToCartAnimation();
				
			}
		}
	}
	
	private void cutToCartAnimation(){
		ScaleAnimation animation = new ScaleAnimation(1f,0.1f,1f,1f,Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF,1f);
		animation.setDuration(500);
		animation.setFillAfter(true);
		addcutrl.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				ScaleAnimation sAnimation = new ScaleAnimation(0f,1f,1f,1f,Animation.RELATIVE_TO_SELF,1f,Animation.RELATIVE_TO_SELF,1f);
				sAnimation.setDuration(500);
				sAnimation.setFillAfter(true);
				cartmid.startAnimation(sAnimation);
				cartmid.setVisibility(View.VISIBLE);
				cartmid.setText("加入购物车");
			}
		});
		
	}
}
