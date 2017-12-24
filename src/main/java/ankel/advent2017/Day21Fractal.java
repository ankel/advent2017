package ankel.advent2017;

import com.google.common.base.Splitter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Binh Tran
 */
public class Day21Fractal
{
  private static final Splitter ARROW_SPLITTER = Splitter.on("=>").omitEmptyStrings().trimResults();

  public static void main(final String[] args) throws Exception
  {
    final List<Pattern> patterns = Day7Recursive.streamFromResFile("Day21Input.txt")
        .map(Pattern::fromString)
        .collect(Collectors.toList());

    boolean[][] current = new boolean[][]{
        {false, true, false},
        {false, false, true},
        {true, true, true}
    };
    int nextSize = 0;
    boolean[][] next;

    for (int n = 0; n < 18; ++n)
    {
      final int step;
      if (current.length % 2 == 0)
      {
        step = 2;
        nextSize = (current.length / 2) * 3;
      }
      else if (current.length % 3 == 0)
      {
        step = 3;
        nextSize = (current.length / 3) * 4;
      }
      else
      {
        throw new IllegalStateException("Size is neither divisible by 2 nor divisible by 3");
      }
      next = new boolean[nextSize][nextSize];

      for (int i = 0; i < current.length / step; i++)
      {
        for (int j = 0; j < current.length / step; j++)
        {
          boolean[][] currentSquare = new boolean[step][step];

          for (int k = 0; k < step; ++k)
          {
            System.arraycopy(current[i * step + k], j * step, currentSquare[k], 0, step);
          }

          currentSquare = expand(currentSquare, patterns);

          for (int k = 0; k < step + 1; ++k)
          {
            System.arraycopy(currentSquare[k], 0, next[i * (step + 1) + k], j * (step + 1), step + 1);
          }
        }
      }

      current = next;
    }

    int count = 0;

    for (final boolean[] row : current)
    {
      for (final boolean b : row)
      {
        count += b ? 1 : 0;
      }
    }

    System.out.println(count);
  }

  private static boolean[][] expand(final boolean[][] currentSquare, final List<Pattern> patterns)
  {
    for (final Pattern p : patterns)
    {
      if (p.size != currentSquare.length)
      {
        continue;
      }
      for (final boolean[][] r : p.rotations)
      {
        boolean equals = true;
        for (int i = 0; i < currentSquare.length; ++i)
        {
          if (!Arrays.equals(r[i], currentSquare[i]))
          {
            equals = false;
            break;
          }
        }
        if (equals)
        {
          return p.output;
        }
      }
    }
    throw new IllegalStateException("Cannot find matching patterns");
  }

  @RequiredArgsConstructor
  private static final class Pattern
  {
    private final List<boolean[][]> rotations;
    private final boolean[][] output;
    private final int size;

    public static Pattern fromString(final String s)
    {
      final int size;
      if (s.length() == 20)
      {
        size = 2;
      }
      else
      {
        size = 3;
      }

      final List<String> parts = ARROW_SPLITTER.splitToList(s);
      final boolean[][] input = toMatrix(parts.get(0), size);

      final List<boolean[][]> rotations = new ArrayList<>();

      List<boolean[][]> temp = flip(input, size);
      rotations.addAll(temp);

      for (int i = 0; i < 3; ++i)
      {
        temp = temp.stream()
            .map(Pattern::rotate)
            .collect(Collectors.toList());

        rotations.addAll(temp);
      }

      final boolean[][] output = toMatrix(parts.get(1), size + 1);

      return new Pattern(rotations, output, size);
    }

    private static boolean[][] rotate(final boolean[][] a)
    {
      if (a.length == 2)
      {
        return rotate2(a);
      }
      else
      {
        return rotate3(a);
      }
    }

    private static boolean[][] rotate3(final boolean[][] a)
    {
      final boolean[][] ret = new boolean[3][3];

      // corners
      ret[0][0] = a[2][0];
      ret[0][2] = a[0][0];
      ret[2][2] = a[0][2];
      ret[2][0] = a[2][2];

      // middle
      ret[0][1] = a[1][0];
      ret[1][2] = a[0][1];
      ret[2][1] = a[1][2];
      ret[1][0] = a[2][1];

      // center
      ret[1][1] = a[1][1];

      return ret;
    }

    private static boolean[][] rotate2(final boolean[][] a)
    {
      final boolean[][] ret = new boolean[2][2];

      ret[0][0] = a[1][0];
      ret[0][1] = a[0][0];
      ret[1][0] = a[1][1];
      ret[1][1] = a[0][1];

      return ret;
    }


    private static List<boolean[][]> flip(final boolean[][] input, final int size)
    {
      final List<boolean[][]> ret = new ArrayList<>(3);
      ret.add(input);

      // flip over x
      boolean[][] arr = new boolean[size][size];
      for (int i = 0; i < size; ++i)
      {
        System.arraycopy(input[i], 0, arr[size - 1 - i], 0, size);
      }
      ret.add(arr);

      // flip over y
      arr = new boolean[size][size];
      for (int i = 0; i < size; ++i)
      {
        for (int j = 0; j < size; ++j)
        {
          arr[i][j] = input[i][size - 1 - j];
        }
      }
      ret.add(arr);

      return ret;
    }

    static boolean[][] toMatrix(final String part, final int size)
    {
      final boolean[][] input = new boolean[size][size];

      int i = 0;
      for (final String p : part.split("/"))
      {
        int j = 0;
        for (final char c : p.toCharArray())
        {
          if (c == '#')
          {
            input[i][j] = true;
          }
          else
          {
            input[i][j] = false;
          }
          j++;
        }
        i++;
      }
      return input;
    }
  }
}
