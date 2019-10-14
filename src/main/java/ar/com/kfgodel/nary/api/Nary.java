package ar.com.kfgodel.nary.api;

import ar.com.kfgodel.nary.api.arity.MonoElement;
import ar.com.kfgodel.nary.api.arity.MultiElement;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * This type represents an uncertain amount of elements<br>
 * A Nary is the compile time type that can be used in runtime as a {@link Stream} or an {@link Optional}, allowing you
 * to switch from one to the other in runtime.<br>
 * <br>
 * Union of a stream and an optional, offers an interface that is the mix of both (with minor modifications).<br>
 * It represents an object that can contain 0, 1, or N elements, and can be accessed assuming one of the scenarios.<br>
 * Allows a method to behave as returning Optional or Stream depending on the arguments
 * (specially useful for query methods).<br>
 * <br>
 * When used as an Optional, because this object may contain more than one element, it is coerced and an exception
 * could be thrown. If the method returning this instance doesn't guarantee how many elements it contains,
 * this instance should be used as a Stream.<br>
 * <br>
 * <p>
 * Created by kfgodel on 06/11/14.
 *
 * @param <T> Type of contained elements
 */
public interface Nary<T> extends MonoElement<T>, MultiElement<T> {

  /**
   * This method is redefined so two instances of {@link Nary} are equal only if they contain
   * equal elements, comparing them in iteration order
   *
   * @see java.lang.Object#equals(Object)
   */
  @Override
  boolean equals(Object obj);

  /**
   * This method is redefined so the hashcode of a {@link Nary} is based on its contained elements.
   *
   * @see Object#hashCode()
   */
  @Override
  int hashCode();

  /**
   * This method is redefined so whenever possible, the contained elements are printed
   *
   * @see Object#toString()
   */
  @Override
  String toString();

}
