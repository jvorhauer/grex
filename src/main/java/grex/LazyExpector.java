package grex;

import grex.control.Lazy;

public final class LazyExpector extends Expector<Lazy<?>, LazyExpector> {

  private final boolean evaluated;
  private final boolean defined;

  public LazyExpector(final Lazy<?> lazy) {
    super(lazy);
    evaluated = lazy.isEvaluated();
    defined = lazy.isDefined();
  }

  public LazyExpector toBeEvaluated() {
    return (inverted == evaluated) ?
      disappoint("to " + (inverted ? "not " : "") + "have been evaluated") :
      self();
  }

  public LazyExpector toBeDefined() {
    return inverted == defined ?
      disappoint("to " + (inverted ? "not " : "") + "be defined") :
      self();
  }
}
