package com.school.schoolapp.classes.alipay;

import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

public class Alipay {

	// 商户PID
	public static final String PARTNER = "2088121118728775";
	// 商户收款账号
	public static final String SELLER = "wuque_store@sina.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAL8jniAgKNX7bJDOjpvvsgNPs6xb4TICCULFrEsfq+zz/JknSPj8n1hYJuvEIAjHpa5FMMIM1gG9FY83Jm0dRm5nepeFV8Ac88z4ha5ymvGkDUTAlb28Ays2awN2wQwDb+def4AE0jdAHOAUqBsdIhuJs7AtVluNRw8lHM1JQImbAgMBAAECgYEAtzEP8IJiIRRkLTWLgPDukDXnYp58600ASzrhsu6b/PKreq4oRlr47dcvFnBWs5OFu80cuh9LTUdITobxEUspoCLxpTmdKyTMUln7pa+VHbL9r4ipJL4C6ey2hAc8RvBwTn6RjzGEYkvoDNyHgOyg0Uv7f7jgTo0RzvPe+OmbX4ECQQD+QksGfQO/nCTV4d4Ji5dnjnwl7p7dEzc3pYjlzV12iE3YYkPWYZRY4v0JDEgvq21PZxAAYsUsreYv/JIJW7NRAkEAwHKtg/KI1yAJJI4yvA+9D/WBWiP+MAL6gGAbJ/pjwEb9nDhnhHym8fGU54/mmrRUSG4xqJtTzuJPS2ANkTn7KwJBANG+gEOe92umdGCYDeGT7Nc5nEJyYU0AIAngnVjtG8+/S3BYR5vfLVBSF+jR3+6rHOZmn7WhxQRBhGVLlnDv27ECQQCyaJ3bePjLFnPjY8A9AAoxxVOJkVyQffJlGLOUOFhJZIawUMoG1Nif62oz0f/i49JO43k96eGkJcGFCyMIw0vJAkEAqmC026yBL0iW9Mgz5hmRC4CWSp+L+qxcNRvtwpGjDwjnhEzyOu39b+50lWrVv+6kUJcam5LdPOS8YMTPyg3vAw==";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/I54gICjV+2yQzo6b77IDT7OsW+EyAglCxaxLH6vs8/yZJ0j4/J9YWCbrxCAIx6WuRTDCDNYBvRWPNyZtHUZuZ3qXhVfAHPPM+IWucprxpA1EwJW9vAMrNmsDdsEMA2/nXn+ABNI3QBzgFKgbHSIbibOwLVZbjUcPJRzNSUCJmwIDAQAB";
	private static final int SDK_PAY_FLAG = 1;
	
	private Context mContext;
	
	private Handler payHandler = new Handler(){
		public void handleMessage(Message msg) {
			String d = (String)msg.obj;
			Log.i("", d);
		};
	};
	
	public Alipay(){
		
	}
	public void setOrderInfo(String subject, String body, String price){
		String orderInfo = getOrderInfo(subject, body, price);
		
		try {
			String sign = getPrivateKey(orderInfo, RSA_PRIVATE);
			sign = URLEncoder.encode(sign, "UTF-8");
			
			final String payInfo = orderInfo + "&sign=\"" + sign + "\"&sign_type=\"RSA\"";
			
			Runnable payRunnable = new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					PayTask alipay = new PayTask(null);
					String result = alipay.pay(payInfo, true);
					
					Message msg = new Message();
					//msg.what = SDK_PAY_FLAG;
	                msg.obj = result;
	                payHandler.sendMessage(msg);
				}
			};
			
			// 必须异步调用
	        Thread payThread = new Thread(payRunnable);
	        payThread.start();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"4234234234234\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}
	
	public String getPrivateKey(String content, String privateKey) throws Exception {
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
				Base64.decode(privateKey));
		KeyFactory keyf = KeyFactory.getInstance("RSA");
		PrivateKey priKey = keyf.generatePrivate(priPKCS8);

		java.security.Signature signature = java.security.Signature
				.getInstance("SHA1WithRSA");

		signature.initSign(priKey);
		signature.update(content.getBytes("UTF-8"));

		byte[] signed = signature.sign();

		return Base64.encode(signed);
  }
}
