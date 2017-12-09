package ankel.advent2017;

/**
 * The characters represent groups - sequences that begin with { and end with }. Within a group, there are zero or more other things, separated by commas: either another group or garbage. Since groups can contain other groups, a } only closes the most-recently-opened unclosed group - that is, they are nestable. Your puzzle input represents a single, large group which itself contains many smaller ones.
 * <p>
 * Sometimes, instead of a group, you will find garbage. Garbage begins with < and ends with >. Between those angle brackets, almost any character can appear, including { and }. Within garbage, < has no special meaning.
 * <p>
 * In a futile attempt to clean up the garbage, some program has canceled some of the characters within it using !: inside garbage, any character that comes after ! should be ignored, including <, >, and even another !.
 * <p>
 * You don't see any characters that deviate from these rules. Outside garbage, you only find well-formed groups, and garbage always terminates according to the rules above.
 * <p>
 * Here are some self-contained pieces of garbage:
 * <p>
 * <>, empty garbage.
 * <random characters>, garbage containing random characters.
 * <<<<>, because the extra < are ignored.
 * <{!>}>, because the first > is canceled.
 * <!!>, because the second ! is canceled, allowing the > to terminate the garbage.
 * <!!!>>, because the second ! and the first > are canceled.
 * <{o"i!a,<{i<a>, which ends at the first >.
 * <p>
 * Here are some examples of whole streams and the number of groups they contain:
 * <p>
 * {}, 1 group.
 * {{{}}}, 3 groups.
 * {{},{}}, also 3 groups.
 * {{{},{},{{}}}}, 6 groups.
 * {<{},{},{{}}>}, 1 group (which itself contains garbage).
 * {<a>,<a>,<a>,<a>}, 1 group.
 * {{<a>},{<a>},{<a>},{<a>}}, 5 groups.
 * {{<!>},{<!>},{<!>},{<a>}}, 2 groups (since all but the last > are canceled).
 * <p>
 * Your goal is to find the total score for all groups in your input. Each group is assigned a score which is one more than the score of the group that immediately contains it. (The outermost group gets a score of 1.)
 * <p>
 * {}, score of 1.
 * {{{}}}, score of 1 + 2 + 3 = 6.
 * {{},{}}, score of 1 + 2 + 2 = 5.
 * {{{},{},{{}}}}, score of 1 + 2 + 3 + 3 + 3 + 4 = 16.
 * {<a>,<a>,<a>,<a>}, score of 1.
 * {{<ab>},{<ab>},{<ab>},{<ab>}}, score of 1 + 2 + 2 + 2 + 2 = 9.
 * {{<!!>},{<!!>},{<!!>},{<!!>}}, score of 1 + 2 + 2 + 2 + 2 = 9.
 * {{<a!>},{<a!>},{<a!>},{<ab>}}, score of 1 + 2 = 3.
 * <p>
 * What is the total score for all groups in your input?
 * <p>
 * Input is from Day9Input.txt.
 * This is a Deterministic Automata / Context-Free Language problem. Fun story: I was picked for an honor project in my
 * undergrad program, class CS 201-202 prof. Bryant York. Boston ascent and soft-voiced but really bright and have
 * knack for jokes.
 *
 * @author Binh Tran
 */
public class Day9DeterministicAutomata
{
  public static void main(final String[] args) throws Exception
  {
    final long start = System.currentTimeMillis();
    Day7Recursive.streamFromResFile("Day9Input.txt")
        .filter(s -> !s.isEmpty())
        .map(String::toCharArray)
        .forEach((ca) ->
            System.out.println(calcLineScore(ca) + " " + countGarbage(ca)));

    System.out.println(System.currentTimeMillis() - start);
  }

  private static int calcLineScore(final char[] line)
  {
    int sum = 0;
    int currentLevel = 0;
    boolean isGarbage = false;
    boolean isEscaped = false;

    for (final char c : line)
    {
      if (isEscaped)
      {
        isEscaped = false;
        continue;
      }

      switch (c)
      {
        case '{':
          if (!isGarbage)
          {
            currentLevel++;
          }
          break;
        case '}':
          if (!isGarbage)
          {
            sum += currentLevel--;
          }
          break;
        case '<':
          isGarbage = true;
          break;
        case '>':
          isGarbage = false;
          break;
        case '!':
          isEscaped = true;
          break;
        default:
          break;
      }
    }

    return sum;
  }

  private static int countGarbage(final char[] line)
  {
    int sum = 0;
    boolean isGarbage = false;
    boolean isEscaped = false;

    for (final char c : line)
    {
      if (isEscaped)
      {
        isEscaped = false;
        continue;
      }

      switch (c)
      {
        case '<':
          sum += isGarbage ? 1 : 0;
          isGarbage = true;
          break;
        case '>':
          isGarbage = false;
          break;
        case '!':
          isEscaped = true;
          break;
        default:
          sum += isGarbage ? 1 : 0;
          break;
      }
    }

    return sum;
  }
}
