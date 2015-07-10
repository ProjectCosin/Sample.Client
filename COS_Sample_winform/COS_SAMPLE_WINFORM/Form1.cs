/**************************************************************************************************
Filename:       Form1.cs
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
 * 本文件是一个示例Windows Form 代码文件, 开发者可根据自己的实际需求，设计自己的Windows Form
 * 
 * */


using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace COS_SAMPLE_WINFORM
{
    public partial class Form1 : Form
    {
        private string epid = "";
        private string hccu_ip = "";
        private string hccu_port = "";
        string[] hccu_info_array = null;

        public Form1()
        {
            InitializeComponent();


            #region 获取EP信息，本例子中根据EPID值获取，该接口还可以支持根据EP名称,开发者ID,EP模型名称,EP模型编码等方式获取EP信息，具体请参考文档

            string epinfo = Base64.Decode(getEPDeviceInfo("15", "epid"));

            if (epinfo != "0" && epinfo != "-4")
            {
                string[] ep_info_array = epinfo.Split('|');

                for (int x = 0; x < ep_info_array.Length; x++)
                {

                    if (ep_info_array[x] != "")
                    {

                        string[] single_ep_item_array = ep_info_array[x].Split(',');

                        epid = single_ep_item_array[0];

                        // 返回值如可能带有中文的，则需要通过以下方式进行解码

                        byte[] epname_b = Convert.FromBase64String(single_ep_item_array[2]);
                        string epname = Encoding.UTF8.GetString(epname_b);

                        this.label2.Text = "控制对象EP信息:" + single_ep_item_array[0] + ",EP名称:" + epname;

                    }

                }

            }
            else
            {

                this.label2.Text = "控制对象EP信息读取错误.";
                this.button1.Enabled = false;
                this.button2.Enabled = false;
                this.button3.Enabled = false;
                this.button4.Enabled = false;
            }

            #endregion

            #region 获取HCCU本地网络的信息

            string hccu_info = Base64.Decode(getHCCUDeviceInfo());

            if (hccu_info == "0")
            {

                this.label2.Text = "无法获取HCCU信息";
                this.button1.Enabled = false;
                this.button2.Enabled = false;
                this.button3.Enabled = false;
                this.button4.Enabled = false;
            }
            else {
                
                hccu_info_array = hccu_info.Split('|');

                this.label3.Text = "HCCU本地网络信息已经获取";

            }

            #endregion


        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (hccu_info_array==null)
            {
                this.label2.Text = "HCCU本地网络信息尚未获取.";
                return;
            }

            string command = epid + ",status,1|";
            string command_md5 = Security.GenerateSK(epid + ",status,1|" + Config.Developer_PrivateKey);
            string command_toSend = command + "^" + command_md5;

            for (int x = 0; x < hccu_info_array.Length; x++)
            {

                if (hccu_info_array[x].ToString() != "")
                {

                    hccu_ip = hccu_info_array[x].Split(',')[0];
                    hccu_port = hccu_info_array[x].Split(',')[1];

                    if (SocketMgr.SendSocket(hccu_ip, hccu_port, command_toSend) == "1")
                    {
                        this.label2.Text = "本地指令发送成功";
                    }
                    else
                    {
                        this.label2.Text = "本地指令发送失败";
                    }
                }
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            if (hccu_info_array==null)
            {
                this.label2.Text = "HCCU本地网络信息尚未获取.";
                return;
            }

            string command = epid + ",status,0|";
            string command_md5 = Security.GenerateSK(epid + ",status,1|" + Config.Developer_PrivateKey);
            string command_toSend = command + "^" + command_md5;


            for (int x = 0; x < hccu_info_array.Length; x++) {

                if (hccu_info_array[x].ToString() != "")
                {

                    hccu_ip = hccu_info_array[x].Split(',')[0];
                    hccu_port = hccu_info_array[x].Split(',')[1];

                    if (SocketMgr.SendSocket(hccu_ip, hccu_port, command_toSend) == "1")
                    {
                        this.label2.Text = "本地指令发送成功";
                    }
                    else
                    {
                        this.label2.Text = "本地指令发送失败";
                    }
                }
            }

               
            
        }

        private void button3_Click(object sender, EventArgs e)
        {
             RemoteControl(epid, "1");

          

        }

        private void button4_Click(object sender, EventArgs e)
        {
            RemoteControl(epid, "0");

           
        }


        /// <summary>
        /// 获取EP信息
        /// </summary>
        /// <param name="epid">EPID</param>
        /// <param name="getendpointlistmode">查询依据的类型</param>
        /// <returns>
        /// BasicOperation.GetEndPoint方法的返回，具体请参考 http://doc.cooshare.com/apidoc/html/85547527-eb9e-8385-7350-21205362dc92.htm 
        /// </returns>
        public string getEPDeviceInfo(string epid, string getendpointlistmode)
        {

            string request_parameter = "para=" + Base64.Encode(epid) + "&getendpointlistmode=" + Base64.Encode(getendpointlistmode) + "&cosid=" + Base64.Encode(Config.Developer_Id) + "&devid=" + Base64.Encode(Config.Developer_Id);
            string sk = Security.GenerateSK(request_parameter);

            // 提交网络请求
            return RequestMgr.Request_POST(RequestMgr.BasicOperation + "GetEndPoint", request_parameter + "&sk=" + sk);
        }


        /// <summary>
        /// 远程控制方法
        /// </summary>
        /// <param name="epid">EPID</param>
        /// <param name="status">SmartCindy风扇的状态值</param>
        /// <returns>
        /// </returns>
        public void RemoteControl(string epid, string status)
        {
            if (epid == "") {

                this.label2.Text = "未获取EP设备信息，请稍后.";
                return;
            }


            string request_parameter = "epid=" + Base64.Encode(epid) + "&method=" + Base64.Encode("ToggleFanSwitcher") + "&para=" + Base64.Encode(status + "," + epid) + "&devid=" + Base64.Encode(Config.Developer_Id);
            string sk = Security.GenerateSK(request_parameter);

            // 提交网络请求
            // CustomMethod.invoke方法的返回，具体请参考 http://doc.cooshare.com/apidoc/html/b1cac735-ebd3-0cbc-7b79-9f0270bb8d48.htm

            string returns = Base64.Decode(RequestMgr.Request_POST(RequestMgr.CustomMethod + "invoke", request_parameter + "&sk=" + sk));

            string Message = "";

            if (returns.IndexOf(',') != -1)
            {
                string returnValueFromMethod = returns.Split(',')[0];
                string exeReturn = returns.Split(',')[1];

                if (exeReturn == "1")
                {

                    Message = "方法执行成功!\r\n返回值为" + returnValueFromMethod + "  \r\n注：远程控制的对象响应时间大约在20~30秒";
                }
                else
                {

                    Message = "方法执行失败!";
                }

            }
            else
            {

                if (returns == "0")
                {
                    Message = "没有找到该自定义方法!";
                }
                else if (returns == "-1")
                {
                    Message = "方法预订参数与实际提供参数不一致!";
                }
                else if (returns == "-3")
                {
                    Message = "方法故障!";
                }
                else if (returns == "-4")
                {
                    Message = "安全码验证错误!";
                }
                else
                {
                    Message = "未知错误!";
                }

            }


            this.label2.Text = Message;
        }

        /// <summary>
        /// 获取HCCU本地网络信息
        /// </summary>
        /// <returns>
        /// 正常返回 IP,PORT
        /// 如返回0：说明无相关信息
        /// </returns>
        public string getHCCUDeviceInfo() {

            string request_parameter = "cosid=" + Base64.Encode(Config.Developer_Id) + "&devid=" + Base64.Encode(Config.Developer_Id);
            string sk = Security.GenerateSK(request_parameter);

            // 提交网络请求
            return RequestMgr.Request_POST(RequestMgr.BasicOperation + "GetHCCUNetworkInfo", request_parameter + "&sk=" + sk);
        
        }



    }
}
