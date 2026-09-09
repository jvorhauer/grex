package grex.control;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

  @Test
  void toOption() {
    Try<String> t = Try.of(() -> "Hi there");
    Option<String> o = t.toOption();
    expect(o).toBeDefined();
    expect(o.get()).toBe("Hi there");

    t = Try.of(() -> thrower("Boom"));
    o = t.toOption();
    expect(o).toBeEmpty();
  }

  @Test
  void map() {
    Try<String> t = Try.of(() -> "Hello");
    Try<Integer> mapped = t.map(String::length);
    expect(mapped.isSuccess()).toBeTrue();
    expect(mapped.get()).toBe(5);

    t = Try.of(() -> thrower("Boom"));
    mapped = t.map(String::length);
    expect(mapped.isFailure()).toBeTrue();
    expect(mapped.getCause()).toBeOf(IllegalArgumentException.class);

    t = Try.of(() -> "Hello");
    mapped = t.map(s -> thrower("Mapper failed").length());
    expect(mapped.isFailure()).toBeTrue();
    expect(mapped.getCause()).toBeOf(IllegalArgumentException.class);
  }

  @Test
  void flatMap() {
    Try<String> t = Try.of(() -> "Hello");
    Try<Integer> mapped = t.flatMap(s -> Try.of(s::length));
    expect(mapped.isSuccess()).toBeTrue();
    expect(mapped.get()).toBe(5);

    t = Try.of(() -> thrower("Boom"));
    mapped = t.flatMap(s -> Try.of(s::length));
    expect(mapped.isFailure()).toBeTrue();
    expect(mapped.getCause()).toBeOf(IllegalArgumentException.class);

    t = Try.of(() -> "Hello");
    mapped = t.flatMap(s -> Try.of(() -> thrower("Mapper failed").length()));
    expect(mapped.isFailure()).toBeTrue();
    expect(mapped.getCause()).toBeOf(IllegalArgumentException.class);
  }

  @Test
  void io() {
    Path path = Paths.get("/tmp/test.tst");
    Try<Path> t1 = Try.of(() -> Files.createFile(path));
    expect(t1).not().toBeNull();
    expect(t1.isSuccess()).toBeTrue();

    Try<Boolean> t2 = Try.of(() -> Files.deleteIfExists(path));
    expect(t2).not().toBeNull();
    expect(t2.isSuccess()).toBeTrue();
    expect(t2.get()).toBeTrue();

    Try<Boolean> t3 = Try.of(() -> Files.createFile(path))
      .flatMap(p -> Try.of(() -> Files.deleteIfExists(p)));
    expect(t3).not().toBeNull();
    expect(t3.isSuccess()).toBeTrue();
    expect(t3.get()).toBeTrue();
  }
}
