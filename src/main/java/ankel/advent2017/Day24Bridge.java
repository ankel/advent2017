package ankel.advent2017;

import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * @author Binh Tran
 */
public class Day24Bridge
{
  public static void main(final String[] args) throws Exception
  {
    final List<Component> components =
        Day7Recursive.streamFromResFile("Day24Input.txt")
            .map(Component::fromString)
            .collect(Collectors.toList());

    int maxScore = -1;

    final LinkedList<List<Component>> bridges = new LinkedList<>();

    for (final Component c : components)
    {
      if (c.i == 0 || c.j == 0)
      {
        final LinkedList<Component> sofar = new LinkedList<>();
        sofar.add(c);
        final int score = buildBridge(components, sofar, c.i == 0 ? c.j : c.i, bridges);
        if (score > maxScore)
        {
          maxScore = score;
        }
      }
    }

    System.out.println(maxScore);

    bridges.sort(
        Comparator
            .comparingInt((ToIntFunction<List<Component>>) List::size)
            .thenComparingInt(Day24Bridge::calcScore));

    System.out.println(calcScore(bridges.peekLast()));
  }

  private static int buildBridge(
      final List<Component> components,
      final LinkedList<Component> sofar,
      final int endConnector,
      final Collection<List<Component>> bridges)
  {
    int maxScore = -1;
    boolean added = false;

    for (final Component c : components)
    {
      if (!sofar.contains(c))
      {
        if (c.i == endConnector)
        {
          added = true;
          sofar.add(c);
          final int score = buildBridge(components, sofar, c.j, bridges);
          if (score > maxScore)
          {
            maxScore = score;
          }
          sofar.removeLast();
        }
        if (c.j == endConnector)
        {
          added = true;
          sofar.add(c);
          final int score = buildBridge(components, sofar, c.i, bridges);
          if (score > maxScore)
          {
            maxScore = score;
          }
          sofar.removeLast();
        }
      }
    }

    if (added)
    {
      return maxScore;
    }
    else
    {
      addTo(sofar, bridges);
      return calcScore(sofar);
    }
  }

  private synchronized static void addTo(final LinkedList<Component> sofar, final Collection<List<Component>> bridges)
  {
    bridges.add(ImmutableList.copyOf(sofar));
  }

  private static int calcScore(final List<Component> bridge)
  {
    int score = 0;
    for (final Component c : bridge)
    {
      score += c.i + c.j;
    }
    return score;
  }

  @RequiredArgsConstructor
  @EqualsAndHashCode
  @ToString
  private static class Component
  {
    private final int i;
    private final int j;

    public static Component fromString(final String s)
    {
      final String[] parts = s.split("/");

      final Integer i = Integer.valueOf(parts[0]);
      final Integer j = Integer.valueOf(parts[1]);

      return new Component(i, j);
    }
  }
}
