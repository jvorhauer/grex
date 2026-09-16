package grex.collection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static grex.Expector.expect;
import static grex.Expector.fail;

class ListTest {

  @Test
  void of() {
    var l = List.of();
    expect(l).not().toBeNull();
    expect(l.size()).toBe(0);
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
  void ofJavaUtilList() {
    ArrayList<String> jl = new ArrayList<>();
    jl.add("first");
    jl.add("second");
    jl.add("third");
    List<String> l = List.of(jl);
    expect(l).toHaveSize(jl.size());
    expect(l.head().get()).toBe("first");
    expect(l.tail().head().get()).toBe("second");
  }

  @Test
  void add() {
    final List<String> l = new List<>();
    expect(l).toBeEmpty();
    expect(l).toHaveSize(0);

    final List<String> la = l.add("first");
    expect(la).not().toBeEmpty();
    expect(la).toHaveSize(1);

    final List<String> ll = List.of("First");
    expect(ll).not().toBeEmpty();
    expect(ll).toHaveSize(1);
  }

  @Test
  void slice() {
    List<String> l = List.of("first", "second", "third", "another", "and more");
    var slice = l.slice(1, 4);
    expect(slice).toHaveSize(3);
    expect(slice.toString()).toBe("List [ second, third, another ]");

    slice = l.slice(2, 15);
    expect(slice).toHaveSize(3);
    expect(slice.toString()).toBe("List [ third, another, and more ]");
  }

  @Test
  void insert() {
    List<String> l = List.of("first", "second", "third", "another", "and more");

    List<String> m = l.insert(2, "inserted");
    expect(l).toHaveSize(5);
    expect(m).toHaveSize(6);
    expect(m.get(2)).toBeDefined();
    expect(m.get(2).get()).toBe("inserted");

    m = l.insert(4, "inserted");
    expect(m).toHaveSize(6);
    expect(m.get(4)).toBeDefined();
    expect(m.get(4).get()).toBe("inserted");

    m = l.insert(7777, "inserted");
    expect(m).toHaveSize(6);
    expect(m.get(5)).toBeDefined();
    expect(m.get(5).get()).toBe("inserted");

    var n = l.insert(-1, "inserted");
    expect(n).toHaveSize(6);
    expect(n.head().get()).toBe("inserted");

    var o = l.insert("inserted", 2);
    expect(o).toHaveSize(6);

    var p = l.prepend("prepended");
    expect(p).toHaveSize(6);
    expect(p.head().get()).toBe("prepended");
  }

  @Test
  void size() {
    final List<String> l = new List<>();
    expect(l).toHaveSize(0);

    final List<String> m = List.of("one", "shoe", "tree");
    expect(m).toHaveSize(3);
    expect(m.size()).toBe(3);
  }

  @Test
  void has() {
    final List<String> l = new List<>();
    expect(l).toBeEmpty();
    expect(l.has("one")).not().toBeTrue();

    final List<String> m = List.of("one", "zwei", "drei", "vier");
    expect(m.has("one")).toBeTrue();
    expect(m.has("fünf")).not().toBeTrue();

    expect(List.of(1, 2, 3).has(2)).toBeTrue();
    expect(List.of(true, true, true).has(false)).not().toBeTrue();
    expect(List.of(2.0f, 3.0f, 66.6f).has(2.0f)).toBeTrue();
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
    List<String> l = List.of();
    expect(l.head()).toBeEmpty();

    l = List.of("eerste");
    expect(l.head()).toBeDefined();
    expect(l.head().get()).toBe("eerste");

    l = List.of("first", "second", "third", "another", "and more");
    expect(l.head().get()).toBe("first");
  }

  @Test
  void tail() {
    List<String> l = List.of();
    expect(l).toBeEmpty();
    expect(l.tail()).toBeEmpty();

    l = List.of("eerste");
    expect(l.tail()).toBeEmpty();

    l = List.of("first", "second", "third", "another", "and more");
    var tail = l.tail();
    expect(tail).not().toBeNull();
    expect(tail.size()).toBe(4);
    expect(tail.head()).toBeDefined();
    expect(tail.head().get()).toBe("second");
  }

  @Test
  void forEach() {
    AtomicInteger ai = new AtomicInteger(0);
    List.of(List.of(), List.of("one"), List.of("first", "second", "third", "another", "and more")).forEach(sl -> {
      ai.set(0);
      sl.forEach(s -> ai.getAndIncrement());
      expect(ai.get()).toBe(sl.size());
    });

    List.of().forEach(t -> fail("empty list must not forEach"));
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
  void sort() {
    List<String> l = List.of("first", "second", "third", "another", "and more");
    List<String> s = l.sort();
    expect(s).toHaveSize(l.size());
    expect(s.toString()).toBe("List [ and more, another, first, second, third ]");
    expect(l.toString()).toBe("List [ first, second, third, another, and more ]");
  }

  @Test
  void iterator() {
    List<String> l = List.of("first", "second", "third", "another", "and more");

    int count = 0;
    for (String s : l) {
      expect(s).not().toBeBlank();
      count++;
    }
    expect(count).toBe(5);

    count = 0;
    l = List.of();
    for (String s : l) {
      expect(s).not().toBeBlank();
      count++;
    }
    expect(count).toBeZero();

    l = List.of("one");
    for (String s : l) {
      expect(s).not().toBeBlank();
      count++;
    }
    expect(count).toBe(1);
  }

  @Test
  void distinct() {
    List<String> l = List.of("first", "second", "third", "another", "second");
    expect(l).toHaveSize(5);

    List<String> d = l.distinct();
    expect(l).toHaveSize(5);
    expect(d).toHaveSize(4);
  }
}
