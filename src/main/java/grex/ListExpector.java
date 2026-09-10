package grex;

import grex.collection.List;

public final class ListExpector extends Expector<List<?>, ListExpector> {
  private final int size;
  private final boolean isEmpty;

  public ListExpector(final List<?> list) {
    super(list);
    size = list.size();
    isEmpty = list.isEmpty();
  }

  public ListExpector toHaveSize(final int expected) {
    return (inverted == (size == expected)) ?
      disappoint("List to " + (inverted ? "not " : "") + "have size " + expected, "List with size " + size) :
      self();
  }

  public ListExpector toBeEmpty() {
    return (inverted == isEmpty) ? disappoint("List to be empty", actual + ", size: " + size) : self();
  }
}
