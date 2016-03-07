package ar.com.kfgodel.nary.impl.others;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.*;

/**
 * This type represents an empty stream, and because it's immutable it's reusable.<br>
 *   We create this class to avoid stream instantiation where it's not needed. Current
 *   implementation of Stream.empty() instantiates every time
 *
 * Created by kfgodel on 07/03/16.
 */
public class EmptyStream implements Stream<Object> {

  private static final EmptyStream INSTANCE = new EmptyStream();

  public static<T> Stream<T> instance(){
    return (Stream<T>) INSTANCE;
  }

  @Override
  public Stream<Object> filter(Predicate<? super Object> predicate) {
    // Ignores the argument, returnd an empty stream
    return instance();
  }

  @Override
  public <R> Stream<R> map(Function<? super Object, ? extends R> mapper) {
    // Ignores the argument, returnd an empty stream
    return instance();
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
  public DoubleStream mapToDouble(ToDoubleFunction<? super Object> mapper) {
    // Ignores the argument, returnd an empty stream
    return DoubleStream.empty();
  }

  @Override
  public <R> Stream<R> flatMap(Function<? super Object, ? extends Stream<? extends R>> mapper) {
    // Ignores the argument, returnd an empty stream
    return instance();
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
  public DoubleStream flatMapToDouble(Function<? super Object, ? extends DoubleStream> mapper) {
    // Ignores the argument, returnd an empty stream
    return DoubleStream.empty();
  }

  @Override
  public Stream<Object> distinct() {
    return instance();
  }

  @Override
  public Stream<Object> sorted() {
    return instance();
  }

  @Override
  public Stream<Object> sorted(Comparator<? super Object> comparator) {
    return instance();
  }

  @Override
  public Stream<Object> peek(Consumer<? super Object> action) {
    return instance();
  }

  @Override
  public Stream<Object> limit(long maxSize) {
    return instance();
  }

  @Override
  public Stream<Object> skip(long n) {
    return instance();
  }

  @Override
  public void forEach(Consumer<? super Object> action) {
    // Do nothing
  }

  @Override
  public void forEachOrdered(Consumer<? super Object> action) {
    // Nothing to do
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
  public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super Object> accumulator, BiConsumer<R, R> combiner) {
    R container = supplier.get();
    return container;
  }

  @Override
  public <R, A> R collect(Collector<? super Object, A, R> collector) {
    A container = collector.supplier().get();
    R result = collector.finisher().apply(container);
    return result;
  }

  @Override
  public Optional<Object> min(Comparator<? super Object> comparator) {
    return Optional.empty();
  }

  @Override
  public Optional<Object> max(Comparator<? super Object> comparator) {
    return Optional.empty();
  }

  @Override
  public long count() {
    // No elements
    return 0;
  }

  @Override
  public boolean anyMatch(Predicate<? super Object> predicate) {
    // In an empty set, exists is false
    return false;
  }

  @Override
  public boolean allMatch(Predicate<? super Object> predicate) {
    // In an empty set, all predicates are true
    return true;
  }

  @Override
  public boolean noneMatch(Predicate<? super Object> predicate) {
    // In an empty set all predicates are true
    return true;
  }

  @Override
  public Optional<Object> findFirst() {
    return Optional.empty();
  }

  @Override
  public Optional<Object> findAny() {
    return Optional.empty();
  }

  @Override
  public Iterator<Object> iterator() {
    return EmptyIterator.instance();
  }

  @Override
  public Spliterator<Object> spliterator() {
    return EmptySpliterator.instance();
  }

  @Override
  public boolean isParallel() {
    return false;
  }

  @Override
  public Stream<Object> sequential() {
    return this;
  }

  @Override
  public Stream<Object> parallel() {
    return this;
  }

  @Override
  public Stream<Object> unordered() {
    return this;
  }

  @Override
  public Stream<Object> onClose(Runnable closeHandler) {
    return this;
  }

  @Override
  public void close() {
    // No need to close
  }
}
