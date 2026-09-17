package grex;

public interface Value<T> {
  T get();
  boolean isEmpty();
  default boolean isDefined() { return !isEmpty(); }
  default boolean isLazy() { return false; }
}
