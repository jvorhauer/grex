package grex;

import grex.control.Either;
import grex.control.Option;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract sealed class Expector<T, S extends Expector<T, S>> permits BooleanExpector, CharSeqExpector, CollectionExpector, EitherExpector, IntegerExpector, LongExpector, ObjectExpector, OptionExpector, SupplierExpector {
  private Class<T> clazz;
  protected String className;
  protected T actual;
  protected boolean isNull;
  protected boolean inverted = false;   // applied .not()
  private String description;

  private final Predicate<Class<?>> pEqCls = c -> !this.clazz.equals(c);

  @SuppressWarnings("unchecked")
  public Expector(final T t) {
    final Option<T> ot = Option.of(t);
    this.isNull = ot.isEmpty();
    this.clazz = (Class<T>) ot.map(u -> u.getClass()).getOrElse(null);
    this.className = ot.map(u -> u.getClass().getCanonicalName()).getOrElse("NULL");
    this.actual = t;
    this.description = className;
  }

  @SuppressWarnings("unchecked")
  protected final S self() {
    inverted = false;  // always reset after .not() and immediate .toBe...()
    return (S) this;
  }

  @SuppressWarnings("unchecked")
  public S not() {
    inverted = !inverted;    // next toBe...() will be inverted (negated), unless inverted was already set
    return (S) this;
  }

  public S and() {
    return self();
  }

  public S as(final String description) {
    this.description = description;
    return self();
  }

  public final S toBeNull() {
    return (inverted == isNull) ? disappoint((inverted ? "not ": "") + "null") : self();
  }

  public final S toBeOf(final Class<?> clazz) {
    return (inverted ? pEqCls.negate() : pEqCls).test(clazz) ? disappoint(clazz.getCanonicalName(), this.className) : self();
  }


  protected final String stringify(final Object value) {
    return Option.of(value).map(o -> switch (o) {
      case String str -> "\"" + str + "\"";
      case Long l -> l + "L";
      default -> "" + o;
    }).getOrElse("NULL");
  }

  protected final S disappoint(final String expected) {
    return disappoint(expected, stringify(actual));
  }

  protected final S disappoint(final String expected, final String actual) {
    System.out.printf("disappoint: description: %s, expected: %s, actual: %s%n", description, expected, actual);
    throw new Disappointment(description, expected, actual);
  }

  protected final Predicate<T> invert(final Predicate<T> pt) {
    return inverted ? pt.negate() : pt;
  }

  public static void fail(final String msg) {
    throw new AssertionError(msg);
  }

  public static void noop() {
    // do nothing
  }


  public static IntegerExpector expect(final Integer value) {
    return new IntegerExpector(value);
  }

  public static CharSeqExpector<?, ?> expect(final CharSequence value) {
    return new CharSeqExpector<>(value);
  }

  public static StringExpector expect(final String s) {
    return new StringExpector(s);
  }

  public static BooleanExpector expect(final Boolean b) {
    return new BooleanExpector(b);
  }

  public static <T> ObjectExpector<T> expect(final T t) {
    return new ObjectExpector<>(t);
  }

  public static CollectionExpector expect(final Collection<?> c) {
    return new CollectionExpector(c);
  }

  public static LongExpector expect(final Long l) {
    return new LongExpector(l);
  }

  public static EitherExpector expect(final Either<?, ?> e) {
    return new EitherExpector(e);
  }

  public static OptionExpector expect(final Option<?> o) {
    return new OptionExpector(o);
  }

  public static SupplierExpector expect(final Supplier<?> s) {
    return new SupplierExpector(s);
  }
}
