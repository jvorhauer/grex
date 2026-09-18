package grex;

import grex.collection.Map;

public final class MapExpector extends Expector<Map<?, ?>, MapExpector> {
  private final int size;
  private final boolean isEmpty;

  public MapExpector(final Map<?, ?> map) {
    size = map.size();
    isEmpty = map.isEmpty();
    super(map);
  }

  public MapExpector toHaveSize(final int expected) {
    return (inverted == (size == expected)) ?
      disappoint("Map to " + (inverted ? "not " : "") + " have size " + expected) :
      self();
  }

  public MapExpector toBeEmpty() {
    return (inverted == isEmpty) ?
      disappoint("Map to " + (inverted ? "not " : "") + " be empty") :
      self();
  }

  @SuppressWarnings("unchecked")
  public MapExpector toHave(final Object expected) {
    return (inverted == ((Map<Object, ?>) actual).has(expected)) ?
      disappoint("Map to " + (inverted ? "not ": "") + "contain " + expected) :
      self();
  }
}
