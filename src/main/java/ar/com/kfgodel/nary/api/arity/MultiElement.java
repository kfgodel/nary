package ar.com.kfgodel.nary.api.arity;

import ar.com.kfgodel.nary.api.Nary;

import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * This type defines the protocol that nary has when its viewed as a container of elements
 *
 * Date: 12/10/19 - 21:42
 */
public interface MultiElement<T> extends Iterable<T>, NaryStream<T> {
  /**
   * Solves conflict between Iterable and Stream because Iterable uses a default implementation.<br>
   * We use Iterable definition
   */
  @Override
  default Spliterator<T> spliterator() {
    return Iterable.super.spliterator();
  }

  /**
   * Solves conflict between Iterable and Stream because Iterable uses a default implementation.<br>
   * We use Iterable definition
   */
  @Override
  default void forEach(Consumer<? super T> action) {
    Iterable.super.forEach(action);
  }

  /**
   * Returns the content of this nary in a list.<br>
   *   This nary, as stream, will be consumed in the operation.<br>
   * This method is a shorthand of calling collect(Collectors.toList())
   * @return The list with the elements of this nary
   */
  List<T> collectToList();

  /**
   * Returns the content of this nary in a set.<br>
   * This nary, as stream, will be consumed in the operation.<br>
   * This method is a shorthand of calling collect(Collectors.toSet())
   *
   * @return The set with the elements of this nary
   */
  Set<T> collectToSet();

  /**
   * Returns a stream of this optional containing the only element (if present)
   * @return The empty stream if this is empty, a one element stream if not
   */
  Stream<T> asStream();

  /**
   * Creates a concatenated stream of the element of this optional, and the given stream
   * @param other The stream to combine after this
   * @return An empty nary if both are empty, a nary with the argument stream if this optional
   * is empty, a nary with this element plus the stream elements
   */
  Nary<T> concat(Stream<? extends T> other);

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
   * Returns the last element in this nary, if present.<br>
   * Is empty if this is empty, it's the only element if this contains one, is the last if this is more than one
   *
   * @return The optional last contained element
   */
  Nary<T> findLast();
}
