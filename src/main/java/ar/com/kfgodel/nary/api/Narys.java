package ar.com.kfgodel.nary.api;

import ar.com.kfgodel.nary.impl.EmptyNary;
import ar.com.kfgodel.nary.impl.OneElementNary;
import ar.com.kfgodel.nary.impl.StreamBasedNary;
import ar.com.kfgodel.nary.impl.others.EnumerationSpliterator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Interface with creation methods to get instances of {@link Nary} <br>
 * This type defines the different ways to create a {@link Nary} and how to interact with other types
 *
 * Date: 14/10/19 - 12:34
 */
public interface Narys {
  /**
   * Gets an empty nary to represent a 0 element Nary.<br>
   *
   * @param <T> Expected type of elements
   * @return The empty instance
   */
  static <T> Nary<T> empty() {
    return EmptyNary.instance(); // NOSONAR Circular dep with empty is necessary to facilitate users experience
  }

  /**
   * Creates a nary from a native stream. Any {@link Stream} operation on this instance will consume the stream.<br>
   * So it should be later discarded (it can't be reused unless the stream contains only 1 element)
   *
   * @param stream original stream
   * @param <T>    Expected type of elements
   * @return A new nary instance
   */
  static <T> Nary<T> from(Stream<? extends T> stream) {
    if (stream instanceof Nary) {
      //To avoid unnecesary wrapping
      return (Nary<T>) stream;
    }
    return StreamBasedNary.create(stream);
  }

  /**
   * Creates a nary containing a single non null element.<br>
   *
   * @param element The non null element
   * @param <T>     The type of expected nary content
   * @return The non empty created nary
   */
  static <T> Nary<T> ofNonNullable(T element) {
    return OneElementNary.create(element);
  }

  /**
   * Creates a nary containing the given elements.<br>
   *
   * @param element     The first mandatory element
   * @param additionals The optional extra elements
   * @param <T>         The type of expected nary content
   * @return The created nary
   */
  static <T> Nary<T> ofNonNullable(T element, T... additionals) {
    Nary<T> elementNary = ofNonNullable(element);
    if (additionals == null || additionals.length == 0) {
      // It's only one element
      return elementNary;
    }
    Nary<T> additionalsNary = from(additionals);
    return elementNary.concat(additionalsNary);
  }

  /**
   * Creates a nary from an element whose absence is represented by null.<br>
   * If null is passed then an empty Nary is returned
   *
   * @param nullableElement An unknown value
   * @param <T>             The expected element type
   * @return A nary with the given element or empty if null was passed
   */
  static <T> Nary<T> of(T nullableElement) {
    if (nullableElement == null) {
      return empty();
    } else {
      return ofNonNullable(nullableElement);
    }
  }

  /**
   * Creates a nary from a native optional. If the optional is empty, the empty Nary is returned
   *
   * @param nativeOptional original optional
   * @param <T>            The expected element type
   * @return A reusable nary
   */
  static <T> Nary<T> from(Optional<? extends T> nativeOptional) {
    return nativeOptional
      .<Nary<T>>map(Narys::ofNonNullable)
      .orElseGet(Narys::empty);
  }

  /**
   * Creates a nary from a spliterator as source for a stream
   *
   * @param spliterator A spliterator
   * @param <T>         The expected iterated element types
   * @return The new nary
   */
  static <T> Nary<T> from(Spliterator<T> spliterator) {
    final Stream<T> stream = StreamSupport.stream(spliterator, false);
    return from(stream);
  }

  /**
   * Creates a nary from an iterator. No assumptions about iterator characteristics are made
   *
   * @param iterator An iterator
   * @param <T>      The expected iterated type
   * @return a new nary
   */
  static <T> Nary<T> from(Iterator<T> iterator) {
    final Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, 0);
    return from(spliterator);
  }

  /**
   * Creates a nary from an iterable source
   *
   * @param iterable An iterable source
   * @param <T>      The expected iterable type
   * @return a new nary
   */
  static <T> Nary<T> from(Iterable<T> iterable) {
    final Spliterator<T> spliterator = iterable.spliterator();
    return from(spliterator);
  }

  /**
   * Creates a nary instance from the elements of a collection
   *
   * @param collection The collection
   * @param <T>        Expected type for collection elements
   * @return The new nary
   */
  static <T> Nary<T> from(Collection<T> collection) {
    final Stream<T> stream = collection.stream();
    return from(stream);
  }

  /**
   * Creates a nary from an array
   *
   * @param array The original array
   * @param <T>   The expected array element type
   * @return a new nary
   */
  static <T> Nary<T> from(T[] array) {
    Stream<T> asStream = Arrays.stream(array);
    return from(asStream);
  }

  /**
   * Creates a nary from an enumeration
   *
   * @param enumeration The input enumeration
   * @param <T>         The expected element types
   * @return The new nary
   */
  static <T> Nary<T> from(Enumeration<T> enumeration) {
    EnumerationSpliterator<T> spliterator = EnumerationSpliterator.create(enumeration);
    return from(spliterator);
  }

  /**
   * Creates a nary from the pairs of elements in a {@link Map}
   *
   * @param map The map whose pairs of elements can be iterated
   * @param <K> The type of Keys
   * @param <V> The type of values
   * @return The created Nary with the entries of the map
   */
  static <K, V> Nary<Map.Entry<K, V>> from(Map<K, V> map) {
    return from(map.entrySet().stream());
  }
}
