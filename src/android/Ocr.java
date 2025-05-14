package com.example.ocr;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

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

        recognizer.process(image)
          .addOnSuccessListener(result -> cb.success(result.getText()))
          .addOnFailureListener(e -> cb.error(e.getMessage()));

      } catch (Exception e) {
        cb.error(e.getMessage());
      }
    });

    return true;
  }
}
