package com.school.schoolapp.fragment.main;

import org.apache.http.Header;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.school.schoolapp.R;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.activity.technical.TechnicalCategoryActivity;
import com.school.schoolapp.activity.technical.TechnicalDemandActivity;
import com.school.schoolapp.activity.technical.TechnicalDetailActivity;
import com.school.schoolapp.activity.technical.TechnicalPublishActivity;
import com.school.schoolapp.adapter.technical.TechnicalHomeAdapter;
import com.school.schoolapp.callback.technical.SkillListCallback;
import com.school.schoolapp.callback.technical.TechnicalCategoryCallback;
import com.school.schoolapp.classes.BaseCallback;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;
import com.school.schoolapp.classes.tools.ToastTool;
import com.school.schoolapp.classes.users.UserCallback;
import com.school.schoolapp.tool.baidumap.BaiduMapSingleton;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the
 * {@link TechnicalMainFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link TechnicalMainFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class TechnicalMainFragment extends Fragment {
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
	 * @return A new instance of fragment TechnicalMainFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static TechnicalMainFragment newInstance(String param1, String param2) {
		TechnicalMainFragment fragment = new TechnicalMainFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public TechnicalMainFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	private View headerV;
	private ListView mListview;
	private Button publish;
	private TechnicalHomeAdapter skillAdapter;
	private int curpage=1;
	private int isCityBind=0;
	//private int[] categorys={R.id.category00,R.id.category01,R.id.category02,R.id.category03,R.id.category04,R.id.category05,R.id.category06,R.id.category07,R.id.category08,R.id.category09};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_technical_main, container,
				false);
		
		//init(v);
		
//		for(int i=0;i<categorys.length;i++){
//			Button btn = (Button)v.findViewById(categorys[i]);
//			btn.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					//startActivity(new Intent(getActivity(),TechnicalCategoryActivity.class));
//					//startActivity(new Intent(getActivity(),TechnicalDemandActivity.class));
//					Toast.makeText(getActivity(), "正在开发中", Toast.LENGTH_SHORT).show();
//				}
//			});
//		}
		return v;
	}
	private void init(View v){
		BaiduMapSingleton.getInstance().getCurCity(getActivity(), new MyLocationListener());
		
		headerV = View.inflate(getActivity(), R.layout.view_technical_headerview, null);
		requestTechnicalCategory();
		mListview = (ListView)v.findViewById(R.id.technicalHomeList);
		mListview.addHeaderView(headerV,null,true);
		skillAdapter = new TechnicalHomeAdapter(getActivity());
		mListview.setAdapter(skillAdapter);
		//获取大厅内容
		requestSkillList();
		mListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),TechnicalDetailActivity.class);
				intent.putExtra("sid", skillAdapter.getSkills().get(position-1).getSid());
				startActivity(intent);
			}
		});
		
		publish = (Button)v.findViewById(R.id.publish);
		publish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(),TechnicalPublishActivity.class));
			}
		});
	}
	
	public class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			if(isCityBind==1)
				return;
			//设置button值
			String cityname;
			if(arg0.getLocType()==61||arg0.getLocType()==161){//定位成功代码
				((Button)getActivity().findViewById(R.id.backButton)).setText(arg0.getCity());
				cityname=arg0.getCity();
				
			}else{
				((Button)getActivity().findViewById(R.id.backButton)).setText("全国");
				cityname="全国";
			}
			//保存城市到本地
			LocalSharedPreferenceSingleton.getInstance().setUserCity(getActivity(), "", cityname);
			//将城市提交服务器
			bindCity(cityname);
			
			
			
		}
		
	}
	private void bindCity(String cityname){
		String url = getString(R.string.base_url) + getString(R.string.user_set_city);
		RequestParams params = new RequestParams();
		params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity()).getTicket());
		params.put("city", cityname);
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				BaseCallback callback = new Gson().fromJson(new String(arg2), BaseCallback.class);
				if(callback.getResult().equals("1")){
					isCityBind=1;
					BaiduMapSingleton.getInstance().stopLocation();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getActivity());
			}
		});
	}
	
	private int cid=0,wid=0,price1=0,price2=0,city=10003;
	private int order=1;
	private String sex="",keys="";
	
	private void requestSkillList(){
		String url = getString(R.string.base_url) + getString(R.string.skill_get_list);
		RequestParams params = new RequestParams();
		final UserCallback user = LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity());
		params.put("ticket", user.getTicket());
		params.put("cid", cid);
		params.put("wid", wid);
		params.put("city", city);
		params.put("ord", order);
		params.put("price1",price1);
		params.put("price2", price2);
		params.put("curpage", curpage);
		params.put("sex", sex);
		params.put("keys", keys);
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				SkillListCallback skill = new Gson().fromJson(new String(arg2), SkillListCallback.class);
				if(skill.getResult().equals("1")&&skill.getData().size()>0){
					skillAdapter.addSkills(skill.getData());
				}else {
					ToastTool.showWithMessage(skill.getMsg(), getActivity());
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getActivity());
			}
		});
	}
	
	//获取技能分类
	public void requestTechnicalCategory(){
		String url = getString(R.string.base_url) + getString(R.string.technical_get_skill_category);
		RequestParams params = new RequestParams();
		final UserCallback user = LocalSharedPreferenceSingleton.getInstance().getUserInfo(getActivity());
		params.put("ticket", user.getTicket());
		//AsyncHttpClient client = new AsyncHttpClient();
		new AsyncHttpClient().post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				TechnicalCategoryCallback category = new Gson().fromJson(new String(arg2), TechnicalCategoryCallback.class);
				if(category.getResult().equals("1") && category.getData().getList().size()>0)
					setupTechnicalCategory(category);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastTool.showNetworkError(getActivity());
			}
		});
	}
	//初始化技能分类
	public void setupTechnicalCategory(TechnicalCategoryCallback category){
		TableRow row1 = (TableRow)headerV.findViewById(R.id.row1);
		TableRow row2 = (TableRow)headerV.findViewById(R.id.row2);
		for(int i=0;i<category.getData().getList().size();i++){
			final Button btn = new Button(getActivity());
			btn.setText(category.getData().getList().get(i).getName());
			btn.setTextSize(12);
			btn.setBackground(null);
			
			if(!ImageLoader.getInstance().isInited())
		    	ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
			//Log.i("", "图片链接"+category.getData().getList().get(i).getIcon());
			ImageLoader.getInstance().loadImage(category.getData().getList().get(i).getIcon(),new SimpleImageLoadingListener(){
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					
					WindowManager wm = (WindowManager) getActivity()
		                    .getSystemService(getActivity().WINDOW_SERVICE);
		 
					int width = wm.getDefaultDisplay().getWidth()/5-wm.getDefaultDisplay().getWidth()/11;
					
					Drawable drawable = new BitmapDrawable(loadedImage);
					drawable.setBounds(0, 0, width, width);
					btn.setCompoundDrawables(null, drawable, null, null);
					btn.setGravity(Gravity.CENTER);
				}
			});
			if(i<=4){
				row1.addView(btn);
			}else {
				row2.addView(btn);
			}
		}
		//添加“分类”按钮
		final Button cate = new Button(getActivity());
		cate.setText("分类");
		cate.setTextSize(12);
		cate.setBackground(null);
		ImageLoader.getInstance().loadImage("drawable://"+R.drawable.icon_skill_category_last,new SimpleImageLoadingListener(){
			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {
				
				WindowManager wm = (WindowManager) getActivity()
	                    .getSystemService(getActivity().WINDOW_SERVICE);
	 
				int width = wm.getDefaultDisplay().getWidth()/5-wm.getDefaultDisplay().getWidth()/11;
				
				Drawable drawable = new BitmapDrawable(loadedImage);
				drawable.setBounds(0, 0, width, width);
				cate.setCompoundDrawables(null, drawable, null, null);
				cate.setGravity(Gravity.CENTER);
			}
		});
		row2.addView(cate);
		
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
	public void onDetach() {
		super.onDetach();
		mListener = null;
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
