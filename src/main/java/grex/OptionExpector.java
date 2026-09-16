package grex;

import grex.control.Option;

public final class OptionExpector extends Expector<Option<?>, OptionExpector> {
  public OptionExpector(final Option<?> option) {
    super(option);
  }

  public OptionExpector toBeEmpty() {
    if (inverted && actual.isEmpty()) {
      disappoint("not empty", "empty");
    }
    if (!inverted && actual.isDefined()) {
      disappoint("empty", "defined (" + actual.get() + ")");
    }
    return self();
  }

  public OptionExpector toBeDefined() {
    if (!inverted && actual.isEmpty()) {
      disappoint("defined", "empty");
    }
    if (inverted && actual.isDefined()) {
      disappoint("empty", "defined (" + actual.get() + ")");
    }
    return self();
  }

  public OptionExpector toContain(final Object expected) {
    if (this.actual.isEmpty() || !this.actual.get().equals(expected)) {
      disappoint("to contain " + expected);
    }
    return self();
  }
}
