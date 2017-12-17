package ankel.advent2017;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Binh Tran
 */
public class Day17Spin
{
  private static final int STEP = 356;

  public static void main(final String[] args) throws Exception
  {
    // init
    final List<Integer> list = new ArrayList<>();
    list.add(0);

    int currentPos = 0;

    for (int i = 0; i < 50000000; ++i)
    {
//      System.out.println(i + ": " + list);
//      currentPos = ((currentPos + STEP) % list.size()) + 1;
//      list.add(currentPos, i + 1);
      currentPos = ((currentPos + STEP) % (i + 1)) + 1;
      if (currentPos == 1)
      {
        System.out.println(i + 1);
      }
    }

//    System.out.println(list.get(currentPos + 1));
    // no print for part 2, the final result from the loop is the answer. The reason is that the existing number
    // doesn't change, so the number immediately after 0 always at index 1.
  }
}
