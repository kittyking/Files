package com.school.schoolapp.classes.network;


public class NetworkAccessSingleton {

	private static NetworkAccessSingleton networkAccesssSingleton = null;
	
	public static NetworkAccessSingleton getInstance(){
		if(networkAccesssSingleton == null){
			networkAccesssSingleton = new NetworkAccessSingleton();
		}
		
		return networkAccesssSingleton;
	}
	
}
