/**************************************************************************************************
Filename:       SocketMgr.cs
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
 * 本文件是一个Socket相关方法的工具类, 开发者无需修改此文件！
 * 
 * */

using System;
using System.Collections.Generic;
using System.Text;
using System.Data;
using System.Net.Sockets;
using System.Net;
using System.Threading;

namespace COS_SAMPLE_WINFORM
{
    public class SocketMgr
    {

        static byte[] result = new byte[1024];
        

        /// <summary>
        /// Socket连接方法
        /// </summary>
        /// <param name="hostip">HCCU的IP地址</param>
        /// <param name="port">HCCU的SOCKET PORT</param>
        /// <param name="sendMessage">发送的SOCKET内容</param>
        /// <returns>返回码, 1代表发送成功，2代表发送失败</returns>
        static public  string SendSocket(string hostip,string port,string sendMessage)
        {
            //设定服务器IP地址  
            IPAddress ip = IPAddress.Parse(hostip);
            Socket clientSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

            try
            {
                clientSocket.Connect(new IPEndPoint(ip, int.Parse(port))); //配置服务器IP与端口  
            }
            catch
            {
                return "-1";
            }

            //通过clientSocket接收数据  
            int receiveLength = clientSocket.Receive(result);
            Console.WriteLine("接收服务器消息：{0}", Encoding.ASCII.GetString(result, 0, receiveLength));

            //通过 clientSocket 发送数据  
            try
            {
                clientSocket.Send(Encoding.ASCII.GetBytes(sendMessage));
                Console.WriteLine("向服务器发送消息：{0}" + sendMessage);

                clientSocket.Shutdown(SocketShutdown.Both);
                clientSocket.Close();

                return "1";
            }
            catch
            {
                clientSocket.Shutdown(SocketShutdown.Both);
                clientSocket.Close();
                return "-1";
            } 
        }
    }

    
}
