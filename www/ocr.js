var exec = require('cordova/exec');

var Ocr = {
  /**
   * Scan a static image for text.
   * @param {string} imageUri  Cordova file:// URI
   * @param {function(string)} success  Recognized text
   * @param {function(string)} error    Error message
   */
  scanImage: function(imageUri, success, error) {
    exec(success, error, 'Ocr', 'scanImage', [imageUri]);
  }
};

module.exports = Ocr;
