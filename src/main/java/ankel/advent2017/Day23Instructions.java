package ankel.advent2017;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.math.IntMath.*;

/**
 * @author Binh Tran
 */
public class Day23Instructions
{
  private static final Splitter SPACE_SPLITTER = Splitter.on(' ').trimResults().omitEmptyStrings();

  public static void main(final String[] args) throws Exception
  {
    final List<String> program =
        Day7Recursive.streamFromResFile("Day23Input.txt")
            .map(String::trim)
            .collect(Collectors.toList());

    final Map<String, BigInteger> registers = new HashMap<>();
    int instructionPointer = 0;
    int mulCount = 0;

    while (0 <= instructionPointer && instructionPointer < program.size())
    {
//      System.out.println(instructionPointer);
      final String instruction = program.get(instructionPointer);

      // C style comments
      if (instruction.startsWith("//"))
      {
        continue;
      }

      final List<String> parts = SPACE_SPLITTER.splitToList(instruction);
      final BigInteger firstOperand = processOperand(registers, parts.get(1));
      final BigInteger secondOperand = processOperand(registers, parts.get(2));

      switch (parts.get(0))
      {
        case "set":
          registers.put(parts.get(1), secondOperand);
          break;
        case "sub":
          registers.compute(
              parts.get(1),
              (__, oldVal) -> oldVal.subtract(secondOperand));
          break;
        case "mul":
          registers.compute(
              parts.get(1),
              (__, oldVal) -> oldVal.multiply(secondOperand));
          mulCount++;
          break;
        case "jnz":
//          System.out.println(instructionPointer + 1 + ": " + firstOperand);
          if (firstOperand.compareTo(BigInteger.ZERO) != 0)
          {
            instructionPointer += secondOperand.intValue() - 1;
          }
          break;
        default:
          throw new IllegalStateException("Instruction unrecognized " + parts.get(0));
      }
      instructionPointer++;
    }

    System.out.println(mulCount);

    System.out.println(part2());
  }

  private static BigInteger processOperand(final Map<String, BigInteger> registers, final String operand)
  {
    if (StringUtils.isAlpha(operand))
    {
      return registers.computeIfAbsent(operand, (__) -> BigInteger.ZERO);
    }
    else
    {
      return BigInteger.valueOf(Long.parseLong(operand));
    }
  }

  /**
   * Algorithm adapted from solution at https://github.com/dp1/AoC17/blob/master/day23.5.txt
   */
  private static int part2()
  {
    final int b = 65 * 100 + 100000;
    final int c = b + 17000;
    int h = 0;

    for (int i = b; i < c + 1; i += 17)
    {
      if (!isPrime(i))
      {
        h++;
      }
    }

    return h;
  }
}
