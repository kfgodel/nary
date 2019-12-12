package ar.com.kfgodel.nary.impl.others;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This class implements a spliterator that takes its only value from a supplier
 * Date: 12/12/19 - 19:06
 */
public class OneElementSupplierSpliterator<T> implements Spliterator<T> {

  private Supplier<T> elementSupplier;
  private OneElementSpliterator<T> delegate;

  public static <T> OneElementSupplierSpliterator<T> create(Supplier<T> elementSupplier) {
    OneElementSupplierSpliterator<T> spliterator = new OneElementSupplierSpliterator<>();
    spliterator.elementSupplier = elementSupplier;
    return spliterator;
  }

  @Override
  public boolean tryAdvance(Consumer<? super T> action) {
    return getDelegate().tryAdvance(action);
  }

  @Override
  public Spliterator<T> trySplit() {
    return getDelegate().trySplit();
  }

  @Override
  public long estimateSize() {
    return getDelegate().estimateSize();
  }

  @Override
  public int characteristics() {
    return getDelegate().characteristics();
  }

  @Override
  public long getExactSizeIfKnown() {
    return getDelegate().getExactSizeIfKnown();
  }

  private Spliterator<T> getDelegate() {
    if(elementSupplier != null){
      this.delegate = OneElementSpliterator.create(elementSupplier.get());
      this.elementSupplier = null; // use it and discard
    }
    return this.delegate;
  }

}
