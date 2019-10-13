package ar.com.kfgodel.nary.impl;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.exceptions.MoreThanOneElementException;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
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
 * This type defines basic behavior for narys to be implemented
 *
 * Created by kfgodel on 07/03/16.
 */
public abstract class NarySupport<T> implements Nary<T> {

  /**
   * Forces this instance to contain only 1 element (or none).<br>
   *   If this nary contains more than one element the coercion fails. This method
   *   helps on checking the constraints that a mono element nary needs to have, that the user may
   *   unintentionally violate
   * @return an empty nary if this is empty, a non empty optional if this
   * has only one element
   * @throws MoreThanOneElementException If this nary has more than 1 element
   */
  protected abstract Nary<T> coerceToMonoElement() throws MoreThanOneElementException;

  @Override
  public Nary<T> concat(Stream<? extends T> other) {
    return returningNaryDo(Stream.concat(this, other));
  }

  @Override
  public Nary<T> concat(Optional<? extends T> other) {
    return concat(Nary.from(other));
  }

  @Override
  public Nary<T> add(T... others) {
    return concat(Nary.from(others));
  }

  @Override
  public T get() throws NoSuchElementException {
    return coerceToMonoElement().get();
  }

  @Override
  public boolean isPresent() {
    return coerceToMonoElement().isPresent();
  }

  @Override
  public Nary<T> ifPresent(Consumer<? super T> consumer) throws MoreThanOneElementException {
    coerceToMonoElement().ifPresent(consumer);
    return this;
  }

  @Override
  public Nary<T> ifAbsent(Runnable runnable) {
    return coerceToMonoElement().ifAbsent(runnable);
  }

  @Override
  public <U> Nary<U> mapFilteringNullResult(Function<? super T, ? extends U> mapper) {
    return returningNaryDo(
      this.<U>map(mapper)
      .filter(Objects::nonNull)
    );
  }

  @Override
  public <U> Nary<U> flatMapOptional(Function<? super T, java.util.Optional<U>> mapper) throws MoreThanOneElementException {
    return returningNaryDo(
      map(mapper)
      .flatMap(Nary::from)
    );
  }

  @Override
  public T orElse(T other) throws MoreThanOneElementException {
    return coerceToMonoElement().orElse(other);
  }

  @Override
  public T orElseGet(Supplier<? extends T> other) throws MoreThanOneElementException {
    return coerceToMonoElement().orElseGet(other);
  }

  @Override
  public Nary<T> orElseUse(Supplier<? extends T> mapper) throws MoreThanOneElementException {
    return coerceToMonoElement().orElseUse(mapper);
  }

  @Override
  public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
    return coerceToMonoElement().orElseThrow(exceptionSupplier);
  }

  @Override
  public <X extends RuntimeException> T orElseThrowRuntime(Supplier<? extends X> exceptionSupplier) {
    return orElseThrow(exceptionSupplier);
  }

  @Override
  public java.util.Optional<T> asOptional() throws MoreThanOneElementException {
    return coerceToMonoElement().asOptional();
  }

  @Override
  public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator) {
    R container = supplier.get();
    for (T element : this) {
      accumulator.accept(container,element);
    }
    return container;
  }

  @Override
  public Nary<T> filter(Predicate<? super T> predicate) {
    return returningNaryDo(asStream().filter(predicate));
  }

  @Override
  public <R> Nary<R> map(Function<? super T, ? extends R> mapper) {
    return returningNaryDo(asStream().map(mapper));
  }

  @Override
  public IntStream mapToInt(ToIntFunction<? super T> mapper) {
    return asStream().mapToInt(mapper);
  }

  @Override
  public LongStream mapToLong(ToLongFunction<? super T> mapper) {
    return asStream().mapToLong(mapper);
  }

  @Override
  public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
    return asStream().mapToDouble(mapper);
  }

  @Override
  public <R> Nary<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
    return returningNaryDo(asStream().flatMap(mapper));
  }

  @Override
  public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
    return asStream().flatMapToInt(mapper);
  }

  @Override
  public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
    return asStream().flatMapToLong(mapper);
  }

  @Override
  public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
    return asStream().flatMapToDouble(mapper);
  }

  @Override
  public void forEach(Consumer<? super T> action) {
    asStream().forEach(action);
  }

  @Override
  public void forEachOrdered(Consumer<? super T> action) {
    asStream().forEachOrdered(action);
  }

  @Override
  public Object[] toArray() {
    return asStream().toArray();
  }

  @Override
  public <A> A[] toArray(IntFunction<A[]> generator) {
    return asStream().toArray(generator);
  }

  @Override
  public T reduce(T identity, BinaryOperator<T> accumulator) {
    return asStream().reduce(identity, accumulator);
  }

  @Override
  public java.util.Optional<T> reduce(BinaryOperator<T> accumulator) {
    return asStream().reduce(accumulator);
  }

  @Override
  public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
    return asStream().reduce(identity, accumulator, combiner);
  }

  @Override
  public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
    return asStream().collect(supplier, accumulator, combiner);
  }

  @Override
  public <R, A> R collect(Collector<? super T, A, R> collector) {
    return asStream().collect(collector);
  }

  @Override
  public java.util.Optional<T> min(Comparator<? super T> comparator) {
    return asStream().min(comparator);
  }

  @Override
  public java.util.Optional<T> max(Comparator<? super T> comparator) {
    return asStream().max(comparator);
  }

  @Override
  public long count() {
    return asStream().count();
  }

  @Override
  public boolean anyMatch(Predicate<? super T> predicate) {
    return asStream().anyMatch(predicate);
  }

  @Override
  public boolean allMatch(Predicate<? super T> predicate) {
    return asStream().allMatch(predicate);
  }

  @Override
  public boolean noneMatch(Predicate<? super T> predicate) {
    return asStream().noneMatch(predicate);
  }

  @Override
  public java.util.Optional<T> findFirst() {
    return asStream().findFirst();
  }

  @Override
  public java.util.Optional<T> findAny() {
    return asStream().findAny();
  }

  @Override
  public Iterator<T> iterator() {
    return asStream().iterator();
  }

  @Override
  public Spliterator<T> spliterator() {
    return asStream().spliterator();
  }

  @Override
  public boolean isParallel() {
    return asStream().isParallel();
  }

  @Override
  public Stream<T> sequential() {
    return asStream().sequential();
  }

  @Override
  public Stream<T> parallel() {
    return asStream().parallel();
  }

  @Override
  public Stream<T> unordered() {
    return asStream().unordered();
  }

  @Override
  public Stream<T> onClose(Runnable closeHandler) {
    return asStream().onClose(closeHandler);
  }

  @Override
  public void close() {
    asStream().close();
  }


  @Override
  public Nary<T> findLast() {
    return reduceNary(this::keepLast);
  }
  /**
   * Reductor operation that keeps the last element as result
   */
  private T keepLast(T previous, T current) {
    return current;
  }


  @Override
  public Nary<T> distinct() {
    return returningNaryDo(asStream().distinct());
  }

  @Override
  public Nary<T> sorted() {
    return returningNaryDo(asStream().sorted());
  }

  @Override
  public Nary<T> sorted(Comparator<? super T> comparator) {
    return returningNaryDo(asStream().sorted(comparator));
  }

  @Override
  public Nary<T> peek(Consumer<? super T> action) {
    return returningNaryDo(asStream().peek(action));
  }

  @Override
  public Nary<T> limit(long maxSize) {
    return returningNaryDo(asStream().limit(maxSize));
  }

  @Override
  public Nary<T> skip(long n) {
    return returningNaryDo(asStream().skip(n));
  }

  @Override
  public Nary<T> reduceNary(BinaryOperator<T> accumulator) {
    return returningNaryDo(reduce(accumulator));
  }

  @Override
  public Nary<T> minNary(Comparator<? super T> comparator) {
    return returningNaryDo(min(comparator));
  }

  @Override
  public Nary<T> maxNary(Comparator<? super T> comparator) {
    return returningNaryDo(max(comparator));
  }

  @Override
  public Nary<T> findFirstNary() {
    return returningNaryDo(findFirst());
  }

  @Override
  public Nary<T> findAnyNary() {
    return returningNaryDo(findAny());
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
    Iterator<T> thisIterator = this.iterator();
    Iterator thatIterator = that.iterator();
    while (thisIterator.hasNext() && thatIterator.hasNext()) {
      T thisElement = thisIterator.next();
      Object thatElement = thatIterator.next();
      if (!Objects.equals(thisElement, thatElement)) {
        return false;
      }
    }
    return !thisIterator.hasNext() && !thatIterator.hasNext();
  }

  @Override
  public int hashCode() {
    // Taken from arrayList implementation
    int hashCode = 1;
    for (T e : this)
      hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
    return hashCode;
  }

  /**
   * Wraps the native stream in a nary to conform to nary interface
   * @param nativeStream The stream to wrap
   * @param <R> type of expected elements
   * @return The created nary
   */
  protected<R> Nary<R> returningNaryDo(Stream<R> nativeStream) {
    return Nary.from(nativeStream);
  }

  /**
   * Wraps the native optional in a nary to conform to nary interface
   * @param nativeOptional The native optional
   * @return The created nary
   */
  protected Nary<T> returningNaryDo(java.util.Optional<T> nativeOptional) {
    return Nary.from(nativeOptional);
  }

}
