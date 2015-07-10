package com.cooshare.os.dev.remotecontrol;

/**************************************************************************************************
Filename:       StringUtil.java
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




import java.util.Random;
import java.util.Vector;

/****

本JAVA文件是一个工具类，提供字符串常规操作等相关方法，用户无需修改本类！

*/

public class StringUtil {
	
	
	public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
    private StringUtil() {
    }


    static public Vector<String> splitToVector(String original, char separator) {
        String[] tmp = split(original, separator);
        Vector<String> vtmp = new Vector<String>();
        if (tmp.length > 0) {
            for (int i = 0; i < tmp.length; i++) {
                vtmp.addElement(tmp[i]);
            }
        }else{
            return null;
        }
        return vtmp;
    }


    static public String[] split(String original, char separator) {
        return split(original, String.valueOf(separator));
    }


    static public String[] split(String original, String separator) {
        Vector<String> nodes = new Vector<String>();

        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }
        nodes.addElement(original);
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++) {
                result[loop] = (String) nodes.elementAt(loop);
            }
        }
        return result;
    }
    
    synchronized static public Vector<String> splitToNMEAVector(String original){

        Vector<String> nodes = new Vector<String>();
        String separator = "$";

        int index = original.indexOf(separator);
        original =  original.substring(index + separator.length());
        index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(separator + original.substring(0, index));

            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }

        nodes.addElement(separator + original);
        
        return nodes;
    }
    


    public static String reverse(String text) {        
        return new StringBuffer(text).reverse().toString();
    }    
    
    public static String integerToString(int i)
    {
        String str1 = Integer.toString(i);
        if(i<10 && i>=0)
        {
            str1 = "0" + str1;
        }
        return str1;
    }

    public static short parseShort(String value, short defaultValue) {
        short parsed = defaultValue;
        if (value != null) {
            try {
                parsed = Short.parseShort(value);
            } catch (NumberFormatException e) {
                parsed = defaultValue;
            }
        }
        return parsed;
    }

    public static int parseInteger(String value, int defaultValue) {
        int parsed = defaultValue;
        if (value != null) {
            try {
                parsed = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                parsed = defaultValue;
            }
        }
        return parsed;
    }

    public static double parseDouble(String value, double defaultValue) {
        double parsed = defaultValue;
        if (value != null) {
            try {
                parsed = Double.parseDouble(value);
            } catch (NumberFormatException e) {
                parsed = defaultValue;
            }
        }
        return parsed;
    }
    
    public static String replace(String s, String f, String r) {
        if (s == null) {
            return s;
        }
        if (f == null) {
            return s;
        }
        if (r == null) {
            r = "";
        }
        int index01 = s.indexOf(f);
        while (index01 != -1) {
            s = s.substring(0, index01) + r + s.substring(index01 + f.length());
            index01 += r.length();
            index01 = s.indexOf(f, index01);
        }
        return s;
    }

    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(allChar.length())));
        }
        return sb.toString();
    }

    public static String toUnicode(String str) {
		char[] arChar = str.toCharArray();
		int iValue = 0;
		String uStr = "";
		for (int i = 0; i < arChar.length; i++) {
			iValue = (int) str.charAt(i);
			if (iValue <= 256) {
				uStr += "^#x00" + Integer.toHexString(iValue) + ";";
			} else {
				uStr += "^#x" + Integer.toHexString(iValue) + ";";
			}
		}
		return uStr;
	}
    
    public static String decodeUnicode(String theString) {    

		  char aChar;    

		  int len = theString.length();    

		  StringBuffer outBuffer = new StringBuffer(len);    

		  for (int x = 0; x < len;) {    

		   aChar = theString.charAt(x++);    

		   if (aChar == '\\') {    

		    aChar = theString.charAt(x++);    

		    if (aChar == 'u') {    


		     int value = 0;    

		     for (int i = 0; i < 4; i++) {    

		      aChar = theString.charAt(x++);    

		      switch (aChar) {    

		      case '0':    

		      case '1':    

		      case '2':    

		      case '3':    

		      case '4':    

		      case '5':    
		  
		          case '6':    
		           case '7':    
		           case '8':    
		           case '9':    
		            value = (value << 4) + aChar - '0';    
		            break;    
		           case 'a':    
		           case 'b':    
		           case 'c':    
		           case 'd':    
		           case 'e':    
		           case 'f':    
		            value = (value << 4) + 10 + aChar - 'a';    
		           break;    
		           case 'A':    
		           case 'B':    
		           case 'C':    
		           case 'D':    
		           case 'E':    
		           case 'F':    
		            value = (value << 4) + 10 + aChar - 'A';    
		            break;    
		           default:    
		            throw new IllegalArgumentException(    
		              "Malformed   \\uxxxx   encoding.");    
		           }    
		  
		         }    
		          outBuffer.append((char) value);    
		         } else {    
		          if (aChar == 't')    
		           aChar = '\t';    
		          else if (aChar == 'r')    
		           aChar = '\r';    
		  
		          else if (aChar == 'n')    
		  
		           aChar = '\n';    
		  
		          else if (aChar == 'f')    
		  
		           aChar = '\f';    
		  
		          outBuffer.append(aChar);    
		  
		         }    
		  
		        } else   
		  
		        outBuffer.append(aChar);    
		  
		       }    
		  
		       return outBuffer.toString();    
		  
		      }    
}
