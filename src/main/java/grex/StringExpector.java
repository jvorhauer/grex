package grex;

import grex.control.Option;

import java.util.function.Predicate;

public final class StringExpector extends CharSeqExpector<String, StringExpector> {

  private int size;
  private final boolean isBlank;

  private final Predicate<String> contains = str -> isNotNull && actual.contains(str);
  private final Predicate<String> containsIgnoringCase = str -> isNotNull && actual.toLowerCase().contains(str.toLowerCase());
  private final Predicate<Integer> longerThen = i -> this.size > i;
  private final Predicate<Integer> longerOrEqualInLength = i -> this.size >= i;
  private final Predicate<Integer> hasLength = i -> this.size == i;

  public StringExpector(final String s) {
    super(s);
    final Option<String> optact = Option.of(s);
    this.size = optact.map(String::length).getOrElse(0);
    this.isBlank = optact.map(String::isBlank).getOrElse(true);
  }

  @Override
  public grex.StringExpector toBeEmpty() {
    return inverted == isEmpty ? disappointment("empty", actual) : this;
  }

  public grex.StringExpector toBeBlank() {
    return inverted == isBlank ? disappointment("blank", actual) : this;
  }

  public grex.StringExpector toHaveLength(final int length) {
    return hasLength.test(length) ? this : disappointment("string with length " + length, stringify(size));
  }

  public grex.StringExpector toHaveLengthGreaterThan(final int length) {
    return longerThen.test(length) ? this : disappointment("string with length greater than " + length, stringify(size));
  }

  public grex.StringExpector toHaveLengthGreaterThanOrEqualTo(final int length) {
    return longerOrEqualInLength.test(length) ? this : disappointment("string with length of " + length + " or greater", stringify(size));
  }

  public grex.StringExpector toContain(final String sub) {
    return !contains.test(sub) ? disappointment("string that contains " + sub, sub + " not in " + actual) : this;
  }

  public grex.StringExpector toContainIgnoreCase(final String sub) {
    return !containsIgnoringCase.test(sub) ? disappointment("string that contains " + sub + " ignoring case", sub + " not in " + actual) : this;
  }

  public grex.StringExpector toBe(final String sub) {
    return isNotNull && actual.contentEquals(sub) ? this : disappointment(actual + " to equal " + sub, actual + " is not equal to " + sub);
  }
}
