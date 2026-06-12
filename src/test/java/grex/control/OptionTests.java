package grex.control;

import org.junit.jupiter.api.Test;

import static grex.Expector.expect;
import static grex.Expector.fail;
import static grex.Expector.noop;

@SuppressWarnings("Convert2MethodRef")
public class OptionTests {

  @Test
  void some() {
    Option.Some<Integer> some = Option.some(1);
    expect(some).not().toBeNull();
    expect(some.isEmpty()).toBeFalse();
    expect(some.get()).toBe(1);
  }

  @Test
  void none() {
    Option.None<Object> none = Option.none();
    expect(none).not().toBeNull();
    expect(none.isEmpty()).toBeTrue();

    Option<Object> nof = Option.of(null);
    expect(nof.isEmpty()).toBeTrue();
  }

  @Test
  void getOrElse() {
    Option.Some<Integer> some = Option.some(1);
    expect(some).not().toBeNull();
    expect(some.getOrElse(2)).toBe(1);

    Option.None<Integer> none = Option.none();
    expect(none).not().toBeNull();
    expect(none.getOrElse(2)).toBe(2);
  }

  @Test
  void orElse() {
    Option.Some<Integer> some = Option.some(1);
    expect(some).not().toBeNull();
    expect(some.orElse(Option.some(2)).get()).toBe(1);

    Option.None<Integer> none = Option.none();
    expect(none).not().toBeNull();
    expect(none.orElse(Option.some(2)).get()).toBe(2);
  }

  @Test
  void isEmptyAndIsDefined() {
    Option.Some<Integer> some = Option.some(1);
    expect(some).not().toBeNull();
    expect(some.isEmpty()).toBeFalse();
    expect(some.isDefined()).toBeTrue();
  }

  @Test
  void fold() {
    Option.Some<Integer> some = Option.some(1);
    expect(some).not().toBeNull();
    some.fold(
      val -> expect(val).toBe(1),
      () -> fail("not the expected value")
    );

    Option.None<Integer> none = Option.none();
    expect(none).not().toBeNull();
    none.fold(
      _ignore -> fail("none should not accept this"),
      () -> noop()
    );
  }
}
