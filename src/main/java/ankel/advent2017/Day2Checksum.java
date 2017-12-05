package ankel.advent2017;

import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;

/**
 * Calculate the spreadsheet's checksum. For each row, determine the difference between the largest value and the
 * smallest value; the checksum is the sum of all of these differences.
 * For example, given the following spreadsheet:
 *
 * 5 1 9 5
 * 7 5 3
 * 2 4 6 8
 *
 * The first row's largest and smallest values are 9 and 1, and their difference is 8.
 * The second row's largest and smallest values are 7 and 3, and their difference is 4.
 * The third row's difference is 6.
 *
 * In this example, the spreadsheet's checksum would be 8 + 4 + 6 = 18.
 *
 * 2nd star:
 * find the only two numbers in each row where one evenly divides the other - that is, where the result of the division operation is a whole number. They would like you to find those numbers on each line, divide them, and add up each line's result.
 *
 * For example, given the following spreadsheet:
 *
 * 5 9 2 8
 * 9 4 7 3
 * 3 8 6 5
 *
 * In the first row, the only two numbers that evenly divide are 8 and 2; the result of this division is 4.
 * In the second row, the two numbers are 9 and 3; the result is 3.
 * In the third row, the result is 2.
 *
 * In this example, the sum of the results would be 4 + 3 + 2 = 9.
 *
 * @author Binh Tran
 */
public class Day2Checksum
{
  public static void main(final String[] args) throws Exception
  {
    final String s = "626\t2424\t2593\t139\t2136\t163\t1689\t367\t2235\t125\t2365\t924\t135\t2583\t1425\t2502\n" +
        "183\t149\t3794\t5221\t5520\t162\t5430\t4395\t2466\t1888\t3999\t3595\t195\t181\t6188\t4863\n" +
        "163\t195\t512\t309\t102\t175\t343\t134\t401\t372\t368\t321\t350\t354\t183\t490\n" +
        "2441\t228\t250\t2710\t200\t1166\t231\t2772\t1473\t2898\t2528\t2719\t1736\t249\t1796\t903\n" +
        "3999\t820\t3277\t3322\t2997\t1219\t1014\t170\t179\t2413\t183\t3759\t3585\t2136\t3700\t188\n" +
        "132\t108\t262\t203\t228\t104\t205\t126\t69\t208\t235\t311\t313\t258\t110\t117\n" +
        "963\t1112\t1106\t50\t186\t45\t154\t60\t1288\t1150\t986\t232\t872\t433\t48\t319\n" +
        "111\t1459\t98\t1624\t2234\t2528\t93\t1182\t97\t583\t2813\t3139\t1792\t1512\t1326\t3227\n" +
        "371\t374\t459\t83\t407\t460\t59\t40\t42\t90\t74\t163\t494\t250\t488\t444\n" +
        "1405\t2497\t2079\t2350\t747\t1792\t2412\t2615\t89\t2332\t1363\t102\t81\t2346\t122\t1356\n" +
        "1496\t2782\t2257\t2258\t961\t214\t219\t2998\t400\t230\t2676\t3003\t2955\t254\t2250\t2707\n" +
        "694\t669\t951\t455\t2752\t216\t1576\t3336\t251\t236\t222\t2967\t3131\t3456\t1586\t1509\n" +
        "170\t2453\t1707\t2017\t2230\t157\t2798\t225\t1891\t945\t943\t2746\t186\t206\t2678\t2156\n" +
        "3632\t3786\t125\t2650\t1765\t1129\t3675\t3445\t1812\t3206\t99\t105\t1922\t112\t1136\t3242\n" +
        "6070\t6670\t1885\t1994\t178\t230\t5857\t241\t253\t5972\t7219\t252\t806\t6116\t4425\t3944\n" +
        "2257\t155\t734\t228\t204\t2180\t175\t2277\t180\t2275\t2239\t2331\t2278\t1763\t112\t2054\n";
    calc(s);
  }

  private static long calc(String s)
  {
    toArr(s);
    return 0;
  }

  private static List<List<Integer>> toArr(final String s)
  {
    Splitter newlineSplitter = Splitter.on('\n').trimResults().omitEmptyStrings();
    Splitter tabSplitter = Splitter.on('\t').trimResults().omitEmptyStrings();

    final List<List<Integer>> ret = new ArrayList<>();

    long sum = 0;

    for (String line : newlineSplitter.split(s))
    {
      List<Integer> row = new ArrayList<>();
      for (String num : tabSplitter.split(line))
      {
        row.add(Integer.valueOf(num));
      }
//      sum += calcRow(row);
      sum += calcRow2(row);
    }

    System.out.println(sum);

    return ret;
  }

  private static long calcRow(List<Integer> row)
  {
    if (row.isEmpty())
    {
      return 0;
    }
    int min = row.get(0);
    int max = row.get(0);
    for (int i : row)
    {
      if (i < min)
      {
        min = i;
      }
      if (i > max)
      {
        max = i;
      }
    }

    return max - min;
  }

  private static long calcRow2(List<Integer> row)
  {
    if (row.isEmpty())
    {
      return 0;
    }

    for (int i = 0; i < row.size(); ++i)
    {
      int currI = row.get(i);
      for (int j = i + 1; j < row.size(); ++j)
      {
        int currJ = row.get(j);

        // there can only be 1 such pair, return ASAP
        if (currI % currJ == 0)
        {
          return currI / currJ;
        }
        if (currJ % currI == 0)
        {
          return currJ / currI;
        }
      }
    }

    return 0;
  }
}
