package grex;

import java.util.function.Predicate;

@SuppressWarnings("UnusedReturnValue")
public final class BooleanExpector extends Expector<Boolean, BooleanExpector> {

  private final Predicate<Boolean> pTrue = b -> b == null || !b;

  public BooleanExpector(final Boolean b) {
    super(b);
  }

  public BooleanExpector toBeTrue() {
    if ((inverted ? pTrue.negate() : pTrue).test(actual)) {
      disappoint(Boolean.toString(!inverted));
    }
    return self();
  }
}
