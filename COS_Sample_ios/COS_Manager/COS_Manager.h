//
//  COS_Manager.h
//
//
//  Created by cooshareinc on 14-1-15.
//  Copyright (c) 2014年 cooshareinc. All rights reserved.
//

#import <Foundation/Foundation.h>

#include "GTMDefines.h"
#include "GTMBase64.h"

#include <sys/types.h>
#include <sys/socket.h>
#include <string.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>

@interface COS_Manager : NSObject

+(BOOL)socket_connect:(NSString *)ip Port:(int)port; //连接socket
+(BOOL)socket_sendstring:(NSString *)content;//发送数据
+(NSString *)socket_recstring;//接受数据
+(BOOL)socket_close;//关闭socket

+(NSMutableDictionary *) makeURL_path_invoke_epid:(NSString *)epid method:(NSString *)method para:(NSString *)para devid:(NSString *)devid ;

+(NSString *)Get_EP_Info;
+(NSString *)Get_HCCU_Info;
+(NSString *)Remote_Controller:(NSString *)epid Method:(NSString *)method Para:(NSString *)parameters;
+(void)Loal_Controller_init;
+(NSString *)Local_Controller:(NSString *)command;

@end
