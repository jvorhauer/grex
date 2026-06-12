package grex.control;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface Option<T> permits Option.None, Option.Some {

  static <T> Option<T> of(final T value) {
    return value == null ? none() : some(value);
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  static <T> Option<T> ofOptional(final Optional<T> other) {
    return other.isEmpty() ? none() : some(other.get());
  }

  static <T> Some<T> some(final T value) {
    return new Some<>(value);
  }

  @SuppressWarnings("unchecked")
  static <T> None<T> none() {
    return (None<T>) None.INSTANCE;
  }

  T get();

  boolean isEmpty();
  default boolean isDefined() { return !isEmpty(); }

  default <U> Option<U> map(final Function<? super T, ? extends U> mapper) {
    return isEmpty() ? none() : some(mapper.apply(get()));
  }

  default T getOrElse(final T other) {
    return isEmpty() ? other : get();
  }

  @SuppressWarnings("unchecked")
  default Option<T> orElse(final Option<? extends T> other) {
    return isEmpty() ? (Option<T>) other : this;
  }

  default void fold(final Consumer<? super T> action, final Runnable emptyAction) {
     if (isEmpty()) emptyAction.run(); else action.accept(get());
  }


  record Some<T>(T value) implements Option<T> {

    @Override
    public T get() {
      return value;
    }

    @Override
    public boolean isEmpty() {
      return false;
    }
  }


  record None<T>() implements Option<T> {
    private static final None<?> INSTANCE = new None<>();

    @Override
    public T get() {
      throw new NoSuchElementException("None does not have a value to get");
    }

    @Override
    public boolean isEmpty() {
      return true;
    }
  }
}
