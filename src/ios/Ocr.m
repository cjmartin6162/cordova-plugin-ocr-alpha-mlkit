#import "Ocr.h"
#import <MLKitTextRecognition/MLKitTextRecognition.h>
#import <MLKitTextRecognitionCommon/MLKitTextRecognitionCommon.h>

@implementation Ocr

- (void)scanImage:(CDVInvokedUrlCommand*)command {
  NSString* uri = command.arguments[0];
  NSString* path = [uri stringByReplacingOccurrencesOfString:@"file://" withString:@""];
  UIImage* img = [UIImage imageWithContentsOfFile:path];
  if (!img) {
    CDVPluginResult* res = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                               messageAsString:@"Could not load image"];
    [self.commandDelegate sendPluginResult:res callbackId:command.callbackId];
    return;
  }

  MLKVisionImage* visionImage = [[MLKVisionImage alloc] initWithImage:img];
  MLKTextRecognizer* recognizer = [MLKTextRecognizer textRecognizer];

  [recognizer processImage:visionImage
               completion:^(MLKText * _Nullable text, NSError * _Nullable error) {
    CDVPluginResult* res;
    if (error || !text) {
      res = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                            messageAsString:error.localizedDescription];
    } else {
      res = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                            messageAsString:text.text];
    }
    [self.commandDelegate sendPluginResult:res callbackId:command.callbackId];
  }];
}

@end
