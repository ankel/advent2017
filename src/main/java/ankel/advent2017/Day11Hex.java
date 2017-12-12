package ankel.advent2017;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Seriously, day 11? Hex? Can't wait what'z for day 12. Off-by-one error correcting code?
 * <p>
 * You have the path the child process took. Starting where he started, you need to determine the fewest number of steps required to reach him. (A "step" means to move from the hex you are in to any adjacent hex.)
 * <p>
 * For example:
 * <p>
 * ne,ne,ne is 3 steps away.
 * ne,ne,sw,sw is 0 steps away (back where you started).
 * ne,ne,z,z is 2 steps away (se,se).
 * se,sw,se,sw,sw is 3 steps away (z,z,sw).
 * <p>
 * Most if not all of the algorithms below are lifted from https://www.redblobgames.com/grids/hexagons/ - the ultimate
 * source for all hex-grid programming stuff. I basically glue them together and cross my fingers that my numbers are
 * correct.
 * <p>
 * Input is from Day11Input.txt
 *
 * @author Binh Tran
 */
public class Day11Hex
{
  private static final Map<String, HexCubicCoordinate> MOVES = ImmutableMap.<String, HexCubicCoordinate>builder()
      .put("N", new HexCubicCoordinate(0, 1, -1))
      .put("NE", new HexCubicCoordinate(1, 0, -1))
      .put("SE", new HexCubicCoordinate(1, -1, 0))
      .put("S", new HexCubicCoordinate(0, -1, 1))
      .put("SW", new HexCubicCoordinate(-1, 0, 1))
      .put("NW", new HexCubicCoordinate(-1, 1, 0))
      .build();

  private static final Splitter COMMA_SPLITTER = Splitter.on(',').trimResults().omitEmptyStrings();

  public static void main(final String[] args) throws Exception
  {
    Day7Recursive
        .streamFromResFile("Day11Input.txt")
        .forEach((input) ->
        {

          HexCubicCoordinate currentPos = new HexCubicCoordinate(0, 0, 0);

          int max = Integer.MIN_VALUE;

          for (final String move : COMMA_SPLITTER.split(input))
          {
            currentPos = currentPos.plus(MOVES.get(move.toUpperCase()));

            max = currentPos.distanceFromOrigin() > max ?
                currentPos.distanceFromOrigin() : max;
          }

          System.out.println(max + " " + currentPos.distanceFromOrigin());
        });

  }

  private static final class HexCubicCoordinate
  {
    private final int x;
    private final int y;
    private final int z;

    private HexCubicCoordinate(final int x, final int y, final int z)
    {
      Preconditions.checkArgument(x + y + z == 0);

      this.x = x;
      this.y = y;
      this.z = z;
    }

    private int distanceFromOrigin()
    {
      return (Math.abs(x) + Math.abs(y) + Math.abs(z)) / 2;
    }

    private HexCubicCoordinate plus(final HexCubicCoordinate another)
    {
      return new HexCubicCoordinate(x + another.x, y + another.y, z + another.z);
    }
  }
}
