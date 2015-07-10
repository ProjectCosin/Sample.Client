package com.cooshare.os.dev.sample;

/**************************************************************************************************
Filename:       MainActivity.java
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




import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.cooshare.os.dev.remotecontrol.*;
import com.cooshare.os.dev.localcontrol.NioClientThread;
import com.cooshare.os.dev.object.*;


/****

本JAVA文件是一个示例Activity, 开发者可根据自己的实际需求，设计自己的Activity。

*/

public class MainActivity extends Activity {

	
	private TextView tv1 = null;
	private Button b1 =null;
	private Button b2 =null;
	private Button b3 =null;
	private Button b4 =null;
	
	// NioClientThread 是本地控制的控制类
	NioClientThread mNioClientThread = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		// 设定该 Activity 说关联的 Layout文件为 res/layout/shell.xml
	    setContentView(R.layout.shell);
	    
	    // 获取当前 Activity里的 TextView实例
	    tv1 = (TextView)this.findViewById(R.id.ShellText);
	   
	    // 获取当前Activity里所有的Button实例
	    b1 = (Button)this.findViewById(R.id.button1);
	    b2 = (Button)this.findViewById(R.id.button2);
	    b3 = (Button)this.findViewById(R.id.button3);
	    b4 = (Button)this.findViewById(R.id.button4);
	    
	    
	    // 调用 GetEpInformation()方法以及GetHCCUInformation()方法，在这两个方法中，远程调用了BasicOperation接口，获取了本界面中所控制的EP对象和HCCU设备信息
	    try {
			GetEpInformation();
			GetHCCUInformatioin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    // 等待 HCCU的本地网络 信息从远程返回
	    while(ObjectBox.HCCU_IP==null){
	    	
	    	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    // 调用 InitialLocalControl()方法，在该方法中，对 NioClientThread进行了实例化，同时初始化并对本地HCCU进行了连接
	    // 如果 InitialLocalControl()返回 False，这说明当前环境不满足本地控制的条件
	    if(!InitialLocalControl()) Toast.makeText(getApplicationContext(),"当前环境不满足使用本地网络条件!",Toast.LENGTH_SHORT).show();
	    
	   
	    
	    // 等待 EP 信息从远程返回，返回后，会自动创建一个 SmartCindy类实例，并赋值到 ObjectBox公共容器类中的 sc 属性里。
	    while(ObjectBox.sc==null){
	    	
	    	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    // 获取到被控EP对象信息后，将该EP的EPID打印在 TextView上
	    tv1.setText("我是COS生产的智能电风扇 SmartCindy :)\r\nEP编码:"+ObjectBox.sc.getEpId()+"\r\nEP名称:"+ObjectBox.sc.getUnicode_EpUserDefinedAlias());
	}
	
	
	
	/*******************
	 * 
	 * 本方法用于初始化本地连接功能
	 * 
	 * @return True: 连接成功  / False:连接失败
	 */
	public boolean InitialLocalControl(){
		
		
		
		 // 实例化一个 NioClientThread类的实例
		 mNioClientThread = new NioClientThread();
		 // 调用该实例的 init()方法用于初始化本地控制功能
		 mNioClientThread.init();
		 // 调用该实例的 connect()方法，用于尝试与本地HCCU的IP地址进行连接，返回 True说明连接成功，返回False说明连接失败
		 Log.w("MainActivity", "Start to connect:"+ObjectBox.HCCU_IP);
		 return mNioClientThread.connect(ObjectBox.HCCU_IP);
		 
	}
	
	/*******************
	 * 
	 * 本方法用于处理用户按键后的逻辑
	 * 
	 * @return void
	 */
	public void ButtonHandlers(View view){
		
		
		// 首先需要判断 ObjectBox公共容器类中的SC成员，如果该成员未被赋值，说明远程端还没有返回被控制EP的信息
		if(ObjectBox.sc == null){
			
			 Toast.makeText(getApplicationContext(),"EP设备信息尚未获取,请等待!",Toast.LENGTH_LONG).show();
			 return;
		}
		
		
		
		
		try{
		    	
		    	if (view.getId() == R.id.button1) {
		    		
		    		
					// 使用本地控制方式打开SmartCindy智能风扇
		    		// 本地控制的指令控制方法 sendString(command)拥有1个入口参数. 
		    		// @command 代表控制命令协议，具体协议规范请参考您的开发者平台中的对应模型信息
		    		
		    		String command = ObjectBox.sc.getEpId()+",status,1|";
		    		String SecuityKey = MD5Security.md5(command+RCManagement.Developer_PrivateKey);
		    		String commandToSend = command+"^"+SecuityKey;
		    		
		    		Log.w("MainActivity", commandToSend);
		    		
		    		
		    		mNioClientThread.sendString(commandToSend);
		    			
		    		
		    		
		    		// 通过Toast方式给出执行反馈
		    		Toast.makeText(getApplicationContext(),"本地控制指令已经发送!",Toast.LENGTH_LONG).show();
		    		
				} else if (view.getId() == R.id.button2) {
					
					// 使用本地控制方式关闭SmartCindy智能风扇
		    		// 本地控制的指令控制方法 sendString(@command)拥有1个入口参数. 
		    		// @command 代表控制命令协议，具体协议规范请参考您的开发者平台中的对应模型信息
					
					String command = ObjectBox.sc.getEpId()+",status,0|";
		    		String SecuityKey = MD5Security.md5(command+RCManagement.Developer_PrivateKey);
		    		String commandToSend = command+"^"+SecuityKey;
		    		mNioClientThread.sendString(commandToSend);
					
					// 通过Toast方式给出执行反馈
					Toast.makeText(getApplicationContext(),"本地控制指令已经发送!",Toast.LENGTH_LONG).show();
					
					
				} else if (view.getId() == R.id.button3) {
					
					// 使用远程控制方式打开SmartCindy智能风扇
					RemoteControlTarget(ObjectBox.sc.getEpId(),"1"); 
					
					
				} else if (view.getId() == R.id.button4) {
						
					// 使用远程控制方式关闭SmartCindy智能风扇
					RemoteControlTarget(ObjectBox.sc.getEpId(),"0");
				
				}
		    	
		}catch(Exception e){
			
			 // 在不满足本地控制的条件下进行本地控制，会触发异常，异常在这里捕获并提示用户
			 Toast.makeText(getApplicationContext(),"执行出现错误!",Toast.LENGTH_LONG).show();
			 return;
			
		}
		
		
	}
	
	
	
	/*******************
	 * 
	 * 本方法用于处理远程控制的逻辑
	 * 
	 * @return void
	 */
	public void RemoteControlTarget(String epid,String status) throws Exception{
		
		
		// 此处需要构造一个向远程服务器请求的API地址
		// COS本身不提供远程开关智能风扇这一操作的API方法，这些API方法归类为开发者自定义方法
		// 开发者可以在设计EP模型的时候，在开发者平台上定义这些方法，编译并提交给COS系统
		// 成功提交的自定义方法，即可在此处进行远程调用
		
		// 在SmartCindy智能风扇这个示例项目中，我们在COS开发者平台中预定义了一个 ToggleFanSwitcher(@status,@epid)方法
		// @status代表风扇的开关状态，0为关、1为开；
		// @epid代表风扇对应的EP编码
		
		// 这相应的API提交构造如下
		// 具体API构造文档，请参考 
		// http://doc.cooshare.com/apidoc/html/M_CustomMethod_invoke_5_bfd000aa.htm
		
		
		// 注意：所有的参数值需要调用本地Base64工具类中的Encode_Content()方法进行传输前加密
		
		final String Request_Url_Root = RCManagement.CustomMethod_RemoteHandler+"invoke?";
		final String Request_Url_Para = "epid="+Base64.Encode_Content(epid)
									+"&method="+Base64.Encode_Content("ToggleFanSwitcher")
									+"&para="+Base64.Encode_Content((status+","+epid))
									+"&devid="+Base64.Encode_Content(RCManagement.Developer_Id);
		
		// 注意：所有API调用的最后一个参数为SK，代表 SecurityKey
		// 其构成是：{Request_Url_Para}与{开发者帐号的私钥}的字符串相连接后，对新的字符串做 16位 MD5加密
        final String SecurityKey = MD5Security.md5(Request_Url_Para + RCManagement.Developer_PrivateKey);
        
        // 最终所构成的API请求地址如下，开发者可通过 Logcat查看
        Log.w("MainActivity_rc", Request_Url_Root+Request_Url_Para+"&sk="+SecurityKey);
			
        
        new Thread()
		{
			   public void run()
			   { 					
				  // 由于网络请求是可能引起进程堵塞的操作，所以我们在一个新的线程中进行
				  // 工具类 RCManagement中的 Request_POST方法用于提交API请求
				  
				  // 注意：所有API调用的返回结果本身是加密的，需通过 Base64工具类中的Decode_Content()方法进行解密 
				  ObjectBox.OperationReturnFlag =  Base64.Decode_Content(RCManagement.Request_POST(Request_Url_Root, Request_Url_Para+"&sk="+SecurityKey));
				  
			   }
			   
		}.start();	
		
		
		  // 等待方法返回值从远程返回
		   while(ObjectBox.OperationReturnFlag==""){
		    	
		    	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		   
		   String returns = ObjectBox.OperationReturnFlag;
		   
		   String Message="";
		    
		   
		   // 以下对返回结果进行分析，具体的返回值格式及意义请参考
		   // http://doc.cooshare.com/apidoc/html/M_CustomMethod_invoke_5_bfd000aa.htm
		   
		   // ObjectBox.NetworkErrorFlag 是我们预定义的一个网络状态异常标志
		   // 在这边用于与返回结果值比较，判断是否网络异常
		   
		    if(!returns.equals(ObjectBox.NetworkErrorFlag)){
		    	
		    	if(returns.indexOf(',')!=-1){
		    		
		    		String returnValueFromMethod = StringUtil.split(returns, ",")[0];
		    		String exeReturn = StringUtil.split(returns, ",")[1];
		    		
		    		if(exeReturn.equals("1")){
		    			
		    			Message = "方法执行成功!\r\n返回值为"+returnValueFromMethod;
		    		}else{
		    			
		    			Message = "方法执行失败!";
		    		}
		    		
		    	} else{
		    		
		    		if(returns.equals("0")){
		    			
		    			Message = "没有找到该自定义方法!";
		    			
		    		}else if(returns.equals("-1")){
		    		
		    			Message = "方法预订参数与实际提供参数不一致!";
		    			
		    		}else if(returns.equals("-3")){
			    		
		    			Message = "方法故障!";
			    		
		    		}else if(returns.equals("-4")){
			    		
		    			Message = "安全码验证错误!";
		    			
		    		}else{
		    			
		    			Message = "未知错误!";
		    		}
		    		
		    	}
		    	 
		    }else{
		    	
		    	Message="网络故障，请核实网络连接情况!";
		    }
        
		    Toast.makeText(getApplicationContext(),Message,Toast.LENGTH_LONG).show();
	}
	
	
	/*******************
	 * 
	 * 本方法用于获取EP控制对象
	 * 
	 * @return void
	 */
	public void GetEpInformation() throws Exception{
		
		// 此处需要构造一个向远程服务器请求的API地址
		// 获取EP信息属于COS API中所提供的基本接口，
		
		// 具体API接口说明，请参考 
		// http://doc.cooshare.com/apidoc/html/M_BasicOperation_GetEndPoint_4_193e86b9.htm
		
		
		// 注意：所有的参数值需要调用本地Base64工具类中的Encode_Content()方法进行传输前加密
		
		final String Request_Url_Root = RCManagement.BasicOperation_RemoteHandler+"GetEndPoint?";
		final String Request_Url_Para = "para="+Base64.Encode_Content("15")
									+"&getendpointlistmode="+Base64.Encode_Content("epid")
									+"&cosid="+Base64.Encode_Content(RCManagement.Developer_Id)
									+"&devid="+Base64.Encode_Content(RCManagement.Developer_Id);
		
		// 注意：所有API调用的最后一个参数为SK，代表 SecurityKey
		// 其构成是：{Request_Url_Para}与{开发者帐号的私钥}的字符串相连接后，对新的字符串做 16位 MD5加密
        final String SecurityKey = MD5Security.md5(Request_Url_Para + RCManagement.Developer_PrivateKey);
        
        // 最终所构成的API请求地址如下，开发者可通过 Logcat查看
        Log.w("MainActivity_GetEpInformation", Request_Url_Root+Request_Url_Para+"&sk="+SecurityKey);
        
        new Thread()
      		{
      			   public void run()
      			   { 						 
      				
      				  // 由于网络请求是可能引起进程堵塞的操作，所以我们在一个新的线程中进行
     				  // 工具类 RCManagement中的 Request_POST方法用于提交API请求
     				  
     				  // 注意：所有API调用的返回结果本身是加密的，需通过 Base64工具类中的Decode_Content()方法进行解密 
      				String returns = Base64.Decode_Content((RCManagement.Request_POST(Request_Url_Root, Request_Url_Para+"&sk="+SecurityKey)));
      				 
      				//String returns =RCManagement.Request_POST(Request_Url_Root, Request_Url_Para+"&sk="+SecurityKey);
      				   
      				 
      				 
      				  // 以下对返回结果进行分析，具体的返回值格式及意义请参考
      				  // http://doc.cooshare.com/apidoc/html/M_BasicOperation_GetEndPoint_4_193e86b9.htm
      			   
      				 
      			      // ObjectBox.NetworkErrorFlag 是我们预定义的一个网络状态异常标志
      			      // 在这边用于与返回结果值比较，判断是否网络异常
      				 
		      				 if(returns.equals("0")||returns.equals("-4")||returns.equals(ObjectBox.NetworkErrorFlag)){
		      					 
		      					// 说明没有找到相关EP信息(返回0)、或安全验证码错误(返回-4)、或本地网络故障
		      					// 将所有的控制按钮都设为不可用
		      					b1.setEnabled(false);
		      					b2.setEnabled(false);
		      					b3.setEnabled(false);
		      					b4.setEnabled(false);
		      					
		      				 }else{
		      					 
		      					 // 获取到EP信息后，对信息进行解析
		      					 
		      					 String[] ep_device_group = StringUtil.split(returns, '|');
		      					 
		      					 for(int x=0;x<ep_device_group.length;x++){
		      						 
		      						 if(!ep_device_group[x].equals("")){
		      							 
		      							 String[] ep_device = StringUtil.split(ep_device_group[x], ",");
		      							 
		      							 // 处理单个EP信息，根据EP编码，实例化一个 SmartCindy对象，并将其放置到公共容器ObjectBox中的sc成员里。
		      							 
		      							 ObjectBox.sc =  new SmartCindy(ep_device[0].toString());
		      							 ObjectBox.sc.setEpTypeId(ep_device[1].toString());
		      							 
		      							 Log.w("MainActivity", ep_device[2].toString());
		      							
		      							 
		      							 // 包含中文的结果返回中，还需要使用 android.util.Base64.decode()进行第二次解码
		      							 byte b[]= android.util.Base64.decode(ep_device[2].toString(),android.util.Base64.DEFAULT);
		      						
		      							 ObjectBox.sc.setUnicode_EpUserDefinedAlias(new String(b));
		      							 
		      							 
		      							 ObjectBox.sc.setEpProductId(ep_device[3].toString());
		      							 ObjectBox.sc.setHccuId(ep_device[4].toString());
		      							 ObjectBox.sc.setEpMac(ep_device[5].toString());
		      						 }
		      					 }
		      					 
		      					 
		      				 }
		      				 
      				 
      			   }
      			   
      		}.start();	
	}
	
	
	/*******************
	 * 
	 * 本方法用于获取HCCU本地网络信息
	 * 
	 * @return void
	 * @throws Exception 
	 */
	public void GetHCCUInformatioin() throws Exception{
		
		final String Request_Url_Root = RCManagement.BasicOperation_RemoteHandler+"GetHCCUNetworkInfo?";
		final String Request_Url_Para = "cosid="+Base64.Encode_Content(RCManagement.Developer_Id)+"&devid="+Base64.Encode_Content(RCManagement.Developer_Id);
		
	    final String SecurityKey = MD5Security.md5(Request_Url_Para + RCManagement.Developer_PrivateKey);
		
	      
        // 最终所构成的API请求地址如下，开发者可通过 Logcat查看
        Log.w("MainActivity_GetHCCUInformation", Request_Url_Root+Request_Url_Para+"&sk="+SecurityKey);
        
        new Thread()
  		{
  			   public void run()
  			   { 						 
  				
  				  // 由于网络请求是可能引起进程堵塞的操作，所以我们在一个新的线程中进行
 				  // 工具类 RCManagement中的 Request_POST方法用于提交API请求
 				  
 				  // 注意：所有API调用的返回结果本身是加密的，需通过 Base64工具类中的Decode_Content()方法进行解密 
  				 String returns = Base64.Decode_Content((RCManagement.Request_POST(Request_Url_Root, Request_Url_Para+"&sk="+SecurityKey)));
  				   
  			
  				 
  				  // 以下对返回结果进行分析，具体的返回值格式及意义请参考
  				  // http://doc.cooshare.com/apidoc/html/M_BasicOperation_GetHCCUNetworkInfo_2_52fce4af.htm
  			   
  				 
  			      // ObjectBox.NetworkErrorFlag 是我们预定义的一个网络状态异常标志
  			      // 在这边用于与返回结果值比较，判断是否网络异常
  				 
	      				 if(returns.equals("0")||returns.equals(ObjectBox.NetworkErrorFlag)){
	      					 
	      					// 说明没有找到相关HCCU信息(返回0)或本地网络故障
	      					// 将所有的控制按钮都设为不可用
	      					b1.setEnabled(false);
	      					b2.setEnabled(false);
	      					b3.setEnabled(false);
	      					b4.setEnabled(false);
	      					
	      				 }else{
	      					 
	      					 // 获取到HCCU信息后，对信息进行解析
	      					 
	      					 String[] hccu_device_group = StringUtil.split(returns, '|');
	      					 
	      					 for(int x=0;x<hccu_device_group.length;x++){
	      						 
	      						 if(!hccu_device_group[x].equals("")){
	      							 
	      							 String[] hccu_device = StringUtil.split(hccu_device_group[x], ",");
	      							 
	      							 ObjectBox.HCCU_IP = hccu_device[0].toString();
	      							 ObjectBox.HCCU_PORT = hccu_device[1].toString();
	      							 
	      							 Log.w("MainActivity", ObjectBox.HCCU_IP+","+ObjectBox.HCCU_PORT);
	      							
	      						 }
	      					 }
	      					 
	      					 
	      				 }
	      				 
  				 
  			   }
  			   
  		}.start();	
	}
	
}