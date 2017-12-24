package ankel.advent2017;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ankel.advent2017.Day19Maze.*;

/**
 * @author Binh Tran
 */
public class Day22Infection
{
  public static void main(final String[] args) throws Exception
  {
    final List<String> strings = Day7Recursive.streamFromResFile("Day22Input.txt")
        .collect(Collectors.toList());

    final Map<Vector, STATE> infected = new HashMap<>();

    for (int i = 0; i < strings.size(); ++i)
    {
      final String row = strings.get(i);
      for (int j = 0; j < row.length(); ++j)
      {
        if (row.charAt(j) == '#')
        {
          infected.put(new Vector(i, j), STATE.INFECTED);
        }
      }
    }

    final Carrier carrier = new Carrier(
        strings.size() / 2,
        strings.get(0).length() / 2,
        UP);

    int count = 0;

    for (int i = 0; i < 10000000; ++i)
    {
      if (carrier.move(infected))
      {
        count++;
      }
    }

    System.out.println(count);
  }

  private enum STATE
  {
    CLEAN,
    WEAKENED,
    INFECTED,
    FLAGGED;

    public STATE next()
    {
      return STATE.values()[(this.ordinal() + 1) % 4];
    }
  }

  private static class Carrier extends Day19Maze.Vector
  {
    private Day19Maze.Vector direction;

    public Carrier(final int x, final int y, final Day19Maze.Vector direction)
    {
      super(x, y);
      this.direction = direction;
    }

    public Vector toVector()
    {
      return new Vector(getX(), getY());
    }

    public boolean move(final Map<Day19Maze.Vector, STATE> infected)
    {
      final int directionIndex = DIRECTIONS.indexOf(direction);
      final Vector currentNode = toVector();
      final STATE currentState = infected.getOrDefault(currentNode, STATE.CLEAN);

      direction = DIRECTIONS.get((4 + directionIndex + currentState.ordinal() - 1) % 4);
      infected.put(currentNode, currentState.next());
      move();
      return currentState == STATE.WEAKENED;
    }

    private void move()
    {
      setX(getX() + direction.getX());
      setY(getY() + direction.getY());
    }
  }
}
