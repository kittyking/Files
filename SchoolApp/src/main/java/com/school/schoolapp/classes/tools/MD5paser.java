package com.school.schoolapp.classes.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5paser {
	public static String getMD5(String val) throws NoSuchAlgorithmException{  
        MessageDigest md5 = MessageDigest.getInstance("MD5");  
        md5.update(val.getBytes());  
        byte[] m = md5.digest();//加密  
        return getString(m);  
	}  
    private static String getString(byte[] b){  
        StringBuffer sb = new StringBuffer();  
         for(int i = 0; i < b.length; i ++){  
          sb.append(b[i]);  
         }  
         return sb.toString();  
    }  
    
    public static String getMd5UTF8(String info){
    	try
    	  {
    	    MessageDigest md5 = MessageDigest.getInstance("MD5");
    	    md5.update(info.getBytes("UTF-8"));
    	    byte[] encryption = md5.digest();
    	      
    	    StringBuffer strBuf = new StringBuffer();
    	    for (int i = 0; i < encryption.length; i++)
    	    {
    	      if (Integer.toHexString(0xff & encryption[i]).length() == 1)
    	      {
    	        strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
    	      }
    	      else
    	      {
    	        strBuf.append(Integer.toHexString(0xff & encryption[i]));
    	      }
    	    }
    	      
    	    return strBuf.toString();
    	  }
    	  catch (NoSuchAlgorithmException e)
    	  {
    	    return "";
    	  }
    	  catch (UnsupportedEncodingException e)
    	  {
    	    return "";
    	  }
    }
    
    public final static String getMessageDigest(byte[] buffer) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
}
