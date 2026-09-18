package grex.collection;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static grex.Expector.expect;

class MapTest {

  @Test
  void of() {
    var m = Map.of();
    expect(m).not().toBeNull();
    expect(m.size()).toBe(0);
    expect(m.isEmpty()).toBeTrue();
  }

  @Test
  void ofPairs() {
    final Map<String, Integer> m = Map.of(
      Pair.of("first", 1),
      Pair.of("second", 2),
      Pair.of("third", 3)
    );
    expect(m).not().toBeNull();
    expect(m.size()).toBe(3);
    expect(m.isEmpty()).not().toBeTrue();
  }

  @Test
  void ofJavaUtilMap() {
    final HashMap<String, Integer> jm = new HashMap<>();
    jm.put("a", 1);
    jm.put("b", 2);
    jm.put("c", 3);
    final Map<String, Integer> m = Map.of(jm);
    expect(m).not().toBeNull();
    expect(m.size()).toBe(3);
    expect(m.has("a")).toBeTrue();
    expect(m.get("a").get()).toBe(1);
  }

  @Test
  void put() {
    final Map<String, Integer> m = Map.of();
    expect(m.size()).toBe(0);

    final Map<String, Integer> m1 = m.put("key", 42);
    expect(m1.size()).toBe(1);
    expect(m1.has("key")).toBeTrue();
    expect(m1.get("key").get()).toBe(42);

    final Map<String, Integer> m2 = m1.put("another", 100);
    expect(m2.size()).toBe(2);
    expect(m2.has("key")).toBeTrue();
    expect(m2.has("another")).toBeTrue();
  }

  @Test
  void putPair() {
    final Map<String, Integer> m = Map.of();
    final Map<String, Integer> m1 = m.put(Pair.of("key", 42));
    expect(m1.size()).toBe(1);
    expect(m1.get("key").get()).toBe(42);
  }

  @Test
  void get() {
    final Map<String, Integer> m = Map.of(
      Pair.of("first", 1),
      Pair.of("second", 2),
      Pair.of("third", 3)
    );
    expect(m.get("first")).toBeDefined();
    expect(m.get("first").get()).toBe(1);
    expect(m.get("second").get()).toBe(2);
    expect(m.get("nonexistent")).toBeEmpty();
  }

  @Test
  void has() {
    final Map<String, Integer> m = Map.of(
      Pair.of("a", 1),
      Pair.of("b", 2)
    );
    expect(m.has("a")).toBeTrue();
    expect(m.has("b")).toBeTrue();
    expect(m.has("c")).not().toBeTrue();
  }

  @Test
  void remove() {
    final Map<String, Integer> m = Map.of(
      Pair.of("a", 1),
      Pair.of("b", 2),
      Pair.of("c", 3)
    );
    expect(m.size()).toBe(3);

    final Map<String, Integer> m2 = m.remove("b");
    expect(m2.size()).toBe(2);
    expect(m2.has("a")).toBeTrue();
    expect(m2.has("b")).not().toBeTrue();
    expect(m2.has("c")).toBeTrue();

    final Map<String, Integer> m3 = m2.remove("nonexistent");
    expect(m3.size()).toBe(2);
  }

  @Test
  void keys() {
    final Map<String, Integer> m = Map.of(
      Pair.of("first", 1),
      Pair.of("second", 2),
      Pair.of("third", 3)
    );
    final List<String> keys = m.keys();
    expect(keys).toHaveSize(3);
    expect(keys.has("first")).toBeTrue();
    expect(keys.has("second")).toBeTrue();
    expect(keys.has("third")).toBeTrue();
  }

  @Test
  void values() {
    final Map<String, Integer> m = Map.of(
      Pair.of("first", 1),
      Pair.of("second", 2),
      Pair.of("third", 3)
    );
    final List<Integer> values = m.values();
    expect(values).toHaveSize(3);
    expect(values.has(1)).toBeTrue();
    expect(values.has(2)).toBeTrue();
    expect(values.has(3)).toBeTrue();
  }

  @Test
  void mapValues() {
    final Map<String, Integer> m = Map.of(
      Pair.of("a", 1),
      Pair.of("b", 2),
      Pair.of("c", 3)
    );
    Map<String, Integer> doubled = m.map(v -> v * 2);
    expect(doubled.size()).toBe(3);
    expect(doubled.get("a").get()).toBe(2);
    expect(doubled.get("b").get()).toBe(4);
    expect(doubled.get("c").get()).toBe(6);
  }

  @Test
  void iterator() {
    Map<String, Integer> m = Map.of(
      Pair.of("first", 1),
      Pair.of("second", 2),
      Pair.of("third", 3)
    );

    int count = 0;
    for (Pair<String, Integer> p : m) {
      expect(p).not().toBeNull();
      count++;
    }
    expect(count).toBe(3);

    m = Map.of();
    count = 0;
    for (Pair<String, Integer> _ : m) {
      count++;
    }
    expect(count).toBe(0);
  }

  @Test
  void toStringTest() {
    Map<String, Integer> m = Map.of(
      Pair.of("a", 1),
      Pair.of("b", 2)
    );
    expect(m.toString()).toBe("Map[ (a -> 1), (b -> 2) ]");

    Map<String, Integer> empty = Map.of();
    expect(empty.toString()).toBe("Map[ ]");
  }

  @Test
  void isEmpty() {
    Map<String, Integer> m = Map.of();
    expect(m.isEmpty()).toBeTrue();

    m = Map.of(Pair.of("a", 1));
    expect(m.isEmpty()).not().toBeTrue();
  }

  @Test
  void size() {
    Map<String, Integer> m = Map.of();
    expect(m.size()).toBe(0);

    m = Map.of(Pair.of("a", 1));
    expect(m.size()).toBe(1);

    m = Map.of(Pair.of("a", 1), Pair.of("b", 2), Pair.of("c", 3));
    expect(m.size()).toBe(3);
  }
}
