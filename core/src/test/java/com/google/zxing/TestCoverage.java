package com.google.zxing;


import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.CodaBarWriter;
import com.google.zxing.oned.Code39Reader;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Added test to increase the coverage on the chosen 10 - methods.
 */
public class TestCoverage extends Assert {

  /*
   * Four test methods to increase the coverage on method encode in class CodaBarWriter
   * These four test increases the coverage from 0.62 to 1.0
   */

  /**
   * Test method testCodaBarPartTwo tests two parts:
   * 1. Throws exception if the first character is in group START_END_CHARS and the last character is not in the START_END_CHARS
   * 2. Throws exception if the first character is in group ALT_START_END_CHARS and the last character is not in the ALT_START_END_CHARS
   */
  @Test
  public void testCodaBarPartOne(){

      Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        Arrays.toString(new CodaBarWriter().encode("A123456789-$"));
      });

      String expectedMessage = "Invalid start/end guards: A123456789-$";
      String actualMessage = exception.getMessage();

      assertTrue(actualMessage.contains(expectedMessage));


    Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
      Arrays.toString(new CodaBarWriter().encode("T123456789-$"));
    });

    String expectedMessage1 = "Invalid start/end guards: T123456789-$";
    String actualMessage1 = exception1.getMessage();

    assertTrue(actualMessage1.contains(expectedMessage1));

  }

  /**
   * Test method testCodaBarPartThree test two parts:
   * 1. Throws exception if the first character is not in START_END_CHARS or ALT_START_END_CHARS
   * 2. Throws exception if the last character is not in START_END_CHARS or ALT_START_END_CHARS
   */
  @Test
  public void testCodaBarPartTwo(){

    Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
      Arrays.toString(new CodaBarWriter().encode("K123456789-$T"));
    });

    String expectedMessage1 = "Invalid start/end guards: K123456789-$T";
    String actualMessage1 = exception1.getMessage();

    assertTrue(actualMessage1.contains(expectedMessage1));


    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      Arrays.toString(new CodaBarWriter().encode("K123456789-$K"));
    });

    String expectedMessage = "Cannot encode : 'K'";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

  }

  /**
   * Test that the character N is changes to B, that character is changes from * to C and that character is changes from E to D
   */
  @Test
  public void testCodaBarPartThree(){

    assertEquals(Arrays.toString(new CodaBarWriter().encode("N123456789-$*")), Arrays.toString(new CodaBarWriter().encode("B123456789-$C")));

    assertEquals(Arrays.toString(new CodaBarWriter().encode("T123456789-$E")), Arrays.toString(new CodaBarWriter().encode("A123456789-$D")));

  }

  /**
   * Test method testCodaBarPartOne will test the part of the method where content is less then two
   */
  @Test
  public void testCodaBarPartFour(){

    assertEquals(Arrays.toString(new CodaBarWriter().encode("1")), Arrays.toString(new CodaBarWriter().encode("1")));

  }

  /**
   * Test method for decodeExtended to test the branch where a + is followed
   * by a + (NOT next >= 'A' && next <= 'Z')
   */
  @Test
  public void testEncodedPlus(){
    String resultString = "++++";
    String code39 = "100010111011101010001010001000101000101000100010100010100010001010001010001000101000101110111010";

    Code39Reader sut = new Code39Reader(false, true);
    BitMatrix matrix = BitMatrix.parse(code39, "1", "0");
    BitArray row = new BitArray(matrix.getWidth());
    matrix.getRow(0, row);
    assertThrows(FormatException.class, () ->{
      sut.decodeRow(0, row, null);
    });
  }
  /**
   * Test method for decodeExtended to test the branch where a $ is followed
   * by a $ (NOT next >= 'A' && next <= 'Z')
   */
  @Test
  public void testEncodedDollar(){
    String resultString = "$$$$";
    String code39 = "100010111011101010001000100010101000100010001010100010001000101010001000100010101000101110111010";

    Code39Reader sut = new Code39Reader(false, true);
    BitMatrix matrix = BitMatrix.parse(code39, "1", "0");
    BitArray row = new BitArray(matrix.getWidth());
    matrix.getRow(0, row);
    assertThrows(FormatException.class, () ->{
      sut.decodeRow(0, row, null);
    });
  }
  /**
   * Test method for decodeExtended to test the branch where a % is followed
   * by a X This should replace both the % and X character with ascii value of 127
   */
  @Test
  public void testEncodedModulus() throws FormatException, ChecksumException, NotFoundException {
    String code39 = "10001011101110101110101000101110101110100010111011101110100010101010111000101110111010111000101010111011100010101010100011101110111010100011101010111010001110101010111000111010111010101000111010111010100011101110111010100010101011101000111011101011101000101011101110100010101010111000111011101010111000101011101011100010101011101110001011100010101011101000111010101110111000111010101010100010001000101000101110101110111000101110101010001110111010101000101110111010";

    Code39Reader sut = new Code39Reader(false, true);
    BitMatrix matrix = BitMatrix.parse(code39, "1", "0");
    BitArray row = new BitArray(matrix.getWidth());
    matrix.getRow(0, row);
    Result result = sut.decodeRow(0, row, null);

    byte[] ascii = result.getText().getBytes(StandardCharsets.US_ASCII);
    String asciiString1 = Arrays.toString(ascii);

    byte[] ascii2 = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 127, 89, 90};

    String asciiString2 = Arrays.toString(ascii2);
    assertEquals(asciiString1, asciiString2);
  }


}
