package ankel.advent2017;

import lombok.ToString;

import java.util.function.Predicate;

/**
 * @author Binh Tran
 */
public class Day15Generator
{
  private static final int ROUNDS_1 = 40000000;
  private static final int ROUNDS_2 = 5000000;

  public static final void main(final String[] args) throws Exception
  {
    Generator a = new Generator(16807, 2147483647, 618, null);
    Generator b = new Generator(48271, 2147483647, 814, null);

    int matches = 0;

    for (int i = 0; i < ROUNDS_1; ++i)
    {
      final long aNext = a.getNext();
      final long bNext = b.getNext();

      if (aNext == bNext)
      {
        matches++;
      }
    }

    System.out.println(matches);

    a = new Generator(16807, 2147483647, 618, i -> i % 4 == 0);
    b = new Generator(48271, 2147483647, 814, i -> i % 8 == 0);

    matches = 0;

    for (int i = 0; i < ROUNDS_2; ++i)
    {
      if (a.getCheckedNext() == b.getCheckedNext())
      {
        matches++;
      }
    }

    System.out.println(matches);
  }

  @ToString
  private static final class Generator
  {
    private final long factor;
    private final long divisor;
    private final Predicate<Long> checker;
    private long seed;

    private Generator(final long factor, final long divisor, final long seed, final Predicate<Long> checker)
    {
      this.factor = factor;
      this.divisor = divisor;
      this.seed = seed;
      this.checker = checker;
    }

    public long getNext()
    {
      seed = (seed * factor) % divisor;

      return seed & 0xffff;
    }

    public long getCheckedNext()
    {
      long ret = getNext();
      while (!checker.test(ret))
      {
        ret = getNext();
      }
      return ret;
    }
  }
}
