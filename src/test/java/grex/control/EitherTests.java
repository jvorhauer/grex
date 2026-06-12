package grex.control;

import grex.control.Either.Left;
import grex.control.Either.Right;
import org.junit.jupiter.api.Test;

import static grex.Expector.expect;
import static grex.Expector.fail;


final class EitherTests {
  @Test
  void left() {
    Either<String, String> left = Either.left("Hello");
    expect(left).not().toBeNull().and().toBeLeft();
    expect(left.isLeft()).toBeTrue();
    expect(left.isRight()).toBeFalse();
    expect(left.getLeft()).not().toBeNull().not().toBeBlank().toBe("Hello");
    expect(left.getOrElse("World")).not().toBeNull().not().toBeBlank().toBe("World");

    left = new Left<>("World");
    expect(left).not().toBeNull().and().toBeLeft();
    expect(left.isLeft()).toBeTrue();
    expect(left.isRight()).toBeFalse();
    expect(left.getLeft()).not().toBeNull().not().toBeBlank().toBe("World");
    expect(left.getOrElse("Hello")).not().toBeNull().not().toBeBlank().toBe("Hello");

    left.ifRight(value -> fail("Left should not be right (" + value + ")" ));
    left.ifLeft(value -> expect(value).toBe("World"));

    expect(left.toOption().isEmpty()).toBeTrue();
  }

  @Test
  void right() {
    Either<String, String> right = Either.right("Hello");
    expect(right).not().toBeNull().toBeRight();
    expect(right.isLeft()).toBeFalse();
    expect(right.isRight()).toBeTrue();
    expect(right.getRight()).not().toBeNull().not().toBeBlank().toBe("Hello");
    expect(right.getOrElse("World")).not().toBeNull().not().toBeBlank().toBe("Hello");

    right = new Right<>("World");
    expect(right).not().toBeNull().toBeRight();
    expect(right.isLeft()).toBeFalse();
    expect(right.isRight()).toBeTrue();
    expect(right.getRight()).not().toBeNull().not().toBeBlank().toBe("World");
    expect(right.get()).toBe(right.getRight());
    expect(right.getOrElse("Hello")).not().toBeNull().not().toBeBlank().toBe("World");

    right.ifLeft(value -> fail("Right should not be left (" + value + ")" ));
    right.ifRight(value -> expect(value).toBe("World"));

    expect(right.toOption().isEmpty()).toBeFalse();
  }

  @Test
  void narrow() {
    Either<String, String> either = Either.right("Test");
    Either<CharSequence, CharSequence> narrowed = Either.narrow(either);
    expect(narrowed).not().toBeNull();
    // NOT WORKING AS EXPECTED: expect(narrowed.get()).toBeOf(CharSequence.class);

    either = Either.left("Hello");
    narrowed = Either.narrow(either);
    expect(narrowed.getLeft()).not().toBeNull().and().not().toBeEmpty();
  }

  @SuppressWarnings({"NumericOverflow", "divzero"})
  @Test
  void attempt() {
    Either<String, Integer> attempted =  Either.attempt(() -> 1 / 0, e -> "Failed: " + e.getMessage());
    expect(attempted).not().toBeNull().toBeLeft();
  }

  @Test
  void fold() {
    Either<String, String> right = Either.right("Hello");
    right.fold(
            r -> expect(r).toBe("Hello"),
            l -> fail("Left should not be left (" + l + ")")
    );

    Either<String, String> left = Either.left("World");
    left.fold(
            r -> fail("should not be right (" + r + ")"),
            l -> expect(l).toBe("World")
    );
  }
}
