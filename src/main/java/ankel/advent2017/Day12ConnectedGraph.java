package ankel.advent2017;

import com.google.common.base.Splitter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static ankel.advent2017.Day7Recursive.streamFromResFile;

/**
 * You need to figure out how many programs are in the group that contains program ID 0.
 * <p>
 * For example, suppose you go door-to-door like a travelling salesman and record the following list:
 * <p>
 * 0 <-> 2
 * 1 <-> 1
 * 2 <-> 0, 3, 4
 * 3 <-> 2, 4
 * 4 <-> 2, 3, 6
 * 5 <-> 6
 * 6 <-> 4, 5
 * <p>
 * In this example, the following programs are in the group that contains program ID 0:
 * <p>
 * Program 0 by definition.
 * Program 2, directly connected to program 0.
 * Program 3 via program 2.
 * Program 4 via program 2.
 * Program 5 via programs 6, then 4, then 2.
 * Program 6 via programs 4, then 2.
 * <p>
 * Therefore, a total of 6 programs are in this group; all but program 1, which has a pipe that connects it to itself.
 * <p>
 * How many programs are in the group that contains program ID 0?
 * <p>
 * Input is from file Day12Input.txt
 * <p>
 * There are more programs than just the ones in the group containing program ID 0. The rest of them have no way of reaching that group, and still might have no way of reaching each other.
 * <p>
 * A group is a collection of programs that can all communicate via pipes either directly or indirectly. The programs you identified just a moment ago are all part of the same group. Now, they would like you to determine the total number of groups.
 * <p>
 * In the example above, there were 2 groups: one consisting of programs 0,2,3,4,5,6, and the other consisting solely of program 1.
 * <p>
 * How many groups are there in total?
 *
 * @author Binh Tran
 */
public class Day12ConnectedGraph
{
  private static final Splitter ARROW_SPLITTER = Splitter.on("<->").trimResults().omitEmptyStrings();
  private static final Splitter COMMA_SPLITTER = Splitter.on(",").trimResults().omitEmptyStrings();

  public static void main(final String[] args) throws Exception
  {
    final Map<Integer, Node> programs = new HashMap<>();


    streamFromResFile("Day12Input.txt")
        .forEach((line) ->
        {
          final List<String> strings = ARROW_SPLITTER.splitToList(line);
          final int label = Integer.valueOf(strings.get(0));
          final Node node = programs.computeIfAbsent(label, Node::new);

          for (final String part : COMMA_SPLITTER.split(strings.get(1)))
          {
            node.getConnectedNodes().add(Integer.valueOf(part));
          }
        });


    final Set<Integer> seen = new HashSet<>();

    int groupCount = 0;

    for (final Integer label : programs.keySet())
    {
      if (!seen.contains(label))
      {
        final Map<Integer, Boolean> visited = new ConcurrentHashMap<>();
        int size = 0;

        visited.put(label, true);

        while (size != visited.size())
        {
          size = visited.size();

          visited.keySet()
              .stream()
              .map(programs::get)
              .map(Node::getConnectedNodes)
              .flatMap(Collection::stream)
              .forEach((i) -> visited.put(i, true));
        }

        System.out.println(size);

        seen.addAll(visited.keySet());
        groupCount++;
      }
    }

    System.out.println(groupCount);
  }

  @RequiredArgsConstructor
  @Getter
  private static final class Node
  {
    final int label;
    private final List<Integer> connectedNodes = new ArrayList<>();
  }
}
