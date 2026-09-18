package grex.collection;

public record Pair<K, V>(K key, V value) {

  public static <K, V> Pair<K, V> of(final K key, final V value) {
    return new Pair<>(key, value);
  }

  @Override
  public String toString() {
    return "(" + key + " -> " + value + ")";
  }
}
