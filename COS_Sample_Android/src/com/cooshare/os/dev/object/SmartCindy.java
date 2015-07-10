package com.cooshare.os.dev.object;

/**************************************************************************************************
Filename:       SmartCindy.java
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

/****

本JAVA文件是一个对象类，在本项目中，控制对象是一个 SmartCindy智能风扇
本类属性、属性读写方法都是依据该对象进行设计的，用户可根据自己控制的对象，自由设计本类成员。

*/

public class SmartCindy {
	
	public String _Status;
	public String _EpId;
	public String _EpTypeId;
	public String _Unicode_EpUserDefinedAlias;
	public String _EpProductId;
	public String _HccuId;
	public String _EpMac;
	
	
	public SmartCindy(){
		
	}
	
	public SmartCindy(String EPID){
		
		this._EpId = EPID;
	}
	
	public void setStatus(String Status){ this._Status = Status; }
	
	public void setEpTypeId(String EpTypeId){ this._EpTypeId = EpTypeId;}
	
	public void setUnicode_EpUserDefinedAlias(String Unicode_EpUserDefinedAlias){ this._Unicode_EpUserDefinedAlias = Unicode_EpUserDefinedAlias;}
	
	public void setEpProductId(String EpProductId){ this._EpProductId = EpProductId;}
	
	public void setHccuId(String HccuId){ this._HccuId = HccuId;}
	
	public void setEpMac(String EpMac){ this._EpMac = EpMac;}
	
	
	
	public String getStatus() { return this._Status; }
	
	public String getEpId(){ return this._EpId;}
	
	public String getEpTypeId(){ return this._EpTypeId;}
	
	public String getUnicode_EpUserDefinedAlias(){ return this._Unicode_EpUserDefinedAlias;}
	
	public String getEpProductId(){ return this._EpProductId;}
	
	public String getHccuId(){ return this._HccuId;}
	
	public String getEpMac(){ return this._EpMac;}
	
	
	
	
	

}
