package ankel.advent2017;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * More instruction madness. Input is from Day18Input.txt
 *
 * @author Binh Tran
 */
public class Day18Instructions
{

  private static final Splitter SPACE_SPLITTER = Splitter.on(' ').trimResults();

  public static void main(final String[] args) throws Exception
  {
    final List<String> instructions = Day7Recursive.streamFromResFile("Day18Input.txt")
        .filter(StringUtils::isNotEmpty)
        .collect(Collectors.toList());

//    System.out.println(solveP2(instructions));

    final ProgramABC prog0 = new ProgramABC(0, instructions);
    final ProgramABC prog1 = new ProgramABC(1, instructions);

    prog0.setOtherRecieveQueue(prog1.getRecieveQueue());
    prog1.setOtherRecieveQueue(prog0.getRecieveQueue());

    while (!((prog0.isBlocked() && prog1.isBlocked()) || (prog0.isTerminated() && prog1.isTerminated())))
    {
      while (!prog0.isBlocked() && !prog0.isTerminated())
      {
        prog0.step();
      }

      System.out.println(prog1.getRecieveQueue().size());

      while (!prog1.isBlocked() && !prog1.isTerminated())
      {
        prog1.step();
      }

      System.out.println(prog0.getRecieveQueue().size());
    }

    System.out.println(prog1.getSendCount());
  }

  @RequiredArgsConstructor
  private static final class RegisterBank
  {
    private final Map<String, BigInteger> registers = new HashMap<>();
    private final Long initialValue;

    public BigInteger get(final String r)
    {
      Preconditions.checkArgument(StringUtils.isAlpha(r));
      return registers.computeIfAbsent(r, (reg) ->
          "p".equals(reg) ? BigInteger.valueOf(initialValue) : BigInteger.ZERO);
    }

    public void apply(final String r, final Function<BigInteger, BigInteger> mutator)
    {
      registers.put(r, mutator.apply(get(r)));
    }

    public String toString()
    {
      return registers.toString();
    }
  }

  @RequiredArgsConstructor
  private static final class ProgramABC
  {
    List<String> instructions;
    @Getter
    Queue<BigInteger> recieveQueue;
    RegisterBank registerBank;
    int instructionPointer = 0;
    @Setter
    Queue<BigInteger> otherRecieveQueue;
    long id;
    boolean blocked = false;
    @Getter
    boolean terminated = false;
    @Getter
    long sendCount = 0;

    public ProgramABC(final long id, final List<String> instruction)
    {
      this.instructions = instruction;
      recieveQueue = new LinkedBlockingQueue<>();
      registerBank = new RegisterBank(id);
      this.id = id;
    }

    public void step()
    {
      final String inst = instructions.get(instructionPointer);
//        System.out.println(inst + registerBank);
      final List<String> fields = SPACE_SPLITTER.splitToList(inst);

      final String firstArg = fields.get(1);
      final BigInteger secondArg;

      if (fields.size() != 3)
      {
        secondArg = null;
      }
      else
      {
        final String s = fields.get(2);
        if (StringUtils.isAlpha(s))
        {
          secondArg = registerBank.get(s);
        }
        else
        {
          secondArg = BigInteger.valueOf(Long.valueOf(s));
        }
      }

      switch (fields.get(0))
      {
        case "snd":
          try
          {
            if (StringUtils.isAlpha(firstArg))
            {
              otherRecieveQueue.add(registerBank.get(firstArg));
            }
            else
            {
              otherRecieveQueue.add(BigInteger.valueOf(Long.valueOf(firstArg)));
            }
            sendCount++;
          } catch (final Exception e)
          {
            throw new RuntimeException(e);
          }
          break;
        case "set":
          registerBank.apply(firstArg, (__) -> secondArg);
          break;
        case "add":
          Preconditions.checkNotNull(secondArg);
          registerBank.apply(firstArg, (x) -> x.add(secondArg));
          break;
        case "mul":
          Preconditions.checkNotNull(secondArg);
          registerBank.apply(firstArg, (x) -> x.multiply(secondArg));
          break;
        case "mod":
          Preconditions.checkNotNull(secondArg);
          registerBank.apply(firstArg, (x) -> x.mod(secondArg));
          break;
        case "rcv":
          if (recieveQueue.isEmpty())
          {
            blocked = true;
            return;
          }
          else
          {
            blocked = false;
            final BigInteger newValue = recieveQueue.remove();
            registerBank.apply(firstArg, (__) -> newValue);
          }
          break;
        case "jgz":
          final BigInteger value;
          if (StringUtils.isAlpha(firstArg))
          {
            value = registerBank.get(firstArg);
          }
          else
          {
            value = BigInteger.valueOf(Long.valueOf(firstArg));
          }
          if (value.compareTo(BigInteger.ZERO) > 0)
          {
            instructionPointer += secondArg.intValue() - 1; // minus 1 here because we'll inc it later
          }
          break;
      }

      instructionPointer++;
      if (instructionPointer < 0 || instructionPointer >= instructions.size())
      {
        terminated = true;
      }
    }

    public boolean isBlocked()
    {
      return blocked && recieveQueue.isEmpty();
    }
  }
}
