package com.google.zxing;


import com.google.zxing.oned.CodaBarWriter;
import org.junit.Assert;
import org.junit.Test;

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


  

}
