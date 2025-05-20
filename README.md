# cordova-plugin-ocr-alpha (ML Kit)

THIS IS A WORK IN PROGRESS

Static-image OCR for Cordova, powered by Google ML Kit on-device text recognition (no Firebase).

## Installation

```bash
cordova plugin add cordova-plugin-ocr-alpha
cordova plugin add cordova-plugin-camera
```

### Android

1. Build your app normally; ML Kit artifact is included via Gradle.

### iOS

1. From `platforms/ios/`, run:
   ```bash
   pod install
   ```
2. Open the `.xcworkspace` and build.

## Usage

```js
navigator.camera.getPicture(onSuccess, onError, {
  quality: 80,
  destinationType: Camera.DestinationType.FILE_URI
});

function onSuccess(imageURI) {
  cordova.plugins.Ocr.scanImage(imageURI,
  function(text) {
    alert(text);
  },
  function(err) {
  	alert(err);
  }
);
}

function onError(msg) {
  alert("Camera error: " + msg);
}
```
