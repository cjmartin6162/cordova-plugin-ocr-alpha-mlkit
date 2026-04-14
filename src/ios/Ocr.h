#import <Cordova/CDVPlugin.h>

@interface Ocr : CDVPlugin
- (void)scanImage:(CDVInvokedUrlCommand*)command;
@end
