package ankel.advent2017;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Binh Tran
 */
public class Day20Particles
{
  public static void main(final String[] args) throws Exception
  {
    final AtomicInteger counter = new AtomicInteger(0);
    final List<Particle> particles = Day7Recursive.streamFromResFile("Day20Input.txt")
        .map(s -> Particle.fromString(counter.getAndIncrement(), s))
        .collect(Collectors.toList());

    int noColisionCount = 0;

    while (noColisionCount <= 500)
    {
      particles.parallelStream()
          .forEach(Particle::step);

      if (!particles.removeAll(checkColision(particles)))
      {
        noColisionCount++;
      }
    }

    System.out.println(particles.size());
  }

  private static Set<Particle> checkColision(final List<Particle> particles)
  {
    final Set<Particle> soFar = new HashSet<>();
    final Set<Particle> colided = new HashSet<>();
    for (final Particle p : particles)
    {
      if (soFar.contains(p))
      {
        colided.add(p);
      }
      else
      {
        soFar.add(p);
      }
    }

    return colided;
  }

  private static final class Vector
  {
    private long x;
    private long y;
    private long z;

    private Vector(final int x, final int y, final int z)
    {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public static Vector fromString(final Matcher m)
    {
      if (m.find())
      {
        return new Vector(
            Integer.valueOf(m.group(1)),
            Integer.valueOf(m.group(2)),
            Integer.valueOf(m.group(3)));
      }
      else
      {
        throw new IllegalArgumentException("String doesn't match pattern");
      }
    }

    @Override
    public boolean equals(final Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (o == null || getClass() != o.getClass())
      {
        return false;
      }
      final Vector vector = (Vector) o;
      return x == vector.x &&
          y == vector.y &&
          z == vector.z;
    }

    @Override
    public int hashCode()
    {

      return Objects.hash(x, y, z);
    }

    public void add(final Vector anotherVector)
    {
      this.x += anotherVector.x;
      this.y += anotherVector.y;
      this.z += anotherVector.z;
    }

    public long distance()
    {
      return Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    @Override
    public String toString()
    {
      return String.format("<%d,%d,%d>", x, y, z);
    }
  }

  private static final class Particle
  {
    private static final Pattern REGEX = Pattern.compile("[apv]=<(-?\\d+),(-?\\d+),(-?\\d+)");
    private final Vector position;
    private final Vector velocity;
    private final Vector acceleration;
    private final int id;
    private long closest = Long.MAX_VALUE;

    private Particle(final int id, final Vector position, final Vector velocity, final Vector acceleration)
    {
      this.position = position;
      this.velocity = velocity;
      this.acceleration = acceleration;
      this.id = id;
    }

    public static Particle fromString(final int id, final String s)
    {
      final Matcher matcher = REGEX.matcher(s);
      return new Particle(
          id,
          Vector.fromString(matcher),
          Vector.fromString(matcher),
          Vector.fromString(matcher));
    }

    @Override
    public boolean equals(final Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (o == null || getClass() != o.getClass())
      {
        return false;
      }
      final Particle particle = (Particle) o;
      return Objects.equals(position, particle.position);
    }

    @Override
    public int hashCode()
    {

      return Objects.hash(position);
    }

    public Particle step()
    {
      this.velocity.add(acceleration);
      this.position.add(velocity);

      if (position.distance() < closest)
      {
        closest = position.distance();
      }

      return this;
    }

    public long distance()
    {
      return Math.abs(position.x) + Math.abs(position.y) + Math.abs(position.z);
    }

    public String toString()
    {
      return String.format("p=%s, v=%s, a=%s", position.toString(), velocity.toString(), acceleration.toString());
    }
  }

}
