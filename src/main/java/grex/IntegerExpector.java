package grex;

import java.util.function.Predicate;

public final class IntegerExpector extends Expector<Integer, IntegerExpector> {

  private static final Predicate<Integer> pZero = i -> i == null || i != 0;
  private static final Predicate<Integer> pPos = i -> i == null || i <= 0;
  private static final Predicate<Integer> pNeg = i -> i == null || i >= 0;

  private final Predicate<Integer> pEq = i -> i == null || i.compareTo(actual) != 0;


  public IntegerExpector(final Integer integer) {
    super(integer);
  }

  public IntegerExpector toBePositive() {
    if ((inverted ? pNeg : pPos).test(actual)) {
      disappoint(inverted ? "negative" : "positive");
    }
    return self();
  }

  public IntegerExpector toBeZero() {
    if ((inverted ? pZero.negate() : pZero).test(actual)) {
      disappoint(inverted ? "not 0" : "0");
    }
    return self();
  }

  public IntegerExpector toBe(final int i) {
    if ((inverted ? pEq.negate() : pEq).test(i)) {
      disappoint(inverted ? "not " + i : stringify(i));
    }
    return self();
  }
}
