package grex;

public class Disappointment extends AssertionError {

  public Disappointment(final String message) {
    super(message);
  }

  public Disappointment(final String expected, final String actual) {
    this("Expected %s to be %s\n\tExpected: %s\n\tActual  : %s".formatted(actual, expected, expected, actual));
  }
}
