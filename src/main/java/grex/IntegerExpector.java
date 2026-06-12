package grex;

import java.util.function.Predicate;

@SuppressWarnings("UnusedReturnValue")
public final class IntegerExpector extends Expector<Integer, IntegerExpector> {

  private final Predicate<Integer> isPositive = n -> n > 0;

  public IntegerExpector(final Integer integer) {
    super(integer);
  }

  public grex.IntegerExpector toBePositive() {
    return isPositive.test(actual) ? this : disappointment(actual + " to be positive", stringify(actual));
  }

  public grex.IntegerExpector toBe(final Integer i) {
    return isNotNull && actual.compareTo(i) == 0 ? this : disappointment(actual + " to be " + i, stringify(actual));
  }
}
