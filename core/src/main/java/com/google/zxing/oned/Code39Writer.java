/*
 * Copyright 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.oned;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.CoverageTool2000;
import com.google.zxing.common.BitMatrix;

import java.util.Collection;
import java.util.Collections;

/**
 * This object renders a CODE39 code as a {@link BitMatrix}.
 *
 * @author erik.barbara@gmail.com (Erik Barbara)
 */
public final class Code39Writer extends OneDimensionalCodeWriter {

  @Override
  protected Collection<BarcodeFormat> getSupportedWriteFormats() {
    return Collections.singleton(BarcodeFormat.CODE_39);
  }

  /**
   * Encodes a string to code39 binary code
   * @param contents barcode contents to encode
   * @return boolean array - representing the code 39 binary code.
   */
  @Override
  public boolean[] encode(String contents) {
    int length = contents.length();
    if (length > 80) {

      throw new IllegalArgumentException(
          "Requested contents should be less than 80 digits long, but got " + length);
    }

    for (int i = 0; i < length; i++) {
      int indexInString = Code39Reader.ALPHABET_STRING.indexOf(contents.charAt(i));
      if (indexInString < 0) {

        contents = tryToConvertToExtendedMode(contents);
        length = contents.length();
        if (length > 80) {

          throw new IllegalArgumentException(
              "Requested contents should be less than 80 digits long, but got " + length + " (extended full ASCII mode)");
        }
        break;
      }
    }

    int[] widths = new int[9];
    int codeWidth = 24 + 1 + (13 * length);
    boolean[] result = new boolean[codeWidth];
    toIntArray(Code39Reader.ASTERISK_ENCODING, widths);
    int pos = appendPattern(result, 0, widths, true);
    int[] narrowWhite = {1};
    pos += appendPattern(result, pos, narrowWhite, false);
    //append next character to byte matrix
    for (int i = 0; i < length; i++) {
      int indexInString = Code39Reader.ALPHABET_STRING.indexOf(contents.charAt(i));
      toIntArray(Code39Reader.CHARACTER_ENCODINGS[indexInString], widths);
      pos += appendPattern(result, pos, widths, true);
      pos += appendPattern(result, pos, narrowWhite, false);
    }
    toIntArray(Code39Reader.ASTERISK_ENCODING, widths);
    appendPattern(result, pos, widths, true);
    return result;
  }

  private static void toIntArray(int a, int[] toReturn) {
    for (int i = 0; i < 9; i++) {
      int temp = a & (1 << (8 - i));
      toReturn[i] = temp == 0 ? 1 : 2;
    }
  }

  /**
   * Encodes extended characters to code 39 barcode sequences.
   * Converts characters to the extended equivalent
   * In Full ASCII Code 39 Symbols 0-9, A-Z, ".", "-" and space are the same
   * as their representations in Code 39.
   * @param contents String - with the code that should be converted to extended char set
   * @return String - with the converted char set
   */
  private static String tryToConvertToExtendedMode(String contents) {
     int length = contents.length();
     StringBuilder extendedContent = new StringBuilder();
     for (int i = 0; i < length; i++) {
       char character = contents.charAt(i);
       switch (character) {
         case '\u0000':
           CoverageTool2000.setCoverageMatrix(6,0);
           extendedContent.append("%U");
           break;
         case ' ':
           CoverageTool2000.setCoverageMatrix(6,1);
         case '-':
           CoverageTool2000.setCoverageMatrix(6,2);
         case '.':
           CoverageTool2000.setCoverageMatrix(6,3);
           extendedContent.append(character);
           break;
         case '@':
           CoverageTool2000.setCoverageMatrix(6,4);
           extendedContent.append("%V");
           break;
         case '`':
           CoverageTool2000.setCoverageMatrix(6,5);
           extendedContent.append("%W");
           break;
         default:
           if (character <= 26) {
             CoverageTool2000.setCoverageMatrix(6,6);
             extendedContent.append('$');
             extendedContent.append((char) ('A' + (character - 1)));
           } else if (character < ' ') {
             CoverageTool2000.setCoverageMatrix(6,7);
             extendedContent.append('%');
             extendedContent.append((char) ('A' + (character - 27)));
           } else if (character <= ',' || character == '/' || character == ':') {
             CoverageTool2000.setCoverageMatrix(6,8);
             extendedContent.append('/');
             extendedContent.append((char) ('A' + (character - 33)));
           } else if (character <= '9') {
             CoverageTool2000.setCoverageMatrix(6,9);
             extendedContent.append((char) ('0' + (character - 48)));
           } else if (character <= '?') {
             CoverageTool2000.setCoverageMatrix(6,10);
             extendedContent.append('%');
             extendedContent.append((char) ('F' + (character - 59)));
           } else if (character <= 'Z') {
             CoverageTool2000.setCoverageMatrix(6,11);
             extendedContent.append((char) ('A' + (character - 65)));
           } else if (character <= '_') {
             CoverageTool2000.setCoverageMatrix(6,12);
             extendedContent.append('%');
             extendedContent.append((char) ('K' + (character - 91)));
           } else if (character <= 'z') {
             CoverageTool2000.setCoverageMatrix(6,13);
             extendedContent.append('+');
             extendedContent.append((char) ('A' + (character - 97)));
           } else if (character <= 127) {
             CoverageTool2000.setCoverageMatrix(6,14);
             extendedContent.append('%');
             extendedContent.append((char) ('P' + (character - 123)));
           } else {
             CoverageTool2000.setCoverageMatrix(6,15);
             throw new IllegalArgumentException(
               "Requested content contains a non-encodable character: '" + contents.charAt(i) + "'");
           }
           break;
       }
    }

    return extendedContent.toString();
  }

}
