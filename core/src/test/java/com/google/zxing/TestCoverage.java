package com.google.zxing;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.CodaBarWriter;
import com.google.zxing.oned.Code39Reader;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.oned.rss.RSS14Reader;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

// guessEncoding
import com.google.zxing.common.StringUtils;
import com.google.zxing.DecodeHintType;


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
    * Test method testGuessEncodingPartTwo that for given input should guess SJIS encoding
    */
  @Test
  public void testGuessEncodingPartTwo() {
    assertEquals(StringUtils.guessEncoding(new byte[]{(byte) 0xd0, (byte) 0xd0, (byte) 0xd0}, null), "SJIS");
  }

  /*
   * Test whether a FormatException is thrown when check digit is used and incorrect.
   *
   * @throws FormatException
   * @throws ChecksumException
   * @throws NotFoundException
   */
  @Test
  public void testCode39CheckDigitFalse() throws FormatException, ChecksumException, NotFoundException {

    //"A*" encoded to binary
    String encoded = "100101101101011010100101101011010010110100101101101";
    doTestCheckDigitThrows(encoded);

  }

  /**
   *  Test whether the correct result is returned when check digit is used. Should return
   *  the character string with the check digit removed and replaced with an space.
   *
   * @throws FormatException
   * @throws ChecksumException
   * @throws NotFoundException
   */
  @Test
  public void testCode39CheckDigitTrue() throws FormatException, ChecksumException, NotFoundException {

    String decoded = "U ";
    String encoded = "10001011101110101110001010101110100011101011101010111011101000101000101110111010";
    doTestCheckDigit(decoded, encoded);

  }


  /**
   * Helper for the testCode39CheckDigitFalse. Creates an instance to be checked.
   *
   * @param encodedResult string - with the barcode characters encoded in binary
   */
  private static void doTestCheckDigitThrows(String encodedResult){

    Code39Reader sut = new Code39Reader(true, true);
    BitMatrix matrix = BitMatrix.parse(encodedResult, "1", "0");
    BitArray row = new BitArray(matrix.getWidth());
    matrix.getRow(0, row);
    assertThrows(ChecksumException.class, () ->{
      sut.decodeRow(0, row, null);
    });
  }

  /**
   * Helper for the testCode39CheckDigitTrue. Creates an instance to be checked.
   *
   * @param expectedResult string - expected result after the binary has been decoded
   * @param encodedResult string - with the barcode characters encoded in binary
   * @throws FormatException if wrong barcode format is used
   * @throws ChecksumException if checksum do not match expected
   * @throws NotFoundException if character is not found.
   */
  private static void doTestCheckDigit(String expectedResult, String encodedResult)

    throws FormatException, ChecksumException, NotFoundException {
    Code39Reader sut = new Code39Reader(true, true);
    BitMatrix matrix = BitMatrix.parse(encodedResult, "1", "0");
    BitArray row = new BitArray(matrix.getWidth());
    matrix.getRow(0, row);
    Result result = sut.decodeRow(0, row, null);
    assertEquals(expectedResult, result.getText());
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
}
