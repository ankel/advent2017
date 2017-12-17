package ankel.advent2017;

import com.google.common.base.Splitter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Input is from file Day16Input.txt
 *
 * @author Binh Tran
 */
public class Day16Dance
{
  private static final int SIZE = 16;
  private static final Splitter COMMA_SPLITTER = Splitter.on(',').trimResults().omitEmptyStrings();
  private static final Splitter SLASH_SPLITTER = Splitter.on('/').trimResults().omitEmptyStrings();
  private static final int ROUNDS = 1000000;

  public static void main(final String[] args) throws Exception
  {
    final List<String> steps = Day7Recursive.streamFromResFile("Day16Input.txt")
        .findFirst()
        .map(COMMA_SPLITTER::splitToList)
        .get();

    final Map<Character, Integer> positionsByChar = new HashMap<>();
    final Map<Integer, Character> characterByPos = new HashMap<>();
    final Map<Integer, ExchangeArgs> exchangeSteps = new HashMap<>();
    final Map<Integer, PartnerArgs> partnerSteps = new HashMap<>();

    // init
    for (int i = 0; i < SIZE; ++i)
    {
      final char current = (char) ('a' + i);
      characterByPos.put(i, current);
      positionsByChar.put(current, i);
    }

    for (int i = 0; i < ROUNDS; ++i)
    {
      int j = 0;
      for (final String step : steps)
      {
//        System.out.println(step);
        switch (step.charAt(0))
        {
          case 's':
            spin(step.substring(1), positionsByChar, characterByPos);
            break;
          case 'x':
            exchange(exchangeSteps, j, step.substring(1), positionsByChar, characterByPos);
            break;
          case 'p':
            parner(partnerSteps, j, step.substring(1), positionsByChar, characterByPos);
            break;
          default:
            throw new IllegalStateException(step);
        }
        j++;
      }

      if (i % 1000000 == 0)
      {
        System.out.println(System.currentTimeMillis());
      }
    }

    System.out.println();

    for (int i = 0; i < SIZE; ++i)
    {
      System.out.print(characterByPos.get(i));
    }
  }

  private static void parner(
      final Map<Integer, PartnerArgs> swapSteps,
      final int j,
      final String step,
      final Map<Character, Integer> positionsByChar,
      final Map<Integer, Character> characterByPos)
  {
    final PartnerArgs partnerArgs = swapSteps.computeIfAbsent(j,
        (__) ->
        {
          final char charA = step.charAt(0);
          final char charB = step.charAt(2);

          return new PartnerArgs(charA, charB);
        });

    swap(positionsByChar, characterByPos, partnerArgs);
  }

  private static void swap(final Map<Character, Integer> positionsByChar, final Map<Integer, Character> characterByPos, final PartnerArgs partnerArgs)
  {
    swap(positionsByChar, characterByPos,
        positionsByChar.get(partnerArgs.getCharA()), positionsByChar.get(partnerArgs.getCharB()),
        partnerArgs.getCharA(), partnerArgs.getCharB());
  }

  private static void exchange(
      final Map<Integer, ExchangeArgs> swapSteps,
      final int j,
      final String step,
      final Map<Character, Integer> positionsByChar,
      final Map<Integer, Character> characterByPos)
  {
    final ExchangeArgs exchangeArgs = swapSteps.computeIfAbsent(j,
        (__) ->
        {
          final List<String> s = SLASH_SPLITTER.splitToList(step);
          final int posA = Integer.valueOf(s.get(0));
          final int posB = Integer.valueOf(s.get(1));

          return new ExchangeArgs(posA, posB);
        });

    swap(positionsByChar, characterByPos, exchangeArgs);
  }

  private static void swap(final Map<Character, Integer> positionsByChar, final Map<Integer, Character> characterByPos, final ExchangeArgs
      exchangeArgs)
  {
    swap(positionsByChar, characterByPos,
        exchangeArgs.getPosA(), exchangeArgs.getPosB(),
        characterByPos.get(exchangeArgs.getPosA()), characterByPos.get(exchangeArgs.getPosB()));
  }

  private static void swap(
      final Map<Character, Integer> positionsByChar,
      final Map<Integer, Character> characterByPos,
      final int posA,
      final int posB,
      final char charA,
      final char charB)
  {
    // swap in character by position
    characterByPos.put(posA, charB);
    characterByPos.put(posB, charA);

    // swap in position by character
    positionsByChar.put(charA, posB);
    positionsByChar.put(charB, posA);
  }

  private static void spin(
      final String step,
      final Map<Character, Integer> line,
      final Map<Integer, Character> positions)
  {
    final int spinStep = Integer.valueOf(step);
    for (int i = 0; i < SIZE; ++i)
    {
      final Character currentCharacter = positions.get(i);
      line.compute(currentCharacter,
          (__, index) ->
              (index + spinStep) % SIZE);
    }

    // re-sync positions
    positions.clear();
    line.forEach((c, i) -> positions.put(i, c));
  }

  @Getter
  @RequiredArgsConstructor
  private static class ExchangeArgs
  {
    private final int posA;
    private final int posB;
  }

  @Getter
  @RequiredArgsConstructor
  private static class PartnerArgs
  {
    private final char charA;
    private final char charB;
  }
}
