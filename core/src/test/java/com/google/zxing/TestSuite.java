package com.google.zxing;

import com.google.zxing.oned.Code39ExtendedBlackBox2TestCase;
import com.google.zxing.oned.Code39ExtendedModeTestCase;
import junit.framework.TestCase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({Code39ExtendedModeTestCase.class, Code39ExtendedBlackBox2TestCase.class, MultiTestCase.class})
public class TestSuite extends TestCase {
  /**
   * One time setup before the tests are run.
   */
  @BeforeClass
  public static void setup() {
    CoverageTool2000.initCoverageMatrix(0, 24);
    CoverageTool2000.initCoverageMatrix(1, 18);
    CoverageTool2000.initCoverageMatrix(4, 10);
  }
  /**
   * One time teardown after all the tests are run.
   */
  @AfterClass
  public static void teardown() {
    System.out.println(CoverageTool2000.checkCoverage(0));
  }
}
