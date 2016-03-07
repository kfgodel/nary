package ar.com.kfgodel.nary.impl.others;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This type represents an empty iterator, for objects that represent empty sets
 * Created by kfgodel on 07/03/16.
 */
public class EmptyIterator implements Iterator<Object> {

  private static final EmptyIterator INSTACE = new EmptyIterator();

  public static<T> Iterator<T> instance(){
    return (Iterator<T>) INSTACE;
  }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public Object next() {
    throw new NoSuchElementException("Next element can't be accessed on empty iterator");
  }
}
