package ar.com.kfgodel.nary.api;

import ar.com.kfgodel.nary.api.arity.MonoElement;
import ar.com.kfgodel.nary.api.arity.MultiElement;
import ar.com.kfgodel.nary.impl.EmptyNary;
import ar.com.kfgodel.nary.impl.OneElementNary;
import ar.com.kfgodel.nary.impl.StreamBasedNary;
import ar.com.kfgodel.nary.impl.others.EnumerationSpliterator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
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
public interface Nary<T> extends MonoElement<T>, MultiElement<T> {

  /**
   * This method is redefined so two naries are equal if they contain equal elements when iterated together
   * @see java.lang.Object#equals(Object)
   */
  @Override
  boolean equals(Object obj);

  /**
   * This method is redefined so the hashcode of a nary is based on its contained elements.
   * @see Object#hashCode()
   */
  @Override
  int hashCode();

  /**
   * This method is redefined so whenever posible, the contained elements are peeked
   * @see Object#toString()
   */
  @Override
  String toString();


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