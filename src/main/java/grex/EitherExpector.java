package grex;

import grex.control.Either;

public final class EitherExpector extends Expector<Either<?, ?>, EitherExpector> {

  private final boolean isLeft;
  private final boolean isRight;

  public EitherExpector(final Either<?, ?> either) {
    super(either);
    isLeft = either != null && either.isLeft();
    isRight = either != null && either.isRight();
  }

  public grex.EitherExpector toBeLeft() {
    if (inverted) {
      if (isLeft) {
        disappoint("Left", "Right (" + actual.get() + ")");
      }
    } else {
      if (isRight) {
        disappoint("Right", "Left");
      }
    }
    return self();
  }

  public grex.EitherExpector toBeRight() {
    if (inverted) {
      if (isRight) {
        disappoint("Left", "Right (" + actual.get() + ")");
      }
    } else {
      if (isLeft) {
        disappoint("Right", "Left");
      }
    }
    return self();
  }
}
