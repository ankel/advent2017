package ankel.advent2017;

import com.google.common.io.Resources;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * a list of the offsets for each jump. Jumps are relative: -1 moves to the previous instruction, and 2 skips the next one. Start at the first instruction in the list. The goal is to follow the jumps until one leads outside the list.
 *
 * In addition, these instructions are a little strange; after each jump, the offset of that instruction increases by 1. So, if you come across an offset of 3, you would move three instructions forward, but change it to a 4 for the next time it is encountered.
 *
 * For example, consider the following list of jump offsets:
 *
 * 0
 * 3
 * 0
 * 1
 * -3
 *
 * Positive jumps ("forward") move downward; negative jumps move upward. For legibility in this example, these offset values will be written all on one line, with the current instruction marked in parentheses. The following steps would be taken before an exit is found:
 *
 * (0) 3  0  1  -3  - before we have taken any steps.
 * (1) 3  0  1  -3  - jump with offset 0 (that is, don't jump at all). Fortunately, the instruction is then incremented to 1.
 * 2 (3) 0  1  -3  - step forward because of the instruction we just modified. The first instruction is incremented again, now to 2.
 * 2  4  0  1 (-3) - jump all the way to the end; leave a 4 behind.
 * 2 (4) 0  1  -2  - go back to where we just were; increment -3 to -2.
 * 2  5  0  1  -2  - jump 4 steps forward, escaping the maze.
 *
 * In this example, the exit is reached in 5 steps.
 *
 * Input is from Day5Input.txt
 *
 * 2nd star:
 *
 * Now, the jumps are even stranger: after each jump, if the offset was three or more, instead decrease it by 1. Otherwise, increase it by 1 as before.
 *
 * Using this rule with the above example, the process now takes 10 steps, and the offset values after finding the exit are left as 2 3 2 3 -1.
 *
 * @author Binh Tran
 */
public class Day5JumpInstruction
{
  public static void main(final String[] args) throws Exception
  {
    long start = System.currentTimeMillis();

    List<Integer> instructions = Files
        .lines(Paths.get(Resources.getResource("Day5Input.txt").toURI()))
        .filter(s -> !s.isEmpty())
        .map(String::trim)
        .map(Integer::valueOf)
        .collect(Collectors.toList());

    int ip = 0;   // instruction pointer.
    int step = 0;

    while (ip < instructions.size())
    {
      step++;
      int newJump = instructions.get(ip);
//      instructions.set(ip, newJump + 1);    star 1
      instructions.set(ip, newJump >= 3 ? newJump - 1 : newJump + 1);
      ip += newJump;
    }

    System.out.println(step);
    System.out.println(System.currentTimeMillis() - start);   // wow. 178ms.
  }
}
