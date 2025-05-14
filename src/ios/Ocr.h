#import <Cordova/CDV.h>

@interface Ocr : CDVPlugin
- (void)scanImage:(CDVInvokedUrlCommand*)command;
@end
