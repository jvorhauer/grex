package grex.collection;

import org.junit.jupiter.api.Test;

import static grex.Expector.expect;

class PairTest {

  @Test
  void ctor() {
    final Pair<String, Integer> pair = new Pair<>("key", 42);
    expect(pair.toString()).toBe("(key -> 42)");

    final Pair<Integer, List<String>> second = Pair.of(13, List.of("first", "second", "third"));
    expect(second.toString()).toBe("(13 -> List[ first, second, third ])");
  }

}
