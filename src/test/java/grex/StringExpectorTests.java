package grex;

import org.junit.jupiter.api.Test;

import static grex.Expector.expect;

class StringExpectorTests {

  @Test
  void nulll() {
    final String NULL = null;
    expect(NULL).toBeNull();
    expect(NULL).toBeEmpty();
    expect(NULL).not().toContain("null").and().not().toContain("NULL");
  }

  @Test
  void notNull() {
    final String NOT_NULL = "not null";
    expect(NOT_NULL).not().toBeNull();
    expect(NOT_NULL).toContain("not");
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
      .not().toBeBlank()
      .and()
      .toContain("not");
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
