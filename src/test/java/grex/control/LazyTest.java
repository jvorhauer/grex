package grex.control;

import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static grex.Expector.expect;

class LazyTest {

  @Test
  void of() {
    final Lazy<String> l = Lazy.of(() -> "Hello");
    expect(l.isEvaluated()).not().toBeTrue();
    expect(l.isEmpty()).not().toBeTrue();
    expect(l.isDefined()).toBeTrue();

    String g = l.get();
    expect(l.isEvaluated()).toBeTrue();
    expect(l.isEmpty()).not().toBeTrue();
    expect(l.isDefined()).toBeTrue();

    expect(g).toBe("Hello");

    expect(l.isLazy()).toBeTrue();
  }

  @Test
  void map() {
    final Lazy<Integer> li = Lazy.of(() -> 42);
    final Lazy<String> ls = li.map(String::valueOf);
    expect(ls.isEvaluated()).not().toBeTrue();
    expect(li.isEvaluated()).not().toBeTrue();
    expect(ls.get()).toBe("42");

    final Lazy<Logger> logger = Lazy.of(Logger::getAnonymousLogger);
    expect(logger.isEvaluated()).not().toBeTrue();
  }

  @Test
  void flatMap() {
    final Lazy<Integer> li = Lazy.of(() -> 42);
    final Lazy<String> ls = li.flatMap(i -> Lazy.of(() -> String.valueOf(i)));
    expect(ls).not().toBeEvaluated();
    expect(li).toBeEvaluated();
    expect(ls.get()).toBe("42");
    expect(ls).toBeEvaluated();
  }
}
