package grex.control;

import java.util.concurrent.Callable;
import java.util.function.Function;

public sealed interface Try<T> extends Mappable<T> {

  static <T> Try<T> of(final Callable<T> c) {
    try {
      return new Success<>(c.call());
    } catch (final Throwable t) {
      return new Failure<>(t);
    }
  }

  T get();
  boolean succeeded();
  default boolean failed() { return !succeeded(); }

  Throwable getCause();

  Either<? extends Throwable, T> toEither();
  Option<T> toOption();

  @Override
  <U> Try<U> map(final Function<? super T, ? extends U> mapper);

  @Override
  <U> Try<U> flatMap(final Function<? super T, ? extends Mappable<U>> mapper);


  record Success<T>(T value) implements Try<T> {

    @Override
    public T get() { return value; }

    public boolean succeeded() { return true; }

    public Throwable getCause() { throw new UnsupportedOperationException("getCause on Success"); }

    public Either<? extends Throwable, T> toEither() {
      return Either.right(value);
    }

    public Option<T> toOption() { return Option.of(value); }

    @Override
    public <U> Try<U> map(final Function<? super T, ? extends U> mapper) {
      try {
        return new Success<>(mapper.apply(value));
      } catch (final Throwable t) {
        return new Failure<>(t);
      }
    }

    @Override
    public <U> Try<U> flatMap(final Function<? super T, ? extends Mappable<U>> mapper) {
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

    public boolean succeeded() { return false; }

    @Override
    public Throwable getCause() { return cause; }

    public Either<? extends Throwable, T> toEither() {
      return Either.left(cause);
    }

    public Option<T> toOption() { return Option.none(); }

    @Override
    public <U> Try<U> map(final Function<? super T, ? extends U> mapper) {
      return new Failure<>(cause);
    }

    @Override
    public <U> Try<U> flatMap(final Function<? super T, ? extends Mappable<U>> mapper) {
      return new Failure<>(cause);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable, R> R hurls(final Throwable t) throws T {
      throw (T) t;
    }
  }
}
