package grex.control;

import org.junit.jupiter.api.Test;

import static grex.Expector.expect;

class TryTests {

  @Test
  void success() {
    Try<String> ts = Try.of(() -> "Hello");
    expect(ts).not().toBeNull();
    expect(ts.get()).toBe("Hello");
    expect(ts.isSuccess()).toBeTrue();
    expect(ts.isFailure()).not().toBeTrue();
  }

  @Test
  void failure() {
    Try<String> ts = Try.of(() -> thrower("You can't just do that!"));
    expect(ts).not().toBeNull();
    expect(ts).toBeOf(Try.Failure.class);
    expect(ts.isSuccess()).not().toBeTrue();
    expect(ts.isFailure()).toBeTrue();
    expect(ts.getCause()).toBeOf(IllegalArgumentException.class);
  }

  @SuppressWarnings({ "EqualsWithItself", "EqualsBetweenInconvertibleTypes" })
  @Test
  void equals() {
    Try<String> t1 = Try.of(() -> "Hello");
    expect(t1.equals(t1)).toBeTrue();
    expect(t1.equals("Hello")).not().toBeTrue();

    Try<String> t2 = Try.of(() -> "Hello");
    expect(t1.equals(t2)).toBeTrue();

    t2 = Try.of(() -> "World");
    expect(t1.equals(t2)).not().toBeTrue();

    t1 = Try.of(() -> thrower("T1"));
    expect(t1.equals(t2)).not().toBeTrue();

    t2 = Try.of(() -> thrower("T1"));
    expect(t1.equals(t2)).not().toBeTrue();    // throwers are called on different lines, the stack trace contains linenumbers!

    t2 = Try.of(() -> thrower("T2"));
    expect(t1.equals(t2)).not().toBeTrue();
  }

  @Test
  void toEither() {
    Try<String> t = Try.of(() -> "Hello");
    Either<? extends Throwable, String> e = t.toEither();
    expect(e).not().toBeNull();
    expect(e.get()).toBe("Hello");

    t = Try.of(() -> thrower("World"));
    e = t.toEither();
    expect(e).not().toBeNull();
    expect(e.getLeft()).toBeOf(IllegalArgumentException.class);
  }

  private String thrower(final String s) {
    throw new IllegalArgumentException(s);
  }
}
