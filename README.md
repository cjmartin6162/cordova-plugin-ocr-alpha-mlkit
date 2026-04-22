# cordova-plugin-ocr-alpha-mlkit

Static-image OCR for Cordova using Google ML Kit on-device text recognition (Latin script).

## JavaScript API

```js
cordova.plugins.Ocr.scanImage(fileUri, success, error)
```

The native service name is `Ocr`. It is registered automatically from this plugin’s `plugin.xml`; you do not need to add an iOS `<feature>` to the host app’s `config.xml`.

## Install

From Git:

```bash
cordova plugin add https://github.com/cjmartin6162/cordova-plugin-ocr-alpha-mlkit.git
```

Common companion plugins in Alpha Anywhere workflows:

```bash
cordova plugin add cordova-plugin-camera
cordova plugin add https://github.com/obeza/cordova-plugin-crop-with-ratio.git
cordova plugin add cordova-plugin-filepath
```

### Cordova CLI / platforms

- Cordova CLI **12+** (matches `plugin.xml` engines).
- **cordova-ios** **7.1.1** or **8.0.1** (and other 7.x / 8.x releases in that range).

## iOS (CocoaPods)

This plugin declares **`GoogleMLKit/TextRecognition`** in `plugin.xml`. Cordova merges it into the iOS `Podfile` on both cordova-ios 7.x and 8.x.

After adding or upgrading the plugin, reinstall the iOS platform (or run `cordova prepare ios`), then install pods:

```bash
cordova prepare ios
cd platforms/ios
pod install
```

Open **`App.xcworkspace`** (or `*.xcworkspace`) in Xcode, not the `.xcodeproj`.

You do **not** need to copy an `<feature name="Ocr">` block into the application `config.xml`; the plugin injects it.

### iOS requirements (this ML Kit line)

| Requirement | Notes |
|-------------|--------|
| **Minimum iOS** | **12.0** (plugin preference + ML Kit policy). |
| **Xcode** | **12.4+** recommended by Google for ML Kit; use a current Xcode that matches your cordova-ios and iOS SDK. |
| **Device** | ML Kit text recognition targets **64-bit** devices. |

## Alpha Anywhere `config.xml` example

```xml
<plugin name="cordova-plugin-ocr-alpha-mlkit" spec="https://github.com/cjmartin6162/cordova-plugin-ocr-alpha-mlkit.git" source="git" />
<plugin name="cordova-plugin-crop-with-ratio" spec="https://github.com/obeza/cordova-plugin-crop-with-ratio.git" source="git" />
<plugin name="cordova-plugin-filepath" source="npm" />
```

## Usage

Success callback receives the recognized text as a **string** (plain text from `MLKText.text`).

```js
navigator.camera.getPicture(onSuccess, onError, {
  quality: 80,
  destinationType: Camera.DestinationType.FILE_URI,
  correctOrientation: true
});

function runOCR(filePath) {
  cordova.plugins.Ocr.scanImage(
    filePath,
    function (result) {
      alert(result);
    },
    function (err) {
      alert('OCR error: ' + err);
    }
  );
}
```

## Clean rebuild after plugin changes

```bash
cordova plugin rm cordova-plugin-ocr-alpha-mlkit
cordova plugin add https://github.com/cjmartin6162/cordova-plugin-ocr-alpha-mlkit.git
cordova platform rm ios
cordova platform add ios
cd platforms/ios
pod install
```
