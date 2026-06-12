package grex;

@SuppressWarnings("UnusedReturnValue")
public final class BooleanExpector extends Expector<Boolean, BooleanExpector> {

  private final boolean isTrue;

  public BooleanExpector(final Boolean b) {
    super(b);
    this.isTrue = isNotNull && b;
  }

  public BooleanExpector toBeTrue() {
    return isTrue ? this : disappointment("true");
  }

  public BooleanExpector toBeFalse() {
    return !isTrue ? this : disappointment("true");
  }
}
