package ankel.advent2017;

import com.google.common.base.Splitter;
import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.google.common.io.Resources;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A new system policy has been put in place that requires all accounts to use a passphrase instead of simply a password. A passphrase consists of a series of words (lowercase letters) separated by spaces.
 *
 * To ensure security, a valid passphrase must contain no duplicate words.
 *
 * For example:
 *
 * aa bb cc dd ee is valid.
 * aa bb cc dd aa is not valid - the word aa appears more than once.
 * aa bb cc dd aaa is valid - aa and aaa count as different words.
 *
 * The system's full passphrase list is available as your puzzle input. How many passphrases are valid?
 *
 * Input is stored in Day4Input.txt
 *
 * For added security, yet another system policy has been put in place. Now, a valid passphrase must contain no two words that are anagrams of each other - that is, a passphrase is invalid if any word's letters can be rearranged to form any other word in the passphrase.
 *
 * For example:
 *
 * abcde fghij is a valid passphrase.
 * abcde xyz ecdab is not valid - the letters from the third word can be rearranged to form the first word.
 * a ab abc abd abf abj is a valid passphrase, because all letters need to be used when forming another word.
 * iiii oiii ooii oooi oooo is valid.
 * oiii ioii iioi iiio is not valid - any of these words can be rearranged to form any other word.
 *
 * Under this new system policy, how many passphrases are valid?
 *
 * Once again there are definitely clever-er ways to do this...
 *
 * @author Binh Tran
 */
public class Day4Passphrase
{

  private static final Splitter SPACE_SPLITTER = Splitter.on(' ').trimResults().omitEmptyStrings();  // pun

  public static void main(final String[] args) throws Exception
  {
    int validPassphraseCount = Files
        .lines(Paths.get(Resources.getResource("Day4Input.txt").toURI()))
//        .map(Day4Passphrase::checkValid)
        .map(Day4Passphrase::checkValid2)
        .reduce((i, j) -> i + j)
        .orElse(0);

    System.out.println(validPassphraseCount);
  }

  /**
   * @return 1 if l is a valid passphrase, 0 if not
   */
  private static int checkValid(String l)
  {
    Set<String> seen = new HashSet<>();
    for (String s : SPACE_SPLITTER.split(l))
    {
      if (seen.contains(s))
      {
        return 0;
      }

      seen.add(s);
    }

    return 1;
  }

  private static int checkValid2(String l)
  {
    List<String> seen = new ArrayList<>();
    for (String s : SPACE_SPLITTER.split(l))
    {
      if (checkAnagrams(s, seen))
      {
        return 0;
      }

      seen.add(s);
    }

    return 1;
  }

  /**
   *
   * @return true if any element of seen is an anagram of s, else false
   */
  private static boolean checkAnagrams(String s, List<String> seen)
  {
    Multiset<Character> sCharCount = countChar(s);

    for (String s1: seen)
    {
      if (sCharCount.equals(countChar(s1)))
      {
        return true;
      }
    }

    return false;
  }

  private static Multiset<Character> countChar(String s)
  {
    Multiset<Character> ret = HashMultiset.create();
    for (char c : s.toCharArray())
    {
      ret.add(c);
    }

    return ret;
  }
}
