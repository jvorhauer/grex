package grex;

import org.junit.jupiter.api.Test;

import static grex.Expector.expect;

public class BooleanExpectorTests {

  @Test
  void truth() {
    final Boolean vero = true;
    expect(vero).not().toBeNull().toBeTrue();

    expect(() -> expect(vero).not().toBeTrue()).toDisappoint("Expected: false").toContain("Actual  : true");
  }

  @Test
  void lier() {
    final Boolean falso = false;
    expect(falso).not().toBeNull().not().toBeTrue();
    expect(falso).not().toBeTrue();

    expect(() -> expect(false).toBeTrue()).toDisappoint("Expected: true");
  }

  @Test
  void onNull() {
    final Boolean zero = null;
    expect(zero).toBeNull().not().toBeTrue();
  }
}
