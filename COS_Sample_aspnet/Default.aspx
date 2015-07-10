<%@ Page Language="C#" AutoEventWireup="true"  CodeFile="Default.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body>
    <form id="form1" runat="server">
    <div>
            
        对SmartCindy智能风扇进行操作：<br />
        <br />
        <asp:Label ID="Label1" runat="server" Text="Label"></asp:Label>
        <br />
        <br />
        <asp:DropDownList ID="DropDownList1" runat="server">
        <asp:ListItem Text="开" Value="1"></asp:ListItem>
        <asp:ListItem Text="关" Value="0"></asp:ListItem>
        </asp:DropDownList>
            
    &nbsp;<asp:Button ID="Button1" runat="server" Text="确定" onclick="Button1_Click" />
            
    </div>
    </form>
</body>
</html>
