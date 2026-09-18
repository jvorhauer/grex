package grex.collection;

import grex.control.Option;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public final class Map<K, V> implements Iterable<Pair<K, V>> {
  private final Pair<K, V>[] pairs;
  private final int size;

  public Map() {
    size = 0;
    pairs = (Pair<K, V>[]) new Pair[0];
  }

  @SafeVarargs
  public Map(final @NonNull Pair<K, V>... pairs) {
    Objects.requireNonNull(pairs);
    this.size = pairs.length;
    this.pairs = Arrays.copyOf(pairs, pairs.length);
  }

  public static <K, V> Map<K, V> of() {
    return new Map<>();
  }

  @SafeVarargs
  public static <K, V> Map<K, V> of(final @NonNull Pair<K, V>... pairs) {
    Objects.requireNonNull(pairs);
    return new Map<>(pairs);
  }

  public static <K, V> Map<K, V> of(final java.util.Map<K, V> jm) {
    Objects.requireNonNull(jm);
    final Pair<K, V>[] result = (Pair<K, V>[]) new Pair[jm.size()];
    int i = 0;
    for (final java.util.Map.Entry<K, V> entry : jm.entrySet()) {
      result[i++] = Pair.of(entry.getKey(), entry.getValue());
    }
    return new Map<>(result);
  }

  public Map<K, V> put(final @NonNull K key, final @NonNull V value) {
    final Pair<K, V> newPair = Pair.of(key, value);
    return put(newPair);
  }

  public Map<K, V> put(final @NonNull Pair<K, V> pair) {
    final Pair<K, V>[] result = Arrays.copyOf(pairs, size + 1);
    result[size] = pair;
    return new Map<>(result);
  }

  public Map<K, V> remove(final @NonNull K key) {
    int count = 0;
    for (int i = 0; i < size; i++) {
      if (!pairs[i].key().equals(key)) {
        count++;
      }
    }
    final Pair<K, V>[] result = (Pair<K, V>[]) new Pair[count];
    for (int i = 0, j = 0; i < size; i++) {
      if (!pairs[i].key().equals(key)) {
        result[j++] = pairs[i];
      }
    }
    return new Map<>(result);
  }

  public Option<V> get(final @NonNull K key) {
    for (int i = 0; i < size; i++) {
      if (pairs[i].key().equals(key)) {
        return Option.some(pairs[i].value());
      }
    }
    return Option.none();
  }

  public boolean has(final @NonNull K key) {
    return get(key).isDefined();
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public List<K> keys() {
    final K[] result = (K[]) new Object[size];
    for (int i = 0; i < size; i++) {
      result[i] = pairs[i].key();
    }
    return new List<>(result);
  }

  public List<V> values() {
    final V[] result = (V[]) new Object[size];
    for (int i = 0; i < size; i++) {
      result[i] = pairs[i].value();
    }
    return new List<>(result);
  }

  public <U> Map<K, U> map(final @NonNull Function<? super V, ? extends U> mapper) {
    final Pair<K, U>[] result = (Pair<K, U>[]) new Pair[size];
    for (int i = 0; i < size; i++) {
      result[i] = Pair.of(pairs[i].key(), mapper.apply(pairs[i].value()));
    }
    return new Map<>(result);
  }

  @Override
  public Iterator<Pair<K, V>> iterator() {
    return new MapIterator();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Map[");
    if (size > 0) {
      sb.append(" ").append(pairs[0]);
      for (int i = 1; i < size; i++) {
        sb.append(", ").append(pairs[i]);
      }
    }
    return sb.append(" ]").toString();
  }

  private class MapIterator implements Iterator<Pair<K, V>> {
    int cursor = 0;

    @Override
    public boolean hasNext() {
      return cursor < size;
    }

    @Override
    public Pair<K, V> next() {
      if (cursor >= size) {
        throw new IllegalStateException();
      }
      return pairs[cursor++];
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
