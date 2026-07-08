package grex;

import org.junit.jupiter.api.Test;

import static grex.Expector.expect;

@SuppressWarnings("ConstantValue")
final class IntegerExpectorTests {

  @Test
  void all() {
    expect(5).not().toBeNull()
      .toBePositive()
      .not().toBeZero()
      .toBe(5);

    expect(-42).not().toBeNull()
      .not().toBePositive()
      .not().toBeZero()
      .toBeOf(Integer.class)
      .toBe(-42);
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
    expect(x - 2).not().toBe(42);
    expect(x - 2).not().not().toBe(0);
  }

  @Test
  void toBePositive() {
    expect(5).toBePositive();
    expect(-1).not().toBePositive();
  }

  @Test
  void toBeGreaterThan() {
    expect(5).toBeGreaterThan(4);
    expect(5).not().toBeGreaterThan(5);
    expect(5).not().toBeGreaterThan(6);
    expect((Integer) null).not().toBeGreaterThan(0);
  }

  @Test
  void toBeZero() {
    expect(0).toBeZero();
    expect(1).not().toBeZero();
    expect((Integer) null).not().toBeZero();

    expect(0).toBe(0);
    expect(0).not().toBe(1);
  }

  @Test
  void nullToBeNotVeryInteresting() {
    Integer n = null;
    expect(n).toBeNull();
    expect(() -> expect(n).as("FAIL").toBeZero()).toDisappoint()
      .toContain("Expected: 0")
      .toContain("FAIL")
      .toContain("NULL");
  }

}
