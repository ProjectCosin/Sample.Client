package com.cooshare.os.dev.sample;

/**************************************************************************************************
Filename:       ObjectBox.java
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



import com.cooshare.os.dev.object.*;

/****

本JAVA文件是一个公共容器, 用于存放SmartCindy智能风扇的实例，以及一些标志位

*/


public class ObjectBox {

	public static SmartCindy sc =  null;
	public static String OperationReturnFlag = "";
	
	public static String NetworkErrorFlag = "CLIENTNETWORKERROR";
	
	public static String HCCU_IP="";
	public static String HCCU_PORT="";
}
