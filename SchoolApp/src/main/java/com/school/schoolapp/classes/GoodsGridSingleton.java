package com.school.schoolapp.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.readystatesoftware.viewbadger.BadgeView;
import com.school.schoolapp.activity.billing.BillingPayActivity;

public class GoodsGridSingleton {

	public static GoodsGridSingleton goodsGridSingleton = null;
	
	public static GoodsGridSingleton getInstance(){
		if(goodsGridSingleton==null){
			goodsGridSingleton = new GoodsGridSingleton();
		}
		return goodsGridSingleton;
	}
	
//	public void displayGoods(Context context){
//		//获取屏幕宽度
//		WindowManager wm = context.getWindowManager();
//	    int width = wm.getDefaultDisplay().getWidth();
//	    int goodsWidth = width/4;
//			    
//		//显示商品
//		for(int i=0;i<4;i++){
//			//创建视图容器
//			RelativeLayout relativeLayout = new RelativeLayout(context);
//			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(goodsWidth, goodsWidth);
//			relativeLayout.setLayoutParams(params);
//			
//			RelativeLayout.LayoutParams ivparams = new RelativeLayout.LayoutParams(goodsWidth , goodsWidth);
//			ivparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			ivparams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//			
//			//创建商品图片
//			ImageView iv = new ImageView(context);
//			iv.setLayoutParams(ivparams);
//			ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
//			ImageLoader.getInstance().init(configuration);
//			//显示图片的配置  
//	        DisplayImageOptions options = new DisplayImageOptions.Builder()  
//	                .cacheInMemory(true)  
//	                .cacheOnDisk(true)  
//	                .bitmapConfig(Bitmap.Config.RGB_565)  
//	                .build();  
//	        ImageLoader.getInstance().displayImage(billingPay.getData().getGoods().get(0).getGoodsImg(), iv,options);
//			iv.setPadding(20, 20, 20, 20);
//	        
//			relativeLayout.addView(iv);
//			//创建价格
//			RelativeLayout.LayoutParams tvparams = new RelativeLayout.LayoutParams(goodsWidth-40, 50);
//			tvparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			tvparams.setMargins(20, goodsWidth-70, 0, goodsWidth-70);
//			TextView tv = new TextView(context);
//			tv.setAlpha((float)0.30);
//			tv.setLayoutParams(tvparams);
//			tv.setText(billingPay.getData().getGoods().get(0).getGoodsPrice());
//			tv.setTextColor(Color.WHITE);
//			tv.setBackgroundColor(Color.BLACK);
//			tv.setGravity(Gravity.CENTER);
//			relativeLayout.addView(tv);
//			
//			//加入列表
//			GridLayout.Spec rowSpec = GridLayout.spec(0);   // 行
//	        GridLayout.Spec columnSpec = GridLayout.spec(i);   //列
//	        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec,
//	                columnSpec);
//			gl.addView(relativeLayout,layoutParams);
//			
//			//添加提醒
//			BadgeView badgeView = new BadgeView(context, relativeLayout);  
//	        badgeView.setText("5");  
//	        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//	        badgeView.show(); 
//			
//		}
//	}
}
