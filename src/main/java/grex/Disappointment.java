package grex;

public final class Disappointment extends AssertionError {

  public Disappointment(final String message) {
    super(message);
  }

  public Disappointment(final String msg, final String expected, final String actual) {
    this("Expected %s with value %s to be %s\n\tExpected: %s\n\tActual  : %s".formatted(
      msg,
      actual, expected,
      expected, actual)
    );
  }
}
