package grex;

public sealed class CharSeqExpector<T extends CharSequence, S extends CharSeqExpector<T, S>> extends Expector<T, S> permits StringExpector {
  protected final boolean isEmpty;

  public CharSeqExpector(final T t) {
    super(t);
    this.isEmpty = t == null || t.isEmpty();
  }

  public grex.CharSeqExpector<?, ?> toBeEmpty() {
    return inverted == isEmpty ? disappointment("empty", stringify(actual)) : this;
  }
}
