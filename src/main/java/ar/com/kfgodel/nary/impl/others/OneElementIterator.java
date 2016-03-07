package ar.com.kfgodel.nary.impl.others;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This type represents an iterator for only one element
 * Created by kfgodel on 07/03/16.
 */
public class OneElementIterator<T> implements Iterator<T> {

  private T element;

  public static<T> OneElementIterator<T> create(T element) {
    OneElementIterator iterator = new OneElementIterator();
    iterator.element = element;
    return iterator;
  }

  @Override
  public boolean hasNext() {
    return element != null;
  }

  @Override
  public T next() {
    if(!hasNext()){
      throw new NoSuchElementException("This one element iterator was already iterated");
    }
    T iterated = element;
    this.element = null;
    return iterated;
  }
}
