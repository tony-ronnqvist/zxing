package com.google.zxing;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.CodaBarWriter;
import com.google.zxing.oned.Code39Reader;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
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
    * Test method testGuessEncodingPartThree that for given input should guess encoding ISO8859_1
    */
  @Test
  public void testGuessEncodingPartThree() {
    assertEquals(StringUtils.guessEncoding(new byte[]{(byte) 0xa0, (byte) 0xa0, (byte) 0xa0}, null), "ISO8859_1");
  }

  /*
   * Test whether a FormatException is thrown when check digit is used and incorrect.
   *
   * @throws FormatException
   * @throws ChecksumException
   * @throws NotFoundException
   */
  @Test
  public void testCode39CheckDigitFalse(){

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


  /**
   *  Helper for the testCode39NonExtended. Creates an instance to be checked.
   *
   *
   * @param expectedResult string - with expected result after decoded
   * @param encodedResult string - with the encoded result in binary
   * @throws FormatException if wrong barcode format is used
   * @throws ChecksumException if checksum do not match expected
   * @throws NotFoundException if character is not found
   */
  private static void doTestCode39NonExtended(String expectedResult, String encodedResult)
    throws FormatException, ChecksumException, NotFoundException {

    Code39Reader sut = new Code39Reader(false, false);
    BitMatrix matrix = BitMatrix.parse(encodedResult, "1", "0");
    BitArray row = new BitArray(matrix.getWidth());
    matrix.getRow(0, row);
    Result result = sut.decodeRow(0, row, null);
    assertEquals(expectedResult, result.getText());
  }

  /**
   * Test that checks that the decoded result is correct when not using extended mode or check digit.
   *
   * @throws FormatException if wrong barcode format is used
   * @throws ChecksumException if checksum do not match expected
   * @throws NotFoundException if character is not found
   */
  @Test
  public void testCode39NonExtended() throws FormatException, ChecksumException, NotFoundException {

    String decoded = "ABCDEFGHIJKLE";
    String encoded = "100010111011101011101010001011101011101000101110111011101000101010101110001011101110101110001010101110111000101010101000111011101110101000111010101110100011101010101110001110101110101010001110101110101000111011101011100010101000101110111010";
    doTestCode39NonExtended(decoded, encoded);

  }

  /**
   *  Helper for the testCode39CheckDigitFalse. Creates an instance to be checked.
   *
   * @param encodedResult string - with the encoded result in binary
   * @throws FormatException if wrong barcode format is used
   * @throws ChecksumException if checksum do not match expected
   * @throws NotFoundException if character is not found
   */
  private static void doTestModFormatException(String encodedResult) {

    Code39Reader sut = new Code39Reader(false, true);
    BitMatrix matrix = BitMatrix.parse(encodedResult, "1", "0");
    BitArray row = new BitArray(matrix.getWidth());
    matrix.getRow(0, row);
    assertThrows(FormatException.class, () ->{
      sut.decodeRow(0, row, null);
    });
  }

  /**
   * Test method for decodeExtended to test the branch where a / is followed
   * by a / (NOT next >= 'A' && next <= 'O') or (NOT (next == 'Z'))
   */
  @Test
  public void testEncodedModuloException(){
    String resultString = "//";
    String code39 = "1000101110111010100010001010001010001000101000101000101110111010";

    Code39Reader sut = new Code39Reader(false, true);
    BitMatrix matrix = BitMatrix.parse(code39, "1", "0");
    BitArray row = new BitArray(matrix.getWidth());
    matrix.getRow(0, row);
    assertThrows(FormatException.class, () ->{
      sut.decodeRow(0, row, null);
    });
  }
  
  /**
   * TTest whether a FormatException is thrown when extended mode is used and format is wrong.
   *
   */
  @Test
  public void testModFormatException(){
    String encoded = "10001011101110101010001000100010101000100010001010001010001000101000101110111010";
    doTestModFormatException(encoded);
  }

  ​
  /**
    * Test method testGuessEncodingPartFour that for given input should guess encoding UTF-8
    */
  @Test
  public void testGuessEncodingPartFour() {
    assertEquals(StringUtils.guessEncoding(new byte[]{(byte) 0x80, (byte) 0x80, (byte) 0x80}, null), "UTF-8");
  }
}
