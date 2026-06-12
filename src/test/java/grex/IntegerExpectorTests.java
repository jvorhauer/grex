package grex;

import org.junit.jupiter.api.Test;

import static grex.Expector.expect;

@SuppressWarnings("ConstantValue")
final class IntegerExpectorTests {

  @Test
  void all() {
    expect(5).not().toBeNull()
            .toBePositive()
            .toBe(5);
  }

  @Test
  void not() {
    expect(5).not().toBeNull();
  }

  @Test
  void calculations() {
    int x = 2;
    expect(x + 2).toBe(4);
    expect(x * 2).toBe(4).toBePositive();
    expect(x - 2).toBe(0);
  }
}
