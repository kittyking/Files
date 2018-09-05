package com.school.schoolapp.fragment.main;

import java.lang.reflect.Field;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.school.schoolapp.R;
import com.school.schoolapp.R.id;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.StoreGoodActivity;
import com.school.schoolapp.activity.login.UserCityActivity;
import com.school.schoolapp.activity.login.UserLoginActivity;
import com.school.schoolapp.activity.login.UserSchoolActivity;
import com.school.schoolapp.activity.mine.work.MineWorkBuildingChooseActivity;
import com.school.schoolapp.adapter.store.StoreListAdapter;
import com.school.schoolapp.callback.store.StoreMainCallback;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCallback;
import com.school.schoolapp.entitys.UserVO;
import com.school.schoolapp.fragment.billing.MineBillingCustomActivity;
import com.school.schoolapp.tool.imageloader.ImageLoaderTool;
import com.school.schoolapp.tool.viewpagerscroll.FixedSpeedScroller;
import com.school.schoolapp.view.refreshlist.RListView;
import com.school.schoolapp.view.refreshlist.RListView.OnRefreshListener;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link StoreMainFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link StoreMainFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class StoreMainFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment StoreMainFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static StoreMainFragment newInstance(String param1, String param2) {
		StoreMainFragment fragment = new StoreMainFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public StoreMainFragment() {
		// Required empty public constructor
	}

	
	private RListView storeListView;
	
	/** 
     * ViewPager 
     */  
    private ViewPager viewPager;  
      
    /** 
     * 装点点的ImageView数组 
     */  
    private ImageView[] tips;  
      
    /** 
     * 装ImageView数组 
     */  
    private ImageView[] mImageViews;  
      
    /** 
     * 图片资源id 
     */  
    private int[] imgIdArray ;  
    
    private int currentItme = 0;
    
    private View viewself;
    
    private String floorid;

	private Button location;
	
	private int noticePlayDelay = 5000;

	private FixedSpeedScroller mScroller;
    private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		//让ViewPager滑到下一页
    		if(store.getData().getAds()!=null && store.getData().getAds().size()>0){
        		viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
        		mScroller.setmDuration(1* 1000);
        		//延时，循环调用handler
        		handler.sendEmptyMessageDelayed(0, noticePlayDelay);
    		}
    	}
    };
    
    private String schoolName;
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		if(LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity())==null){

			Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(getActivity(),UserLoginActivity.class));
		}
		
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		
	}
	private View headerV;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		String floorname = "";
		final View v =  inflater.inflate(R.layout.fragment_store_main, container, false);
		if(!LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity()).getData().getFloorID().equals("0")){
			floorid = LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity()).getData().getFloorID();
			floorname =LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity()).getData().getFloorName();
		}
		if(floorid==null){
			startActivity(new Intent(getActivity(), UserCityActivity.class));
		}
		setupListView(v);

		storeListView = (RListView)v.findViewById(R.id.storeListView);
		storeListView.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				setupListView(v);
				storeListView.onRefreshComplete();
			}
		});
		
		location = (Button)v.findViewById(R.id.location);
		schoolName = LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity()).getData().getSchoolName();
		if(schoolName!=null && !schoolName.equals(""))
			location.setText(schoolName+floorname);
		location.setText(schoolName+ floorname);
		location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent= new Intent(getActivity(),MineWorkBuildingChooseActivity.class);
				intent.putExtra("from", "workapply");
				startActivityForResult(intent, 103);
			}
		});
		
		

		headerV = View.inflate(getActivity(), R.layout.view_store_headerview, null);
		((RelativeLayout)headerV.findViewById(R.id.noticeRL)).setVisibility(View.GONE);
        viewPager = (ViewPager)headerV.findViewById(R.id.adViewpager); 
        LinearLayout.LayoutParams paramas = ( LinearLayout.LayoutParams)viewPager.getLayoutParams();
        WindowManager wm = (WindowManager)getActivity().getSystemService(getActivity().WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        paramas.height =width/3;
        paramas.width=width;
        viewPager.setLayoutParams(paramas);

		storeListView.addHeaderView(headerV,null,true);
        try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			mScroller = new FixedSpeedScroller(viewPager.getContext(),new AccelerateInterpolator());
			mField.set(viewPager, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return v;
	}
	public class MyAdapter extends PagerAdapter{  
		  
        @Override  
        public int getCount() {  
            return 100;  
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  
  
        @Override  
        public void destroyItem(View container, int position, Object object) {  
            ((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);  
              
        }  
  
        /** 
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键 
         */  
        @Override  
        public Object instantiateItem(View container, int position) { 
        	ImageView imageView = new ImageView(getActivity());
        	imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setBackground(null);
        	String url = getString(R.string.base_url) + store.getData().getAds().get(position % mImageViews.length).getAdImgsrc();
            ImageLoaderTool.getInstance().displayImage(url, imageView, getActivity());
            
            ((ViewPager)container).addView(imageView);
            return imageView;
        } 
    }  
	
	 
	    
	    
	private StoreMainCallback store;
	    
	public void setupListView(final View v){
		
		String url = getString(R.string.base_url)+getString(R.string.store_get_shop_list);
		RequestParams params = new RequestParams();
		
		final UserCallback user = LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity());
		params.put("ticket", user.getTicket());
		params.put("floorid",floorid);
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url,params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				Gson gson = new Gson();
				store = new StoreMainCallback();
				store = gson.fromJson(new String(arg2), StoreMainCallback.class);
				
				if(!store.getResult().equals("1")){
					Toast.makeText(getActivity(), store.getMsg(), Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(store.getData().getAds().size()>0){
					//加入广告滚动
					mImageViews = new ImageView[store.getData().getAds().size()];
					for(int i=0; i <store.getData().getAds().size();i++){
						//将图片装载到数组中  
						ImageView imageView = new ImageView(getActivity()); 
						imageView.setScaleType(ScaleType.FIT_XY);
						imageView.setBackground(null);
						
						mImageViews[i] = imageView;
				        
					}
					viewPager.setAdapter(new MyAdapter());
					handler.removeMessages(0);
			        handler.sendEmptyMessageDelayed(0, noticePlayDelay);
					
				}
				
				
				//设置店铺列表
				if(store.getData().getShops().size()>=0){
					StoreListAdapter adapter =new StoreListAdapter(getActivity(),store.getData().getShops());
					storeListView.setAdapter(adapter);
					
					storeListView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							String url = getString(R.string.base_url)+getString(R.string.store_bind_shop);
							RequestParams params = new RequestParams();
							params.put("ticket", user.getTicket());
							params.put("shopid", store.getData().getShops().get(position-2).getShopID());
							AsyncHttpClient client = new AsyncHttpClient();
							client.post(url,params, new AsyncHttpResponseHandler() {
								
								@Override
								public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
									// TODO Auto-generated method stub
									Gson gson =new Gson();
									BaseCallback callback  = new BaseCallback();
									callback = gson.fromJson(new String(arg2), BaseCallback.class);
									if(callback.getResult().equals("1")){
										Intent intent = new Intent();
										intent.setClass(getActivity(), StoreGoodActivity.class);
										startActivity(intent);
									}
								}
								
								@Override
								public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
									// TODO Auto-generated method stub
									ToastTool.showNetworkError(getActivity());
								}
							});
						}
					});
					
					
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getActivity());
			}
		});
		
	}
	
	
	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		schoolName = LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity()).getData().getSchoolName();
			if(requestCode==103){//floorid改变
				location.setText(schoolName+LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity()).getData().getFloorName());
				floorid = LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity()).getData().getFloorID();
				setupListView(viewself);
			}else{
				location.setText(schoolName+LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity()).getData().getFloorName());
				floorid = LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity()).getData().getFloorID();
				setupListView(viewself);
			}
		Log.i("刷新", "刷新了店铺列表");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//handler.removeMessages(0);
	}
	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
