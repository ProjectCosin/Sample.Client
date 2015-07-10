/**************************************************************************************************
Filename:       Request.cs
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
 * 本类提供一个HTTP请求的工具类，开发者无需更改此类！
 * 
 * */

using System;
using System.Collections.Generic;
using System.Web;
using System.Net;
using System.Text;
using System.IO;
using System.Xml;

/// <summary>
///Request 的摘要说明
/// </summary>
public class RequestMgr
{
    public RequestMgr()
	{
		//
		//TODO: 在此处添加构造函数逻辑
		//
	}

    static public string BasicOperation = "http://api.dev.cooshare.com/BasicOperation.asmx/";
    static public string CustomMethod = "http://api.dev.cooshare.com/CustomMethod.asmx/";

    static public string Request_POST(string rooturl, string param)
    {

        HttpWebRequest req = (HttpWebRequest)HttpWebRequest.Create(rooturl);
        Encoding encoding = Encoding.UTF8;
        byte[] bs = Encoding.ASCII.GetBytes(param);
        string responseData = String.Empty;
        req.Method = "POST";
        req.ContentType = "application/x-www-form-urlencoded";
        req.ContentLength = bs.Length;
        using (Stream reqStream = req.GetRequestStream())
        {
            reqStream.Write(bs, 0, bs.Length);
            reqStream.Close();
        }
        using (HttpWebResponse response = (HttpWebResponse)req.GetResponse())
        {
            using (StreamReader reader = new StreamReader(response.GetResponseStream(), encoding))
            {
                responseData = reader.ReadToEnd().ToString();
            }
            
        }

        XmlDocument doc = new XmlDocument();
        doc.LoadXml(responseData);
        XmlElement root = doc.DocumentElement;
        return root.InnerText;

    }


    #region  private  string  UnicodeString(  string  inputString  )
    ///  <summary>  
    ///  对字符串加入头"&#x"和尾";"  
    ///  </summary>  
    ///  <param  name="_Str"></param>  
    ///  <returns></returns>  
    private static string UnicodeString(string inputString)
    {
        return "&#x" + inputString + ";";
    }
    #endregion

    #region  public  static  string  Encode(  string  inputString  )
    ///  <summary>  
    ///  Unicode编码  
    ///  </summary>  
    ///  <param  name="_CStr">源字符串</param>  
    ///  <returns>目的字符串</returns>  
    public static string Encode(string inputString)
    {
        return encode(inputString);
    }

    private static string encode(string inputString)
    {
        inputString = inputString.Replace("&", "&amp;").Replace("#", "＃");
        string unicodeString = "";
        UnicodeEncoding unicodeEncoding = new UnicodeEncoding();
        byte[] bOut = unicodeEncoding.GetBytes(inputString);
        for (int i = 0; i < bOut.Length; i++)
        {
            string lowChar = bOut[i].ToString("X");
            i++;
            string highChar = bOut[i].ToString("X");

            if (lowChar.Length == 1)
            {
                lowChar = "0" + lowChar;
            }

            if (highChar.Length == 1)
            {
                highChar = "0" + highChar;
            }

            //如果不是中文字符（高位为0）则解码，否则加入Unicode头"&#x"和";"

            if (bOut[i] == 0)
            {
                unicodeString += Decode(highChar + lowChar);
            }
            else
            {
                unicodeString += UnicodeString(highChar + lowChar);
            }
        }

        return unicodeString.Trim();
    }

    #endregion

    #region  public  static  string  Decode(  string  inputString  )
    ///  <summary>  
    ///  Unicode解码  
    ///  </summary>  
    ///  <param  name="_UStr">源字符串</param>  
    ///  <returns>目的字符串</returns>  
    public static string Decode(string inputString)
    {

        return decode(inputString);
    }

  

    private static string decode(string inputString)
    {
        string decodeString = "";
        string tempString = "";
        string finalreturn = "";

        string[] Anay_str = inputString.ToString().Split(';');
        for (int z = 0; z < Anay_str.Length; z++)
        {

            int pivo = Anay_str[z].ToString().IndexOf('&');

            if (pivo == 0)
            {
                decodeString = "";
                tempString = Anay_str[z].ToString().Replace("&#x", "");
                int iASCII = int.Parse(tempString, System.Globalization.NumberStyles.HexNumber);
                decodeString = decodeString + (char)iASCII;
            }
            else if (pivo > 0)
            {
                decodeString = "";
                string ordinaryString = Anay_str[z].ToString().Substring(0, pivo);
                tempString = Anay_str[z].ToString().Substring(pivo, Anay_str[z].ToString().Length - pivo);
                tempString = tempString.Replace("&#x", "");
                int iASCII = int.Parse(tempString, System.Globalization.NumberStyles.HexNumber);
                decodeString = decodeString + (char)iASCII;
                decodeString = ordinaryString + decodeString;

            }
            else if (pivo == -1)
            {
                decodeString = "";
                decodeString = Anay_str[z].ToString();

            }

            finalreturn += decodeString;

        }

        return finalreturn;
    }


 
    #endregion
}