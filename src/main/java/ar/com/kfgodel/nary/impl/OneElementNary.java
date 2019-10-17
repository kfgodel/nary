package ar.com.kfgodel.nary.impl;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.Narys;
import ar.com.kfgodel.nary.api.exceptions.MoreThanOneElementException;
import ar.com.kfgodel.nary.impl.others.OneElementIterator;
import ar.com.kfgodel.nary.impl.others.OneElementSpliterator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * This type represents a nary
 * Created by kfgodel on 07/03/16.
 */
public class OneElementNary<T> extends NarySupport<T> {

  private T element;

  @Override
  public boolean allMatch(Predicate<? super T> predicate) {
    // Because there's only one
    return predicate.test(element);
  }

  @Override
  public boolean anyMatch(Predicate<? super T> predicate) {
    return predicate.test(element);
  }

  @Override
  public Optional<T> asOptional() throws MoreThanOneElementException {
    return Optional.of(element);
  }

  @Override
  public Stream<T> asStream() {
    return Stream.of(this.element);
  }

  @Override
  public void close() {
    // We have nothing to close
  }

  @Override
  public Nary<T> coerceToMonoElement() throws MoreThanOneElementException {
    return this;
  }

  @Override
  public <R, A> R collect(Collector<? super T, A, R> collector) {
    // Combiner can be ignored when there's 1 element bc there's no combination
    A container = collect(collector.supplier(), collector.accumulator());
    R result = collector.finisher().apply(container);
    return result;
  }

  @Override
  public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
    // We ignore the combiner because there's nothing to combine when there's only 1 element
    return collect(supplier, accumulator);
  }

  @Override
  public List<T> collectToList() {
    List<T> lista = new ArrayList<>(1);
    lista.add(element);
    return lista;
  }

  @Override
  public Set<T> collectToSet() {
    Set<T> set = new HashSet<>();
    set.add(element);
    return set;
  }

  private boolean compareToElement(Iterator thatIterator) {
    if (!thatIterator.hasNext()) {
      // We are not equal if the other is empty
      return false;
    }
    final Object otherElement = thatIterator.next();
    // We are equal if the first element is equal, and there are no more
    return this.element.equals(otherElement) && !thatIterator.hasNext();
  }

  @Override
  public Nary<T> concat(Optional<? extends T> other) {
    if (other.isPresent()) {
      return concat(Narys.ofNonNullable(other.get()));
    }
    return this;
  }

  @Override
  public long count() {
    return 1;
  }

  @Override
  public Nary<T> distinct() {
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Nary)) {
      return false;
    }
    Nary that = (Nary) obj;
    Iterator thatIterator = that.iterator();
    return compareToElement(thatIterator);
  }

  @Override
  public Optional<T> findAny() {
    return asOptional();
  }

  @Override
  public Nary<T> findAnyNary() {
    return this;
  }

  @Override
  public Optional<T> findFirst() {
    return asOptional();
  }

  @Override
  public Nary<T> findFirstNary() {
    return this;
  }

  @Override
  public Nary<T> findLast() {
    return this;
  }

  @Override
  public void forEach(Consumer<? super T> action) {
    action.accept(element);
  }

  @Override
  public void forEachOrdered(Consumer<? super T> action) {
    forEach(action);
  }

  @Override
  public T get() throws NoSuchElementException {
    return element;
  }

  @Override
  public int hashCode() {
    // Based on arrayList implementation with one element
    return 31 + element.hashCode();
  }

  @Override
  public Nary<T> ifAbsent(Runnable runnable) {
    // Nothing to do
    return this;
  }

  @Override
  public Nary<T> ifPresent(Consumer<? super T> consumer) throws MoreThanOneElementException {
    consumer.accept(element);
    return this;
  }

  @Override
  public boolean isParallel() {
    return false;
  }

  @Override
  public boolean isPresent() {
    return true;
  }

  @Override
  public Iterator<T> iterator() {
    return OneElementIterator.create(element);
  }

  @Override
  public Nary<T> limit(long maxSize) {
    if (maxSize > 0) {
      return this;
    } else {
      return Narys.empty();
    }
  }

  @Override
  public <U> Nary<U> mapFilteringNullResult(Function<? super T, ? extends U> mapper) {
    U mapped = mapper.apply(element);
    return Narys.of(mapped);
  }

  @Override
  public Optional<T> max(Comparator<? super T> comparator) {
    return asOptional();
  }

  @Override
  public Nary<T> maxNary(Comparator<? super T> comparator) {
    return this;
  }

  @Override
  public Optional<T> min(Comparator<? super T> comparator) {
    return asOptional();
  }

  @Override
  public Nary<T> minNary(Comparator<? super T> comparator) {
    return this;
  }

  @Override
  public boolean noneMatch(Predicate<? super T> predicate) {
    return !predicate.test(element);
  }

  @Override
  public T orElse(T other) throws MoreThanOneElementException {
    return element;
  }

  @Override
  public T orElseGet(Supplier<? extends T> other) throws MoreThanOneElementException {
    return element;
  }

  @Override
  public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) {
    return element;
  }

  @Override
  public Nary<T> orElseUse(Supplier<? extends T> mapper) throws MoreThanOneElementException {
    return this;
  }

  @Override
  public Stream<T> parallel() {
    return this;
  }

  @Override
  public T reduce(T identity, BinaryOperator<T> accumulator) {
    T result = accumulator.apply(identity, element);
    return result;
  }

  @Override
  public Optional<T> reduce(BinaryOperator<T> accumulator) {
    return asOptional();
  }

  @Override
  public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
    U result = accumulator.apply(identity, element);
    return result;
  }

  @Override
  public Nary<T> reduceNary(BinaryOperator<T> accumulator) {
    return this;
  }

  @Override
  public Stream<T> sequential() {
    return this;
  }

  @Override
  public Nary<T> skip(long n) {
    if (n > 0) {
      return Narys.empty();
    } else {
      return this;
    }
  }

  @Override
  public Nary<T> sorted(Comparator<? super T> comparator) {
    return this;
  }

  @Override
  public Nary<T> sorted() {
    return this;
  }

  @Override
  public Spliterator<T> spliterator() {
    return OneElementSpliterator.create(element);
  }

  @Override
  public <A> A[] toArray(IntFunction<A[]> generator) {
    A[] oneElementArray = generator.apply(1);
    oneElementArray[0] = (A) element;
    return oneElementArray;
  }

  @Override
  public Object[] toArray() {
    return new Object[]{element};
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(getClass().getSimpleName());
    builder.append("{ ");
    builder.append(element);
    builder.append(" }");
    return builder.toString();
  }

  @Override
  public Stream<T> unordered() {
    return this;
  }

  public static <T> OneElementNary<T> create(T element) {
    if (element == null) {
      throw new IllegalArgumentException("Element can't be null");
    }
    OneElementNary<T> nary = new OneElementNary<>();
    nary.element = element;
    return nary;
  }
}
