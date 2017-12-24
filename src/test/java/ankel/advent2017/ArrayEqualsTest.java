package ankel.advent2017;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * @author Binh Tran
 */
public class ArrayEqualsTest
{
  @Test
  public void testFoo()
  {
    final boolean[] a1 = new boolean[5];
    final boolean[] a2 = new boolean[5];

    for (int i = 0; i < 5; ++i)
    {
      final boolean b = i % 2 == 0;
      a1[i] = b;
      a2[i] = b;
    }

//    assertEquals(a1, a2);
    assertEquals(2, 3 / 2);

    final Set<boolean[]> set = new HashSet<>();
    set.add(a1);
    set.add(a2);

    assertEquals(1, set.size());
  }
}
