package ankel.advent2017;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.io.Resources;
import lombok.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * One program at the bottom supports the entire tower. It's holding a large disc, and on the disc are balanced several more sub-towers. At the bottom of these sub-towers, standing on the bottom disc, are other programs, each holding their own disc, and so on. At the very tops of these sub-sub-sub-...-towers, many programs stand simply keeping the disc below them balanced but with no disc of their own.
 * <p>
 * You offer to help, but first you need to understand the structure of these towers. You ask each program to yell out their name, their weight, and (if they're holding a disc) the names of the programs immediately above them balancing on that disc. You write this information down (your puzzle input). Unfortunately, in their panic, they don't do this in an orderly fashion; by the time you're done, you're not sure which program gave which information.
 * <p>
 * For example, if your list is the following:
 * <p>
 * pbga (66)
 * xhth (57)
 * ebii (61)
 * havc (66)
 * ktlj (57)
 * fwft (72) -> ktlj, cntj, xhth
 * qoyq (66)
 * padx (45) -> pbga, havc, qoyq
 * tknk (41) -> ugml, padx, fwft
 * jptl (61)
 * ugml (68) -> gyxo, ebii, jptl
 * gyxo (61)
 * cntj (57)
 * <p>
 * ...then you would be able to recreate the structure of the towers that looks like this:
 * <p>
 * gyxo
 * /
 * ugml - ebii
 * /      \
 * |         jptl
 * |
 * |         pbga
 * /        /
 * tknk --- padx - havc
 * \        \
 * |         qoyq
 * |
 * |         ktlj
 * \      /
 * fwft - cntj
 * \
 * xhth
 * <p>
 * In this example, tknk is at the bottom of the tower (the bottom program), and is holding up ugml, padx, and fwft. Those programs are, in turn, holding up other programs; in this example, none of those programs are holding up any other programs, and are all the tops of their own towers. (The actual tower balancing in front of you is much larger.)
 * <p>
 *
 * Input is from Day7Input.txt
 *
 * The programs explain the situation: they can't get down. Rather, they could get down, if they weren't expending all of their energy trying to keep the tower balanced. Apparently, one program has the wrong weight, and until it's fixed, they're stuck here.
 *
 * For any program holding a disc, each program standing on that disc forms a sub-tower. Each of those sub-towers are supposed to be the same weight, or the disc itself isn't balanced. The weight of a tower is the sum of the weights of the programs in that tower.
 *
 * In the example above, this means that for ugml's disc to be balanced, gyxo, ebii, and jptl must all have the same weight, and they do: 61.
 *
 * However, for tknk to be balanced, each of the programs standing on its disc and all programs above it must each match. This means that the following sums must all be the same:
 *
 * ugml + (gyxo + ebii + jptl) = 68 + (61 + 61 + 61) = 251
 * padx + (pbga + havc + qoyq) = 45 + (66 + 66 + 66) = 243
 * fwft + (ktlj + cntj + xhth) = 72 + (57 + 57 + 57) = 243
 *
 * As you can see, tknk's disc is unbalanced: ugml's stack is heavier than the other two. Even though the nodes above ugml are balanced, ugml itself is too heavy: it needs to be 8 units lighter for its stack to weigh 243 and keep the towers balanced. If this change were made, its weight would be 60.
 *
 * Given that exactly one program is the wrong weight, what would its weight need to be to balance the entire tower?
 *
 * Your puzzle answer was 1674.
 *
 * @author Binh Tran
 */
public class Day7Recursive
{
  private static final Splitter ARROW_SPLITTER = Splitter.on("->").trimResults().omitEmptyStrings();
  private static final Splitter COMMA_SPLITTER = Splitter.on(',').trimResults().omitEmptyStrings();

  public static void main(final String[] args) throws Exception
  {
    final List<String> inputs = streamFromResFile("Day7Input.txt")
        .collect(Collectors.toList());

    final Map<String, Node> nodes = new HashMap<>();

    Node root = null;

    for (final String s : inputs)
    {
      final List<String> list1 = ARROW_SPLITTER.splitToList(s);
      Preconditions.checkArgument(list1.size() == 1 || list1.size() == 2, "List1 size is %d", list1.size());

      final Node temp = Node.fromString(list1.get(0));

      root = nodes
          .computeIfAbsent(temp.getName(), (__) -> temp);

      root.setWeight(temp.getWeight());

      if (list1.size() == 2)
      {
        for (final String n : COMMA_SPLITTER.split(list1.get(1)))
        {
          final Node tmp = nodes.computeIfAbsent(n, Node::ofName);
          tmp.setParent(root);
          root.addChild(tmp);
        }
      }
    }

    while (root.getParent() != null)
    {
      root = root.getParent();
    }

    System.out.println(root);

    final Map<String, Integer> weights = new HashMap<>();

    final Node imba = root.getImbaNode(weights);

    System.out.println(imba);

    for (final Node n : imba.getParent().getChildren())
    {
      System.out.println(n.getName() + ": " + n.getSubTreeWeight(weights));
    }
  }

  public static Stream<String> streamFromResFile(final String s) throws URISyntaxException, IOException
  {
    return Files
        .lines(Paths.get(Resources.getResource(s).toURI()))
        .filter(line -> !line.isEmpty());
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @ToString(exclude = {"children", "parent"})
  @EqualsAndHashCode(exclude = {"children", "parent"})
  private static class Node
  {
    private static final Pattern NAME_AND_WEIGHT = Pattern.compile("^(\\w+) \\((\\d+)\\)$");

    private final List<Node> children = new ArrayList<>();
    private int weight;
    private String name;
    private Node parent;

    public static Node fromString(final String s)
    {
      final Matcher matcher = NAME_AND_WEIGHT.matcher(s);
      matcher.matches();
      Preconditions.checkArgument(matcher.groupCount() == 2);

      final Node node = new Node();
      node.setName(matcher.group(1));
      node.setWeight(Integer.valueOf(matcher.group(2)));

      return node;
    }

    public static Node ofName(final String name)
    {
      final Node ret = new Node();
      ret.setName(name);
      return ret;
    }

    public void addChild(final Node child)
    {
      children.add(child);
    }

    public int getSubTreeWeight(final Map<String, Integer> subtreeWeight)
    {
      return subtreeWeight.computeIfAbsent(
          name,
          (n) ->
          {
            int sum = getWeight();
            for (final Node child : children)
            {
              sum += child.getSubTreeWeight(subtreeWeight);
            }
            return sum;
          });
    }

    public Node getImbaNode(final Map<String, Integer> subtreeWeights)
    {
      switch (children.size())
      {
        case 0:
          return null;
        case 1:
          return children.get(0).getImbaNode(subtreeWeights);
        case 2:
          // todo here
          if (children.get(0).getSubTreeWeight(subtreeWeights) !=
              children.get(1).getSubTreeWeight(subtreeWeights))
          {
            throw new RuntimeException("todo fix this");
          }
          return null;
        default:
          final int c0w = children.get(0).getSubTreeWeight(subtreeWeights);
          final int c1w = children.get(1).getSubTreeWeight(subtreeWeights);
          final int c2w = children.get(2).getSubTreeWeight(subtreeWeights);

          if (c0w == c1w)
          {
            for (int i = 2; i < children.size(); ++i)
            {
              if (children.get(i).getSubTreeWeight(subtreeWeights) != c0w)
              {
                return children.get(i).getImbaNode(subtreeWeights);
              }
            }
            // so none of the child is imba, time to look at the siblings
            final Node parent = getParent();
            for (final Node n : parent.getChildren())
            {
              if (!n.equals(this) && n.getSubTreeWeight(subtreeWeights) == getSubTreeWeight(subtreeWeights))
              {
                // find a sibling whose weight == self, then i'm not imba
                return null;
              }
            }
            // none of sibling has weight == self
            return this;
          }
          else
          {
            if (c0w == c2w)
            {
              return children.get(1).getImbaNode(subtreeWeights);
            }
            else
            {
              return children.get(0).getImbaNode(subtreeWeights);
            }
          }
      }
    }

//    public boolean isSelfImba(final Map<String, Integer> subtreeWeights)
//    {
//      if (children.isEmpty())
//      {
//        return false;   // leaf can't be imba
//      }
//      else
//      {
//        final int w0 = children.get(0).getSubTreeWeight(subtreeWeights);
//        for (int i = 1; i < children.size(); ++i)
//        {
//          if (children.get(i).getSubTreeWeight(subtreeWeights) != w0)
//          {
//            return true;
//          }
//        }
//        return false;
//      }
//    }
//
//    // Return null if no imba node (is leaf or everything is fine), else return the imba node.
//    public Node getImbaNode(final Map<String, Integer> weights)
//    {
//      if (isSelfImba(weights))
//      {
//        // if any of the children is imba, ask them for it
//        Node temp = null;
//        for (final Node n : children)
//        {
//          if (n.isSelfImba(weights))
//          {
//            temp = n;
//            break;
//          }
//        }
//        if (temp != null)
//        {
//          return temp.getImbaNode(weights);
//        }
//        else
//        {
//          return this;
//        }
//      }
//      else
//      {
//        return null;
//      }
//    }
  }
}
