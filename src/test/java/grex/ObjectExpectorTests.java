package grex;

import org.junit.jupiter.api.Test;

import static grex.Expector.expect;

class ObjectExpectorTests {

  @SuppressWarnings("ConstantValue")
  @Test
  void nulll() {
    final Klont knul = null;
    expect(knul).toBeNull();
  }

  @Test
  void notNull() {
    final Klont klontje = new Klont();
    klontje.label = "test";

    expect(klontje).not().toBeNull();
  }

  private static final class Klont {
    public String label;
  }
}
