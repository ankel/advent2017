package ankel.advent2017;

import com.google.common.base.Splitter;
import com.google.common.collect.Ordering;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ankel.advent2017.Day7Recursive.streamFromResFile;

/**
 * Input is from Day13Input.txt
 *
 * @author Binh Tran
 */
public class Day13Firewall
{
  private static final Splitter COLON_SPLITTER = Splitter.on(':').trimResults().omitEmptyStrings();

  public static void main(final String[] args) throws Exception
  {
    final Map<Integer, Layer> firewall = streamFromResFile("Day13Input.txt")
        .map(line ->
        {
          List<String> fields = COLON_SPLITTER.splitToList(line);
          return new AbstractMap.SimpleEntry<>(
              Integer.valueOf(fields.get(0)),
              new Layer(Integer.valueOf(fields.get(1))));
        })
        .collect(Collectors.toMap(
            AbstractMap.SimpleEntry::getKey,
            AbstractMap.SimpleEntry::getValue));

    final int max = firewall.keySet().stream().max(Ordering.natural()).get();
    int severity = 0;
    final int currentBlock = 0;

    for (int currentTime = 0; currentTime <= max; ++currentTime)
    {
      final int currentLayer = currentTime;
      final Layer currentFirewallLayer = firewall.get(currentLayer);
      if (currentFirewallLayer != null)
      {
        final int currentLayerBlock = currentFirewallLayer.moveForward(currentTime);
        if (currentLayerBlock == currentBlock)
        {
          severity += currentFirewallLayer.getLength() * currentTime;
        }
      }
    }

    System.out.println(severity);

    // part 2, brute force:

    int delay = 0;
    boolean caught = true;
    while (caught)
    {
      caught = false;
      for (int currentTime = 0; currentTime <= max; ++currentTime)
      {
        final Layer currentFirewallLayer = firewall.get(currentTime);
        if (currentFirewallLayer != null)
        {
          final int currentLayerBlock = currentFirewallLayer.moveForward(currentTime + delay);
          if (currentLayerBlock == currentBlock)
          {
            caught = true;
            delay++;
            break;
          }
        }
      }
    }

    System.out.println(delay);
  }

  @RequiredArgsConstructor
  @Getter
  @ToString
  private static final class Layer
  {
    private final int length;

    public int moveForward(final int i)
    {
      final int rounds = i / (length - 1);

      if (rounds % 2 == 0)
      {
        return i % (length - 1);
      }
      else
      {
        return (length - 1) - i % (length - 1);
      }
    }
  }
}
