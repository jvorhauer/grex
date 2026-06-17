package grex;

import org.junit.jupiter.api.Test;

import static grex.Expector.expect;

class SupplierExpectorTests {

  @Test
  void isDisappointed() {
    expect(() -> expect(1L).toBeZero()).toDisappoint("Expected: zero");
  }

  @SuppressWarnings({"divzero", "NumericOverflow"})
  @Test
  void hasThrown() {
    expect(() -> 1 / 0).toHaveThrown(ArithmeticException.class, "/ by zero");
  }

  @Test
  void didNotThrow() {
    expect(() -> 1 + 1).toBeNull();
  }
}
