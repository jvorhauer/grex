package grex.control;

import grex.Value;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public final class Lazy<T> implements Supplier<T>, Value<T>, Mappable<T> {
  private final ReentrantLock lock = new ReentrantLock();
  private transient volatile Supplier<? extends T> supplier;

  private volatile T value;

  private Lazy(final Supplier<? extends T> supplier) {
    this.supplier = supplier;
  }

  public static <T> Lazy<T> of(final @NonNull Supplier<? extends T> supplier) {
    Objects.requireNonNull(supplier, "supplier is null");
    if (supplier instanceof Lazy) {
      return (Lazy<T>) supplier;
    } else {
      return new Lazy<>(supplier);
    }
  }

  public T get() {
    return (supplier == null) ? value : compute();
  }

  private T compute() {
    lock.lock();
    try {
      final Supplier<? extends T> s = this.supplier;
      if (s != null) {
        this.value = s.get();
        this.supplier = null;
      }
    } finally {
      lock.unlock();
    }
    return this.value;
  }

  @Override
  public boolean isEmpty() { return false; }
  public boolean isEvaluated() { return supplier == null; }
  @Override
  public boolean isLazy() { return true; }

  @Override
  public <U> Lazy<U> map(final @NonNull Function<? super T, ? extends U> mapper) {
    Objects.requireNonNull(mapper, "mapper is null");
    return Lazy.of(() -> mapper.apply(get()));
  }

  @Override
  public <U> Lazy<U> flatMap(final @NonNull Function<? super T, ? extends Mappable<? extends U>> mapper) {
    Objects.requireNonNull(mapper, "mapper is null");
    return (Lazy<U>) mapper.apply(get());
  }
}
