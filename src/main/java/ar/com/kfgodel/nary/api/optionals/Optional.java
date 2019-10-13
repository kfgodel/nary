package ar.com.kfgodel.nary.api.optionals;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.exceptions.MoreThanOneElementException;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * This type defines the protocol Nary has to operate with its element, when we know it has just 1 element.<br>
 *   It's based on {@link java.util.Optional} to be as compatible and intuitive as possible but it departs
 *   from its concepts adding own.<br>
 *
 * Created by kfgodel on 06/11/14.
 */
@Deprecated
public interface Optional<T> extends InterfacedOptional<T>, Stream<T> {

  /**
   * Negation of isPresent(). Facility method
   *
   * @return true if there's no value to get
   * @throws MoreThanOneElementException If there's more than one value to get as Optional
   */
  boolean isAbsent() throws MoreThanOneElementException;

  /**
   * [Method not present in Optional]
   * If the value is absent invoke the given runnable, or else do nothing
   *
   * @param runnable The code to execute if this optional is empty
   * @return This instance for method chaining
   */
  Optional<T> ifAbsent(Runnable runnable);


  /**
   * If the only value is present, apply the given action over it, returning self.<br>
   * This method is semantically similar to ifPresent() but it allows method chaining
   *
   * @param action a <a href="package-summary.html#NonInterference">
   *               non-interfering</a> action to perform on the elements as
   *               they are consumed from the stream
   * @return an {@code Optional} describing the value of this {@code Optional}
   * if a value is present and the value matches the given predicate,
   * otherwise an empty {@code Optional}
   */
  Optional<T> peekOptional(Consumer<? super T> action);

  /**
   * If the only value is present, and the value matches the given predicate,
   * return an {@code Optional} describing the value, otherwise return an
   * empty {@code Optional}.
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
  <U> Optional<U> flatMapOptionally(Function<? super T, Optional<U>> mapper);

  /**
   * If a value is present, when the returned nary is consumed, the
   * given action will be apllied to it.
   *
   * <p>This is an <a href="package-summary.html#StreamOps">intermediate
   * operation</a>.
   * </p>
   *
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
   * Returns a nary consisting of the elements of this stream that match
   * the given predicate.
   *
   * <p>This is an <a href="package-summary.html#StreamOps">intermediate
   * operation</a>.
   *
   * @param predicate a <a href="package-summary.html#NonInterference">non-interfering</a>,
   *                  <a href="package-summary.html#Statelessness">stateless</a>
   *                  predicate to apply to each element to determine if it
   *                  should be included
   * @return the new stream
   */
  Nary<T> filterNary(Predicate<? super T> predicate);


  /**
   * Returns a nary consisting of the results of applying the given
   * function to the elements of this stream.
   *
   * <p>This is an <a href="package-summary.html#StreamOps">intermediate
   * operation</a>.
   *
   * @param <R> The element type of the new stream
   * @param mapper a <a href="package-summary.html#NonInterference">non-interfering</a>,
   *               <a href="package-summary.html#Statelessness">stateless</a>
   *               function to apply to each element
   * @return the new stream
   */
  <R> Nary<R> mapNary(Function<? super T, ? extends R> mapper);

  /**
   * Returns a nary consisting of the results of replacing each element of
   * this stream with the contents of a mapped stream produced by applying
   * the provided mapping function to each element.  Each mapped stream is
   * {@link java.util.stream.BaseStream#close() closed} after its contents
   * have been placed into this stream.  (If a mapped stream is {@code null}
   * an empty stream is used, instead.)
   *
   * <p>This is an <a href="package-summary.html#StreamOps">intermediate
   * operation</a>.
   *
   * The {@code flatMap()} operation has the effect of applying a one-to-many
   * transformation to the elements of the stream, and then flattening the
   * resulting elements into a new stream.
   *
   * <p><b>Examples.</b>
   *
   * <p>If {@code orders} is a stream of purchase orders, and each purchase
   * order contains a collection of line items, then the following produces a
   * stream containing all the line items in all the orders:
   * <pre>{@code
   *     orders.flatMap(order -> order.getLineItems().nary())...
   * }</pre>
   *
   * <p>If {@code path} is the path to a file, then the following produces a
   * stream of the {@code words} contained in that file:
   * <pre>{@code
   *     Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8);
   *     Stream<String> words = lines.flatMap(line -> Stream.of(line.split(" +")));
   * }</pre>
   * The {@code mapper} function passed to {@code flatMap} splits a line,
   * using a simple regular expression, into an array of words, and then
   * creates a stream of words from that array.
   *
   * @param <R> The element type of the new stream
   * @param mapper a <a href="package-summary.html#NonInterference">non-interfering</a>,
   *               <a href="package-summary.html#Statelessness">stateless</a>
   *               function to apply to each element which produces a stream
   *               of new values
   * @return the new stream
   */
  <R> Nary<R> flatMapNary(Function<? super T, ? extends Nary<? extends R>> mapper);



  /**
   * Returns an Object array containing the element of this optional (if any).
   *
   * @return an empty array if this optional is empty
   */
  Object[] toArray();

  /**
   * Returns an array containing the element of this optional, using the
   * provided {@code generator} function to allocate the returned array.
   *
   * The generator function takes an integer, which is the size of the
   * desired array, and produces an array of the desired size.  This can be
   * concisely expressed with an array constructor reference:
   * <pre>{@code
   *     Person[] men = people.stream()
   *                          .filter(p -> p.getGender() == MALE)
   *                          .toArray(Person[]::new);
   * }</pre>
   *
   * @param <A> the element type of the resulting array
   * @param generator a function which produces a new array of the desired
   *                  type and the provided length
   * @return an array containing the elements in this stream
   * @throws ArrayStoreException if the runtime type of the array returned
   * from the array generator is not a supertype of the runtime type of every
   * element in this stream
   */
  <A> A[] toArray(IntFunction<A[]> generator);

  /**
   * Performs a <a href="package-summary.html#Reduction">reduction</a> on the
   * element of this optional, using the provided identity value and an
   * <a href="package-summary.html#Associativity">associative</a>
   * accumulation function, and returns the reduced value.  This is equivalent
   * to:
   * <pre>{@code
   *     T result = identity;
   *     result = accumulator.apply(result, element)
   *     return result;
   * }</pre>
   *
   * <p>The {@code identity} value must be an identity for the accumulator
   * function. This means that for all {@code t},
   * {@code accumulator.apply(identity, t)} is equal to {@code t}.
   * The {@code accumulator} function must be an
   * <a href="package-summary.html#Associativity">associative</a> function.
   *
   * Sum, min, max, average, and string concatenation are all special
   * cases of reduction. Summing a stream of numbers can be expressed as:
   *
   * <pre>{@code
   *     Integer sum = integers.reduce(0, (a, b) -> a+b);
   * }</pre>
   *
   * or:
   *
   * <pre>{@code
   *     Integer sum = integers.reduce(0, Integer::sum);
   * }</pre>
   *
   * <p>While this may seem a more roundabout way to perform an aggregation
   * compared to simply mutating a running total in a loop, reduction
   * operations parallelize more gracefully, without needing additional
   * synchronization and with greatly reduced risk of data races.
   *
   * @param identity the identity value for the accumulating function
   * @param accumulator an <a href="package-summary.html#Associativity">associative</a>,
   *                    <a href="package-summary.html#NonInterference">non-interfering</a>,
   *                    <a href="package-summary.html#Statelessness">stateless</a>
   *                    function for combining two values
   * @return the result of the reduction
   */
  T reduce(T identity, BinaryOperator<T> accumulator);

  /**
   * Performs a <a href="package-summary.html#MutableReduction">mutable
   * reduction</a> operation on the element of this optional.  A mutable
   * reduction is one in which the reduced value is a mutable result container,
   * such as an {@code ArrayList}, and elements are incorporated by updating
   * the state of the result rather than by replacing the result.  This
   * produces a result equivalent to:
   * <pre>{@code
   *     R result = supplier.get();
   *     accumulator.accept(result, element);
   *     return result;
   * }</pre>
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
   * @param supplier a function that creates a new result container
   * @param accumulator an <a href="package-summary.html#Associativity">associative</a>,
   *                    <a href="package-summary.html#NonInterference">non-interfering</a>,
   *                    <a href="package-summary.html#Statelessness">stateless</a>
   *                    function for incorporating an additional element into a result
   * @return the result of the reduction
   */
  <R> R collect(Supplier<R> supplier,
                BiConsumer<R, ? super T> accumulator);

  /**
   * Returns the count of elements in this optional.  This is a special case of
   * a <a href="package-summary.html#Reduction">reduction</a> and is
   * equivalent to:
   * <pre>{@code
   *     return mapOptional(e -> 1L).sum();
   * }</pre>
   *
   * @return 0 if this is empty, one if it has an element
   */
  long count();

  /**
   * Returns whether the element of this optional matches the provided
   * predicate. If this is empty then
   * {@code false} is returned and the predicate is not evaluated.
   *
   * This method evaluates the <em>existential quantification</em> of the
   * predicate over the element of the optional (for some x P(x)).
   *
   * @param predicate a <a href="package-summary.html#NonInterference">non-interfering</a>,
   *                  <a href="package-summary.html#Statelessness">stateless</a>
   *                  predicate to apply to elements of this stream
   * @return {@code false} if this optional is empty, or the element doesn't match the predicate
   */
  boolean anyMatch(Predicate<? super T> predicate);

  /**
   * Returns whether the element of this optional matches the provided predicate.
   * If the stream is empty then {@code true} is returned and the predicate is
   * not evaluated.
   *
   * This method evaluates the <em>universal quantification</em> of the
   * predicate over the elements of the stream (for all x P(x)).  If the
   * stream is empty, the quantification is said to be <em>vacuously
   * satisfied</em> and is always {@code true} (regardless of P(x)).
   *
   * @param predicate a <a href="package-summary.html#NonInterference">non-interfering</a>,
   *                  <a href="package-summary.html#Statelessness">stateless</a>
   *                  predicate to apply to elements of this stream
   * @return {@code true} if the element matches the predicate, or this optional is empty
   */
  boolean allMatch(Predicate<? super T> predicate);

  /**
   * Returns whether no element of this optional matches the provided predicate.
   * If the stream is empty then {@code true} is returned and the predicate is not evaluated.
   *
   * This method evaluates the <em>universal quantification</em> of the
   * negated predicate over the elements of the stream (for all x ~P(x)).  If
   * the stream is empty, the quantification is said to be vacuously satisfied
   * and is always {@code true}, regardless of P(x).
   *
   * @param predicate a <a href="package-summary.html#NonInterference">non-interfering</a>,
   *                  <a href="package-summary.html#Statelessness">stateless</a>
   *                  predicate to apply to elements of this stream
   * @return {@code true} if the element doesn't match the predicate or this optional is empty
   */
  boolean noneMatch(Predicate<? super T> predicate);


  /**
   * Indicates whether some other object is "equal to" this Optional. The
   * other object is considered equal if:
   * <ul>
   * <li>it is also an {@code Optional} and;
   * <li>both instances have no value present or;
   * <li>the present values are "equal to" each other via {@code equals()}.
   * </ul>
   *
   * @param obj an object to be tested for equality
   * @return {code true} if the other object is "equal to" this object
   * otherwise {@code false}
   */
  @Override
  boolean equals(Object obj);

  /**
   * Returns the hash code value of the present value, if any, or 0 (zero) if
   * no value is present.
   *
   * @return hash code value of the present value or 0 if no value is present
   */
  @Override
  int hashCode();

  /**
   * Returns a non-empty string representation of this Optional suitable for
   * debugging. The exact presentation format is unspecified and may vary
   * between implementations and versions.
   * If a value is present the result must include its string
   * representation in the result. Empty and present Optionals must be
   * unambiguously differentiable.
   *
   * @return the string representation of this instance
   */
  @Override
  String toString();

  /**
   * Returns a native Optional instance to be used with native API
   *
   * @return The native instance that represents this Nary content
   * @throws MoreThanOneElementException if there are more than one
   */
  java.util.Optional<T> asNativeOptional() throws MoreThanOneElementException;

  /**
   * Returns a stream of this optional containing the only element (if present)
   * @return The empty stream if this is empty, a one element stream if not
   */
  Stream<T> asStream();

  /**
   * @return The nary that represents this optional
   */
  Nary<T> asNary();

  /**
   * Creates a concatenation of the elements of this optional and the given
   * @param other The other optional to concat elements
   * @return An empty nary if both are empty, a one element nary if one is empty,
   * a two element naryif both have elements
   */
  Nary<T> concatOptional(Optional<? extends T> other);

  /**
   * Creates a concatenated stream of the element of this optional, and the given stream
   * @param other The stream to combine after this
   * @return An empty nary if both are empty, a nary with the argument stream if this optional
   * is empty, a nary with this element plus the stream elements
   */
  Nary<T> concatStream(Stream<? extends T> other);

  /**
   * Returns the only contained value, if present, otherwise throws a runtime exception
   * to be created by the provided supplier.<br>
   * This method allows throwing runtime exception where the compiler cannot infer if it's runtime
   * or compile time checked
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
  <X extends RuntimeException> T orElseThrowRuntime(Supplier<? extends X> exceptionSupplier) throws X;

  /**
   * Returns the content of this optional inside a list.<br>
   * It will be empty if this optional is empty, or a one element list if not.
   *
   * @return The list version of this optional
   */
  List<T> toList();

  /**
   * Returns the content of this optional inside a set<br>
   * It will be empty, ot have just one element depending on this optional.
   *
   * @return The set version of this optional
   */
  Set<T> toSet();

}
