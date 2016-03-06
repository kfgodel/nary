package ar.com.kfgodel.nary.api;

import ar.com.kfgodel.nary.api.exceptions.MoreThanOneElementException;
import ar.com.kfgodel.nary.impl.NaryFromNative;
import ar.com.kfgodel.optionals.Optional;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * This type represents an uncertain set of elements, without any assumptions about order, uniqueness, or quantity.<br>
 * A nary, as a stream, cannot be modified, but it can be consumed. It's the least structure-coupled container,
 * allowing it to be used as return type when there's no guarantee about collection types, element sources,
 * element quantities, element presence, etc. <br>
 * <br>
 * Joint of a stream and an optional, offers an interface that is the mix of both (with minor modifications to Optional).<br>
 * It represents an object that can contain 0, 1, or N elements, and can be accessed assuming one of the scenarios.<br>
 * This allows a method to be used as returning an optional, or a stream based on the arguments (without having to change
 * the return type). Specially useful for query methods.<br>
 * <br>
 * Because java.util.Optional is a concrete class and has colliding method names, an alternative Optional with same semantics
 * is used instead. asNativeOptional() method is offered to get an java.util.Optional instance.<br>
 * <br>
 * When used as an Optional, because this object may contain more than one element, an exception could be thrown. If
 * the method returning this instance doesn't guarantee how many elements it contains, it is safest to use it as a Stream.<br>
 * <p/>
 * Created by kfgodel on 06/11/14.
 */
public interface Nary<T> extends Stream<T>, Optional<T>, Iterable<T> {


  /**
   * Solves conflict between Iterable and Stream
   */
  @Override
  default Spliterator<T> spliterator() {
    return Iterable.super.spliterator();
  }

  /**
   * Solves conflict between Iterable and Stream
   */
  @Override
  default void forEach(Consumer<? super T> action) {
    Iterable.super.forEach(action);
  }


  /**
   * If only one value is present in this {@code Nary}, returns the value,
   * otherwise throws {@code NoSuchElementException}, or {@code MoreThanOneElementException}.
   * This Nary as Stream is consumed to return the value
   *
   * @return the non-null value held by this {@code Nary}
   * @throws java.util.NoSuchElementException if there is no value present
   * @throws MoreThanOneElementException      if there are more than one
   * @see Optional#isPresent()
   */
  T get() throws NoSuchElementException, MoreThanOneElementException;

  /**
   * Return {@code true} if there is only a value present, otherwise {@code false}.
   * This Nary as Stream is consumed.
   *
   * @return {@code true} if there is a value present, otherwise {@code false}
   * @throws MoreThanOneElementException if there are more than one
   */
  boolean isPresent() throws MoreThanOneElementException;

  /**
   * If the only value is present, invoke the specified consumer with the value,
   * otherwise do nothing.
   * This Nary as Stream is consumed.
   *
   * @param consumer block to be executed if a value is present
   * @throws NullPointerException        if value is present and {@code consumer} is
   *                                     null
   * @throws MoreThanOneElementException if there are more than one
   */
  void ifPresent(Consumer<? super T> consumer) throws MoreThanOneElementException;

  /**
   * [Method not present in Optional]
   * If the only value is absent invoke the given runnable, or else do nothing
   * This Nary as Stream is consumed.
   *
   * @param runnable The code to execute if this optional is empty
   * @throws MoreThanOneElementException if there are more than one
   */
  void ifAbsent(Runnable runnable) throws MoreThanOneElementException;

  /**
   * If the only value is present, and the value matches the given predicate,
   * return an {@code Optional} describing the value, otherwise return an
   * empty {@code Optional}.
   * This Nary as Stream is consumed.
   *
   * @param predicate a predicate to apply to the value, if present
   * @return an {@code Optional} describing the value of this {@code Optional}
   * if a value is present and the value matches the given predicate,
   * otherwise an empty {@code Optional}
   * @throws NullPointerException        if the predicate is null
   * @throws MoreThanOneElementException if there are more than one
   */
  Optional<T> filterOptional(Predicate<? super T> predicate) throws MoreThanOneElementException;

  /**
   * If the only value is present, apply the provided mapping function to it,
   * and if the result is non-null, return an {@code Optional} describing the
   * result.  Otherwise return an empty {@code Optional}.
   *
   * @param <U>    The type of the result of the mapping function
   * @param mapper a mapping function to apply to the value, if present
   * @return an {@code Optional} describing the result of applying a mapping
   * function to the value of this {@code Optional}, if a value is present,
   * otherwise an empty {@code Optional}
   * @throws NullPointerException        if the mapping function is null
   * @throws MoreThanOneElementException if there are more than one
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
  <U> Optional<U> mapOptional(Function<? super T, ? extends U> mapper) throws MoreThanOneElementException;

  /**
   * If the only value is present, apply the provided {@code Optional}-bearing
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
   * @throws NullPointerException        if the mapping function is null or returns
   *                                     a null result
   * @throws MoreThanOneElementException if there are more than one
   */
  <U> Optional<U> flatMapOptional(Function<? super T, java.util.Optional<U>> mapper) throws MoreThanOneElementException;

  /**
   * Return the only value if present, otherwise return {@code other}.
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
   *
   * @param other a {@code Supplier} whose result is returned if no value
   *              is present
   * @return the value if present otherwise the result of {@code other.get()}
   * @throws NullPointerException        if value is not present and {@code other} is
   *                                     null
   * @throws MoreThanOneElementException if there are more than one
   */
  T orElseGet(Supplier<? extends T> other) throws MoreThanOneElementException;

  /**
   * Return the only contained value, if present, otherwise throw an exception
   * to be created by the provided supplier.
   *
   * @param <X>               Type of the exception to be thrown
   * @param exceptionSupplier The supplier which will return the exception to
   *                          be thrown
   * @return the present value
   * @throws X                           if there is no value present
   * @throws NullPointerException        if no value is present and
   *                                     {@code exceptionSupplier} is null
   * @throws MoreThanOneElementException if there are more than one
   * @apiNote A method reference to the exception constructor with an empty
   * argument list can be used as the supplier. For example,
   * {@code IllegalStateException::new}
   */
  <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X, MoreThanOneElementException;

  /**
   * @see java.lang.Object#equals(Object)
   */
  @Override
  boolean equals(Object obj);

  /**
   * @see Object#hashCode()
   */
  @Override
  int hashCode();

  /**
   * @see Object#toString()
   */
  @Override
  String toString();

  /**
   * Returns the last element in this nary, if present.<br>
   * Is empty if this is empty, it's the only element if this contains one, is the last if this is more than one
   *
   * @return The optional last contained element
   */
  Optional<T> findLast();

  /**
   * Creates another nary that is the result of joining the elements of this nary, and the given parameter
   *
   * @param otherNary The other nary to join
   * @return The created joined elements nary
   */
  <S> Nary<S> joinedWith(Stream<? extends S> otherNary);


  /**
   * Creates a nary enumerating its elements
   *
   * @param element     The first mandatory element
   * @param additionals The optional extra elements
   * @param <T>         The type of expected nary content
   * @return The created nary
   */
  static <T> Nary<T> of(T element, T... additionals){
    return NaryFromNative.of(element,additionals);
  }

  /**
   * Creates a nary from a native stream. The operation on this nary will consume the stream
   */
  static<T> Nary<T> create(Stream<T> nativeStream){
    return NaryFromNative.create(nativeStream);
  }

  /**
   * Creates a nary from a native optional. Stream like operations will generate a stream from the
   * optional
   */
  static<T> Nary<T> create(java.util.Optional<T> nativeOptional){
    return NaryFromNative.create(nativeOptional);
  }

  /**
   * Creates a nary from an element whose absence is represented by null
   */
  static<T> Nary<T> ofNullable(T nullableElement){
    return NaryFromNative.ofNullable(nullableElement);
  }

  /**
   * Creates an empty nary to represent an empty set
   */
  static<T> Nary<T> empty(){
    return NaryFromNative.empty();
  }

  /**
   * Creates a nary from a spliterator as source for a stream
   */
  static<T> Nary<T> create(Spliterator<T> spliterator){
    return NaryFromNative.create(spliterator);
  }

  /**
   * Creates a nary from an iterator. No assumptions about iterator characteristics are made
   */
  static<T> Nary<T> create(Iterator<T> iterator){
    return NaryFromNative.create(Spliterators.spliteratorUnknownSize(iterator, 0));
  }

  /**
   * Creates a nary from an iterable source
   */
  static<T> Nary<T> create(Iterable<T> iterable){
    return NaryFromNative.create(iterable.spliterator());
  }

  static<T> Nary<T> create(Object[] array){
    Stream<T> asStream = Arrays.stream((T[]) array);
    return NaryFromNative.create(asStream);
  }

}