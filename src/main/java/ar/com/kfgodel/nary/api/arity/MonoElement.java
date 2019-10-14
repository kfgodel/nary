package ar.com.kfgodel.nary.api.arity;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.exceptions.MoreThanOneElementException;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This type defines the protocol that {@link Nary} has to be used as an {@link Optional}.
 * Operations defined in this type makes sense when we know there's only 1 element on this instance.<br>
 * It's based on {@link Optional} to be as compatible and intuitive as possible but it departs
 * from its concepts adding own variants to simplify o complete use cases. Also makes explicit differences
 * when there are similar methods between {@link java.util.stream.Stream} and {@link Optional} but they have
 * different semantics.<br>
 * <br>
 * Date: 12/10/19 - 21:21
 *
 * @param <T> Type of contained element
 */
public interface MonoElement<T> {

  /**
   * If only one value is present in this {@code Nary}, returns the value,
   * otherwise throws {@code NoSuchElementException}, or {@code MoreThanOneElementException}.
   * This Nary as Stream is consumed to return the value so any attempt to use it as stream will fail after this method
   * call
   *
   * @return the non-null value held by this {@code Nary}
   * @throws java.util.NoSuchElementException if there is no value present
   * @throws MoreThanOneElementException      if there are more than one values
   * @see Optional#get()
   */
  T get() throws NoSuchElementException, MoreThanOneElementException;

  /**
   * Return {@code true} if there is only a value present, otherwise {@code false}.
   * This Nary as Stream is consumed.
   *
   * @return {@code true} if there is a value present, otherwise {@code false}
   * @throws MoreThanOneElementException if there are more than one
   * @see Optional#isPresent()
   */
  boolean isPresent() throws MoreThanOneElementException;

  /**
   * Negation of isPresent(). Facility method to check for missing element without having to negate
   *
   * @return true if there's no value to get
   * @throws MoreThanOneElementException If there's more than one value to get as Optional
   */
  default boolean isAbsent() throws MoreThanOneElementException {
    return !isPresent();
  }

  /**
   * If the only value is present, invoke the specified consumer with the value,
   * otherwise do nothing.
   * This Nary as Stream is consumed.
   *
   * @param consumer block to be executed if a value is present
   * @return this for easy method chaining
   * @throws NullPointerException        if value is present and {@code consumer} is
   *                                     null
   * @throws MoreThanOneElementException if there are more than one
   * @see Optional#ifPresent(Consumer)
   */
  Nary<T> ifPresent(Consumer<? super T> consumer) throws MoreThanOneElementException;

  /**
   * If the only value is absent invoke the given lambda, or else do nothing
   * This Nary as Stream is consumed.
   *
   * @param runnable The code to execute if this optional is empty
   * @return this for easy method chaining
   * @throws MoreThanOneElementException if there are more than one
   */
  Nary<T> ifAbsent(Runnable runnable) throws MoreThanOneElementException;

  /**
   * Return the only value if present, otherwise return {@code other}.
   * This Nary as Stream is consumed.
   *
   * @param other the value to be returned if there is no value present, may
   *              be null
   * @return the value, if present, otherwise {@code other}
   * @throws MoreThanOneElementException if there are more than one
   */
  T orElse(T other) throws MoreThanOneElementException;

  /**
   * Return the only value if present, otherwise invoke {@code other} and return
   * the result of that invocation.
   * This Nary as Stream is consumed.
   *
   * @param other a {@code Supplier} whose result is returned if no value
   *              is present
   * @return the value if present otherwise the result of {@code other.get()}
   * @throws MoreThanOneElementException if there are more than one
   */
  T orElseGet(Supplier<? extends T> other) throws MoreThanOneElementException;

  /**
   * Returns a Nary element that is populated with the given supplier only if this Nary is empty.<br>
   * This method allows defining a mapping function for nary that can be empty and keep chaining other
   * mapping definitions after that.<br>
   * This stream is consumed and it's assumed to have at most 1 element. It fails otherwise
   *
   * @param mapper The function that supplies a value if missing
   * @return A non empty nary with this instance element or the one given by the supplier
   * @throws MoreThanOneElementException If this instance has more than 1 element
   */
  Nary<T> orElseUse(Supplier<? extends T> mapper) throws MoreThanOneElementException;


  /**
   * Return the only contained value, if present, otherwise throw an exception created by the provided supplier.
   * This Nary as Stream is consumed.
   *
   * @param <X>               Type of the exception to be thrown
   * @param exceptionSupplier The supplier which will return the exception to
   *                          be thrown
   * @return the present value
   * @throws X                           if there is no value present
   * @throws NullPointerException        if no value is present and
   *                                     {@code exceptionSupplier} is null
   * @throws MoreThanOneElementException if there are more than one
   */
  <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X, MoreThanOneElementException;

  /**
   * Variant of {@link MonoElement#orElseThrow(Supplier)} that bounds type parameter X to be a {@link RuntimeException}
   * and avoid having to catch the exception if used inside a lambda block with bad inference.
   *
   * @param <X>               Type of the exception to be thrown
   * @param exceptionSupplier The supplier which will return the exception to
   *                          be thrown
   * @return the present value
   * @throws X                           if there is no value present
   * @throws NullPointerException        if no value is present and
   *                                     {@code exceptionSupplier} is null
   * @throws MoreThanOneElementException if there are more than one
   */
  <X extends RuntimeException> T orElseThrowRuntime(Supplier<? extends X> exceptionSupplier)
    throws X, MoreThanOneElementException;

  /**
   * Returns a native Optional instance to be used with Nary unaware code
   *
   * @return The native instance that represents this Nary content
   * @throws MoreThanOneElementException if there are more than one elements in this instance
   */
  Optional<T> asOptional() throws MoreThanOneElementException;

  /**
   * Map each element on this instance and filter null results out.<br>
   * If the result of mapping an element produces {@code null}, then it's skipped, reducing the
   * amount of elements contained in the returned Nary.<br>
   * <br>
   * This is semantically equivalent to {@link Optional#map(Function)} and
   * different from {@link java.util.stream.Stream#map(Function)} that takes null
   * as valid results
   *
   * @param <U>    The type of the result of the mapping function
   * @param mapper a mapping function to apply to the value, if present
   * @return a Nary with the mapped results excluding the ones that returned null
   */
  <U> Nary<U> mapFilteringNullResult(Function<? super T, ? extends U> mapper);

  /**
   * Makes a normal {@link java.util.stream.Stream#flatMap(Function)} transformation but accepts {@link Optional}
   * as result for the mapper instead of {@link java.util.stream.Stream}.<br>
   * This method is the semantic equivalent of {@link Optional#flatMap(Function)} but it can be applied to more
   * than one element.<br>
   * <br>
   *
   * @param <U>    The type parameter to the {@code Optional} returned by
   * @param mapper a mapping function to apply to the value, if present
   *               the mapping function
   * @return the result of applying an {@code Optional}-bearing mapping
   * function to the elements of this instance which may be 0, 1, or more elements
   */
  <U> Nary<U> flatMapOptional(Function<? super T, Optional<U>> mapper);
}
