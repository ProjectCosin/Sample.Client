//
//  RCManagement.m
//  
//
//  Created by cooshareinc on 14-1-15.
//  Copyright (c) 2014年 cooshareinc. All rights reserved.
//

#import "COS_Manager.h"


static NSString *custom_method_url = @"http://api.dev.cooshare.com/CustomMethod.asmx";
static NSString *basic_operation_url = @"http://api.dev.cooshare.com/BasicOperation.asmx";
static NSString *Developer_PrivateKey = @"100005655453";
static NSString *Developer_Id = @"1";


static struct sockaddr_in pin;
static struct hostent *nlp_host;
static int mSocket;
static char recvBuf[1000];
@implementation COS_Manager

//连接socket
+(BOOL)socket_connect:(NSString *)ip Port:(int)port{
    NSLog(@"socket_connect.");
    //解析域名，如果是IP则不用解析，如果出错，显示错误信息
    while ((nlp_host=gethostbyname([ip UTF8String]))==0){
        NSLog(@"get host by name err.");
        return FALSE;
    }
    
    //设置pin变量，包括协议、地址、端口等，此段可直接复制到自己的程序中
    bzero(&pin,sizeof(pin));
    pin.sin_family=AF_INET; //AF_INET表示使用IPv4
    pin.sin_addr.s_addr=htonl(INADDR_ANY);
    pin.sin_addr.s_addr=((struct in_addr *)(nlp_host->h_addr))->s_addr;
    pin.sin_port=htons(port);
    
    //建立socket
    mSocket = socket(AF_INET,SOCK_STREAM,0);
    
    //建立连接
    while (connect(mSocket,(struct sockaddr*)&pin,sizeof(pin))==-1){
        NSLog(@"connect error !");
        return FALSE;
    }
    return TRUE;
}

//socket发送数据
+(BOOL)socket_sendstring:(NSString *)command{
    NSLog(@"%@",command);
    long send_num = 0;
    if (command.length > 0) {
        send_num = sendto(mSocket, [command UTF8String], strlen([command UTF8String])+1, 0,(struct sockaddr *)&pin, sizeof(pin));
        if (send_num > 0) {
            NSLog(@"socket send success ! content == %@",command);
            return true;
        }else{
            return false;
        }
    }
    
    return true;
}

//socket接受数据
+(NSString *)socket_recstring{
    NSString *ret = @"";
    recv(mSocket, recvBuf, sizeof(recvBuf), 0);
    //    recvfrom(fd,recvBuf,128,0,(struct sockaddr *)&address, (socklen_t*)sizeof(address));
    //    char* ipSvr = inet_ntoa(fd.sin_addr);
    ret = [NSString stringWithUTF8String:recvBuf];
    NSLog(@"%@",ret);
    return ret;
}

//关闭socket
+(BOOL)socket_close{
    shutdown(mSocket, SHUT_RDWR);
    return TRUE;
}

+(NSMutableDictionary *) makeURL_path_invoke_epid:(NSString *)epid method:(NSString *)method para:(NSString *)para devid:(NSString *)devid {
    NSMutableDictionary *dic = [[NSMutableDictionary alloc] init];
    
    [dic setValue:[GTMBase64 Encode_Content:epid] forKey:@"epid"];
    [dic setValue:[GTMBase64 Encode_Content:method] forKey:@"method"];
    [dic setValue:[GTMBase64 Encode_Content:para] forKey:@"para"];
    [dic setValue:[GTMBase64 Encode_Content:devid] forKey:@"devid"];
    
    [dic setValue:[GTMBase64 md5:[NSString stringWithFormat:@"epid=%@&method=%@&para=%@&devid=%@%@",[GTMBase64 Encode_Content:epid],[GTMBase64 Encode_Content:method],[GTMBase64 Encode_Content:para],[GTMBase64 Encode_Content:devid],Developer_PrivateKey]] forKey:@"sk"];
    
    return dic;
}

//获取HCCU的IP和Port
+(NSString *)Get_HCCU_Info{
    
    /* ios http request post */
    NSString * urltmp = basic_operation_url;
    //构造url；
    urltmp = [urltmp stringByAppendingFormat:@"/GetHCCUNetworkInfo"];
    //生成request
    NSMutableURLRequest *loginrequest = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urltmp] cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:20];
    [loginrequest setHTTPMethod:@"POST"];
    //构造url；
    NSString *para = [NSString stringWithFormat:@"devid=%@",[GTMBase64 Encode_Content:Developer_Id] ];
    NSString *sk = [GTMBase64 md5:[para stringByAppendingString:Developer_PrivateKey]];
    para = [NSString stringWithFormat:@"%@&sk=%@",para,sk];
    NSData *data = [para dataUsingEncoding:NSUTF8StringEncoding];
    [loginrequest setHTTPBody:data];
    NSData *returnData = [NSURLConnection sendSynchronousRequest:loginrequest returningResponse:nil error:nil];
    //转换编码格式
    NSString *retdata = [[NSString alloc]initWithData:returnData encoding:NSUTF8StringEncoding];
    //提取返回值,提取的数据放在retvalue中
    NSArray *arr = [retdata componentsSeparatedByString:@">"];
    NSArray *ar = [arr[2] componentsSeparatedByString:@"<"];
    NSString *tmp = [[ar objectAtIndex:0] substringWithRange:NSMakeRange(3, [[ar objectAtIndex:0] length] -7)];
    NSString *return_value = [GTMBase64 decodeBase64String:tmp];
    NSLog(@"%@",return_value);
    return return_value;
}

//获取本DEV_ID下所有的EP信息
+(NSString *)Get_EP_Info{
    
    /* ios http request post */
    NSString * urltmp = basic_operation_url;
    //构造url；
    urltmp = [urltmp stringByAppendingFormat:@"/GetEndPoint"];
    //生成request
    NSMutableURLRequest *loginrequest = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urltmp] cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:20];
    [loginrequest setHTTPMethod:@"POST"];
    //构造url；
    NSString *para = [NSString stringWithFormat:@"para=%@&getendpointlistmode=%@&devid=%@",
                      [GTMBase64 Encode_Content:@"15"],
                      [GTMBase64 Encode_Content:@"epid"],
                      [GTMBase64 Encode_Content:Developer_Id] ];
    NSString *sk = [GTMBase64 md5:[para stringByAppendingString:Developer_PrivateKey]];
    para = [NSString stringWithFormat:@"%@&sk=%@",para,sk];
    NSData *data = [para dataUsingEncoding:NSUTF8StringEncoding];
    [loginrequest setHTTPBody:data];
    NSData *returnData = [NSURLConnection sendSynchronousRequest:loginrequest returningResponse:nil error:nil];
    //转换编码格式
    NSString *retdata = [[NSString alloc]initWithData:returnData encoding:NSUTF8StringEncoding];
    //提取返回值,提取的数据放在retvalue中
    NSArray *arr = [retdata componentsSeparatedByString:@">"];
    NSArray *ar = [arr[2] componentsSeparatedByString:@"<"];
    NSString *tmp = [[ar objectAtIndex:0] substringWithRange:NSMakeRange(3, [[ar objectAtIndex:0] length] -7)];
    NSString *return_value = [GTMBase64 decodeBase64String:tmp];
    NSArray *array = [return_value componentsSeparatedByString:@","];
    NSString *name = [GTMBase64 decodeBase64String:[array objectAtIndex:2]];
    return_value = [return_value stringByReplacingOccurrencesOfString:array[2] withString:name ];
    
    return return_value;
}

//远程控制指令发送
+(NSString *)Remote_Controller:(NSString *)epid Method:(NSString *)method Para:(NSString *)parameters {
    /* ios http request post */
    NSString * urltmp = custom_method_url;
    //构造url；
    urltmp = [urltmp stringByAppendingFormat:@"/invoke"];
    NSLog(@"%@",urltmp);
    //生成request
    NSMutableURLRequest *loginrequest = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urltmp] cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:20];
    [loginrequest setHTTPMethod:@"POST"];
    //构造url；
    NSString *para = [NSString stringWithFormat:@"epid=%@&method=%@&para=%@&devid=%@",
                      [GTMBase64 Encode_Content:epid],
                      [GTMBase64 Encode_Content:method],
                      [GTMBase64 Encode_Content:parameters],
                      [GTMBase64 Encode_Content:Developer_Id] ];
    NSString *sk = [GTMBase64 md5:[para stringByAppendingString:Developer_PrivateKey]];
    para = [NSString stringWithFormat:@"%@&sk=%@",para,sk];
    
    NSData *data = [para dataUsingEncoding:NSUTF8StringEncoding];
    [loginrequest setHTTPBody:data];
    NSData *returnData = [NSURLConnection sendSynchronousRequest:loginrequest returningResponse:nil error:nil];
    //转换编码格式
    NSString *retdata = [[NSString alloc]initWithData:returnData encoding:NSUTF8StringEncoding];
    //提取返回值,提取的数据放在retvalue中
    NSArray *arr = [retdata componentsSeparatedByString:@">"];
    NSArray *ar = [arr[2] componentsSeparatedByString:@"<"];
    NSString *tmp = [[ar objectAtIndex:0] substringWithRange:NSMakeRange(3, [[ar objectAtIndex:0] length] -7)];
    NSString *return_value = [GTMBase64 decodeBase64String:tmp];
    NSLog(@"%@",return_value);
    return return_value;
}

//初始化本地控制
+(void)Loal_Controller_init{
    NSArray *ip_port = [[self Get_HCCU_Info] componentsSeparatedByString:@","];
    NSArray *port = [[ip_port objectAtIndex:1] componentsSeparatedByString:@"|"];
    [self socket_connect:[ip_port objectAtIndex:0] Port:[[port objectAtIndex:0] intValue]];
}

//本地控制指令发送
+(NSString *)Local_Controller:(NSString *)command{

    NSString *sendpacket = [[command stringByAppendingString:@"^"] stringByAppendingString:[GTMBase64 md5:[command stringByAppendingString:Developer_PrivateKey]]];

    [self socket_sendstring:sendpacket];
    NSString *ret = [self socket_recstring];
    return ret;
}
@end
