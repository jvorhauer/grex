package grex.control;

import java.util.function.Function;

/**
 * A monad is a type that supports map and flatMap operations.
 * This interface unifies the monadic behavior of Try and Either.
 *
 * @param <T> the type of the value wrapped by this monad
 */
public interface Mappable<T> {

  /**
   * Transforms the value inside this monad using the given mapper function.
   *
   * @param mapper the function to apply to the wrapped value
   * @param <U> the result type of the mapper function
   * @return a new monad containing the result of applying the mapper
   */
  <U> Mappable<U> map(Function<? super T, ? extends U> mapper);

  /**
   * Transforms the value inside this monad using the given mapper function,
   * which returns a monad. This is also known as bind or chain.
   *
   * @param mapper the function to apply to the wrapped value, returning a monad
   * @param <U> the type wrapped by the resulting monad
   * @return the monad returned by the mapper function
   */
  <U> Mappable<U> flatMap(Function<? super T, ? extends Mappable<U>> mapper);
}
