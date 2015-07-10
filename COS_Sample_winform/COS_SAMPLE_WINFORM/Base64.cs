/**************************************************************************************************
Filename:       Base64.cs
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

/**
 * 
 * 本类提供一个Base64相关方法的工具类，开发者无需更改此类！
 * 
 * */


using System;
using System.Collections.Generic;
using System.Web;
using System.Text;

/// <summary>
///Base64 的摘要说明
/// </summary>
public class Base64
{
	public Base64()
	{
		//
		//TODO: 在此处添加构造函数逻辑
		//
	}

    public static string Encode(string text){

        byte[] bytes = Encoding.Default.GetBytes(text);
        return GenerateRandom(3) + Convert.ToBase64String(bytes) + GenerateRandom(4);
    
    }

    public static string Decode(string text) {

        text = text.Substring(3, text.Length - 7);
        byte[] outputb = Convert.FromBase64String(text);
        return Encoding.Default.GetString(outputb);

    }


    private static char[] constant =
        {
           '0','1','2','3','4','5','6','7','8','9',
           'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
           'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
        };
    private static string GenerateRandom(int Length)
    {
        System.Text.StringBuilder newRandom = new System.Text.StringBuilder(62);
        Random rd = new Random();
        for (int i = 0; i < Length; i++)
        {
            newRandom.Append(constant[rd.Next(62)]);
        }
        return newRandom.ToString();
    }

}