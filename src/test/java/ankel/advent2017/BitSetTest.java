package ankel.advent2017;

import org.junit.Test;

import java.util.BitSet;

/**
 * @author Binh Tran
 */
public class BitSetTest
{
  @Test
  public void fromByteArray()
  {
    for (byte i = 0; i < 16; ++i)
    {
      System.out.println(String.format("%2d : %s", i, BitSet.valueOf(new byte[]{i})));
    }

    System.out.println(BitSet.valueOf(new byte[]{15, 14}));
  }
}
