package ankel.advent2017;

import com.google.common.base.Splitter;
import lombok.RequiredArgsConstructor;

import java.util.*;

/**
 * Input is from file Day16Input.txt
 *
 * @author Binh Tran
 */
public class Day16Dance2
{
  private static final int MEMBERS = 16;
  private static final Splitter COMMA_SPLITTER = Splitter.on(',').trimResults().omitEmptyStrings();
  private static final Splitter SLASH_SPLITTER = Splitter.on('/').trimResults().omitEmptyStrings();
  private static final int ROUNDS = 1000000000;

  public static void main(final String[] args) throws Exception
  {
    final String steps = Day7Recursive.streamFromResFile("Day16Input.txt")
        .findFirst()
        .get();

    final char[] line = new char[MEMBERS];

    // init
    for (int i = 0; i < MEMBERS; ++i)
    {
      line[i] = (char) ('a' + i);
    }

    int fixPoint = 0;

    // cache
    final Map<Integer, XArgs> exchangeArgs = new HashMap<>();
    final List<String> roundResults = new ArrayList<>();

    int i = 0, j = 0;

    roundLoop:
    for (i = 0; i < ROUNDS; ++i)
    {
      final String previousResult = printLine(line, fixPoint);
      for (j = 0; j < roundResults.size(); ++j)
      {
        if (roundResults.get(j).equals(previousResult))
        {
          System.out.println(String.format("Loop found at [%d-%d]", j, i));
          break roundLoop;
        }
      }

      roundResults.add(previousResult);
      int s = 0;
      for (final String step : COMMA_SPLITTER.split(steps))
      {
//        System.out.println(step);
//        printLine(line, fixPoint);
//        System.out.println();
        switch (step.charAt(0))
        {
          case 's':
            final Integer spinSteps = Integer.valueOf(step.substring(1));
            fixPoint = fixPoint - spinSteps;
            while (fixPoint < 0)
            {
              fixPoint += MEMBERS;
            }
            break;
          case 'x':
            final XArgs xArgs = exchangeArgs.computeIfAbsent(s,
                (__) ->
                {
                  final Iterator<String> exchanges = SLASH_SPLITTER.split(step.substring(1)).iterator();
                  final int posA = Integer.valueOf(exchanges.next());
                  final int posB = Integer.valueOf(exchanges.next());

                  return new XArgs(posA, posB);
                });

            final int posA = (fixPoint + xArgs.posA) % MEMBERS;
            final int posB = (fixPoint + xArgs.posB) % MEMBERS;

            final char t = line[posA];
            line[posA] = line[posB];
            line[posB] = t;
            break;
          case 'p':
            final char charA = step.charAt(1);
            final char charB = step.charAt(3);
            for (int k = 0, count = 0; j < MEMBERS && count != 2; ++j)
            {
              if (line[k] == charA)
              {
                line[k] = charB;
                count++;
                continue;
              }
              if (line[k] == charB)
              {
                line[k] = charA;
                count++;
              }
            }
            break;
          default:
            throw new IllegalStateException(step);
        }
        s++;
      }

      if (i % 10000 == 0)
      {
        System.out.println(System.currentTimeMillis());   // ~4s
      }
    }

    System.out.println(roundResults.get(i + ROUNDS % (j - i)));
  }

  static String printLine(final char[] line, final int fixPoint)
  {
    final StringBuilder sb = new StringBuilder(16);
    for (int i = 0; i < MEMBERS; ++i)
    {
      sb.append(line[(fixPoint + i) % MEMBERS]);
    }

    return sb.toString();
  }

  @RequiredArgsConstructor
  private static class XArgs
  {
    final int posA;
    final int posB;
  }
}
