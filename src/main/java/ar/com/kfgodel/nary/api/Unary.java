package ar.com.kfgodel.nary.api;

import ar.com.kfgodel.nary.api.arity.MonoElement;

/**
 * This interface extends the operations of a {@link Nary} when it only contains 1 or 0 elements.<br>
 * It can be seen as an extended {@link java.util.Optional} api including {@link java.util.stream.Stream} operations.<br>
 * <br>
 * This interface allows using the concept of {@link java.util.Optional} but integrating it better with
 * {@link java.util.stream.Stream}s, and allows combining them without having to do intermediate conversions
 * (in contrast to native {@link java.util.stream.Stream} and {@link java.util.Optional} types).
 * Date: 4/1/20 - 12:14
 */
public interface Unary<T> extends Nary<T>, MonoElement<T> {

  /**
   * Creates a unary from an element whose absence is represented by null.<br>
   * If null is passed then an empty {@link Unary} is returned.<br>
   * This method is an alias for {@link Nary#of(Object)}
   *
   * @param nullableElement An value that may be null
   * @param <T>             The expected element type
   * @return A unary with the given element or empty if null was passed
   */
  static <T> Unary<T> of(T nullableElement) {
    return Nary.of(nullableElement);
  }
}
