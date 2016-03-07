package ar.com.kfgodel.nary.api.optionals;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This type represents the interface definition of an optional.<br>
 * Copied from concrete class java.util.Optional, and modified to be compatible with Stream<br>
 * It conserves the same semantics but it's slightly modified to be extended by a stream compatible type
 * <p/>

 * Created by kfgodel on 07/03/16.
 */
public interface InterfacedOptional<T> {
  /**
   * If a value is present in this {@code Optional}, returns the value,
   * otherwise throws {@code NoSuchElementException}.
   *
   * @return the non-null value held by this {@code Optional}
   * @throws java.util.NoSuchElementException if there is no value present
   * @see Optional#isPresent()
   */
  T get() throws NoSuchElementException;

  /**
   * Return {@code true} if there is a value present, otherwise {@code false}.
   *
   * @return {@code true} if there is a value present, otherwise {@code false}
   */
  boolean isPresent();

  /**
   * If a value is present, invoke the specified consumer with the value,
   * otherwise do nothing.
   *
   * @param consumer block to be executed if a value is present
   * @throws NullPointerException if value is present and {@code consumer} is
   *                              null
   */
  void ifPresent(Consumer<? super T> consumer);

  /**
   * If a value is present, and the value matches the given predicate,
   * return an {@code Optional} describing the value, otherwise return an
   * empty {@code Optional}.
   *
   * @param predicate a predicate to apply to the value, if present
   * @return an {@code Optional} describing the value of this {@code Optional}
   * if a value is present and the value matches the given predicate,
   * otherwise an empty {@code Optional}
   * @throws NullPointerException if the predicate is null
   */
  Optional<T> filterOptional(Predicate<? super T> predicate);

  /**
   * If a value is present, apply the provided mapping function to it,
   * and if the result is non-null, return an {@code Optional} describing the
   * result.  Otherwise return an empty {@code Optional}.
   *
   * @param <U>    The type of the result of the mapping function
   * @param mapper a mapping function to apply to the value, if present
   * @return an {@code Optional} describing the result of applying a mapping
   * function to the value of this {@code Optional}, if a value is present,
   * otherwise an empty {@code Optional}
   * @throws NullPointerException if the mapping function is null
   * @apiNote This method supports post-processing on optional values, without
   * the need to explicitly check for a return status.  For example, the
   * following code traverses a stream of file names, selects one that has
   * not yet been processed, and then opens that file, returning an
   * {@code Optional<FileInputStream>}:
   * <p/>
   * <pre>{@code
   *     Optional<FileInputStream> fis =
   *         names.stream().filter(name -> !isProcessedYet(name))
   *                       .findFirst()
   *                       .mapOptional(name -> new FileInputStream(name));
   * }</pre>
   * <p/>
   * Here, {@code findFirst} returns an {@code Optional<String>}, and then
   * {@code mapOptional} returns an {@code Optional<FileInputStream>} for the desired
   * file if one exists.
   */
  <U> Optional<U> mapOptional(Function<? super T, ? extends U> mapper);

  /**
   * If a value is present, apply the provided {@code Optional}-bearing
   * mapping function to it, return that result, otherwise return an empty
   * {@code Optional}.  This method is similar to {@link #mapOptional(Function)},
   * but the provided mapper is one whose result is already an {@code Optional},
   * and if invoked, {@code flatMapOptional} does not wrap it with an additional
   * {@code Optional}.
   *
   * @param <U>    The type parameter to the {@code Optional} returned by
   * @param mapper a mapping function to apply to the value, if present
   *               the mapping function
   * @return the result of applying an {@code Optional}-bearing mapping
   * function to the value of this {@code Optional}, if a value is present,
   * otherwise an empty {@code Optional}
   * @throws NullPointerException if the mapping function is null or returns
   *                              a null result
   */
  <U> Optional<U> flatMapOptional(Function<? super T, java.util.Optional<U>> mapper);

  /**
   * Performs an action for on the element of this Optional, if present.
   *
   * @param action an action to perform on the element
   */
  void forEach(Consumer<? super T> action);

  /**
   * Return the value if present, otherwise return {@code other}.
   *
   * @param other the value to be returned if there is no value present, may
   *              be null
   * @return the value, if present, otherwise {@code other}
   */
  T orElse(T other);

  /**
   * Return the value if present, otherwise invoke {@code other} and return
   * the result of that invocation.
   *
   * @param other a {@code Supplier} whose result is returned if no value
   *              is present
   * @return the value if present otherwise the result of {@code other.get()}
   * @throws NullPointerException if value is not present and {@code other} is
   *                              null
   */
  T orElseGet(Supplier<? extends T> other);

  /**
   * Return the contained value, if present, otherwise throw an exception
   * to be created by the provided supplier.
   *
   * @param <X>               Type of the exception to be thrown
   * @param exceptionSupplier The supplier which will return the exception to
   *                          be thrown
   * @return the present value
   * @throws X                    if there is no value present
   * @throws NullPointerException if no value is present and
   *                              {@code exceptionSupplier} is null
   * @apiNote A method reference to the exception constructor with an empty
   * argument list can be used as the supplier. For example,
   * {@code IllegalStateException::new}
   */
  <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;
}
