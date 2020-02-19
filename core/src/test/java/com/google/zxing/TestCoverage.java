package com.google.zxing;


import com.google.zxing.aztec.AztecDetectorResult;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.aztec.decoder.Decoder;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.oned.CodaBarWriter;
import org.junit.Assert;
import org.junit.Test;

import java.util.EnumMap;
import java.util.Map;

/**
 * Added test two increase the coverage on the chosen 10 - methods.
 */
public class TestCoverage extends Assert {


  // @Test
  // Put in your test methods

  @Test
  public void testCorrectBits1() throws FormatException {

    BitMatrix bits=new BitMatrix(16);
    boolean [][]image=new boolean[16][16];
    for (int i=0;i<16;i++){
      for (int j=0;j<16;j++){
        image[i][j]=true;
      }
    }
    bits.parse(image);
    ResultPoint[] points = new ResultPoint[0];
    AztecDetectorResult t1= new AztecDetectorResult(bits,points, true,1,1);
    Decoder r1=new Decoder();
    try{
      r1.decode(t1);
    }catch (Exception e) {
    }

  }
  @Test
  public void testCorrectBits2() throws FormatException {
    BitMatrix bits=new BitMatrix(16);
    boolean [][]image=new boolean[1][1];
    for (int i=0;i<1;i++){
      for (int j=0;j<1;j++){
        image[i][j]=false;
      }
    }
    bits.parse(image);
    ResultPoint[] points = new ResultPoint[0];
    AztecDetectorResult t1= new AztecDetectorResult(bits,points, true,1,1);
    Decoder r1=new Decoder();
    try{
      r1.decode(t1);
    }catch (Exception e) {
    }
  }

  @Test
  public void testCorrectBits3() throws FormatException {
    BitMatrix bits=new BitMatrix(25);
    boolean [][]image=new boolean[25][25];
    for (int i=0;i<25;i++){
      for (int j=0;j<25;j++){
        image[i][j]=false;
      }
    }
    bits.parse(image);
    ResultPoint[] points = new ResultPoint[0];
    AztecDetectorResult t1= new AztecDetectorResult(bits,points, true,625,1);
    Decoder r1=new Decoder();
    try{
      r1.decode(t1);
    }catch (Exception e) {
    }
  }

  @Test
  public void testCorrectBits4() throws FormatException {
    BitMatrix bits=new BitMatrix(16);
    boolean [][]image=new boolean[16][16];
    for (int i=0;i<16;i++){
      for (int j=0;j<16;j++){
        image[i][j]=false;
      }
    }
    bits.parse(image);
    ResultPoint[] points = new ResultPoint[0];
    AztecDetectorResult t1= new AztecDetectorResult(bits,points, true,256,1);
    Decoder r1=new Decoder();
    try{
      r1.decode(t1);
    }catch (Exception e) {
    }
  }

}


