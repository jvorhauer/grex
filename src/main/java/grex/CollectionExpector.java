package grex;

import java.util.Collection;

public final class CollectionExpector extends Expector<Collection<?>, CollectionExpector> {

  private final int size;
  private final boolean isEmpty;

  public CollectionExpector(final Collection<?> collection) {
    super(collection);
    size = isNull ? 0 : collection.size();
    isEmpty = size == 0;
  }

  public CollectionExpector toHaveSize(final int expected) {
    return (inverted == (size == expected)) ?
      disappoint(className + " to " + (inverted ? "not " : "") + " have size " + expected, className + " with size " + expected) :
      self();
  }

  public CollectionExpector toBeEmpty() {
    return (inverted == isEmpty) ? disappoint(className + " to be empty", actual + ", size: " + size) : self();
  }
}
