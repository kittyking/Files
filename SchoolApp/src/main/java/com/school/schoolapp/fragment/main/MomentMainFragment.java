package com.school.schoolapp.fragment.main;

import java.util.ArrayList;
import java.util.List;

import com.school.schoolapp.R;
import com.school.schoolapp.R.layout;
import com.school.schoolapp.activity.moment.MomentPublishActivity;
import com.school.schoolapp.adapter.moment.MomentMainAdapter;
import com.school.schoolapp.adapter.moment.MomentPagerAdapter;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link MomentMainFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link MomentMainFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class MomentMainFragment extends Fragment {
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
	 * @return A new instance of fragment MomentMainFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static MomentMainFragment newInstance(String param1, String param2) {
		MomentMainFragment fragment = new MomentMainFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public MomentMainFragment() {
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

	private List<ListView> listviews;
	private ViewPager mViewpager;
	private List<Fragment> fragments;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_moment_main, container, false);
		
		mViewpager =(ViewPager)v.findViewById(R.id.momentVP);
		mViewpager.setAdapter(new MyMomentPager());
		mViewpager.setCurrentItem(0);
		
		
		Button publish = (Button)v.findViewById(R.id.publish);
		publish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(),MomentPublishActivity.class));
			}
		});
		return v;
	}
	
	public class MyMomentPager extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			if(((ViewPager) container).getChildAt(position)==null){
				ListView listview=new ListView(getActivity());
				listview.setAdapter(new MomentMainAdapter(getActivity()));
				LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 1000);
				listview.setLayoutParams(params);
				
				((ViewPager) container).addView(listview);
				return listview;
			}else{
				return ((ViewPager) container).getChildAt(position);
			}
			
		}
		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			//((ViewPager) container).removeViewAt(position);
		}
		
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
