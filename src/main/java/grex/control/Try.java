package grex.control;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;

public sealed interface Try<T> extends Monad<T> {

  static <T> Try<T> of(final Callable<T> c) {
    Objects.requireNonNull(c);
    try {
      return new Success<>(c.call());
    } catch (final Throwable t) {
      return new Failure<>(t);
    }
  }

  T get();
  boolean isSuccess();
  default boolean isFailure() { return !isSuccess(); }

  Throwable getCause();

  Either<? extends Throwable, T> toEither();
  Option<T> toOption();

  <U> Try<U> map(final Function<? super T, ? extends U> mapper);

  @Override
  <U> Try<U> flatMap(final Function<? super T, ? extends Monad<U>> mapper);


  record Success<T>(T value) implements Try<T> {

    @Override
    public T get() { return value; }

    public boolean isSuccess() { return true; }

    public Throwable getCause() { throw new UnsupportedOperationException("getCause on Success"); }

    public boolean equals(final Object o) {
      return this == o || (o instanceof Success<?>(Object os) && Objects.equals(value, os));
    }

    public Either<? extends Throwable, T> toEither() {
      return Either.right(value);
    }

    public Option<T> toOption() { return Option.of(value); }

    @Override
    public int hashCode() { return Objects.hashCode(value); }

    @Override
    public String toString() { return "Success(" + value + ")"; }

    @Override
    public <U> Try<U> map(final Function<? super T, ? extends U> mapper) {
      try {
        return new Success<>(mapper.apply(value));
      } catch (final Throwable t) {
        return new Failure<>(t);
      }
    }

    @Override
    public <U> Try<U> flatMap(final Function<? super T, ? extends Monad<U>> mapper) {
      try {
        return (Try<U>) mapper.apply(value);
      } catch (final Throwable t) {
        return new Failure<>(t);
      }
    }
  }


  record Failure<T>(Throwable cause) implements Try<T> {

    @Override
    public T get() {
      return hurls(cause);
    }

    public boolean isSuccess() { return false; }

    @Override
    public Throwable getCause() { return cause; }

    public Either<? extends Throwable, T> toEither() {
      return Either.left(cause);
    }

    public Option<T> toOption() { return Option.none(); }

    public boolean equals(final Object o) {
      return (this == o) || (o instanceof Try.Failure<?>(Throwable oc) && Arrays.deepEquals(cause.getStackTrace(), (oc.getStackTrace())));
    }

    @Override
    public int hashCode() { return Arrays.hashCode(cause.getStackTrace()); }

    @Override
    public String toString() { return "Failure(" + cause.getMessage() + ")"; }

    @Override
    public <U> Try<U> map(final Function<? super T, ? extends U> mapper) {
      return new Failure<>(cause);
    }

    @Override
    public <U> Try<U> flatMap(final Function<? super T, ? extends Monad<U>> mapper) {
      return new Failure<>(cause);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable, R> R hurls(final Throwable t) throws T {
      throw (T) t;
    }
  }
}
