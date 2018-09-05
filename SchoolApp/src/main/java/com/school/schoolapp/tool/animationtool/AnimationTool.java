package com.school.schoolapp.tool.animationtool;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import com.school.schoolapp.tool.baidumap.BaiduMapSingleton;

public class AnimationTool {

	public static AnimationTool animationTool= null;
	
	public static AnimationTool getInstance(){
		if(animationTool==null){
			animationTool=new AnimationTool();
		}
		return animationTool;
	}
	
	private AnimationSet animationSet;
	
	
	
	public static AnimationTool getAnimationTool() {
		return animationTool;
	}



	public static void setAnimationTool(AnimationTool animationTool) {
		AnimationTool.animationTool = animationTool;
	}



	public AnimationSet getAnimationSet() {
		return animationSet;
	}



	public void setAnimationSet(AnimationSet animationSet) {
		this.animationSet = animationSet;
	}

	private float startX;
	private float startY;
	private float endX;
	private float endY;
	


	public float getStartX() {
		return startX;
	}



	public void setStartX(float startX) {
		this.startX = startX;
	}



	public float getStartY() {
		return startY;
	}



	public void setStartY(float startY) {
		this.startY = startY;
	}



	public float getEndX() {
		return endX;
	}



	public void setEndX(float endX) {
		this.endX = endX;
	}



	public float getEndY() {
		return endY;
	}



	public void setEndY(float endY) {
		this.endY = endY;
	}



	public AnimationSet getParabola(){
		Animation translateAnimationX = new TranslateAnimation(startX-20,endX-20,0,0);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		translateAnimationX.setRepeatCount(0);
		translateAnimationX.setFillAfter(true); 
		
		Animation translateAnimationY = new TranslateAnimation(0,0,startY,endY-20);
		translateAnimationY.setInterpolator(new AccelerateInterpolator());
		translateAnimationY.setRepeatCount(0);
		translateAnimationY.setFillAfter(true);
		
		animationSet = new AnimationSet(false);
		animationSet.setFillAfter(false);
		animationSet.addAnimation(translateAnimationX);
		animationSet.addAnimation(translateAnimationY);
		animationSet.setDuration(500);// 动画的执行时间
		
		return animationSet;
	}
}
