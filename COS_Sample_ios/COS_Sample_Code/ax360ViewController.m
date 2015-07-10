//
//  ax360ViewController.m
//  COS_Sample_Code
//
//  Created by cooshareinc on 14-1-16.
//  Copyright (c) 2014年 cooshareinc. All rights reserved.
//

#import "ax360ViewController.h"

@interface ax360ViewController ()
- (IBAction)LC_open:(id)sender;
- (IBAction)LC_close:(id)sender;
- (IBAction)RC_open:(id)sender;
- (IBAction)RC_close:(id)sender;

@end

@implementation ax360ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)LC_open:(id)sender {
    NSString *command = @"15,status,1|";
    [COS_Manager Local_Controller:command];
}

- (IBAction)LC_close:(id)sender {
    
    NSString *command = @"15,status,0|";
    [COS_Manager Local_Controller:command];
}

- (IBAction)RC_open:(id)sender {
    
    NSString *all_ep = [COS_Manager Get_EP_Info];
    NSLog(@"%@",all_ep);
    
    [COS_Manager Remote_Controller:@"15" Method:@"ToggleFanSwitcher" Para:@"1,15"];
}

- (IBAction)RC_close:(id)sender {
    [COS_Manager Remote_Controller:@"15" Method:@"ToggleFanSwitcher" Para:@"0,15"];
}
@end
