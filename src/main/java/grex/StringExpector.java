package grex;

import grex.control.Option;

import java.util.function.Predicate;

public final class StringExpector extends CharSeqExpector<String, StringExpector> {

  private int size;
  private final boolean isBlank;

  private final Predicate<String> pCont = s -> s == null || !actual.contains(s);
  private final Predicate<String> pContNoCase = s -> s == null || !actual.toLowerCase().contains(s.toLowerCase());
  private final Predicate<Integer> pLonger = i -> i == null || size <= i;
  private final Predicate<Integer> pLongerOrEqualLength = i -> i == null || size < i;
  private final Predicate<Integer> pLen = i -> i == null || size != i;
  private final Predicate<String> pEq = s -> s == null || s.compareTo(actual) != 0;

  public StringExpector(final String s) {
    super(s);
    final Option<String> optact = Option.of(s);
    this.size = optact.map(String::length).getOrElse(0);
    this.isBlank = optact.map(String::isBlank).getOrElse(true);
  }

  @Override
  public StringExpector toBeEmpty() {
    return inverted == isEmpty ? disappoint(inverted ? "not empty" : "empty") : this;
  }

  public StringExpector toBeBlank() {
    return inverted == isBlank ? disappoint(inverted ? "not blank" : "blank") : self();
  }

  public StringExpector toHaveLength(final int length) {
    return pLen.test(length) ? disappoint("string with length " + length, stringify(size)) : self();
  }

  public StringExpector toHaveLengthGreaterThan(final int length) {
    return pLonger.test(length) ? disappoint("string with length greater than " + length, stringify(size)) : self();
  }

  public StringExpector toHaveLengthGreaterThanOrEqualTo(final int length) {
    return pLongerOrEqualLength.test(length) ? disappoint("string with length of " + length + " or greater", stringify(size)) : self();
  }

  public StringExpector toContain(final String sub) {
    return pCont.test(sub) ? disappoint("string that contains " + sub, sub + " not in " + actual) : self();
  }

  public StringExpector toContainIgnoreCase(final String sub) {
    return invert(pContNoCase).test(sub) ? disappoint("string that contains " + sub + " ignoring case", sub + " not in " + actual) : self();
  }

  public StringExpector toBe(final String s) {
    return invert(pEq).test(s) ? disappoint((inverted ? "not " : "") + s) : self();
  }
}
