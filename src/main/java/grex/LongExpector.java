package grex;

import java.util.function.Predicate;

public final class LongExpector extends Expector<Long, LongExpector> {

  private final Predicate<Long> equal = l -> isNotNull && actual.compareTo(l) == 0;
  private final Predicate<Long> positive = l -> isNotNull && l > 0L;
  private final Predicate<Long> zero = l -> isNotNull && l == 0;

  public LongExpector(final Long l) {
    super(l);
  }

  public LongExpector toBe(final Long l) {
    return equal.test(l) ? this : disappointment(stringify(l), stringify(actual));
  }

  public LongExpector toBePositive() {
    return positive.test(actual) ? this : disappointment("positive long value", stringify(actual));
  }

  public LongExpector toBeZero() {
    return zero.test(actual) ? this : disappointment("zero (0) long value", stringify(actual));
  }
}
