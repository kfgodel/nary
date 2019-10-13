package ar.com.kfgodel.nary.api;

import ar.com.kfgodel.nary.api.exceptions.MoreThanOneElementException;
import ar.com.kfgodel.nary.api.optionals.Optional;
import ar.com.kfgodel.nary.impl.EmptyNary;
import ar.com.kfgodel.nary.impl.OneElementNary;
import ar.com.kfgodel.nary.impl.StreamBasedNary;
import ar.com.kfgodel.nary.impl.others.EnumerationSpliterator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
 * <br>
 * Created by kfgodel on 06/11/14.
 */
public interface Nary<T> extends Optional<T>, Iterable<T> {

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
  Optional<T> ifAbsent(Runnable runnable) throws MoreThanOneElementException;

  /**
   * If the only value is present, apply the given action over it, returning self.<br>
   *   This method is semantically similar to ifPresent() but it allows method chaining
   * This Nary as Stream is consumed.
   *
   * @param action a <a href="package-summary.html#NonInterference">
   *                 non-interfering</a> action to perform on the elements as
   *                 they are consumed from the stream
   * @return an {@code Optional} describing the value of this {@code Optional}
   * if a value is present and the value matches the given predicate,
   * otherwise an empty {@code Optional}
   */
  Optional<T> peekOptional(Consumer<? super T> action);

  /**
   * Returns a Nary consisting of the elements of this Nary, additionally
   * performing the provided action on each element as elements are consumed
   * from the resulting Nary.
   * <p>This is an <a href="package-summary.html#StreamOps">intermediate
   * operation</a>.
   * </p>
   * <p>For parallel stream pipelines, the action may be called at
   * whatever time and in whatever thread the element is made available by the
   * upstream operation.  If the action modifies shared state,
   * it is responsible for providing the required synchronization.
   * </p>
   *
   * @param action a <a href="package-summary.html#NonInterference">
   *               non-interfering</a> action to perform on the elements as
   *               they are consumed from the stream
   * @return the new Nary
   */
  Nary<T> peekNary(Consumer<? super T> action);


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
   * This Nary as Stream is consumed.
   *
   *
   * This method supports post-processing on optional values, without
   * the need to explicitly check for a return status.  For example, the
   * following code traverses a stream of file names, selects one that has
   * not yet been processed, and then opens that file, returning an
   * {@code Optional<FileInputStream>}:
   *
   * <pre>{@code
   *     Optional<FileInputStream> fis =
   *         names.stream().filter(name -> !isProcessedYet(name))
   *                       .findFirst()
   *                       .mapOptional(name -> new FileInputStream(name));
   * }</pre>
   *
   * Here, {@code findFirst} returns an {@code Optional<String>}, and then
   * {@code mapOptional} returns an {@code Optional<FileInputStream>} for the desired
   * file if one exists.
   *
   * @param <U>    The type of the result of the mapping function
   * @param mapper a mapping function to apply to the value, if present
   * @return an {@code Optional} describing the result of applying a mapping
   * function to the value of this {@code Optional}, if a value is present,
   * otherwise an empty {@code Optional}
   * @throws NullPointerException        if the mapping function is null
   * @throws MoreThanOneElementException if there are more than one
   */
  <U> Optional<U> mapOptional(Function<? super T, ? extends U> mapper) throws MoreThanOneElementException;

  /**
   * If the only value is present, apply the provided {@code Optional}-bearing
   * mapping function to it, return that result, otherwise return an empty
   * {@code Optional}.  This method is similar to {@link #mapOptional(Function)},
   * but the provided mapper is one whose result is already an {@code Optional},
   * and if invoked, {@code flatMapOptional} does not wrap it with an additional
   * {@code Optional}.
   * This Nary as Stream is consumed.
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
   * @throws NullPointerException        if value is not present and {@code other} is
   *                                     null
   * @throws MoreThanOneElementException if there are more than one
   */
  T orElseGet(Supplier<? extends T> other) throws MoreThanOneElementException;

  /**
   * Return the only contained value, if present, otherwise throw an exception
   * to be created by the provided supplier.
   * This Nary as Stream is consumed.
   *
   * A method reference to the exception constructor with an empty
   * argument list can be used as the supplier. For example,
   * {@code IllegalStateException::new}
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
   * Returns the only contained value, if present, otherwise throws a runtime exception
   * to be created by the provided supplier.
   * This method allows throwing runtime exception where the compiler cannot infer if it's runtime
   * or compile time checked
   * This Nary as Stream is consumed.
   *
   * A method reference to the exception constructor with an empty
   * argument list can be used as the supplier. For example,
   * {@code IllegalStateException::new}
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
  <X extends RuntimeException> T orElseThrowRuntime(Supplier<? extends X> exceptionSupplier) throws X, MoreThanOneElementException;

  /**
   * Performs a <a href="package-summary.html#MutableReduction">mutable
   * reduction</a> operation on the elements of this stream.  A mutable
   * reduction is one in which the reduced value is a mutable result container,
   * such as an {@code ArrayList}, and elements are incorporated by updating
   * the state of the result rather than by replacing the result.  This
   * produces a result equivalent to:
   * <pre>{@code
   *     R result = supplier.get();
   *     for (T element : this stream)
   *         accumulator.accept(result, element);
   *     return result;
   * }</pre>
   *
   * <p>This is a <a href="package-summary.html#StreamOps">terminal
   * operation</a>.
   *
   * There are many existing classes in the JDK whose signatures are
   * well-suited for use with method references as arguments to {@code collect()}.
   * For example, the following will accumulate strings into an {@code ArrayList}:
   * <pre>{@code
   *     List<String> asList = stringStream.collect(ArrayList::new, ArrayList::add);
   * }</pre>
   *
   * <p>The following will take a stream of strings and concatenates them into a
   * single string:
   * <pre>{@code
   *     String concat = stringStream.collect(StringBuilder::new, StringBuilder::append)
   *                                 .toString();
   * }</pre>
   *
   * @param <R> type of the result
   * @param supplier a function that creates a new result container. For a
   *                 parallel execution, this function may be called
   *                 multiple times and must return a fresh value each time.
   * @param accumulator an <a href="package-summary.html#Associativity">associative</a>,
   *                    <a href="package-summary.html#NonInterference">non-interfering</a>,
   *                    <a href="package-summary.html#Statelessness">stateless</a>
   *                    function for incorporating an additional element into a result
   * @return the result of the reduction
   */
  <R> R collect(Supplier<R> supplier,
                BiConsumer<R, ? super T> accumulator);

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
  Nary<T> findLast();

  /**
   * Returns a nary consisting of the distinct elements (according to
   * {@link Object#equals(Object)}) of this nary.
   *
   * <p>For ordered streams, the selection of distinct elements is stable
   * (for duplicated elements, the element appearing first in the encounter
   * order is preserved.)  For unordered streams, no stability guarantees
   * are made.
   *
   * <p>This is a <a href="package-summary.html#StreamOps">stateful
   * intermediate operation</a>.
   *
   * Preserving stability for {@code distinct()} in parallel pipelines is
   * relatively expensive (requires that the operation act as a full barrier,
   * with substantial buffering overhead), and stability is often not needed.
   * Using an unordered stream source (such as {@link #generate(Supplier)})
   * or removing the ordering constraint with {@link #unordered()} may result
   * in significantly more efficient execution for {@code distinct()} in parallel
   * pipelines, if the semantics of your situation permit.  If consistency
   * with encounter order is required, and you are experiencing poor performance
   * or memory utilization with {@code distinct()} in parallel pipelines,
   * switching to sequential execution with {@link #sequential()} may improve
   * performance.
   *
   * @return the new stream
   */
  Nary<T> distinct();

  /**
   * Returns a nary consisting of the elements of this stream, sorted
   * according to natural order.  If the elements of this nary are not
   * {@code Comparable}, a {@code java.lang.ClassCastException} may be thrown
   * when the terminal operation is executed.
   *
   * <p>For ordered streams, the sort is stable.  For unordered streams, no
   * stability guarantees are made.
   *
   * <p>This is a <a href="package-summary.html#StreamOps">stateful
   * intermediate operation</a>.
   *
   * @return the new stream
   */
  Nary<T> sorted();

  /**
   * Returns a nary consisting of the elements of this stream, sorted
   * according to the provided {@code Comparator}.
   *
   * <p>For ordered streams, the sort is stable.  For unordered streams, no
   * stability guarantees are made.
   *
   * <p>This is a <a href="package-summary.html#StreamOps">stateful
   * intermediate operation</a>.
   *
   * @param comparator a <a href="package-summary.html#NonInterference">non-interfering</a>,
   *                   <a href="package-summary.html#Statelessness">stateless</a>
   *                   {@code Comparator} to be used to compare stream elements
   * @return the new stream
   */
  Nary<T> sorted(Comparator<? super T> comparator);

  /**
   * Returns a nary consisting of the elements of this stream, additionally
   * performing the provided action on each element as elements are consumed
   * from the resulting stream.
   *
   * <p>This is an <a href="package-summary.html#StreamOps">intermediate
   * operation</a>.
   *
   * <p>For parallel stream pipelines, the action may be called at
   * whatever time and in whatever thread the element is made available by the
   * upstream operation.  If the action modifies shared state,
   * it is responsible for providing the required synchronization.
   *
   * This method exists mainly to support debugging, where you want
   * to see the elements as they flow past a certain point in a pipeline:
   * <pre>{@code
   *     Stream.of("one", "two", "three", "four")
   *         .filter(e -> e.length() > 3)
   *         .peek(e -> System.out.println("Filtered value: " + e))
   *         .map(String::toUpperCase)
   *         .peek(e -> System.out.println("Mapped value: " + e))
   *         .collect(Collectors.toList());
   * }</pre>
   *
   * @param action a <a href="package-summary.html#NonInterference">
   *                 non-interfering</a> action to perform on the elements as
   *                 they are consumed from the stream
   * @return the new stream
   */
  Nary<T> peek(Consumer<? super T> action);

  /**
   * Returns a nary consisting of the elements of this stream, truncated
   * to be no longer than {@code maxSize} in length.
   *
   * <p>This is a <a href="package-summary.html#StreamOps">short-circuiting
   * stateful intermediate operation</a>.
   *
   *
   * While {@code limit()} is generally a cheap operation on sequential
   * stream pipelines, it can be quite expensive on ordered parallel pipelines,
   * especially for large values of {@code maxSize}, since {@code limit(n)}
   * is constrained to return not just any <em>n</em> elements, but the
   * <em>first n</em> elements in the encounter order.  Using an unordered
   * stream source (such as {@link #generate(Supplier)}) or removing the
   * ordering constraint with {@link #unordered()} may result in significant
   * speedups of {@code limit()} in parallel pipelines, if the semantics of
   * your situation permit.  If consistency with encounter order is required,
   * and you are experiencing poor performance or memory utilization with
   * {@code limit()} in parallel pipelines, switching to sequential execution
   * with {@link #sequential()} may improve performance.
   *
   * @param maxSize the number of elements the stream should be limited to
   * @return the new stream
   * @throws IllegalArgumentException if {@code maxSize} is negative
   */
  Nary<T> limit(long maxSize);

  /**
   * Returns a nary consisting of the remaining elements of this stream
   * after discarding the first {@code n} elements of the stream.
   * If this stream contains fewer than {@code n} elements then an
   * empty stream will be returned.
   *
   * <p>This is a <a href="package-summary.html#StreamOps">stateful
   * intermediate operation</a>.
   *
   *
   * While {@code skip()} is generally a cheap operation on sequential
   * stream pipelines, it can be quite expensive on ordered parallel pipelines,
   * especially for large values of {@code n}, since {@code skip(n)}
   * is constrained to skip not just any <em>n</em> elements, but the
   * <em>first n</em> elements in the encounter order.  Using an unordered
   * stream source (such as {@link #generate(Supplier)}) or removing the
   * ordering constraint with {@link #unordered()} may result in significant
   * speedups of {@code skip()} in parallel pipelines, if the semantics of
   * your situation permit.  If consistency with encounter order is required,
   * and you are experiencing poor performance or memory utilization with
   * {@code skip()} in parallel pipelines, switching to sequential execution
   * with {@link #sequential()} may improve performance.
   *
   * @param n the number of leading elements to skip
   * @return the new stream
   * @throws IllegalArgumentException if {@code n} is negative
   */
  Nary<T> skip(long n);

  /**
   * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
   * elements of this stream, using an
   * <a href="package-summary.html#Associativity">associative</a> accumulation
   * function, and returns an {@code Optional} describing the reduced value,
   * if any. This is equivalent to:
   * <pre>{@code
   *     boolean foundAny = false;
   *     T result = null;
   *     for (T element : this stream) {
   *         if (!foundAny) {
   *             foundAny = true;
   *             result = element;
   *         }
   *         else
   *             result = accumulator.apply(result, element);
   *     }
   *     return foundAny ? Nary.of(result) : Optional.empty();
   * }</pre>
   *
   * but is not constrained to execute sequentially.
   *
   * <p>The {@code accumulator} function must be an
   * <a href="package-summary.html#Associativity">associative</a> function.
   *
   * <p>This is a <a href="package-summary.html#StreamOps">terminal
   * operation</a>.
   *
   * @param accumulator an <a href="package-summary.html#Associativity">associative</a>,
   *                    <a href="package-summary.html#NonInterference">non-interfering</a>,
   *                    <a href="package-summary.html#Statelessness">stateless</a>
   *                    function for combining two values
   * @return an {@link java.util.Optional} describing the result of the reduction
   * @throws NullPointerException if the result of the reduction is null
   * @see #reduce(Object, BinaryOperator)
   * @see #min(Comparator)
   * @see #max(Comparator)
   */
  Nary<T> reduceNary(BinaryOperator<T> accumulator);

  /**
   * Returns the minimum element of this stream according to the provided
   * {@code Comparator}.  This is a special case of a
   * <a href="package-summary.html#Reduction">reduction</a>.
   *
   * <p>This is a <a href="package-summary.html#StreamOps">terminal operation</a>.
   *
   * @param comparator a <a href="package-summary.html#NonInterference">non-interfering</a>,
   *                   <a href="package-summary.html#Statelessness">stateless</a>
   *                   {@code Comparator} to compare elements of this stream
   * @return an {@code Optional} describing the minimum element of this stream,
   * or an empty {@code Optional} if the stream is empty
   * @throws NullPointerException if the minimum element is null
   */
  Nary<T> minNary(Comparator<? super T> comparator);

  /**
   * Returns the maximum element of this stream according to the provided
   * {@code Comparator}.  This is a special case of a
   * <a href="package-summary.html#Reduction">reduction</a>.
   *
   * <p>This is a <a href="package-summary.html#StreamOps">terminal
   * operation</a>.
   *
   * @param comparator a <a href="package-summary.html#NonInterference">non-interfering</a>,
   *                   <a href="package-summary.html#Statelessness">stateless</a>
   *                   {@code Comparator} to compare elements of this stream
   * @return an {@code Optional} describing the maximum element of this stream,
   * or an empty {@code Optional} if the stream is empty
   * @throws NullPointerException if the maximum element is null
   */
  Nary<T> maxNary(Comparator<? super T> comparator);

  /**
   * Returns an {@link java.util.Optional} describing the first element of this stream,
   * or an empty {@code Optional} if the stream is empty.  If the stream has
   * no encounter order, then any element may be returned.
   *
   * <p>This is a <a href="package-summary.html#StreamOps">short-circuiting
   * terminal operation</a>.
   *
   * @return an {@code Optional} describing the first element of this stream,
   * or an empty {@code Optional} if the stream is empty
   * @throws NullPointerException if the element selected is null
   */
  Nary<T> findFirstNary();

  /**
   * Returns an {@link java.util.Optional} describing some element of the stream, or an
   * empty {@code Optional} if the stream is empty.
   *
   * <p>This is a <a href="package-summary.html#StreamOps">short-circuiting
   * terminal operation</a>.
   *
   * <p>The behavior of this operation is explicitly nondeterministic; it is
   * free to select any element in the stream.  This is to allow for maximal
   * performance in parallel operations; the cost is that multiple invocations
   * on the same source may not return the same result.  (If a stable result
   * is desired, use {@link #findFirst()} instead.)
   *
   * @return an {@code Optional} describing some element of this stream, or an
   * empty {@code Optional} if the stream is empty
   * @throws NullPointerException if the element selected is null
   * @see #findFirst()
   */
  Nary<T> findAnyNary();

  /**
   * Reduces this nary to an optional, trying to represent 0, or 1 element.
   * If this nary contains more than one, then an exception is thrown
   * @return an empty optional if this is empty, a non empty optional if this
   * has one element
   */
  Optional<T> asOptional() throws MoreThanOneElementException;


  /**
   * Returns the content of this nary in a list.<br>
   *   This nary, as stream, will be consumed in the operation.<br>
   * This method is a shorthand of calling collect(Collectors.toList())
   * @return The list with the elements of this nary
   */
  @Override
  List<T> toList();

  /**
   * Returns the content of this nary in a set.<br>
   * This nary, as stream, will be consumed in the operation.<br>
   * This method is a shorthand of calling collect(Collectors.toSet())
   *
   * @return The set with the elements of this nary
   */
  @Override
  Set<T> toSet();

  /**
   * Creates a nary enumerating its elements
   *
   * @param element     The first mandatory element
   * @param additionals The optional extra elements
   * @param <T>         The type of expected nary content
   * @return The created nary
   */
  static <T> Nary<T> of(T element, T... additionals){
    Nary<T> elementNary = OneElementNary.create(element);
    if (additionals == null || additionals.length == 0) {
      // It's only one element
      return elementNary;
    }
    Nary<T> additionalsNary = Nary.create(additionals);
    return elementNary.concatStream(additionalsNary);
  }

  /**
   * Creates a nary from a native stream. The operation on this nary will consume the stream
   * @param nativeStream original stream
   * @param <T> Expected type
   * @return A nre nary
   */
  static<T> Nary<T> create(Stream<T> nativeStream){
    return StreamBasedNary.create(nativeStream);
  }

  /**
   * Creates a nary from a native optional. Stream like operations will generate a stream from the
   * optional
   * @param nativeOptional original optional
   * @param <T> The expected element type
   * @return A new nary
   */
  static<T> Nary<T> create(java.util.Optional<T> nativeOptional){
    return nativeOptional
      .map((value)-> Nary.of(value))
      .orElse(Nary.empty());
  }

  /**
   * Creates a nary from an element whose absence is represented by null
   * @param nullableElement An unknown value
   * @param <T> The expected element type
   * @return The created nary
   */
  static<T> Nary<T> ofNullable(T nullableElement){
    if(nullableElement == null){
      return Nary.empty();
    }else{
      return Nary.of(nullableElement);
    }
  }

  /**
   * Creates an empty nary to represent an empty set
   * @param <T> Expected type
   * @return The empty instance
   */
  static<T> Nary<T> empty(){
    return EmptyNary.instance();
  }

  /**
   * Creates a nary from a spliterator as source for a stream
   * @param spliterator A spliterator
   * @param <T> The expected iterated element types
   * @return The new nary
   */
  static<T> Nary<T> create(Spliterator<T> spliterator){
    return Nary.create(StreamSupport.stream(spliterator, false));
  }

  /**
   * Creates a nary from an iterator. No assumptions about iterator characteristics are made
   * @param iterator An iterator
   * @param <T> The expected iterated type
   * @return a new nary
   */
  static<T> Nary<T> create(Iterator<T> iterator){
    return Nary.create(Spliterators.spliteratorUnknownSize(iterator, 0));
  }

  /**
   * Creates a nary from an iterable source
   * @param iterable An iterable source
   * @param <T> The expected iterable type
   * @return a new nary
   */
  static<T> Nary<T> create(Iterable<T> iterable){
    return Nary.create(iterable.spliterator());
  }

  /**
   * Creates a nary instance from the elements of a collection
   *
   * @param collection The collection
   * @param <T>        Expected type for collection elements
   * @return The new nary
   */
  static<T> Nary<T> create(Collection<T> collection){
    return Nary.create(collection.stream());
  }

  /**
   * Creates a nary from an array
   * @param array The original array
   * @param <T> The expected array element type
   * @return a new nary
   */
  static<T> Nary<T> create(T[] array){
    Stream<T> asStream = Arrays.stream(array);
    return Nary.create(asStream);
  }

  /**
   * Creates a nary from an enumeration
   *
   * @param enumeration The input enumeration
   * @param <T>         The expected element types
   * @return The new nary
   */
  static <T> Nary<T> create(Enumeration<T> enumeration) {
    EnumerationSpliterator<T> spliterator = EnumerationSpliterator.create(enumeration);
    return Nary.create(spliterator);
  }


}