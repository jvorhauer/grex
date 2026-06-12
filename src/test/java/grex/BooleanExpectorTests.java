package grex;

import org.junit.jupiter.api.Test;

import static grex.Expector.expect;

public class BooleanExpectorTests {

  @Test
  void truth() {
    final Boolean vero = true;
    expect(vero).not().toBeNull().toBeTrue();
  }

  @Test
  void lier() {
    final boolean falso = false;
    expect(falso).not().toBeNull().toBeFalse();
  }
}
