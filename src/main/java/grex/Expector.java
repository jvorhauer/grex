package grex;

import grex.control.Either;
import grex.control.Option;

import java.util.Collection;

@SuppressWarnings("UnusedReturnValue")
public abstract sealed class Expector<T, S extends Expector<T, S>> permits BooleanExpector, CharSeqExpector, CollectionExpector, EitherExpector, ObjectExpector, IntegerExpector, LongExpector, OptionExpector {
  protected final Class<T> clazz;
  protected final String className;
  protected final T actual;
  protected final boolean isNull;
  protected final boolean isNotNull;
  protected boolean inverted = false;   // applied .not()

  @SuppressWarnings("unchecked")
  public Expector(final T t) {
    final Option<T> ot = Option.of(t);
    this.isNull = ot.isEmpty();
    this.isNotNull = !isNull;
    this.clazz = (Class<T>) ot.map(u -> u.getClass()).getOrElse(null);
    this.className = ot.map(u -> u.getClass().getCanonicalName()).getOrElse(null);
    this.actual = t;
  }

  @SuppressWarnings("unchecked")
  protected final S self() {
    inverted = false;
    return (S) this;
  }

  @SuppressWarnings("unchecked")
  public S not() {
    inverted = true;
    return (S) this;
  }

  public S and() {
    return self();
  }

  public final S toBeNull() {
    if (inverted ? isNull : isNotNull) {
      throw new Disappointment("null", stringify(actual));
    }
    return self();
  }

  public final S toBeOf(final Class<?> clazz) {
    if (!inverted && !this.clazz.equals(clazz)) {
      throw new Disappointment(clazz.getCanonicalName(), className);
    }
    if ((inverted && this.clazz.equals(clazz))) {
      throw new Disappointment("not " + clazz.getCanonicalName(), className);
    }
    return self();
  }

  protected final String stringify(final Object value) {
    return Option.of(value).map(o -> switch (o) {
      case String str -> "\"" + str + "\"";
      case Long l -> l + "L";
      default -> "" + o;
    }).getOrElse("NULL");
  }

  protected S disappointment(final String expected) {
    return disappointment(stringify(expected), stringify(this.actual));
  }

  protected S disappointment(final String expected, final String other) {
    throw new Disappointment(expected, other);
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
}
