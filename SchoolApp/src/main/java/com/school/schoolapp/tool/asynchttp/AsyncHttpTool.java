package com.school.schoolapp.tool.asynchttp;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.school.schoolapp.R;
import com.school.schoolapp.classes.LocalSharedPreferenceSingleton;

import android.content.Context;

public class AsyncHttpTool {
	public static AsyncHttpTool asyncHttpTool;
    public static AsyncHttpTool getInstance(){
    	if(asyncHttpTool == null)
    		asyncHttpTool = new AsyncHttpTool();
    	return asyncHttpTool;
    }
    
    private AsyncHttpClient client;
    public void request(Context context,String requestUrl,RequestParams params,AsyncHttpResponseHandler responseHandler){
    	String url=context.getString(R.string.base_url)+requestUrl;
    	if(params == null)
    		params = new RequestParams();
    	params.put("ticket", LocalSharedPreferenceSingleton.getInstance().getUserInfo(context).getTicket());
    	
    	if(client == null)
    		client = new AsyncHttpClient();
    	
    	client.post(url, params, responseHandler);
    }
}
