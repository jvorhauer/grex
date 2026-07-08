package grex;

import java.util.function.Predicate;

public final class LongExpector extends Expector<Long, LongExpector> {

  private final Predicate<Long> pEq = l -> l == null || actual == null || actual.compareTo(l) != 0;
  private final Predicate<Long> pPos = l -> l == null ||  l <= 0L;
  private final Predicate<Long> pNeg = l -> l == null || l >= 0L;
  private final Predicate<Long> pZero = l -> l == null ||  l != 0L;

  public LongExpector(final Long l) {
    super(l);
  }

  public LongExpector toBe(final Long l) {
    if ((inverted ? pEq.negate() : pEq).test(l)) {
      disappoint(inverted ? "not " + stringify(l) : stringify(l));
    }
    return self();
  }

  public LongExpector toBePositive() {
    if ((inverted ? pNeg : pPos).test(actual)) {
      disappoint(inverted ? "negative" : "positive");
    }
    return self();
  }

  public LongExpector toBeZero() {
    if ((inverted ? pZero.negate() : pZero).test(actual)) {
      disappoint(inverted ? "not zero" : "zero");
    }
    return self();
  }
}
