package grex;

import grex.control.Either;
import grex.control.Option;

import java.util.Collection;
import java.util.function.Predicate;

@SuppressWarnings("UnusedReturnValue")
public abstract sealed class Expector<T, S extends Expector<T, S>> permits BooleanExpector, Expector.CharSeqExpector, Expector.CollectionExpector, Expector.EitherExpector, Expector.ObjectExpector, IntegerExpector, LongExpector {
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
    this.className = ot.map(u -> u.getClass().getSimpleName()).getOrElse(null);
    this.actual = t;
  }

  @SuppressWarnings("unchecked")
  protected final S self() {
    return (S) this;
  }

  public S not() {
    inverted = true;
    return self();
  }

  public S and() {
    return self();
  }

  public final S toBeNull() {
    if (inverted ? isNull : isNotNull) {
      throw new Disappointment("null", stringify(actual));
    }
    inverted = false;
    return self();
  }

  public final S toBeOf(final Class<?> clazz) {
    if (!this.clazz.equals(clazz)) {
      throw new Disappointment(clazz.getCanonicalName(), className);
    }
    inverted = false;
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

  public static final class ObjectExpector<T> extends Expector<T, ObjectExpector<T>> {
    public ObjectExpector(final T t) {
      super(t);
    }
  }

  public static final class CollectionExpector extends Expector<Collection<?>, CollectionExpector> {

    private final int size;
    private final boolean isEmpty;

    public CollectionExpector(final Collection<?> collection) {
      super(collection);
      size = isNull ? 0 : collection.size();
      isEmpty = size == 0;
    }

    public CollectionExpector toHaveSize(final int expected) {
      return (inverted == (this.size != expected)) ? this : disappointment(className + " with size " + expected, className + " with size " + expected);
    }

    public CollectionExpector toBeEmpty() {
      return (inverted != isEmpty) ? this : disappointment(className + " to be empty", actual + ", size: " + size);
    }
  }

  public static sealed class CharSeqExpector<T extends CharSequence, S extends CharSeqExpector<T, S>> extends Expector<T, S> {
    protected final boolean isEmpty;

    public CharSeqExpector(final T t) {
      super(t);
      this.isEmpty = t == null || t.isEmpty();
    }

    public CharSeqExpector<?, ?> toBeEmpty() {
      return inverted == isEmpty ? disappointment("empty", stringify(actual)) : this;
    }
  }

  public static final class StringExpector extends CharSeqExpector<String, StringExpector> {

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
    public StringExpector toBeEmpty() {
      return inverted == isEmpty ? disappointment("empty", actual) : this;
    }

    public StringExpector toBeBlank() {
      return inverted == isBlank ? disappointment("blank", actual) : this;
    }

    public StringExpector toHaveLength(final int length) {
      return hasLength.test(length) ? this : disappointment("string with length " + length, stringify(size));
    }

    public StringExpector toHaveLengthGreaterThan(final int length) {
      return longerThen.test(length) ? this : disappointment("string with length greater than " + length, stringify(size));
    }

    public StringExpector toHaveLengthGreaterThanOrEqualTo(final int length) {
      return longerOrEqualInLength.test(length) ? this : disappointment("string with length of " + length + " or greater", stringify(size));
    }

    public StringExpector toContain(final String sub) {
      return !contains.test(sub) ? disappointment("string that contains " + sub, sub + " not in " + actual) : this;
    }

    public StringExpector toContainIgnoreCase(final String sub) {
      return !containsIgnoringCase.test(sub) ? disappointment("string that contains " + sub + " ignoring case", sub + " not in " + actual) : this;
    }

    public StringExpector toBe(final String sub) {
      return isNotNull && actual.contentEquals(sub) ? this : disappointment(actual + " to equal " + sub, actual + " is not equal to " + sub);
    }
  }

  public static final class EitherExpector extends Expector<Either<?, ?>, EitherExpector> {

    public EitherExpector(final Either<?, ?> either) {
      super(either);
    }

    public EitherExpector toBeLeft() {
      return isNotNull && actual.isLeft() ? this : disappointment("Expected Left");
    }

    public EitherExpector toBeRight() {
      return isNotNull && actual.isRight() ? this : disappointment("Expected Right");
    }
  }
}
