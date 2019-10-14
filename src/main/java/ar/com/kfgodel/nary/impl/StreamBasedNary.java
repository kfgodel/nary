package ar.com.kfgodel.nary.impl;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.exceptions.MoreThanOneElementException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This type represents a Nary with a stream as a source of elements.<br>
 *   This implies the greatest level of uncertainty as we don't know if the stream
 *   is empty, contains one, or more than one elements until it's consumed. So the nary
 *   offers an interface that may be bigger that what the stream actually supports.<br>
 *   <br>
 *   That is why in Runtime, this type may throw exceptions depending on how it's used, but
 *   it's assumed that the user may have a better judgement to avoid unsafe methods at runtime.
 *   This brings much more flexibility on the type, than narrowing the interface on compile time
 *
 * Created by kfgodel on 07/03/16.
 */
public class StreamBasedNary<T> extends NarySupport<T>  {

  private Stream<? extends T> sourceStream;
  /**
   * Because optionals are not consumed, we can cache it to reuse everytime.
   * In this way, once the stream is consumed we still can act as an optional
   */
  private Nary<T> cachedOptional;

  public static<T> StreamBasedNary<T> create(Stream<? extends T> source) {
    StreamBasedNary<T> nary = new StreamBasedNary<>();
    nary.sourceStream = source;
    return nary;
  }

  @Override
  public Nary<T> coerceToMonoElement() throws MoreThanOneElementException {
    if(cachedOptional == null){
      this.cachedOptional = reduceStreamToOptional();
    }
    return cachedOptional;
  }

  @Override
  public List<T> collectToList() {
    return collect(Collectors.toList());
  }

  @Override
  public Set<T> collectToSet() {
    return collect(Collectors.toSet());
  }

  private Nary<T> reduceStreamToOptional() {
    Iterator<T> iterator = asStream().iterator();
    if(!iterator.hasNext()){
      return Nary.empty();
    }
    T onlyElement = iterator.next();
    if(iterator.hasNext()){
      throw new MoreThanOneElementException("Expecting 1 element in the stream to create an optional but found at least 2: " + Arrays.asList(onlyElement, iterator.next()));
    }
    return Nary.of(onlyElement);
  }

  @Override
  public Stream<T> asStream() {
    return (Stream<T>) sourceStream;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(getClass().getSimpleName());
    builder.append("{stream: ");
    builder.append(sourceStream);
    builder.append("}");
    return builder.toString();
  }
}
