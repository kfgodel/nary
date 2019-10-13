package ar.com.kfgodel.nary.api.arity;

import ar.com.kfgodel.nary.api.Nary;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * This type represents the extended api from {@link java.util.stream.Stream} modified to return {@link ar.com.kfgodel.nary.api.Nary}.<br>
 * The purpose of this type is to group all and only the methods from {@link java.util.stream.Stream} so they are separated
 * from the ones added to {@link java.util.stream.Stream} api (just for organization purpose).<br>
 * Date: 13/10/19 - 14:35
 */
public interface NaryStream<T> extends Stream<T> {

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

  @Override
  Nary<T> peek(Consumer<? super T> action);

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

  @Override
  Nary<T> filter(Predicate<? super T> predicate);

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

  @Override
  <R> Nary<R> map(Function<? super T, ? extends R> mapper);

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
  <R> Nary<R> flatMapNary(Function<? super T, ? extends Stream<? extends R>> mapper);

  @Override
  <R> Nary<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);

}
