package ar.com.kfgodel.nary.api.arity;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.Unary;
import ar.com.kfgodel.nary.api.exceptions.MoreThanOneElementException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * This type defines the protocol that {@link Nary} offers to manipulate its elements beyond those of
 * {@link Stream}. This is the extended api that Nary offers over a native {@link Stream}.
 * <br>
 * Date: 12/10/19 - 21:42
 *
 * @param <T> Type of contained elements
 */
public interface MultiElement<T> extends Iterable<T>, NaryStream<T> {
  /**
   * Solves conflict between {@link Iterable} and {@link Stream} because {@link Iterable} defines a default
   * implementation.<br>
   * We use {@link Iterable#spliterator()} definition
   */
  @Override
  default Spliterator<T> spliterator() {
    return Iterable.super.spliterator(); // NOSONAR common-java:InsufficientLineCoverage this line is never used
  }

  /**
   * Solves conflict between {@link Iterable} and {@link Stream} because {@link Iterable} defines a default
   * implementation.<br>
   * We use {@link Iterable#forEach(Consumer)} definition
   */
  @Override
  default void forEach(Consumer<? super T> action) {
    Iterable.super.forEach(action); // NOSONAR common-java:InsufficientLineCoverage this line is never used
  }

  /**
   * Returns the content of this nary in a list.<br>
   * This method is a shorthand of calling collect(Collectors.toList()).<br>
   * <br>
   * This nary, as stream, will be consumed in the operation.<br>
   *
   * @return The list with the elements of this nary
   */
  List<T> collectToList();

  /**
   * Returns the content of this nary in a set.<br>
   * This method is a shorthand of calling collect(Collectors.toSet())
   * <br>
   * This nary, as stream, will be consumed in the operation.<br>
   *
   * @return The set with the elements of this nary
   */
  Set<T> collectToSet();

  /**
   * Creates another nary that will contain the elements of this instance and the given stream
   *
   * @param other The stream to combine after this
   * @return A nary with these elements first and the one on the stream later. Or empty if both are empty
   */
  Nary<T> concat(Stream<? extends T> other);

  /**
   * Creates a concatenated Nary with the elements of this instance and the one (if any) on the given {@link Optional}
   *
   * @param other The optional to join elements with
   * @return A new Nary with the elements from both instances
   */
  Nary<T> concat(Optional<? extends T> other);

  /**
   * Creates a new Nary with the elements of this instance and the ones passed as var arg array
   *
   * @param others The elements to append to this stream
   * @return A new Nary with the elements from this instance plus the ones on the given array
   */
  Nary<T> add(T... others);

  /**
   * Performs a <a href="package-summary.html#MutableReduction">mutable
   * reduction</a> operation on the elements of this stream.  A mutable
   * reduction is one in which the reduced value is a mutable result container,
   * such as an {@code ArrayList}, and elements are incorporated by updating
   * the state of the result rather than by replacing the result.  This
   * produces a result equivalent to:
   * <pre>{@code
   *     R result = supplier.asUni().get();
   *     for (T element : this stream)
   *         accumulator.accept(result, element);
   *     return result;
   * }</pre>
   *
   * <p>This is a <a href="package-summary.html#StreamOps">terminal
   * operation</a>.
   * <p>
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
   * @param <R>         type of the result
   * @param supplier    a function that creates a new result container. For a
   *                    parallel execution, this function may be called
   *                    multiple times and must return a fresh value each time.
   * @param accumulator an <a href="package-summary.html#Associativity">associative</a>,
   *                    <a href="package-summary.html#NonInterference">non-interfering</a>,
   *                    <a href="package-summary.html#Statelessness">stateless</a>
   *                    function for incorporating an additional element into a result
   * @return the result of the reduction
   */
  <R> R collect(Supplier<R> supplier,
                BiConsumer<R, ? super T> accumulator);

  /**
   * Returns the last element in this nary, if present.<br>
   * Depending on the amount of elements on this Nary, the result may be empty or contain the last element
   *
   * @return The optional last element
   */
  Unary<T> findLast();

  /**
   * Alternative to {@link Stream#reduce(BinaryOperator)} that returns {@link Unary} instead of {@link java.util.Optional}
   *
   * @param accumulator The associative function to reduce the elements of this nary to a result of the same type
   * @return The optional result of the reduction (or an empty nary)
   * @see Stream#reduce(BinaryOperator) for a complete reference
   */
  Unary<T> reduceNary(BinaryOperator<T> accumulator);

  /**
   * Alternative to {@link Stream#min(Comparator)} that returns {@link Nary} instead of {@link java.util.Optional}
   *
   * @param comparator a <a href="package-summary.html#NonInterference">non-interfering</a>,
   *                   <a href="package-summary.html#Statelessness">stateless</a>
   *                   {@code Comparator} to compare elements of this stream
   * @return a {@link Nary} describing the minimum element of this stream,
   * or an empty if there are no elements
   * @throws NullPointerException if the minimum element is null
   */
  Unary<T> minNary(Comparator<? super T> comparator);

  /**
   * Alternative to {@link Stream#max(Comparator)} that returns {@link Nary} instead of {@link java.util.Optional}
   *
   * @param comparator a <a href="package-summary.html#NonInterference">non-interfering</a>,
   *                   <a href="package-summary.html#Statelessness">stateless</a>
   *                   {@code Comparator} to compare elements of this stream
   * @return a {@link Nary} describing the maximum element of this stream,
   * or an empty if there are no elements
   * @throws NullPointerException if the maximum element is null
   */
  Unary<T> maxNary(Comparator<? super T> comparator);

  /**
   * Alternative to {@link Stream#findFirst()} that returns {@link Nary} instead of {@link java.util.Optional}
   *
   * @return a  {@code Nary} describing the first element of this stream,
   * or an empty {@code Nary} if the stream is empty
   * @throws NullPointerException if the element selected is null
   */
  Unary<T> findFirstNary();

  /**
   * Alternative to {@link Stream#findAny()} that returns {@link Nary} instead of {@link java.util.Optional}
   *
   * @return an {@code Nary} describing some element of this stream, or an
   * empty {@code Nary} if the stream is empty
   * @throws NullPointerException if the element selected is null
   * @see #findFirst()
   */
  Unary<T> findAnyNary();

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

  /**
   * Treats this instance as having a single element. Returns an object that can be used to extend the operations
   * available once we know there are not more elements than 1.<br>
   * If this instance has more than 1 element then this method will fail when trying to return a {@link Unary}
   * because expectations between runtime and compile time won't match
   *
   * @return The extended api for mono element
   * @throws MoreThanOneElementException If this instance has more than one element
   */
  Unary<T> unique() throws MoreThanOneElementException;
}
