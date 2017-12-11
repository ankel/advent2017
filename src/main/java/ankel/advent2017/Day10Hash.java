package ankel.advent2017;

import com.google.common.base.Preconditions;

/**
 * To achieve this, begin with a list of numbers from 0 to 255, a current position which begins at 0 (the first element in the list), a skip size (which starts at 0), and a sequence of lengths (your puzzle input). Then, for each length:
 * <p>
 * Reverse the order of that length of elements in the list, starting with the element at the current position.
 * Move the current position forward by that length plus the skip size.
 * Increase the skip size by one.
 * <p>
 * The list is circular; if the current position and the length try to reverse elements beyond the end of the list, the operation reverses using as many extra elements as it needs from the front of the list. If the current position moves past the end of the list, it wraps around to the front. Lengths larger than the size of the list are invalid.
 * <p>
 * Suppose we instead only had a circular list containing five elements, 0, 1, 2, 3, 4, and were given input lengths
 * of 3, 4, 1, 5. The result is 3 4 2 1 0.
 *
 * @author Binh Tran
 */
public class Day10Hash
{
  private static final int SIZE = 256;
  private static final int[] INPUT = new int[]{197, 97, 204, 108, 1, 29, 5, 71, 0, 50, 2, 255, 248, 78, 254, 63};
  private static final int[] SUFFIX = new int[]{17, 31, 73, 47, 23};
  private static final String INPUT_STRING = "197,97,204,108,1,29,5,71,0,50,2,255,248,78,254,63";
  private static final int ROUND = 64;
  private static final int STEP = 16;

  public static void main(final String[] args)
  {
    final long start = System.currentTimeMillis();
    final int[] hash = initArr(SIZE);

    int currentPosition = 0;
    int skip = 0;

//    final int[] input = INPUT;
    final int[] input = processString(INPUT_STRING);

    for (int round = 0; round < ROUND; ++round)
    {
      for (final int length : input)
      {
        reverse(hash, currentPosition, length);
        currentPosition += length + skip++;
        currentPosition %= hash.length;
      }
    }

    final int[] dense = toDenseHash(hash, STEP);

    final StringBuilder sb = new StringBuilder();

    for (final int h : dense)
    {
      sb.append(String.format("%02x", h));
    }

//    System.out.println(hash[0] * hash[1]);

    System.out.println(sb.toString());

    System.out.println(System.currentTimeMillis() - start);
  }

  private static int[] toDenseHash(final int[] hash, final int step)
  {
    Preconditions.checkArgument(hash.length % step == 0);
    final int[] ret = new int[hash.length / step];

    for (int i = 0; i < hash.length; i += step)
    {
      int val = hash[i];
      for (int j = 1; j < step; ++j)
      {
        val ^= hash[i + j];
      }
      ret[i / step] = val;
    }

    return ret;
  }

  private static int[] processString(final String inputString)
  {
    final int[] ret = new int[inputString.length() + 5];
    final char[] chars = inputString.toCharArray();
    for (int i = 0; i < chars.length; ++i)
    {
      // ASCII printable character are between 32 and 126, falls within positive byte of Java
      ret[i] = (byte) chars[i];
    }

    System.arraycopy(SUFFIX, 0, ret, chars.length, SUFFIX.length);

    return ret;
  }

  private static void reverse(final int[] hash, final int currentPosition, final int size)
  {
    Preconditions.checkArgument(currentPosition < hash.length);

    for (int i = 0; i < size / 2; ++i)
    {
      final int i1 = (currentPosition + i) % hash.length;
      final int i2 = (currentPosition + size - i - 1) % hash.length;
      final int temp = hash[i1];
      hash[i1] = hash[i2];
      hash[i2] = temp;
    }
  }

  private static int[] initArr(final int size)
  {
    final int[] ret = new int[size];
    for (int i = 0; i < size; ++i)
    {
      ret[i] = i;
    }
    return ret;
  }
}
