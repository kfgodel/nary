package ar.com.kfgodel.nary.impl.others;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * This type represents an empty spliterator for empty sets
 * Created by kfgodel on 07/03/16.s
 */
public class EmptySpliterator implements Spliterator<Object> {

  private static final EmptySpliterator INSTANCE = new EmptySpliterator();

  public static<T> Spliterator<T> instance(){
    return (Spliterator<T>) INSTANCE;
  }

  @Override
  public boolean tryAdvance(Consumer<? super Object> action) {
    return false;
  }

  @Override
  public Spliterator<Object> trySplit() {
    return this;
  }

  @Override
  public long estimateSize() {
    return 0;
  }

  @Override
  public int characteristics() {
    return 0;
  }
}
