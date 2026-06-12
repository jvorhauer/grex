package grex;

import grex.control.Either;

public final class EitherExpector extends Expector<Either<?, ?>, EitherExpector> {

  public EitherExpector(final Either<?, ?> either) {
    super(either);
  }

  public grex.EitherExpector toBeLeft() {
    return isNotNull && actual.isLeft() ? this : disappointment("Expected Left");
  }

  public grex.EitherExpector toBeRight() {
    return isNotNull && actual.isRight() ? this : disappointment("Expected Right");
  }
}
