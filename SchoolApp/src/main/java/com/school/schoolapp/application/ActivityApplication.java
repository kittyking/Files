package com.school.schoolapp.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class ActivityApplication extends Application {
	private List<Activity> mList = new ArrayList<Activity>(); 
	  private static ActivityApplication instance; 
	  private ActivityApplication() {  
	  } 
	  public synchronized static ActivityApplication getInstance() { 
	    if (null == instance) { 
	      instance = new ActivityApplication(); 
	    } 
	    return instance; 
	  } 
	  // add Activity 
	  public void addActivity(Activity activity) { 
	    mList.add(activity); 
	  } 
	  public void exit() { 
	    try { 
	      for (Activity activity : mList) { 
	        if (activity != null) 
	          activity.finish(); 
	      } 
	    } catch (Exception e) { 
	      e.printStackTrace(); 
	    } finally { 
	      System.exit(0); 
	    } 
	  } 
	  public void onLowMemory() { 
	    super.onLowMemory();   
	    System.gc(); 
	  } 
}
