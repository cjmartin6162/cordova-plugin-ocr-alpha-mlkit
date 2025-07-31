# cordova-plugin-ocr-alpha (ML Kit)

THIS IS A WORK IN PROGRESS

Static-image OCR for Cordova, powered by Google ML Kit on-device text recognition (no Firebase).

## Installation

```bash
cordova plugin add cordova-plugin-ocr-alpha
cordova plugin add cordova-plugin-camera
```
### config.xml
	<plugin name="cordova-plugin-ocr-alpha-mlkit" spec="https://github.com/cjmartin6162/cordova-plugin-ocr-alpha-mlkit.git" source="git" />
		<config-file parent="/*" target="config.xml">
  		<feature name="Ocr">
    		<!-- exactly the Objective-C class name from Ocr.h -->
    		<param name="ios-package" value="Ocr"/>
  		</feature>
		</config-file>
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
  destinationType: Camera.DestinationType.FILE_URI,
  correctOrientation: true
});

function extractFilePath(response) {
  try {
    if (typeof response === 'string') {
      const parsed = JSON.parse(response);
      return parsed.filename;
    } else if (response.filename) {
      return response.filename;
    }
  } catch (e) {
    alert("Invalid image response format: " + e.message);
  }
  return null;
}

function onSuccess(imageData) {
  const imageURI = extractFilePath(imageData);
  if (!imageURI) {
    alert("Image capture failed or was cancelled.");
    return;
  }

  window.plugins.crop(
    function(croppedImageURI) {
      if (!croppedImageURI) {
        alert("Cropped image not found.");
        return;
      }

      if (window.FilePath) {
        window.FilePath.resolveNativePath(
          croppedImageURI,
          function(nativePath) {
            runOCR(nativePath);
          },
          function(err) {
            alert("Error resolving native path: " + err);
          }
        );
      } else {
        runOCR(croppedImageURI);
      }
    },
    function(error) {
      alert("Cropping failed: " + error);
    },
    imageURI,
    {
      quality: 100,
      targetWidth: -1,
      targetHeight: -1,
      ratioX: 4,
      ratioY: 3,
      fixRatio: true
    }
  );
}

function runOCR(filePath) {
  cordova.plugins.Ocr.scanImage(
    filePath,
    function(result) {
      let parsedResult;
      try {
        parsedResult = (typeof result === "string") ? JSON.parse(result) : result;
      } catch (e) {
        if (typeof dialog !== 'undefined' && dialog.object && typeof dialog.object.setValue === 'function') {
          alert('OCR_Text', result);
        } else {
          alert('OCR_Text', result);
        }
        return;
      }

      const s = formatRecognizedText(parsedResult);

      if (typeof dialog !== 'undefined' && dialog.object && typeof dialog.object.setValue === 'function') {
        {dialog.object}.setValue('OCR_Text', s);
      } else {
        {dialog.object}.setValue('OCR_Text', s);
        //alert(s);
      }
    },
    function(err) {
      alert("OCR error: " + err);
    }
  );
}

function onError(msg) {
  alert("Camera error: " + msg);
}

function formatRecognizedText(textResult) {
  let formatted = "";

  if (textResult && Array.isArray(textResult.blocks)) {
    for (const block of textResult.blocks) {
      if (Array.isArray(block.lines)) {
        for (const line of block.lines) {
          formatted += line.text + "\r\n";
        }
      }
    }
  } else if (textResult && typeof textResult.text === "string") {
    formatted = textResult.text.replace(/\n/g, "\r\n\r\n");
  } else {
    formatted = "[No text recognized]";
  }

  return formatted;
}
```
