package ankel.advent2017;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static ankel.advent2017.Day7Recursive.streamFromResFile;

/**
 * Each instruction consists of several parts: the register to modify, whether to increase or decrease that register's value, the amount by which to increase or decrease it, and a condition. If the condition fails, skip the instruction without modifying the register. The registers all start at 0. The instructions look like this:
 * <p>
 * b inc 5 if a > 1
 * a inc 1 if b < 5
 * c dec -10 if a >= 1
 * c inc -20 if c == 10
 * <p>
 * These instructions would be processed as follows:
 * <p>
 * Because a starts at 0, it is not greater than 1, and so b is not modified.
 * a is increased by 1 (to 1) because b is less than 5 (it is 0).
 * c is decreased by -10 (to 10) because a is now greater than or equal to 1 (it is 1).
 * c is increased by -20 (to -10) because c is equal to 10.
 * <p>
 * After this process, the largest value in any register is 1.
 * <p>
 * You might also encounter <= (less than or equal to) or != (not equal to). However, the CPU doesn't have the bandwidth to tell you what all the registers are named, and leaves that to you to determine.
 * <p>
 * What is the largest value in any register after completing the instructions in your puzzle input?
 * <p>
 * Input is from Day8Input.txt
 * <p>
 * To be safe, the CPU also needs to know the highest value held in any register during this process so that it can decide how much memory to allocate to these operations. For example, in the above instructions, the highest value ever held was 10 (in register c after the third instruction was evaluated).
 *
 * @author Binh Tran
 */
public class Day8Registers
{
  private static final Map<String, Comparision> COMPARISION_MAP = Lists.newArrayList(Comparision.values())
      .stream()
      .collect(Collectors.toMap(Comparision::getText, o -> o));

  private static final Splitter IF_SPLITTER = Splitter.on("if").trimResults();
  private static final Splitter SPACE_SPLITTER = Splitter.on(" ").trimResults();

  public static void main(final String[] args) throws Exception
  {
    final long start = System.currentTimeMillis();

    final RegistryBank bank = new RegistryBank();
    final List<Instruction> prog = streamFromResFile("Day8Input.txt")
        .map(Day8Registers::toInstruction)
//        .forEachOrdered(i -> i.execute(bank));
        .collect(Collectors.toList());

    int max = Integer.MIN_VALUE;

    for (final Instruction i : prog)
    {
      i.execute(bank);
      final int currentMax = bank.getMaxValue();
      if (currentMax > max)
      {
        max = currentMax;
      }
    }

    System.out.println(bank.getMaxValue());
    System.out.println(max);

    System.out.println(System.currentTimeMillis() - start);


  }

  private static Instruction toInstruction(final String l)
  {
    final List<String> temp = IF_SPLITTER.splitToList(l);
    final Condition cond = Condition.ofString(temp.get(1));
    final List<String> temp2 = SPACE_SPLITTER.splitToList(temp.get(0));
    return new Instruction(
        temp2.get(0),
        new Operator(temp2.get(1), temp2.get(2)),
        cond);
  }

  @Getter
  private enum Comparision implements BiFunction<Integer, Integer, Boolean>
  {
    LT("<", (i, j) -> i < j),
    LE("<=", (i, j) -> i <= j),
    EQ("==", Objects::equals),
    NE("!=", (i, j) -> !Objects.equals(i, j)),
    GE(">=", (i, j) -> i >= j),
    GT(">", (i, j) -> i > j);

    private final String text;
    private final BiFunction<Integer, Integer, Boolean> delegate;

    Comparision(final String text, final BiFunction<Integer, Integer, Boolean> delegate)
    {
      this.text = text;
      this.delegate = delegate;
    }

    @Override
    public Boolean apply(final Integer i, final Integer j)
    {
      return delegate.apply(i, j);
    }
  }

  private static final class RegistryBank
  {
    private final Map<String, Integer> registries = new HashMap<>();

    public Integer getValue(final String registry)
    {
      return registries.computeIfAbsent(registry, (__) -> 0);
    }

    public boolean check(final Condition cond)
    {
      final int val = getValue(cond.getRegistry());
      return cond.getComparision().apply(val, cond.getOperand());
    }

    public void apply(final String registry, final UnaryOperator<Integer> func)
    {
      registries.compute(
          registry,
          (__, old) -> old == null ? func.apply(0) : func.apply(old));
    }

    public Integer getMaxValue()
    {
      return registries.values().stream().max((i, j) -> i - j).get();
    }
  }

  @RequiredArgsConstructor
  private static final class Instruction
  {
    private final String registry;
    private final Operator op;
    private final Condition cond;

    public boolean execute(final RegistryBank registryBank)
    {
      if (registryBank.check(cond))
      {
        registryBank.apply(registry, op);
        return true;
      }
      else
      {
        return false;
      }
    }
  }

  private static class Operator implements UnaryOperator<Integer>
  {
    private final boolean isInc;
    private final Integer operand;

    private Operator(final String operator, final String operand)
    {
      switch (operator)
      {
        case "inc":
          isInc = true;
          break;
        case "dec":
          isInc = false;
          break;
        default:
          throw new IllegalArgumentException(operator + " is not valid");
      }
      this.operand = Integer.valueOf(operand);
    }

    @Override
    public Integer apply(final Integer integer)
    {
      return isInc ? integer + operand : integer - operand;
    }
  }

  @Getter
  @RequiredArgsConstructor
  private static class Condition
  {
    private final String registry;
    private final Comparision comparision;
    private final Integer operand;

    public static Condition ofString(final String s)
    {
      final List<String> temp = SPACE_SPLITTER.splitToList(s);
      final Comparision comparision = COMPARISION_MAP.get(temp.get(1));
      if (comparision == null)
      {
        throw new IllegalArgumentException(s + " cannot be parsed into Condition");
      }

      return new Condition(
          temp.get(0),
          comparision,
          Integer.valueOf(temp.get(2)));
    }
  }
}
