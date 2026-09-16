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
  }

  @Test
  void map() {
    final Lazy<Integer> i = Lazy.of(() -> 42);
    final Lazy<String> s = i.map(String::valueOf);
    expect(s.isEvaluated()).not().toBeTrue();
    expect(i.isEvaluated()).not().toBeTrue();
    expect(s.get()).toBe("42");

    final Lazy<Logger> logger = Lazy.of(Logger::getAnonymousLogger);
    expect(logger.isEvaluated()).not().toBeTrue();
    logger.get().info("logger");
  }
}
