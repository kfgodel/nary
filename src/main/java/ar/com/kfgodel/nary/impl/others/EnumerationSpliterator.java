package ar.com.kfgodel.nary.impl.others;

import java.util.Enumeration;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * This type adapts an enumeration to spliterator interface
 * Date: 29/04/18 - 15:53
 */
public class EnumerationSpliterator<T> implements Spliterator<T> {

  private Enumeration<T> enumeration;

  public static <T> EnumerationSpliterator<T> create(Enumeration<T> enumeration) {
    EnumerationSpliterator spliterator = new EnumerationSpliterator();
    spliterator.enumeration = enumeration;
    return spliterator;
  }

  @Override
  public boolean tryAdvance(Consumer<? super T> action) {
    if (enumeration.hasMoreElements()) {
      T element = enumeration.nextElement();
      action.accept(element);
      return true;
    }
    return false;
  }

  @Override
  public Spliterator<T> trySplit() {
    return null;
  }

  @Override
  public long estimateSize() {
    return Long.MAX_VALUE;
  }

  @Override
  public int characteristics() {
    return Spliterator.ORDERED;
  }
}
