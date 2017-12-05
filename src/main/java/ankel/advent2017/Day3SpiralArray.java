package ankel.advent2017;

import com.google.common.collect.ImmutableList;
import lombok.Value;

import java.util.*;

/**
 * Each square on the grid is allocated in a spiral pattern starting at a location marked 1 and then counting up while spiraling outward. For example, the first few squares are allocated like this:
 *
 * 17  16  15  14  13
 * 18   5   4   3  12
 * 19   6   1   2  11
 * 20   7   8   9  10
 * 21  22  23---> ...
 *
 * While this is very space-efficient (no squares are skipped), requested data must be carried back to square 1 (the location of the only access port for this memory system) by programs that can only move up, down, left, or right. They always take the shortest path: the Manhattan Distance between the location of the data and square 1.
 *
 * For example:
 *
 * Data from square 1 is carried 0 steps, since it's at the access port.
 * Data from square 12 is carried 3 steps, such as: down, left, left.
 * Data from square 23 is carried only 2 steps: up twice.
 * Data from square 1024 must be carried 31 steps.
 *
 * How many steps are required to carry the data from the square identified in your puzzle input all the way to the access port?
 *
 * Your puzzle input is 325489.
 *
 * 2nd
 * the programs here clear the grid and then store the value 1 in square 1. Then, in the same allocation order as shown above, they store the sum of the values in all adjacent squares, including diagonals.
 *
 * So, the first few squares' values are chosen as follows:
 *
 * Square 1 starts with the value 1.
 * Square 2 has only one adjacent filled square (with value 1), so it also stores 1.
 * Square 3 has both of the above squares as neighbors and stores the sum of their values, 2.
 * Square 4 has all three of the aforementioned squares as neighbors and stores the sum of their values, 4.
 * Square 5 only has the first and fourth squares as neighbors, so it gets the value 5.
 *
 * Once a square is written, its value does not change. Therefore, the first few squares would receive the following values:
 *
 * 147  142  133  122   59
 * 304    5    4    2   57
 * 330   10    1    1   54
 * 351   11   23   25   26
 * 362  747  806--->   ...
 *
 * What is the first value written that is larger than your puzzle input?
 *
 * Your puzzle answer was 330785.
 *
 * @author Binh Tran
 */
public class Day3SpiralArray
{
  private static final int VALUE = 325489;

  private static final List<Vector> MOVES = ImmutableList.of(
      new Vector(1, 0),     // first right
      new Vector(0, 1),     // then up
      new Vector(-1, 0),    // then left
      new Vector(0, -1)     // then down
  );

  private static final List<Vector> ADJACENCIES = ImmutableList.of(
      new Vector(1, 0),     // E
      new Vector(1, 1),     // NE
      new Vector(0, 1),     // N
      new Vector(-1, 1),    // NW
      new Vector(-1, 0),    // W
      new Vector(-1, -1),   // SW
      new Vector(0, -1),    // S
      new Vector(1, -1)     // SE
  );

  public static void main(final String[] args) throws Exception
  {
    final Map<Integer, Vector> intToCoord = new HashMap<>();
    final Map<Vector, Integer> cartesian = new HashMap<>();     // mark the visited coordinates

    // move right but look up
    int nextDirectionIndex = 1;
    Vector currentDirection = new Vector(1, 0);

    // init
    Vector currentCoord = new Vector(0, 0);
    intToCoord.put(1, currentCoord);
    cartesian.put(currentCoord, 1);

    boolean  printed = false;

    for (int i = 2; i <= VALUE; ++i)
    {
      // keep moving until we get an empty
      currentCoord = currentCoord.plus(currentDirection);

      intToCoord.put(i, currentCoord);
      int nextValue = calcValue(currentCoord, cartesian);
      cartesian.put(currentCoord, nextValue);

      if (nextValue > VALUE && !printed)
      {
        System.out.println(nextValue);
        printed = true;
      }

      // Check if we can switch direction
      Vector lookAhead = currentCoord.plus(MOVES.get(nextDirectionIndex));
      if (!cartesian.containsKey(lookAhead))
      {
        // empty
        currentDirection = MOVES.get(nextDirectionIndex);
        nextDirectionIndex = ( nextDirectionIndex + 1 ) % 4;
      }
    }

    System.out.println(intToCoord.get(1).toDistance());
    System.out.println(intToCoord.get(12).toDistance());
    System.out.println(intToCoord.get(23).toDistance());
    System.out.println(intToCoord.get(1024).toDistance());
    System.out.println(intToCoord.get(VALUE).toDistance());
  }

  private static int calcValue(Vector currCoord, Map<Vector, Integer> cartesian)
  {
    int sum = 0;
    for (Vector direction : ADJACENCIES)
    {
      Vector coord = currCoord.plus(direction);
      if (cartesian.containsKey(coord))
      {
        sum += cartesian.get(coord);
      }
    }

    return sum;
  }

  @Value
  public static class Vector
  {
    private int x;
    private int y;

    public Vector plus(Vector another)
    {
      return new Vector(x + another.x, y + another.y);
    }

    public long toDistance()
    {
      return Math.abs(x) + Math.abs(y);
    }
  }
}
