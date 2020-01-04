package ar.com.kfgodel.nary.impl.others;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This type represents an iterator for only one element
 * Created by kfgodel on 07/03/16.
 */
public class OneElementIterator<T> implements Iterator<T> {

  private boolean iterated;
  private T element;

  public static <T> OneElementIterator<T> create(T element) {
    OneElementIterator iterator = new OneElementIterator();
    iterator.iterated = false;
    iterator.element = element;
    return iterator;
  }

  @Override
  public boolean hasNext() {
    return !iterated;
  }

  @Override
  public T next() {
    if (!hasNext()) {
      throw new NoSuchElementException("This one element iterator was already iterated");
    }
    iterated = true;
    return element;
  }
}
