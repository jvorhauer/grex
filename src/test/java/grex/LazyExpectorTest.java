package grex;

import grex.control.Lazy;
import org.junit.jupiter.api.Test;

import static grex.Expector.expect;

class LazyExpectorTest {

  @Test
  void evaluated() {
    final Lazy<String> ls = Lazy.of(() -> "Hello");
    expect(ls).not().toBeEvaluated();

    ls.get();
    expect(ls).toBeEvaluated();
  }

  @Test
  void defined() {
    final Lazy<String> ls = Lazy.of(() -> "Hello");
    expect(ls).toBeDefined();

    ls.get();
    expect(ls).toBeDefined();
  }
}
