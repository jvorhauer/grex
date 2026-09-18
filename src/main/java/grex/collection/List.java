package grex.collection;

import grex.control.Option;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class List<T>  implements Iterable<T> {
  private final T[] elements;
  private final int size;

  public List() {
    size = 0;
    elements = (T[]) new Object[0];
  }

  @SafeVarargs
  public List(final @NonNull T... ts) {
    size = ts.length;
    elements = Arrays.copyOf(ts, ts.length);
  }

  public static <T> List<T> of() {
    return new List<>();
  }

  @SafeVarargs
  public static <T> List<T> of(final @NonNull T... ts) {
    Objects.requireNonNull(ts);
    return new List<>(ts);
  }

  public static <T> List<T> of(final java.util.List<T> jl) {
    Objects.requireNonNull(jl);
    T[] result = (T[]) new Object[jl.size()];
    result = jl.toArray(result);
    return new List<>(result);
  }

  public List<T> add(final @NonNull List<T> l) {
    return add(l.elements);
  }

  @SuppressWarnings("ManualArrayCopy")    // bullshit, proposed alternative does NOT work (System.arraycopy)
  @SafeVarargs
  public final List<T> add(final @NonNull T... ts) {
    final T[] result = Arrays.copyOf(elements, size + ts.length);
    for (int i = 0; i < ts.length; i++) {
      result[size + i] = ts[i];
    }
    return new List<>(result);
  }

  public List<T> insert(final int where, final @NonNull T t) {
    if (where > size) {
      return List.of(this.elements).add(t);
    } else {
      if (where >= 0) {
        return slice(0, where).add(t).add(slice(where, size));
      } else {
        return List.of(t).add(this);
      }
    }
  }

  // swapped params, I always mix them up :-)
  public List<T> insert(final @NonNull T t, final int where) {
    return insert(where, t);
  }

  public List<T> prepend(final @NonNull T t) {
    return insert(-1, t);
  }

  public List<T> slice(final int start, final int end) {
    return start <= Math.min(end, size) ? List.of(Arrays.copyOfRange(this.elements, start, Math.min(end, size))) : this;
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public Option<T> get(final int index) {
    return index < 0 || index >= size ? Option.none() : Option.some(elements[index]);
  }

  public boolean has(final @NonNull T t) {
    for (int i = 0; i < size; i++) {
      if (elements[i].equals(t)) {
        return true;
      }
    }
    return false;
  }

  public boolean exists(final int index) {
    return get(index).isDefined();
  }

  public Option<T> head() {
    return get(0);
  }

  public List<T> tail() {
    return size < 2 ? List.of() : of(Arrays.copyOfRange(elements, 1, size));
  }

  @Override
  public void forEach(final Consumer<? super T> c) {
    for (int i = 0; i < size; i++) {
      c.accept(elements[i]);
    }
  }

  public Stream<T> stream() {
    return Arrays.stream(elements);
  }

  public List<T> distinct() {
    return new List<>((T[]) stream().distinct().toArray());
  }

  public List<T> filter(final @NonNull Predicate<T> pred) {
    int count = 0;
    for (int i = 0; i < size; i++) {
      if (pred.test(elements[i])) {
        count++;
      }
    }
    final T[] result = (T[]) new Object[count];
    for (int i = 0, j = 0; i < size; i++) {
      if (pred.test(elements[i])) {
        result[j++] = elements[i];
      }
    }
    return new List<>(result);
  }

  public <U> List<U> map(final @NonNull Function<? super T, ? extends U> mapper) {
    final U[] result = (U[]) new Object[size];
    for (int i = 0; i < size; i++) {
      result[i] = mapper.apply(elements[i]);
    }
    return new List<>(result);
  }

  public <U> U reduce(final @NonNull U accumulator, final @NonNull BiFunction<U, ? super T, U> reducer) {
    U result = accumulator;
    for (int i = 0; i < size; i++) {
      result = reducer.apply(result, elements[i]);
    }
    return result;
  }

  public List<T> sort() {
    final T[] result = Arrays.copyOf(elements, size);
    Arrays.sort(result);
    return new List<>(result);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("List[ ");
    if (size > 0) {
      sb.append(head().get());
      tail().forEach(obj -> sb.append(", ").append(obj));
    }
    sb.append(" ]");
    return sb.toString();
  }

  @Override
  public Iterator<T> iterator() {
    return new ListIterator();
  }

  private class ListIterator implements Iterator<T> {
    int cursor = 0;       // index of next element to return

    ListIterator() {}

    @Override
    public boolean hasNext() { return cursor < size; }

    @Override
    public T next() {
      if (cursor > size) {
        throw new IllegalStateException();
      }
      return elements[cursor++];
    }

    @Override
    public void remove() {
      // List is immutable, so niente remove.
      throw new UnsupportedOperationException();
    }
  }
}
