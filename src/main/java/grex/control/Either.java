package grex.control;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public sealed interface Either<L, R> extends Serializable permits Either.Left, Either.Right {
  static <L, R>  Either<L, R> left(final L error) {
    return new Left<>(error);
  }

  static <L, R> Either<L, R> right(final R result) {
    return new Right<>(result);
  }

  @SuppressWarnings("unchecked")
  static <L, R> Either<L, R> narrow(final Either<? extends L, ? extends R> either) {
    return (Either<L, R>) either;
  }

  static <L, R> Either<L, R> attempt(final Supplier<? extends R> supplier, Function<Throwable, ? extends L> errorer) {
    try {
      return Either.right(supplier.get());
    } catch (final Throwable t) {
      return Either.left(errorer.apply(t));
    }
  }

  boolean isLeft();
  default boolean isRight() { return !isLeft(); }
  L getLeft();
  R getRight();
  default R get() {
    return getRight();
  }
  default R getOrElse(R fallback) {
    return isLeft() ? fallback : getRight();
  }
  void ifLeft(Consumer<? super L> consumer);
  void ifRight(Consumer<? super R> consumer);

  default void fold(final Consumer<? super R> rightConsumer, final Consumer<? super L> errorConsumer) {
    if (isLeft()) errorConsumer.accept(getLeft()); else rightConsumer.accept(getRight());
  }

  Option<?> toOption();

  record Left<L, R>(L value) implements Either<L, R> {

    @Override
    public boolean isLeft() {
      return true;
    }

    @Override
    public L getLeft() {
      return value;
    }

    @Override
    public R getRight() {
      throw new IllegalStateException("Cannot get Right from Left");
    }

    @Override
    public void ifLeft(final Consumer<? super L> consumer) {
      Objects.requireNonNull(consumer).accept(value);
    }

    @Override
    public void ifRight(final Consumer<? super R> consumer) {}

    public Option<L> toOption() {
      return Option.none();
    }
  }

  record Right<L, R>(R value) implements Either<L, R> {

    @Override
    public boolean isLeft() {
      return false;
    }

    @Override
    public L getLeft() {
      throw new IllegalStateException("Cannot get Left from Right");
    }

    @Override
    public R getRight() {
      return value;
    }

    @Override
    public void ifLeft(final Consumer<? super L> consumer) {}

    @Override
    public void ifRight(final Consumer<? super R> consumer) {
      Objects.requireNonNull(consumer).accept(value);
    }

    public Option<R> toOption() {
      return Option.of(value);
    }
  }
}
