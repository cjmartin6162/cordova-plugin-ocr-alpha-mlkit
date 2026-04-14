#import "Ocr.h"
#import <Cordova/CDV.h>
#import <UIKit/UIKit.h>
#import <GoogleMLKit/TextRecognition.h>

@implementation Ocr

- (void)scanImage:(CDVInvokedUrlCommand*)command {
    NSString *uri = command.arguments.count > 0 ? command.arguments[0] : nil;
    if (uri == nil || ![uri isKindOfClass:[NSString class]] || uri.length == 0) {
        CDVPluginResult *res = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                 messageAsString:@"Missing image URI"];
        [self.commandDelegate sendPluginResult:res callbackId:command.callbackId];
        return;
    }

    NSString *path = [uri stringByReplacingOccurrencesOfString:@"file://" withString:@""];
    UIImage *img = [UIImage imageWithContentsOfFile:path];

    if (!img) {
        CDVPluginResult *res = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                 messageAsString:@"Could not load image"];
        [self.commandDelegate sendPluginResult:res callbackId:command.callbackId];
        return;
    }

    MLKVisionImage *visionImage = [[MLKVisionImage alloc] initWithImage:img];
    MLKTextRecognizer *recognizer = [MLKTextRecognizer textRecognizer];

    [recognizer processImage:visionImage
                  completion:^(MLKText * _Nullable text, NSError * _Nullable error) {
        CDVPluginResult *res = nil;

        if (error || !text) {
            NSString *message = error.localizedDescription ?: @"No text recognized";
            res = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                    messageAsString:message];
        } else {
            res = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                    messageAsString:text.text ?: @""];
        }

        [self.commandDelegate sendPluginResult:res callbackId:command.callbackId];
    }];
}

@end
