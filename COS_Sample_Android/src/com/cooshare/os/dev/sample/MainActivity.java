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

��JAVA�ļ���һ��ʾ��Activity, �����߿ɸ����Լ���ʵ����������Լ���Activity��

*/

public class MainActivity extends Activity {

	
	private TextView tv1 = null;
	private Button b1 =null;
	private Button b2 =null;
	private Button b3 =null;
	private Button b4 =null;
	
	// NioClientThread �Ǳ��ؿ��ƵĿ�����
	NioClientThread mNioClientThread = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		// �趨�� Activity ˵������ Layout�ļ�Ϊ res/layout/shell.xml
	    setContentView(R.layout.shell);
	    
	    // ��ȡ��ǰ Activity��� TextViewʵ��
	    tv1 = (TextView)this.findViewById(R.id.ShellText);
	   
	    // ��ȡ��ǰActivity�����е�Buttonʵ��
	    b1 = (Button)this.findViewById(R.id.button1);
	    b2 = (Button)this.findViewById(R.id.button2);
	    b3 = (Button)this.findViewById(R.id.button3);
	    b4 = (Button)this.findViewById(R.id.button4);
	    
	    
	    // ���� GetEpInformation()�����Լ�GetHCCUInformation()�������������������У�Զ�̵�����BasicOperation�ӿڣ���ȡ�˱������������Ƶ�EP�����HCCU�豸��Ϣ
	    try {
			GetEpInformation();
			GetHCCUInformatioin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    // �ȴ� HCCU�ı������� ��Ϣ��Զ�̷���
	    while(ObjectBox.HCCU_IP==null){
	    	
	    	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    // ���� InitialLocalControl()�������ڸ÷����У��� NioClientThread������ʵ������ͬʱ��ʼ�����Ա���HCCU����������
	    // ��� InitialLocalControl()���� False����˵����ǰ���������㱾�ؿ��Ƶ�����
	    if(!InitialLocalControl()) Toast.makeText(getApplicationContext(),"��ǰ����������ʹ�ñ�����������!",Toast.LENGTH_SHORT).show();
	    
	   
	    
	    // �ȴ� EP ��Ϣ��Զ�̷��أ����غ󣬻��Զ�����һ�� SmartCindy��ʵ��������ֵ�� ObjectBox�����������е� sc �����
	    while(ObjectBox.sc==null){
	    	
	    	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    // ��ȡ������EP������Ϣ�󣬽���EP��EPID��ӡ�� TextView��
	    tv1.setText("����COS���������ܵ���� SmartCindy :)\r\nEP����:"+ObjectBox.sc.getEpId()+"\r\nEP����:"+ObjectBox.sc.getUnicode_EpUserDefinedAlias());
	}
	
	
	
	/*******************
	 * 
	 * ���������ڳ�ʼ���������ӹ���
	 * 
	 * @return True: ���ӳɹ�  / False:����ʧ��
	 */
	public boolean InitialLocalControl(){
		
		
		
		 // ʵ����һ�� NioClientThread���ʵ��
		 mNioClientThread = new NioClientThread();
		 // ���ø�ʵ���� init()�������ڳ�ʼ�����ؿ��ƹ���
		 mNioClientThread.init();
		 // ���ø�ʵ���� connect()���������ڳ����뱾��HCCU��IP��ַ�������ӣ����� True˵�����ӳɹ�������False˵������ʧ��
		 Log.w("MainActivity", "Start to connect:"+ObjectBox.HCCU_IP);
		 return mNioClientThread.connect(ObjectBox.HCCU_IP);
		 
	}
	
	/*******************
	 * 
	 * ���������ڴ����û���������߼�
	 * 
	 * @return void
	 */
	public void ButtonHandlers(View view){
		
		
		// ������Ҫ�ж� ObjectBox�����������е�SC��Ա������ó�Աδ����ֵ��˵��Զ�̶˻�û�з��ر�����EP����Ϣ
		if(ObjectBox.sc == null){
			
			 Toast.makeText(getApplicationContext(),"EP�豸��Ϣ��δ��ȡ,��ȴ�!",Toast.LENGTH_LONG).show();
			 return;
		}
		
		
		
		
		try{
		    	
		    	if (view.getId() == R.id.button1) {
		    		
		    		
					// ʹ�ñ��ؿ��Ʒ�ʽ��SmartCindy���ܷ���
		    		// ���ؿ��Ƶ�ָ����Ʒ��� sendString(command)ӵ��1����ڲ���. 
		    		// @command �����������Э�飬����Э��淶��ο����Ŀ�����ƽ̨�еĶ�Ӧģ����Ϣ
		    		
		    		String command = ObjectBox.sc.getEpId()+",status,1|";
		    		String SecuityKey = MD5Security.md5(command+RCManagement.Developer_PrivateKey);
		    		String commandToSend = command+"^"+SecuityKey;
		    		
		    		Log.w("MainActivity", commandToSend);
		    		
		    		
		    		mNioClientThread.sendString(commandToSend);
		    			
		    		
		    		
		    		// ͨ��Toast��ʽ����ִ�з���
		    		Toast.makeText(getApplicationContext(),"���ؿ���ָ���Ѿ�����!",Toast.LENGTH_LONG).show();
		    		
				} else if (view.getId() == R.id.button2) {
					
					// ʹ�ñ��ؿ��Ʒ�ʽ�ر�SmartCindy���ܷ���
		    		// ���ؿ��Ƶ�ָ����Ʒ��� sendString(@command)ӵ��1����ڲ���. 
		    		// @command �����������Э�飬����Э��淶��ο����Ŀ�����ƽ̨�еĶ�Ӧģ����Ϣ
					
					String command = ObjectBox.sc.getEpId()+",status,0|";
		    		String SecuityKey = MD5Security.md5(command+RCManagement.Developer_PrivateKey);
		    		String commandToSend = command+"^"+SecuityKey;
		    		mNioClientThread.sendString(commandToSend);
					
					// ͨ��Toast��ʽ����ִ�з���
					Toast.makeText(getApplicationContext(),"���ؿ���ָ���Ѿ�����!",Toast.LENGTH_LONG).show();
					
					
				} else if (view.getId() == R.id.button3) {
					
					// ʹ��Զ�̿��Ʒ�ʽ��SmartCindy���ܷ���
					RemoteControlTarget(ObjectBox.sc.getEpId(),"1"); 
					
					
				} else if (view.getId() == R.id.button4) {
						
					// ʹ��Զ�̿��Ʒ�ʽ�ر�SmartCindy���ܷ���
					RemoteControlTarget(ObjectBox.sc.getEpId(),"0");
				
				}
		    	
		}catch(Exception e){
			
			 // �ڲ����㱾�ؿ��Ƶ������½��б��ؿ��ƣ��ᴥ���쳣���쳣�����ﲶ����ʾ�û�
			 Toast.makeText(getApplicationContext(),"ִ�г��ִ���!",Toast.LENGTH_LONG).show();
			 return;
			
		}
		
		
	}
	
	
	
	/*******************
	 * 
	 * ���������ڴ���Զ�̿��Ƶ��߼�
	 * 
	 * @return void
	 */
	public void RemoteControlTarget(String epid,String status) throws Exception{
		
		
		// �˴���Ҫ����һ����Զ�̷����������API��ַ
		// COS�����ṩԶ�̿������ܷ�����һ������API��������ЩAPI��������Ϊ�������Զ��巽��
		// �����߿��������EPģ�͵�ʱ���ڿ�����ƽ̨�϶�����Щ���������벢�ύ��COSϵͳ
		// �ɹ��ύ���Զ��巽���������ڴ˴�����Զ�̵���
		
		// ��SmartCindy���ܷ������ʾ����Ŀ�У�������COS������ƽ̨��Ԥ������һ�� ToggleFanSwitcher(@status,@epid)����
		// @status������ȵĿ���״̬��0Ϊ�ء�1Ϊ����
		// @epid������ȶ�Ӧ��EP����
		
		// ����Ӧ��API�ύ��������
		// ����API�����ĵ�����ο� 
		// http://doc.cooshare.com/apidoc/html/M_CustomMethod_invoke_5_bfd000aa.htm
		
		
		// ע�⣺���еĲ���ֵ��Ҫ���ñ���Base64�������е�Encode_Content()�������д���ǰ����
		
		final String Request_Url_Root = RCManagement.CustomMethod_RemoteHandler+"invoke?";
		final String Request_Url_Para = "epid="+Base64.Encode_Content(epid)
									+"&method="+Base64.Encode_Content("ToggleFanSwitcher")
									+"&para="+Base64.Encode_Content((status+","+epid))
									+"&devid="+Base64.Encode_Content(RCManagement.Developer_Id);
		
		// ע�⣺����API���õ����һ������ΪSK������ SecurityKey
		// �乹���ǣ�{Request_Url_Para}��{�������ʺŵ�˽Կ}���ַ��������Ӻ󣬶��µ��ַ����� 16λ MD5����
        final String SecurityKey = MD5Security.md5(Request_Url_Para + RCManagement.Developer_PrivateKey);
        
        // ���������ɵ�API�����ַ���£������߿�ͨ�� Logcat�鿴
        Log.w("MainActivity_rc", Request_Url_Root+Request_Url_Para+"&sk="+SecurityKey);
			
        
        new Thread()
		{
			   public void run()
			   { 					
				  // �������������ǿ���������̶����Ĳ���������������һ���µ��߳��н���
				  // ������ RCManagement�е� Request_POST���������ύAPI����
				  
				  // ע�⣺����API���õķ��ؽ�������Ǽ��ܵģ���ͨ�� Base64�������е�Decode_Content()�������н��� 
				  ObjectBox.OperationReturnFlag =  Base64.Decode_Content(RCManagement.Request_POST(Request_Url_Root, Request_Url_Para+"&sk="+SecurityKey));
				  
			   }
			   
		}.start();	
		
		
		  // �ȴ���������ֵ��Զ�̷���
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
		    
		   
		   // ���¶Է��ؽ�����з���������ķ���ֵ��ʽ��������ο�
		   // http://doc.cooshare.com/apidoc/html/M_CustomMethod_invoke_5_bfd000aa.htm
		   
		   // ObjectBox.NetworkErrorFlag ������Ԥ�����һ������״̬�쳣��־
		   // ����������뷵�ؽ��ֵ�Ƚϣ��ж��Ƿ������쳣
		   
		    if(!returns.equals(ObjectBox.NetworkErrorFlag)){
		    	
		    	if(returns.indexOf(',')!=-1){
		    		
		    		String returnValueFromMethod = StringUtil.split(returns, ",")[0];
		    		String exeReturn = StringUtil.split(returns, ",")[1];
		    		
		    		if(exeReturn.equals("1")){
		    			
		    			Message = "����ִ�гɹ�!\r\n����ֵΪ"+returnValueFromMethod;
		    		}else{
		    			
		    			Message = "����ִ��ʧ��!";
		    		}
		    		
		    	} else{
		    		
		    		if(returns.equals("0")){
		    			
		    			Message = "û���ҵ����Զ��巽��!";
		    			
		    		}else if(returns.equals("-1")){
		    		
		    			Message = "����Ԥ��������ʵ���ṩ������һ��!";
		    			
		    		}else if(returns.equals("-3")){
			    		
		    			Message = "��������!";
			    		
		    		}else if(returns.equals("-4")){
			    		
		    			Message = "��ȫ����֤����!";
		    			
		    		}else{
		    			
		    			Message = "δ֪����!";
		    		}
		    		
		    	}
		    	 
		    }else{
		    	
		    	Message="������ϣ����ʵ�����������!";
		    }
        
		    Toast.makeText(getApplicationContext(),Message,Toast.LENGTH_LONG).show();
	}
	
	
	/*******************
	 * 
	 * ���������ڻ�ȡEP���ƶ���
	 * 
	 * @return void
	 */
	public void GetEpInformation() throws Exception{
		
		// �˴���Ҫ����һ����Զ�̷����������API��ַ
		// ��ȡEP��Ϣ����COS API�����ṩ�Ļ����ӿڣ�
		
		// ����API�ӿ�˵������ο� 
		// http://doc.cooshare.com/apidoc/html/M_BasicOperation_GetEndPoint_4_193e86b9.htm
		
		
		// ע�⣺���еĲ���ֵ��Ҫ���ñ���Base64�������е�Encode_Content()�������д���ǰ����
		
		final String Request_Url_Root = RCManagement.BasicOperation_RemoteHandler+"GetEndPoint?";
		final String Request_Url_Para = "para="+Base64.Encode_Content("15")
									+"&getendpointlistmode="+Base64.Encode_Content("epid")
									+"&cosid="+Base64.Encode_Content(RCManagement.Developer_Id)
									+"&devid="+Base64.Encode_Content(RCManagement.Developer_Id);
		
		// ע�⣺����API���õ����һ������ΪSK������ SecurityKey
		// �乹���ǣ�{Request_Url_Para}��{�������ʺŵ�˽Կ}���ַ��������Ӻ󣬶��µ��ַ����� 16λ MD5����
        final String SecurityKey = MD5Security.md5(Request_Url_Para + RCManagement.Developer_PrivateKey);
        
        // ���������ɵ�API�����ַ���£������߿�ͨ�� Logcat�鿴
        Log.w("MainActivity_GetEpInformation", Request_Url_Root+Request_Url_Para+"&sk="+SecurityKey);
        
        new Thread()
      		{
      			   public void run()
      			   { 						 
      				
      				  // �������������ǿ���������̶����Ĳ���������������һ���µ��߳��н���
     				  // ������ RCManagement�е� Request_POST���������ύAPI����
     				  
     				  // ע�⣺����API���õķ��ؽ�������Ǽ��ܵģ���ͨ�� Base64�������е�Decode_Content()�������н��� 
      				String returns = Base64.Decode_Content((RCManagement.Request_POST(Request_Url_Root, Request_Url_Para+"&sk="+SecurityKey)));
      				 
      				//String returns =RCManagement.Request_POST(Request_Url_Root, Request_Url_Para+"&sk="+SecurityKey);
      				   
      				 
      				 
      				  // ���¶Է��ؽ�����з���������ķ���ֵ��ʽ��������ο�
      				  // http://doc.cooshare.com/apidoc/html/M_BasicOperation_GetEndPoint_4_193e86b9.htm
      			   
      				 
      			      // ObjectBox.NetworkErrorFlag ������Ԥ�����һ������״̬�쳣��־
      			      // ����������뷵�ؽ��ֵ�Ƚϣ��ж��Ƿ������쳣
      				 
		      				 if(returns.equals("0")||returns.equals("-4")||returns.equals(ObjectBox.NetworkErrorFlag)){
		      					 
		      					// ˵��û���ҵ����EP��Ϣ(����0)����ȫ��֤�����(����-4)���򱾵��������
		      					// �����еĿ��ư�ť����Ϊ������
		      					b1.setEnabled(false);
		      					b2.setEnabled(false);
		      					b3.setEnabled(false);
		      					b4.setEnabled(false);
		      					
		      				 }else{
		      					 
		      					 // ��ȡ��EP��Ϣ�󣬶���Ϣ���н���
		      					 
		      					 String[] ep_device_group = StringUtil.split(returns, '|');
		      					 
		      					 for(int x=0;x<ep_device_group.length;x++){
		      						 
		      						 if(!ep_device_group[x].equals("")){
		      							 
		      							 String[] ep_device = StringUtil.split(ep_device_group[x], ",");
		      							 
		      							 // ������EP��Ϣ������EP���룬ʵ����һ�� SmartCindy���󣬲�������õ���������ObjectBox�е�sc��Ա�
		      							 
		      							 ObjectBox.sc =  new SmartCindy(ep_device[0].toString());
		      							 ObjectBox.sc.setEpTypeId(ep_device[1].toString());
		      							 
		      							 Log.w("MainActivity", ep_device[2].toString());
		      							
		      							 
		      							 // �������ĵĽ�������У�����Ҫʹ�� android.util.Base64.decode()���еڶ��ν���
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
	 * ���������ڻ�ȡHCCU����������Ϣ
	 * 
	 * @return void
	 * @throws Exception 
	 */
	public void GetHCCUInformatioin() throws Exception{
		
		final String Request_Url_Root = RCManagement.BasicOperation_RemoteHandler+"GetHCCUNetworkInfo?";
		final String Request_Url_Para = "cosid="+Base64.Encode_Content(RCManagement.Developer_Id)+"&devid="+Base64.Encode_Content(RCManagement.Developer_Id);
		
	    final String SecurityKey = MD5Security.md5(Request_Url_Para + RCManagement.Developer_PrivateKey);
		
	      
        // ���������ɵ�API�����ַ���£������߿�ͨ�� Logcat�鿴
        Log.w("MainActivity_GetHCCUInformation", Request_Url_Root+Request_Url_Para+"&sk="+SecurityKey);
        
        new Thread()
  		{
  			   public void run()
  			   { 						 
  				
  				  // �������������ǿ���������̶����Ĳ���������������һ���µ��߳��н���
 				  // ������ RCManagement�е� Request_POST���������ύAPI����
 				  
 				  // ע�⣺����API���õķ��ؽ�������Ǽ��ܵģ���ͨ�� Base64�������е�Decode_Content()�������н��� 
  				 String returns = Base64.Decode_Content((RCManagement.Request_POST(Request_Url_Root, Request_Url_Para+"&sk="+SecurityKey)));
  				   
  			
  				 
  				  // ���¶Է��ؽ�����з���������ķ���ֵ��ʽ��������ο�
  				  // http://doc.cooshare.com/apidoc/html/M_BasicOperation_GetHCCUNetworkInfo_2_52fce4af.htm
  			   
  				 
  			      // ObjectBox.NetworkErrorFlag ������Ԥ�����һ������״̬�쳣��־
  			      // ����������뷵�ؽ��ֵ�Ƚϣ��ж��Ƿ������쳣
  				 
	      				 if(returns.equals("0")||returns.equals(ObjectBox.NetworkErrorFlag)){
	      					 
	      					// ˵��û���ҵ����HCCU��Ϣ(����0)�򱾵��������
	      					// �����еĿ��ư�ť����Ϊ������
	      					b1.setEnabled(false);
	      					b2.setEnabled(false);
	      					b3.setEnabled(false);
	      					b4.setEnabled(false);
	      					
	      				 }else{
	      					 
	      					 // ��ȡ��HCCU��Ϣ�󣬶���Ϣ���н���
	      					 
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