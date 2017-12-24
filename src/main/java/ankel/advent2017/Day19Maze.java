package ankel.advent2017;

import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Binh Tran
 */
public class Day19Maze
{
  public static final Vector RIGHT = new Vector(0, 1);
  public static final Vector DOWN = new Vector(1, 0);
  public static final Vector LEFT = new Vector(0, -1);
  public static final Vector UP = new Vector(-1, 0);
  public static final List<Vector> DIRECTIONS = ImmutableList.of(RIGHT, DOWN, LEFT, UP);

  public static void main(final String[] args) throws Exception
  {
    final List<String> maze = Day7Recursive.streamFromResFile("Day19Input.txt")
        .collect(Collectors.toList());

    Vector pos = new Vector(0, 1);  // from the input
    Vector direction = DOWN;
    boolean ended = false;
    final StringBuilder sb = new StringBuilder();

    int step = 0;

//    mainLoop:
    while (!ended)
    {
      pos = pos.move(direction);

      final char posChar = pos.getChar(maze);
      step++;
//      System.out.print(posChar);

      switch (posChar)
      {
        case ' ':
          // end of the line
          ended = true;
          break;
        case '|':
        case '-':
          continue;
        case '+':
          Vector lookAhead = pos.move(direction);
          if (lookAhead.getChar(maze) != ' ')
          {
            // we can continue
            continue;
          }
          else
          {
            // we have to make a perpendicular turn
            Vector nextDirection = null;
            for (final Vector v : DIRECTIONS)
            {
              if (direction.dotProduct(v) == 0)
              {
                lookAhead = pos.move(v);
                if (lookAhead.getChar(maze) != ' ')
                {
                  if (nextDirection == null)
                  {

                    nextDirection = v;
                  }
                  else
                  {
                    throw new RuntimeException("Can make both turn here " + pos.toString());
                  }
                }
              }
            }
            if (nextDirection == null)
            {
              // no valid turn
              ended = true;
            }
            else
            {
              direction = nextDirection;
            }
          }
          break;
        default:
          sb.append(pos.getChar(maze));
          break;
      }
    }

    System.out.println(step + ": " + sb.toString());
  }

  @EqualsAndHashCode
  @ToString
  @Getter
  @Setter
  public static class Vector
  {
    private int x;
    private int y;

    public Vector(final int x, final int y)
    {
      this.x = x;
      this.y = y;
    }

    public Vector move(final Vector direction)
    {
      return new Vector(x + direction.x, y + direction.y);
    }

    public char getChar(final List<String> maze)
    {
      if (x < 0 || x >= maze.size())
      {
        return ' ';
      }
      if (y < 0 || y >= maze.get(x).length())
      {
        return ' ';
      }
      return maze.get(x).charAt(y);
    }

    public int dotProduct(final Vector another)
    {
      return x * another.x + y * another.y;
    }
  }
}
