package grex;

import org.junit.jupiter.api.Test;

import static grex.Expector.expect;

class StringExpectorTests {

  @Test
  void nulll() {
    final String NULL = null;
    expect(NULL).toBeNull();
  }

  @Test
  void notNull() {
    final String NOT_NULL = "not null";
    expect(NOT_NULL).not().toBeNull();
  }

  @Test
  void empty() {
    final String EMPTY = "";
    expect(EMPTY).not().toBeNull().toBeEmpty().toBeBlank();
  }

  @Test
  void notEmpty() {
    final String NOT_EMPTY = "not empty";
    expect(NOT_EMPTY).not().toBeNull()
            .and()
            .not().toBeEmpty()
            .and()
            .not().toBeBlank();
  }

  @Test
  void throwing() {
    final String NOT_EMPTY = "not empty";
    try {
      expect(NOT_EMPTY).toBeNull();
    } catch (final Disappointment e) {
      expect(e.getMessage()).toContain("Expected: null").toContain(": \"not empty\"");
    }
  }


  @Test
  void all() {
    final String NOT_EMPTY = "not empty";
    expect(NOT_EMPTY)
            .not().toBeNull()
            .and()
            .not().toBeEmpty()
            .and()
            .not().toBeBlank()
            .and()
            .toContain("t e")
            .toHaveLength(9)
            .toHaveLengthGreaterThan(8)
            .toHaveLengthGreaterThanOrEqualTo(9)
            .toContainIgnoreCase("EmPTy");
  }
}
