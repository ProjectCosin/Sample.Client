/**************************************************************************************************
Filename:       Default.aspx.cs
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
 * 本文件是一个示例WebPage Code-behine文件, 开发者可根据自己的实际需求，设计自己的WebPage
 * 
 * */


using System;
using System.Collections.Generic;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Text;

public partial class _Default : System.Web.UI.Page 
{

    protected void Page_Load(object sender, EventArgs e)
    {

        if (!IsPostBack)
        {


            #region 获取EP信息，本例子中根据EPID值获取，该接口还可以支持根据EP名称,开发者ID,EP模型名称,EP模型编码等方式获取EP信息，具体请参考文档

            string epinfo = Base64.Decode(getEPDeviceInfo("15", "epid"));

            if (epinfo != "0" && epinfo != "-4")
            {
                string[] ep_info_array = epinfo.Split('|');

                for (int x = 0; x < ep_info_array.Length; x++) {

                    if (ep_info_array[x] != "") {

                        string[] single_ep_item_array = ep_info_array[x].Split(',');

                        ViewState["epid"] = single_ep_item_array[0];

                        // 返回值如可能带有中文的，则需要通过以下方式进行解码

                        byte[] epname_b = Convert.FromBase64String(single_ep_item_array[2]);
                        string epname = Encoding.UTF8.GetString(epname_b);

                        this.Label1.Text = "控制对象EP信息:" + single_ep_item_array[0] + ",EP名称:" + epname;

                    }

                }

            }
            else {

                this.Label1.Text = "控制对象EP信息读取错误.";
                this.Button1.Enabled = false;
            }

            #endregion



        }



    }

    /// <summary>
    /// Button事件处理方法
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void Button1_Click(object sender, EventArgs e)
    {

        if (ViewState["epid"] == null)
        {

            this.Label1.Text = "EP设备信息尚未获取";
            return;
        }

        string returns = Base64.Decode(RemoteControl(ViewState["epid"].ToString(), this.DropDownList1.SelectedValue));
        string Message = "";

        if (returns.IndexOf(',') != -1)
        {
            string returnValueFromMethod = returns.Split(',')[0];
            string exeReturn = returns.Split(',')[1];

            if (exeReturn=="1")
            {

                Message = "方法执行成功!\r\n返回值为" + returnValueFromMethod+"  注：远程控制的对象响应时间大约在20~30秒";
            }
            else
            {

                Message = "方法执行失败!";
            }

        }
        else {

            if (returns=="0")
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


        this.Label1.Text = Message;
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
    /// CustomMethod.invoke方法的返回，具体请参考 http://doc.cooshare.com/apidoc/html/b1cac735-ebd3-0cbc-7b79-9f0270bb8d48.htm
    /// </returns>
    public string RemoteControl(string epid, string status)
    {

        string request_parameter = "epid=" + Base64.Encode(epid) + "&method=" + Base64.Encode("ToggleFanSwitcher") + "&para=" + Base64.Encode(status + "," + epid) + "&devid=" + Base64.Encode(Config.Developer_Id);
        string sk = Security.GenerateSK(request_parameter);

        // 提交网络请求
        return RequestMgr.Request_POST(RequestMgr.CustomMethod + "invoke", request_parameter + "&sk=" + sk);
    }

}