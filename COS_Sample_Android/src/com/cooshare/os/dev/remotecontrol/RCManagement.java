package com.cooshare.os.dev.remotecontrol;

/**************************************************************************************************
Filename:       RCManagement.java
Revised:        $Date: 2014-01-10 17:22:23
Revision:       $Revision: cos v1.0.1 $

Description:    


Copyright CooShare Inc. All rights reserved.

IMPORTANT: Your use of this Software is limited to those specific rights
granted under the terms of a software license agreement between the user
who downloaded the software, his/her employer (which must be your employer)
and CooShare Inc.  You may not use this Software unless you agree to abide 
by the terms of the License. The License limits your use, and you acknowledge, 
that the Software may not be modified, copied or distributed unless embedded 
on a CooShare Inc license .  Other than for the foregoing purpose, you may not 
use, reproduce, copy, prepare derivative works of, modify, distribute, perform, 
display or sell this Software and/or its documentation for any purpose.

Should you have any questions regarding your right to use this Software,
contact CooShare Inc. at www.cooshare.com. 

**************************************************************************************************/



import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/****

本JAVA文件是一个工具类，提供远程操控的相关方法，在本文件中，用户只需修改两处

public static String Developer_PrivateKey = "{PRIVATEKEY}";
public static String Developer_Id = "{DEVELOPER ID}";

*/

public class RCManagement {

	
	// 修改成自己的开发者私钥，可从开发者平台中的用户信息里获取
	public static String Developer_PrivateKey = "100005655453";
	// 修改成自己的开发者ID，可从开发者平台中的用户信息里获取
	public static String Developer_Id = "1";
	
	
	public static String CustomMethod_RemoteHandler = "http://api.dev.cooshare.com/CustomMethod.asmx/";
	public static String BasicOperation_RemoteHandler = "http://api.dev.cooshare.com/BasicOperation.asmx/";
	 
	
	public static String  Request_POST(String inputurl,String param){
			
			
	      HttpURLConnection httpurlconnection = null;
	      int errorCounter=0;
	      
	        try
	        {
	          InputStream stream = null;
	          int code=-1;
	          int iRetry=0;
	          
	          while(iRetry<10){
	        	  
	        	  iRetry++;

	        	  try{	        		   									  
								        				  String proxyHost = android.net.Proxy.getDefaultHost(); 
								        				  
								        				  if (proxyHost!=null) {
								        					  
										        				  java.net.Proxy p = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(android.net.Proxy.getDefaultHost(),android.net.Proxy.getDefaultPort())); 
										        				  httpurlconnection = (HttpURLConnection) new URL(inputurl).openConnection(p); 
								        				  } 
								        				
								        				  else { 
								        					
								        					  httpurlconnection = (HttpURLConnection) new URL(inputurl).openConnection(); 
								        				  } 
						        				 
		        				  				  
		        				  				  httpurlconnection.setConnectTimeout(10000);
		        				  				  httpurlconnection.setReadTimeout(10000);
		        				    	          httpurlconnection.setRequestMethod("POST");
		        				    	          
		        				    	       
		        				    	          
		        				    	          httpurlconnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");			      
		        				    	          httpurlconnection.setRequestProperty("Content-Length", "" +Integer.toString(param.getBytes().length));      
		        				    	          httpurlconnection.setUseCaches (false);      
		        				    	          httpurlconnection.setDoInput(true);      
		        				    	          httpurlconnection.setDoOutput(true);      
		        				    	          
		        				    	          DataOutputStream wr = new DataOutputStream ( httpurlconnection.getOutputStream ());
		        				    	          wr.writeBytes (param);      
		        				    	          wr.flush ();      
		        				    	          wr.close ();
		        				    	          
		        				    	          code = httpurlconnection.getResponseCode();
		        				    	          stream = httpurlconnection.getInputStream();
		        				  
		
	        	  }catch(Exception e){
	        		  
	        		  errorCounter++;
	        		  code = -1;  
	        		  e.printStackTrace();
	        		  
	        		  if(errorCounter>1){
	        			  break;
	        		  }
	        	  }
	        	  
	        	  if (code == HttpURLConnection.HTTP_OK ) { 
	                  iRetry = 10 ; 
	        	  }
	          }
	          
	        
	          
	          if(code==HttpURLConnection.HTTP_OK){
	        	  
	        	  String str = convertStreamToString(stream);
	        	  
		          try{
		        	  
		        	  if(str.contains("<html"))   return "CLIENTNETWORKERROR";
		        	  
		        	  str = LocalParse(str);
		        	  str = replacenoisywords(str);
		        	  
		        	  
		        
		        	   
		              
		          }catch(Exception ex){
		        	
		        	  str="CLIENTNETWORKERROR";
		          }
	              return str;
	          }else{
	        	
	        	 
	        	  return "CLIENTNETWORKERROR";
	          }	          
	        }
	        catch(Exception e)
	        {
	          e.printStackTrace();
	         
	          return "CLIENTNETWORKERROR";
	        }
	        finally
	        {
	          if(httpurlconnection!=null)
	            httpurlconnection.disconnect();
	        }

	}
	 
	static Pattern pattern;
	static Matcher matcher;
	 
	public static String replacenoisywords(String Htmlstring){
		 
		 pattern = Pattern.compile("\\s*|\t|\r|\n"); 
		 matcher = pattern.matcher(Htmlstring);

		 Htmlstring = matcher.replaceAll("");
		 return Htmlstring;

	 }
	 
	public static String LocalParse(String Htmlstring)
	 {
		 pattern = Pattern.compile("<.+?>");
		 matcher = pattern.matcher(Htmlstring);
	            
		 while (matcher.find()) {
			 
			 Htmlstring = Htmlstring.replace(matcher.group(),"");
		 }
		 
		 return Htmlstring;	 
	 }
	 
	public static String convertStreamToString(InputStream is) throws UnsupportedEncodingException {   
	        /*  
	         * To convert the InputStream to String we use the BufferedReader.readLine()  
	         * method. We iterate until the BufferedReader return null which means  
	         * there's no more data to read. Each line will appended to a StringBuilder  
	         * and returned as String.  
	         */  
	        
				StringBuffer sb = new StringBuffer();
		        InputStreamReader r= new InputStreamReader(is, "UTF-8");
		        int read=0;
		        
		        try{
		        	
		        	while ((read=r.read())>=0)
			        {sb.append( (char) read); }
		        	
		        }catch(Exception e){}
		        
		        
		        	String ret = new String(sb);
		        
			        return ret;   
	}   
		
}
