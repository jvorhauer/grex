package grex;

import java.math.BigDecimal;
import java.util.function.Predicate;

public final class BigDecimalExpector extends Expector<BigDecimal, BigDecimalExpector> {

  private static final Predicate<BigDecimal> pZero = b -> b == null || b.compareTo(BigDecimal.ZERO) != 0;
  private static final Predicate<BigDecimal> pPos = b -> b == null || b.compareTo(BigDecimal.ZERO) <= 0;
  private static final Predicate<BigDecimal> pNeg = b -> b == null || b.compareTo(BigDecimal.ZERO) >= 0;

  private final Predicate<BigDecimal> pEq = b -> b == null || actual == null || actual.compareTo(b) != 0;

  public BigDecimalExpector(final BigDecimal b) {
    super(b);
  }

  public BigDecimalExpector toBePositive() {
    if ((inverted ? pNeg : pPos).test(actual)) {
      disappoint(inverted ? "negative" : "positive");
    }
    return self();
  }

  public BigDecimalExpector toBeZero() {
    if ((inverted ? pZero.negate() : pZero).test(actual)) {
      disappoint(inverted ? "not 0" : "0");
    }
    return self();
  }

  public BigDecimalExpector toBe(final BigDecimal b) {
    if ((inverted ? pEq.negate() : pEq).test(b)) {
      disappoint(inverted ? "not " + stringify(b) : stringify(b));
    }
    return self();
  }
}