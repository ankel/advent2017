package ankel.advent2017;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A debugger program here is having an issue: it is trying to repair a memory reallocation routine, but it keeps getting stuck in an infinite loop.
 *
 * In this area, there are sixteen memory banks; each memory bank can hold any number of blocks. The goal of the reallocation routine is to balance the blocks between the memory banks.
 *
 * The reallocation routine operates in cycles. In each cycle, it finds the memory bank with the most blocks (ties won by the lowest-numbered memory bank) and redistributes those blocks among the banks. To do this, it removes all of the blocks from the selected bank, then moves to the next (by index) memory bank and inserts one of the blocks. It continues doing this until it runs out of blocks; if it reaches the last memory bank, it wraps around to the first one.
 *
 * The debugger would like to know how many redistributions can be done before a blocks-in-banks configuration is produced that has been seen before.
 *
 * For example, imagine a scenario with only four memory banks:
 *
 * The banks start with 0, 2, 7, and 0 blocks. The third bank has the most blocks, so it is chosen for redistribution.
 * Starting with the next bank (the fourth bank) and then continuing to the first bank, the second bank, and so on, the 7 blocks are spread out over the memory banks. The fourth, first, and second banks get two blocks each, and the third bank gets one back. The final result looks like this: 2 4 1 2.
 * Next, the second bank is chosen because it contains the most blocks (four). Because there are four memory banks, each gets one block. The result is: 3 1 2 3.
 * Now, there is a tie between the first and fourth memory banks, both of which have three blocks. The first bank wins the tie, and its three blocks are distributed evenly over the other three banks, leaving it with none: 0 2 3 4.
 * The fourth bank is chosen, and its four blocks are distributed such that each of the four banks receives one: 1 3 4 1.
 * The third bank is chosen, and the same thing happens: 2 4 1 2.
 *
 * At this point, we've reached a state we've seen before: 2 4 1 2 was already seen. The infinite loop is detected after the fifth block redistribution cycle, and so the answer in this example is 5.
 *
 * Given the initial block counts in your puzzle input, how many redistribution cycles must be completed before a configuration is produced that has been seen before?
 *
 * Out of curiosity, the debugger would also like to know the size of the loop: starting from a state that has already been seen, how many block redistribution cycles must be performed before that same state is seen again?
 *
 * In the example above, 2 4 1 2 is seen again after four cycles, and so the answer in that example would be 4.
 *
 * How many cycles are in the infinite loop that arises from the configuration in your puzzle input?
 *
 * @author Binh Tran
 */
public class Day6Malloc
{
  public static void main(final String[] args)
  {
    long start = System.currentTimeMillis();

    final String input = "11\t11\t13\t7\t0\t15\t5\t5\t4\t4\t1\t1\t7\t1\t15\t11";

    List<Integer> currentConfiguration = Lists.newArrayList(input.split("\t"))
        .stream()
        .map(Integer::valueOf)
        .collect(Collectors.toList());

    ImmutableList<Integer> clone = ImmutableList.copyOf(currentConfiguration);

    Map<List<Integer>, Integer> seen = new HashMap<>();

    int length = currentConfiguration.size();

    int step = 0;

    while (!seen.containsKey(clone))
    {
      seen.put(clone, step);
      step++;

      int i = findBlockToSpread(currentConfiguration);
      int oldVal = currentConfiguration.get(i);
      currentConfiguration.set(i, 0);

      // spread the values evenly first
      int delta = oldVal / length;

      currentConfiguration.replaceAll(j -> j + delta);

      // then work on the remainder
      i = (i + 1) % length;  // starting from the block right after
      int leftOver = oldVal % length;
      while (leftOver != 0)
      {
        currentConfiguration.set(i, currentConfiguration.get(i) + 1);
        --leftOver;
        i = (i + 1) % length;
      }

      clone = ImmutableList.copyOf(currentConfiguration);
    }

    System.out.println(step);

    System.out.println(step - seen.get(clone));

    System.out.println(System.currentTimeMillis() - start); // 60ms
  }

  private static int findBlockToSpread(List<Integer> currentConfiguration)
  {
    int ret = 0, max = Integer.MIN_VALUE;

    for (int i = 0; i < currentConfiguration.size(); ++i)
    {
      if (currentConfiguration.get(i) > max)
      {
        ret = i;
        max = currentConfiguration.get(i);
      }
    }

    return ret;
  }
}
