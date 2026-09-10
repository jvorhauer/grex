package grex.collection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static grex.Expector.expect;

class ListTest {

  @Test
  void of() {
    var l = List.of();
    expect(l).not().toBeNull();
    expect(l.size()).toBe(0);
  }

  @Test
  void ofCollection() {
    var c = new ArrayList<String>();
    expect(c.addAll(java.util.List.of("one", "two", "three"))).toBeTrue();
    var l = List.of(c);
    expect(l).not().toBeNull();
    expect(l.size()).toBe(3);
    expect(l.head()).toBeDefined();
  }

  @Test
  void ofVarargs() {
    List<String> l = List.of("first", "second", "third", "another", "and more");
    expect(l).not().toBeNull();
    expect(l.size()).toBe(5);
    expect(l.head()).toBeDefined();
    expect(l.head().get()).toBe("first");
    expect(l.get(1)).toBeDefined();
    expect(l.get(1).get()).toBe("second");
  }

  @Test
  void append() {
    final List<String> l = new List<>();
    final List<String> la = l.append("first");
    expect(l).not().toBeNull();
    expect(l.size()).toBe(1);
  }

  @Test
  void size() {
  }

  @Test
  void isEmpty() {
    List<Integer> l = List.of();
    expect(l).not().toBeNull();
    expect(l.isEmpty()).toBeTrue();
    expect(l.size()).toBe(0);
  }

  @Test
  void get() {
    List<String> l = List.of("first", "second", "third", "another", "and more");
    expect(l.get(1)).toBeDefined();
    expect(l.get(6)).toBeEmpty();
    expect(l.get(-1)).toBeEmpty();
  }

  @Test
  void exists() {
    List<String> l = List.of("first", "second", "third", "another", "and more");
    expect(l.exists(3)).toBeTrue();
    expect(l.exists(6)).not().toBeTrue();
    expect(l.exists(-1)).not().toBeTrue();
  }

  @Test
  void head() {
    List<String> l = List.of("first", "second", "third", "another", "and more");
    expect(l.head().get()).toBe("first");
  }

  @Test
  void tail() {
    List<String> l = List.of("first", "second", "third", "another", "and more");
    var tail = l.tail();
    expect(tail).not().toBeNull();
    expect(tail.size()).toBe(4);
    expect(tail.head()).toBeDefined();
    expect(tail.head().get()).toBe("second");
  }

  @Test
  void filter() {
    List<String> l = List.of("first", "second", "third", "another", "and more");
    expect(l).not().toBeEmpty();
    expect(l).toHaveSize(5);
    List<String> f = l.filter(e -> e.startsWith("a"));
    expect(f).not().toBeEmpty();
    expect(f).toHaveSize(2);

    l = List.of();
    f = l.filter(e -> e.endsWith("?"));
    expect(f).not().toBeNull();
    expect(f).toBeEmpty();
    expect(f).toHaveSize(0);
  }

  @Test
  void map() {
    List<String> l = List.of("first", "second", "third", "another", "and more");
    List<Integer> m = l.map(String::length);
    expect(m).not().toBeEmpty();
    expect(m).toHaveSize(5);
    expect(m.head().get()).toBe(5);
  }

  @Test
  void flatten() {
    List<String> inner1 = List.of("a", "b", "c");
    List<String> inner2 = List.of("d", "e");
    List<String> inner3 = List.of("f");
    List<List<String>> nested = List.of(inner1, inner2, inner3);
    List<String> flattened = nested.flatten();
    expect(flattened).not().toBeNull();
    expect(flattened.size()).toBe(6);
    expect(flattened.get(0).get()).toBe("a");
    expect(flattened.get(1).get()).toBe("b");
    expect(flattened.get(2).get()).toBe("c");
    expect(flattened.get(3).get()).toBe("d");
    expect(flattened.get(4).get()).toBe("e");
    expect(flattened.get(5).get()).toBe("f");
  }

  @Test
  void flattenEmpty() {
    List<List<String>> nested = List.of();
    List<String> flattened = nested.flatten();
    expect(flattened).not().toBeNull();
    expect(flattened.size()).toBe(0);
  }

  @Test
  void flattenWithEmptySublists() {
    List<String> inner1 = List.of("a", "b");
    List<String> inner2 = List.of();
    List<String> inner3 = List.of("c");
    List<List<String>> nested = List.of(inner1, inner2, inner3);
    List<String> flattened = nested.flatten();
    expect(flattened.size()).toBe(3);
    expect(flattened.get(0).get()).toBe("a");
    expect(flattened.get(1).get()).toBe("b");
    expect(flattened.get(2).get()).toBe("c");
  }

  @Test
  void flatMap() {
    List<String> l = List.of("hello", "world");
    List<String> result = l.flatMap(s -> List.of(s.substring(0, 1), s.substring(s.length() - 1)));
    expect(result).not().toBeNull();
    expect(result.size()).toBe(4);
    expect(result.get(0).get()).toBe("h");
    expect(result.get(1).get()).toBe("o");
    expect(result.get(2).get()).toBe("w");
    expect(result.get(3).get()).toBe("d");
  }

  @Test
  void flatMapEmpty() {
    List<String> l = List.of();
    List<String> result = l.flatMap(s -> List.of(s.substring(0, 1)));
    expect(result).not().toBeNull();
    expect(result.size()).toBe(0);
  }
}
