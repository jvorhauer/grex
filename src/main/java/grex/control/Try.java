package grex.control;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Callable;

public sealed interface Try<T> permits Try.Success, Try.Failure {

  static <T> Try<T> of(Callable<T> c) {
    Objects.requireNonNull(c, "The callable is null");
    try {
      return new Success<>(c.call());
    } catch (final Throwable t) {
      return new Failure<>(t);
    }
  }

  T get();
  boolean isSuccess();
  default boolean isFailure() { return !isSuccess(); }
  default boolean isEmpty() { return isSuccess(); }

  Throwable getCause();

  Either<? extends Throwable, T> toEither();


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

    @Override
    public int hashCode() { return Objects.hashCode(value); }

    @Override
    public String toString() { return "Some(" + value + ")"; }
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

    public boolean equals(final Object o) {
      return (this == o) || (o instanceof Try.Failure<?>(Throwable oc) && Arrays.deepEquals(cause.getStackTrace(), (oc.getStackTrace())));
    }

    @Override
    public int hashCode() { return Arrays.hashCode(cause.getStackTrace()); }

    @Override
    public String toString() { return "Failure(" + cause.getMessage() + ")"; }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable, R> R hurls(Throwable t) throws T {
      throw (T) t;
    }
  }
}
