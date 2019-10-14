package ar.com.kfgodel.nary.impl.others;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * This type represents a spliterator with only one element
 * Created by kfgodel on 07/03/16.
 */
public class OneElementSpliterator<T> implements Spliterator<T> {

  private T element;

  public static <T> OneElementSpliterator<T> create(T element) {
    OneElementSpliterator<T> spliterator = new OneElementSpliterator<>();
    spliterator.element = element;
    return spliterator;
  }


  @Override
  public boolean tryAdvance(Consumer<? super T> action) {
    boolean hadElement = hasElement();
    if (hadElement) {
      action.accept(element);
      element = null;
    }
    return hadElement;
  }

  private boolean hasElement() {
    return element != null;
  }

  @Override
  public Spliterator<T> trySplit() {
    return null;
  }

  @Override
  public long estimateSize() {
    return hasElement() ? 1 : 0;
  }

  @Override
  public int characteristics() {
    return ORDERED | DISTINCT | SIZED | NONNULL | IMMUTABLE | CONCURRENT;
  }

  @Override
  public long getExactSizeIfKnown() {
    return estimateSize();
  }
}
