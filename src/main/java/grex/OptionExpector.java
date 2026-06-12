package grex;

import grex.control.Option;

public final class OptionExpector extends Expector<Option<?>, OptionExpector> {
  public OptionExpector(final Option<?> option) {
    super(option);
  }

  public OptionExpector toBeEmpty() {
    if (!inverted && actual.isDefined()) {
      disappointment("empty", "defined (" + actual.get() + ")");
    }
    if (inverted && actual.isEmpty()) {
      disappointment("not empty", "empty");
    }
    inverted = false;
    return self();
  }

  public OptionExpector toBeDefined() {
    if (!inverted && actual.isEmpty()) {
      disappointment("defined", "empty");
    }
    if (inverted && actual.isDefined()) {
      disappointment("empty", "defined (" + actual.get() + ")");
    }
    inverted = false;
    return self();

  }
}
