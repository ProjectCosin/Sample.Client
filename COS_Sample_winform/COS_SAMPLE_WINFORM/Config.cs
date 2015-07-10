/**************************************************************************************************
Filename:       Config.cs
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



using System;
using System.Collections.Generic;
using System.Web;

/// <summary>
///Config 的摘要说明
/// </summary>
public class Config
{

    /**
     * 开发者只需要配置以下两行代码，将开发者自己的 DeveloperId 与 PrivateKey配置到以下变量中
     * */
    static public string Developer_Id = "1";
    static public string Developer_PrivateKey = "100005655453";

	public Config()
	{
		//
		//TODO: 在此处添加构造函数逻辑
		//
	}

    
}