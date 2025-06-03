package com.example.ocr;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Ocr extends CordovaPlugin {

  @Override
  public boolean execute(String action, JSONArray args, final CallbackContext cb) throws JSONException {
    if (!"scanImage".equals(action)) return false;

    final String uri = args.getString(0);
    cordova.getThreadPool().execute(() -> {
      try {
        String path = uri.replace("file://", "");
        Bitmap bmp = BitmapFactory.decodeFile(path);
        if (bmp == null) {
          cb.error("Could not load image at " + path);
          return;
        }

        InputImage image = InputImage.fromBitmap(bmp, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        //recognizer.process(image)
         // .addOnSuccessListener(result -> cb.success(result.getText()))
         // .addOnFailureListener(e -> cb.error(e.getMessage()));
        recognizer.process(image)
    .addOnSuccessListener(new OnSuccessListener<Text>() {
        @Override
        public void onSuccess(Text visionText) {
            try {
                JSONArray blocksArray = new JSONArray();

                for (Text.TextBlock block : visionText.getTextBlocks()) {
                    JSONObject blockObj = new JSONObject();
                    JSONArray linesArray = new JSONArray();

                    for (Text.Line line : block.getLines()) {
                        JSONObject lineObj = new JSONObject();
                        lineObj.put("text", line.getText());
                        linesArray.put(lineObj);
                    }

                    blockObj.put("lines", linesArray);
                    blocksArray.put(blockObj);
                }

                JSONObject result = new JSONObject();
                result.put("text", visionText.getText()); // the raw full text
                result.put("blocks", blocksArray);        // structured version

                callbackContext.success(result);
            } catch (Exception e) {
                callbackContext.error("Error formatting OCR result: " + e.getMessage());
            }
        }
    });


      } catch (Exception e) {
        cb.error(e.getMessage());
      }
    });

    return true;
  }
}
