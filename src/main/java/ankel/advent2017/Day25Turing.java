package ankel.advent2017;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Binh Tran
 */
public class Day25Turing
{
  private static final Map<Character, Step[]> program = ImmutableMap.<Character, Step[]>builder()
      .put(
          'A', new Step[]
              {
                  new Step(1, 1, 'B'),
                  new Step(0, -1, 'B')
              })
      .put(
          'B', new Step[]
              {
                  new Step(0, 1, 'C'),
                  new Step(1, -1, 'B')
              })
      .put(
          'C', new Step[]
              {
                  new Step(1, 1, 'D'),
                  new Step(0, -1, 'A')
              })
      .put(
          'D', new Step[]
              {
                  new Step(1, -1, 'E'),
                  new Step(1, -1, 'F')
              })
      .put(
          'E', new Step[]
              {
                  new Step(1, -1, 'A'),
                  new Step(0, -1, 'D')
              })
      .put(
          'F', new Step[]
              {
                  new Step(1, 1, 'A'),
                  new Step(1, -1, 'E')
              })
      .build();

  public static void main(final String[] args) throws Exception
  {
    final Machine m = new Machine();
    for (int i = 0; i < 12586542; ++i)
    {
      m.step();
    }

    System.out.println(m.setIndex.size());
  }

  private static class Machine
  {
    private final Set<Long> setIndex = new HashSet<>();
    private long index = 0;
    private char state = 'A';

    public void step()
    {
      final int currentValue = setIndex.contains(index) ? 1 : 0;

      final Step step = program
          .get(state)[currentValue];

      if (step.newValue == 1)
      {
        setIndex.add(index);
      }
      else
      {
        setIndex.remove(index);
      }

      index += step.move;
      state = step.nextState;
    }
  }

  @RequiredArgsConstructor
  private static class Step
  {
    final int newValue;
    final int move;
    final char nextState;
  }


}
