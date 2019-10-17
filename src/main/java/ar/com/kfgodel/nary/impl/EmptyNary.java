package ar.com.kfgodel.nary.impl;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.Narys;
import ar.com.kfgodel.nary.api.exceptions.MoreThanOneElementException;
import ar.com.kfgodel.nary.impl.others.EmptyArray;
import ar.com.kfgodel.nary.impl.others.EmptyIterator;
import ar.com.kfgodel.nary.impl.others.EmptySpliterator;

import java.util.Collections;
import java.util.Comparator;
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
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * This type represents the empty nary
 * <br>
 * Created by kfgodel on 07/03/16.
 */
public class EmptyNary extends NarySupport<Object> {

  private static final EmptyNary INSTANCE = new EmptyNary();

  @Override
  public boolean allMatch(Predicate<? super Object> predicate) {
    // In an empty set, all predicates are true
    return true;
  }

  @Override
  public boolean anyMatch(Predicate<? super Object> predicate) {
    // In an empty set, exists is false
    return false;
  }

  @Override
  public Optional<Object> asOptional() throws MoreThanOneElementException {
    return Optional.empty();
  }

  @Override
  public Stream<Object> asStream() {
    return this;
  }

  @Override
  public void close() {
    // Nothing to really close
  }

  @Override
  public Nary<Object> coerceToMonoElement() throws MoreThanOneElementException {
    return this;
  }

  @Override
  public <R, A> R collect(Collector<? super Object, A, R> collector) {
    A container = collector.supplier().get();
    R result = collector.finisher().apply(container);
    return result;
  }

  @Override
  public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super Object> accumulator, BiConsumer<R, R> combiner) {
    R container = supplier.get();
    return container;
  }

  @Override
  public List<Object> collectToList() {
    return Collections.emptyList();
  }

  @Override
  public Set<Object> collectToSet() {
    return Collections.emptySet();
  }

  @Override
  public Nary<Object> concat(Optional<?> other) {
    // If we are empty, only the other content matters for result
    return Narys.from(other);
  }

  @Override
  public Nary<Object> concat(Stream<?> other) {
    // This is empty. Only the other elements are relevant
    return Narys.from(other);
  }

  @Override
  public long count() {
    // No elements
    return 0;
  }

  @Override
  public Nary<Object> distinct() {
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
    //we are equal if the other is empty too
    return !thatIterator.hasNext();
  }

  @Override
  public Nary<Object> filter(Predicate<? super Object> predicate) {
    // Ignore the argument, return an empty nary
    return this;
  }

  @Override
  public Optional<Object> findAny() {
    return Optional.empty();
  }

  @Override
  public Nary<Object> findAnyNary() {
    return this;
  }

  @Override
  public Optional<Object> findFirst() {
    return Optional.empty();
  }

  @Override
  public Nary<Object> findFirstNary() {
    return this;
  }

  @Override
  public Nary<Object> findLast() {
    return this;
  }

  @Override
  public <R> Nary<R> flatMap(Function<? super Object, ? extends Stream<? extends R>> mapper) {
    // Ignore the argument, return an empty nary
    return instance();
  }

  @Override
  public <U> Nary<U> flatMapOptional(Function<? super Object, Optional<U>> mapper) throws MoreThanOneElementException {
    // Ignore the mapper
    return instance();
  }

  @Override
  public DoubleStream flatMapToDouble(Function<? super Object, ? extends DoubleStream> mapper) {
    // Ignores the argument, returnd an empty stream
    return DoubleStream.empty();
  }

  @Override
  public IntStream flatMapToInt(Function<? super Object, ? extends IntStream> mapper) {
    // Ignores the argument, returnd an empty stream
    return IntStream.empty();
  }

  @Override
  public LongStream flatMapToLong(Function<? super Object, ? extends LongStream> mapper) {
    // Ignores the argument, returnd an empty stream
    return LongStream.empty();
  }

  @Override
  public void forEach(Consumer<? super Object> action) {
    // Do nothing
  }

  @Override
  public void forEachOrdered(Consumer<? super Object> action) {
    // Do nothing
  }

  @Override
  public Object get() throws NoSuchElementException {
    throw new NoSuchElementException("Can't call get() on an empty nary: No value present");
  }

  @Override
  public int hashCode() {
    // Based on list implementation, hashcode for an empty list
    return 1;
  }

  @Override
  public Nary<Object> ifAbsent(Runnable runnable) {
    runnable.run();
    return this;
  }

  @Override
  public Nary<Object> ifPresent(Consumer<? super Object> consumer) throws MoreThanOneElementException {
    //Ignore the consumer
    return this;
  }

  @Override
  public boolean isParallel() {
    return false;
  }

  @Override
  public boolean isPresent() {
    return false;
  }

  @Override
  public Iterator<Object> iterator() {
    return EmptyIterator.instance();
  }

  @Override
  public Nary<Object> limit(long maxSize) {
    return this;
  }

  @Override
  public <R> Nary<R> map(Function<? super Object, ? extends R> mapper) {
    // Ignore the argument, return an empty nary
    return instance();
  }

  @Override
  public <U> Nary<U> mapFilteringNullResult(Function<? super Object, ? extends U> mapper) {
    // Ignore the argument
    return instance();
  }

  @Override
  public DoubleStream mapToDouble(ToDoubleFunction<? super Object> mapper) {
    // Ignores the argument, returnd an empty stream
    return DoubleStream.empty();
  }

  @Override
  public IntStream mapToInt(ToIntFunction<? super Object> mapper) {
    // Ignores the argument, returnd an empty stream
    return IntStream.empty();
  }

  @Override
  public LongStream mapToLong(ToLongFunction<? super Object> mapper) {
    // Ignores the argument, returnd an empty stream
    return LongStream.empty();
  }

  @Override
  public Optional<Object> max(Comparator<? super Object> comparator) {
    return Optional.empty();
  }

  @Override
  public Nary<Object> maxNary(Comparator<? super Object> comparator) {
    return this;
  }

  @Override
  public Optional<Object> min(Comparator<? super Object> comparator) {
    return Optional.empty();
  }

  @Override
  public Nary<Object> minNary(Comparator<? super Object> comparator) {
    return this;
  }

  @Override
  public boolean noneMatch(Predicate<? super Object> predicate) {
    // In an empty set all predicates are true
    return true;
  }

  @Override
  public Stream<Object> onClose(Runnable closeHandler) {
    // Because adding a handler is mutable operation, we use a new instance to
    // be affected and returned
    return Stream.empty().onClose(closeHandler);
  }

  @Override
  public Object orElse(Object other) throws MoreThanOneElementException {
    return other;
  }

  @Override
  public Object orElseGet(Supplier<?> other) throws MoreThanOneElementException {
    Object alternative = other.get();
    return alternative;
  }

  @Override
  public <X extends Throwable> Object orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
    throw exceptionSupplier.get();
  }

  @Override
  public Nary<Object> orElseUse(Supplier<?> replacer) throws MoreThanOneElementException {
    final Object element = replacer.get();
    return Narys.of(element);
  }

  @Override
  public Stream<Object> parallel() {
    return this;
  }

  @Override
  public Nary<Object> peek(Consumer<? super Object> action) {
    return this;
  }

  @Override
  public Object reduce(Object identity, BinaryOperator<Object> accumulator) {
    return identity;
  }

  @Override
  public Optional<Object> reduce(BinaryOperator<Object> accumulator) {
    return Optional.empty();
  }

  @Override
  public <U> U reduce(U identity, BiFunction<U, ? super Object, U> accumulator, BinaryOperator<U> combiner) {
    return identity;
  }

  @Override
  public Nary<Object> reduceNary(BinaryOperator<Object> accumulator) {
    return this;
  }

  @Override
  public Stream<Object> sequential() {
    return this;
  }

  @Override
  public Nary<Object> skip(long n) {
    return this;
  }

  @Override
  public Nary<Object> sorted() {
    return this;
  }

  @Override
  public Nary<Object> sorted(Comparator<? super Object> comparator) {
    return this;
  }

  @Override
  public Spliterator<Object> spliterator() {
    // Avoid intermediate step of converting to stream, return empty spliterator right away
    return EmptySpliterator.instance();
  }

  @Override
  public Object[] toArray() {
    return EmptyArray.INSTANCE;
  }

  @Override
  public <A> A[] toArray(IntFunction<A[]> generator) {
    return generator.apply(0);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

  @Override
  public Stream<Object> unordered() {
    return this;
  }

  /**
   * Accessor method for the only needed instance<br>
   * Generics help to make it appear multi-type
   *
   * @param <T> The expected element type
   * @return The empty instance
   */
  public static <T> Nary<T> instance() {
    return (Nary<T>) INSTANCE;
  }
}
