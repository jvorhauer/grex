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
    return (inverted == (this.size != expected)) ? self() : disappoint(className + " with size " + expected, className + " with size " + expected);
  }

  public CollectionExpector toBeEmpty() {
    return (inverted != isEmpty) ? self() : disappoint(className + " to be empty", actual + ", size: " + size);
  }
}
