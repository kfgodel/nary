package ar.com.kfgodel.nary.impl;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.nary.api.exceptions.MoreThanOneElementException;
import ar.com.kfgodel.nary.impl.others.EmptyStream;
import ar.com.kfgodel.optionals.Optional;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * This type represents the empty nary
 *
 * Created by kfgodel on 07/03/16.
 */
public class EmptyNary extends NarySupport<Object> {

  private static final EmptyNary INSTANCE = new EmptyNary();

  /**
   * Accessor method for the only needed instance<br>
   *   Generics help to make it appear multi-type
   * @param <T> The expected element type
   * @return The empty instance
   */
  public static<T> Nary<T> instance(){
    return (Nary<T>) INSTANCE;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

  @Override
  public Object get() throws NoSuchElementException {
    throw new NoSuchElementException("Can't call get() on an empty nary: No value present");
  }

  @Override
  public boolean isPresent() {
    return false;
  }

  @Override
  public boolean isAbsent() throws MoreThanOneElementException {
    return true;
  }

  @Override
  public void ifPresent(Consumer<? super Object> consumer) throws MoreThanOneElementException {
    //Ignore the consumer
  }

  @Override
  public Optional<Object> ifAbsent(Runnable runnable) {
    runnable.run();
    return this;
  }

  @Override
  public Nary<Object> filterNary(Predicate<? super Object> predicate) {
    // Ignore the predicate, return an empty nary
    return this;
  }

  @Override
  public <R> Nary<R> mapNary(Function<? super Object, ? extends R> mapper) {
    // Ignore the mapper, return empty nary
    return instance();
  }

  @Override
  public <R> Nary<R> flatMapNary(Function<? super Object, ? extends Nary<? extends R>> mapper) {
    // Ignore the mapper, return an empty nary
    return instance();
  }

  @Override
  public Optional<Object> filterOptional(Predicate<? super Object> predicate) throws MoreThanOneElementException {
    // Ignore the argument
    return instance();
  }

  @Override
  public <U> Optional<U> mapOptional(Function<? super Object, ? extends U> mapper) throws MoreThanOneElementException {
    // Ignore the argument
    return instance();
  }

  @Override
  public <U> Optional<U> flatMapOptional(Function<? super Object, java.util.Optional<U>> mapper) throws MoreThanOneElementException {
    // Ignore the mapper
    return instance();
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
  public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super Object> accumulator) {
    R container = supplier.get();
    return container;
  }

  @Override
  public java.util.Optional<Object> asNativeOptional() throws MoreThanOneElementException {
    return java.util.Optional.empty();
  }

  @Override
  public Nary<Object> findLast() {
    return this;
  }

  @Override
  public Stream<Object> filter(Predicate<? super Object> predicate) {
    // Ignore the argument, return an empty nary
    return instance();
  }

  @Override
  public <R> Stream<R> map(Function<? super Object, ? extends R> mapper) {
    // Ignore the argument, return an empty nary
    return instance();
  }

  @Override
  public <R> Stream<R> flatMap(Function<? super Object, ? extends Stream<? extends R>> mapper) {
    // Ignore the argument, return an empty nary
    return instance();
  }

  @Override
  public Nary<Object> distinct() {
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
  public Nary<Object> peek(Consumer<? super Object> action) {
    return this;
  }

  @Override
  public Nary<Object> limit(long maxSize) {
    return this;
  }

  @Override
  public Nary<Object> skip(long n) {
    return this;
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
  public Nary<Object> reduceNary(BinaryOperator<Object> accumulator) {
    return this;
  }

  @Override
  public Nary<Object> minNary(Comparator<? super Object> comparator) {
    return this;
  }

  @Override
  public Nary<Object> maxNary(Comparator<? super Object> comparator) {
    return this;
  }

  @Override
  public Nary<Object> findFirstNary() {
    return this;
  }

  @Override
  public Nary<Object> findAnyNary() {
    return this;
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
    // Ignore the handler, we will never close
    return this;
  }

  @Override
  public void close() {
    // Nothing to close
  }

  @Override
  public Stream<Object> asStream() {
    return EmptyStream.instance();
  }

  @Override
  public Optional<Object> asOptional() throws MoreThanOneElementException {
    return this;
  }
}
