package ar.com.kfgodel.nary.impl;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.Unary;
import ar.com.kfgodel.nary.api.exceptions.MoreThanOneElementException;
import ar.com.kfgodel.nary.api.exceptions.NaryException;
import ar.com.kfgodel.nary.impl.others.OneElementIterator;
import ar.com.kfgodel.nary.impl.others.OneElementSpliterator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
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
public class OneElementNary<T> extends NarySupport<T> implements Unary<T> {

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
  public Unary<T> peek(Consumer<? super T> action) {
    return super.peek(action).unique();
  }

  @Override
  public Optional<T> asOptional() throws MoreThanOneElementException {
    return Optional.of(element);
  }

  @Override
  public Unary<T> unique() throws NaryException {
    return this;
  }

  protected Stream<T> asStream() {
    return Stream.of(this.element);
  }

  @Override
  public void close() {
    // We have nothing to close
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
      return concat(Nary.ofNonNullable(other.get()));
    }
    return this;
  }

  @Override
  public long count() {
    return 1;
  }

  @Override
  public Unary<T> distinct() {
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
  public Unary<T> findAnyNary() {
    return this;
  }

  @Override
  public Optional<T> findFirst() {
    return asOptional();
  }

  @Override
  public Unary<T> findFirstNary() {
    return this;
  }

  @Override
  public Unary<T> findLast() {
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
  public Unary<T> ifAbsent(Runnable runnable) {
    // Nothing to do
    return this;
  }

  @Override
  public Unary<T> ifPresent(Consumer<? super T> consumer) throws MoreThanOneElementException {
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
  public boolean isAbsent() throws MoreThanOneElementException {
    return !isPresent();
  }

  @Override
  public Iterator<T> iterator() {
    return OneElementIterator.create(element);
  }

  @Override
  public Unary<T> limit(long maxSize) {
    if (maxSize > 0) {
      return this;
    } else {
      return Nary.empty();
    }
  }

  @Override
  public <U> Unary<U> mapFilteringNullResult(Function<? super T, ? extends U> mapper) {
    return this.<U>map(mapper)
      .filter(obj -> !Objects.isNull(obj));
  }

  @Override
  public Optional<T> max(Comparator<? super T> comparator) {
    return asOptional();
  }

  @Override
  public Unary<T> maxNary(Comparator<? super T> comparator) {
    return this;
  }

  @Override
  public Optional<T> min(Comparator<? super T> comparator) {
    return asOptional();
  }

  @Override
  public Unary<T> minNary(Comparator<? super T> comparator) {
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
  public Unary<T> orElseUse(Supplier<? extends T> mapper) throws MoreThanOneElementException {
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
  public Unary<T> reduceNary(BinaryOperator<T> accumulator) {
    return this;
  }

  @Override
  public Stream<T> sequential() {
    return this;
  }

  @Override
  public Unary<T> skip(long n) {
    if (n > 0) {
      return Nary.empty();
    } else {
      return this;
    }
  }

  @Override
  public Unary<T> sorted(Comparator<? super T> comparator) {
    return this;
  }

  @Override
  public Unary<T> sorted() {
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
  public Unary<T> filter(Predicate<? super T> predicate) {
    return super.filter(predicate).unique();
  }

  @Override
  public <R> Unary<R> map(Function<? super T, ? extends R> mapper) {
    return super.<R>map(mapper).unique();
  }


  @Override
  public Stream<T> unordered() {
    return this;
  }

  public static <T> OneElementNary<T> create(T element) {
    OneElementNary<T> nary = new OneElementNary<>();
    nary.element = element;
    return nary;
  }
}
