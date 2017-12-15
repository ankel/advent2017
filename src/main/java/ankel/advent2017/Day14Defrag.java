package ankel.advent2017;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Binh Tran
 */
public class Day14Defrag
{
  public static final int DIMENSION_SIZE = 128;
  private static final String INPUT = "uugsqrei";
  private static final Map<Character, String> HEX_TO_BINARY_STRING;

  static
  {
    HEX_TO_BINARY_STRING = IntStream.range(0, 16)
        .boxed()
        .collect(Collectors.toMap(
            i -> String.format("%x", i).charAt(0),
            i -> String.format("%4s", Integer.toBinaryString(i)).replace(' ', '0')));
  }

  public static final void main(final String[] args) throws Exception
  {
    final BitSet bitSet = stringToBitSet("a0c2017");

    final List<BitSet> bitmap = IntStream.range(0, DIMENSION_SIZE)
        .parallel()
        .mapToObj(i -> INPUT + "-" + i)
        .map(Day10Hash::calcHash)
        .map(Day14Defrag::stringToBitSet)
        .collect(Collectors.toList());


    // part 1
    final int totalBit = bitmap
        .stream()
        .mapToInt(BitSet::cardinality)
        .sum();

    System.out.println(totalBit);

    // part 2

    // I believe this algorithm is called ink blob or coloring or something similar.
    // basically if a bit is set, do a breath-first-search starting from that bit for all set bits, then un-set them.

    int regionCount = 0;

    for (int i = 0; i < DIMENSION_SIZE; ++i)
    {
      for (int j = 0; j < DIMENSION_SIZE; ++j)
      {
        if (bitmap.get(i).get(j))
        {
          final Collection<Coordinate> toBeFlipped = bfs(bitmap, new Coordinate(i, j));

          for (final Coordinate coordinate : toBeFlipped)
          {
            bitmap.get(coordinate.x).flip(coordinate.y);
          }

          regionCount++;
        }
      }
    }

    System.out.println(regionCount);

  }

  private static Collection<Coordinate> bfs(final List<BitSet> bitmap, final Coordinate startingPos)
  {
    final Set<Coordinate> ret = new HashSet<>();

    int prevSize = ret.size();
    ret.add(startingPos);

    while (prevSize != ret.size())
    {
      prevSize = ret.size();

      final List<Coordinate> adjacencies = ret.stream()
          .map(coord -> coord.getAdjacencyBitSet(bitmap))
          .flatMap(Collection::stream)
          .collect(Collectors.toList());

      ret.addAll(adjacencies);
    }

    return ret;
  }

  private static BitSet stringToBitSet(final String s)
  {
    final StringBuilder sb = new StringBuilder();
    for (final char c : s.toCharArray())
    {
      sb.append(HEX_TO_BINARY_STRING.get(c));
    }

    // Ugh this is messy, note to future self: don't use bit set
    // 2nd note to future self: i know you'll ignore note above!
    final BitSet ret = new BitSet(sb.length());
    int i = 0;
    for (final char c : sb.toString().toCharArray())
    {
      switch (c)
      {
        case '1':
          ret.set(i);
          break;
        case '0':
          // ignore
          break;
        default:
          throw new IllegalStateException("Why is there a quantum state here?");
      }
      i++;
    }

    return ret;
  }

  @Getter
  @ToString
  @EqualsAndHashCode
  private static class Coordinate
  {
    private final int x;
    private final int y;

    private Coordinate(final int x, final int y)
    {
      Preconditions.checkArgument(0 <= x && x < DIMENSION_SIZE);
      Preconditions.checkArgument(0 <= y && y < DIMENSION_SIZE);

      this.x = x;
      this.y = y;
    }

    public List<Coordinate> getAdjacencyBitSet(final List<BitSet> bitmap)
    {
      final List<Coordinate> ret = new ArrayList<>();

      if (x - 1 >= 0 && bitmap.get(x - 1).get(y))
      {
        ret.add(new Coordinate(x - 1, y));
      }

      if (x + 1 < DIMENSION_SIZE && bitmap.get(x + 1).get(y))
      {
        ret.add(new Coordinate(x + 1, y));
      }

      if (y - 1 >= 0 && bitmap.get(x).get(y - 1))
      {
        ret.add(new Coordinate(x, y - 1));
      }

      if (y + 1 < DIMENSION_SIZE && bitmap.get(x).get(y + 1))
      {
        ret.add(new Coordinate(x, y + 1));
      }

      return ret;
    }
  }
}
