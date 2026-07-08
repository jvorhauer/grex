package grex;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static grex.Expector.expect;

@SuppressWarnings("ConstantValue")
final class BigDecimalExpectorTests {

  @Test
  void all() {
    expect(BigDecimal.valueOf(5)).not().toBeNull()
      .toBePositive()
      .not().toBeZero()
      .toBe(BigDecimal.valueOf(5));

    expect(BigDecimal.valueOf(-42)).not().toBeNull()
      .not().toBePositive()
      .not().toBeZero()
      .toBeOf(BigDecimal.class)
      .toBe(BigDecimal.valueOf(-42));
  }

  @Test
  void not() {
    expect(BigDecimal.valueOf(5)).not().toBeNull();
  }

  @Test
  void calculations() {
    BigDecimal x = BigDecimal.valueOf(2);
    expect(x.add(BigDecimal.valueOf(2))).toBe(BigDecimal.valueOf(4));
    expect(x.multiply(BigDecimal.valueOf(2))).toBe(BigDecimal.valueOf(4)).toBePositive();
    expect(x.subtract(BigDecimal.valueOf(2))).toBe(BigDecimal.ZERO);
    expect(x.subtract(BigDecimal.valueOf(2))).not().toBe(BigDecimal.valueOf(42));
    expect(x.subtract(BigDecimal.valueOf(2))).not().not().toBe(BigDecimal.ZERO);
  }

  @Test
  void toBePositive() {
    expect(BigDecimal.valueOf(5)).toBePositive();
    expect(BigDecimal.valueOf(-1)).not().toBePositive();
  }

  @Test
  void toBeZero() {
    expect(BigDecimal.ZERO).toBeZero();
    expect(BigDecimal.valueOf(1)).not().toBeZero();
    expect((BigDecimal) null).not().toBeZero();

    expect(BigDecimal.ZERO).toBe(BigDecimal.ZERO);
    expect(BigDecimal.ZERO).not().toBe(BigDecimal.valueOf(1));
  }

  @Test
  void nullToBeNotVeryInteresting() {
    BigDecimal n = null;
    expect(n).toBeNull();
    expect(() -> expect(n).as("FAIL").toBeZero()).toDisappoint()
      .toContain("Expected: 0")
      .toContain("FAIL")
      .toContain("NULL");
  }

}
